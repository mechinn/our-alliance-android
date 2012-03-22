package com.mechinn.android.myalliance.activity;

import com.bugsense.trace.BugSenseHandler;
import com.mechinn.android.myalliance.R;
import com.mechinn.android.myalliance.ResetDB;
import com.mechinn.android.myalliance.data.Prefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MyAlliance extends Activity {
	private Prefs prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        BugSenseHandler.setup(this, "a92dda4a");
        prefs = new Prefs(this);
        
        //first run setup the DB
        Log.d("first run",Boolean.toString(prefs.getSetupDB()));
        if(prefs.getSetupDB()) {
        	new ResetDB(MyAlliance.this,true).show();
        }
        
        final Button scouting = (Button)  findViewById(R.id.scouting);
        scouting.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String actionName = "com.mechinn.android.myalliance.OpenMatches";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        
        final Button teams = (Button)  findViewById(R.id.teams);
        teams.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String actionName = "com.mechinn.android.myalliance.OpenTeams";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        
        final Button teamRankings = (Button)  findViewById(R.id.teamRankings);
        teamRankings.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String actionName = "com.mechinn.android.myalliance.OpenTeamRankings";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	String actionName = "com.mechinn.android.myalliance.OpenSettings";
		    	Intent intent = new Intent(actionName);
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}