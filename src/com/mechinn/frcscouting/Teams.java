package com.mechinn.frcscouting;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Teams extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] teams = getResources().getStringArray(R.array.teams);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, teams));
		
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
