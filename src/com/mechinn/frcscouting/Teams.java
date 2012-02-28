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
	private TeamInfoDb dbHelper;
	private int teamNumber = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        dbHelper = new TeamInfoDb(this);
        dbHelper.open();
        
        // Get all of the notes from the database and create the item list
        Cursor c = dbHelper.fetchAllTeams();
        startManagingCursor(c);
        
        if(c.getCount()>0){
        	fillData();
        } else {
        	createTeams();
        }
	}
    
    private void createTeams() {
        dbHelper.createTeam(87,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(224,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(225,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(272,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(293,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(341,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(357,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(486,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(708,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(709,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(714,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(834,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(869,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1143,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1168,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1218,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1391,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1495,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1640,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1647,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1712,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1791,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(1923,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2016,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2229,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2234,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2539,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2590,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2600,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(2607,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(3123,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(3167,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(3607,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(3637,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(4285,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(4342,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(4361,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        dbHelper.createTeam(4373,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
        fillData();
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = dbHelper.fetchAllTeams();
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
