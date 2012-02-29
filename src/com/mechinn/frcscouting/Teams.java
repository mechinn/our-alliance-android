package com.mechinn.frcscouting;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Teams extends ListActivity {
	private TeamInfoDb teamInfoDb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        
        teamInfoDb = new TeamInfoDb(this);

        fillData();
	}
	
	public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = teamInfoDb.fetchAllTeams();
        startManagingCursor(c);
        
        String[] from = new String[] { TeamInfoDb.KEY_TEAM };
        int[] to = new int[] { R.id.row };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter teams = new SimpleCursorAdapter(this, R.layout.list_row, c, from, to);
        setListAdapter(teams);
        
        ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String actionName = "com.mechinn.frcscouting.OpenTeamInfo";
				Intent intent = new Intent(actionName);
				intent.putExtra("team", Integer.parseInt((String) ((TextView) view).getText()));
				startActivity(intent);
			}
		});
    }
}
