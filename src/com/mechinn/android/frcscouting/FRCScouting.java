package com.mechinn.android.frcscouting;

import com.mechinn.android.frcscouting.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FRCScouting extends Activity {
    private static final int RESETPROG = 1;
	private Prefs prefs;
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
	        case RESETPROG:
	        	return new ResetDB(FRCScouting.this);
        }
        return null;
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prefs = new Prefs(this);
        
        //first run setup the DB
        if(prefs.getFirstRun()) {
        	showDialog(RESETPROG);
        	prefs.setRunned();
        }
        
        final Button scouting = (Button)  findViewById(R.id.scouting);
        scouting.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.android.frcscouting.OpenScouting";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        final Button teams = (Button)  findViewById(R.id.teams);
        teams.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.android.frcscouting.OpenTeams";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        final Button settings = (Button)  findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.android.frcscouting.OpenSettings";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
    }
}