package com.mechinn.android.frcscouting;

import com.mechinn.android.frcscouting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends Activity {
    private static final int RESETMSG = 1;
    private static final int RESETPROG = 2;
    
	private TeamInfoDb teamInfoDb;
	private Button pullFRC;
	private Button reset;

	private AlertDialog resetDialog;
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
	        case RESETMSG:
	        	resetDialog = new AlertDialog.Builder(Settings.this)
	                .setTitle("Really reset DB?")
	                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	showDialog(RESETPROG);
	                    }
	                })
	                .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	resetDialog.dismiss();
	                    }
	                })
	                .create();
	            return resetDialog;
	        case RESETPROG:
	        	return new ResetDB(Settings.this);
        }
        return null;
    }
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        teamInfoDb = new TeamInfoDb(this, true);
        
        pullFRC = (Button) findViewById(R.id.pullfrc);
        pullFRC.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                
            }
        });
        
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	showDialog(RESETMSG);
            }
        });
    }
    
    public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
    }
}
