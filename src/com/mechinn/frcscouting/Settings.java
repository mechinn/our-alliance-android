package com.mechinn.frcscouting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Settings extends Activity {
    private static final int RESETMSG = 1;
    private static final int RESETPROG = 2;
    
	private TeamInfoDb teamInfoDb;
	private Button pullFRC;
	private Button reset;

	private AlertDialog resetDialog;
    private ProgressDialog resetProgressDialog;
    private ResetThread resetThread;
    
    // Define the Handler that receives messages from the thread and update the progress
	final Handler resetHandler = new Handler() {
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			resetProgressDialog.setProgress(total);
			if (total >= 40){
				dismissDialog(RESETPROG);
			}
		}
	};
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
	        case RESETMSG:
	        	resetDialog = new AlertDialog.Builder(this)
	                .setTitle("Really reset DB?")
	                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	showDialog(RESETPROG);
	                    	teamInfoDb.reset();
	                    	resetProgressDialog.setProgress(2);
	                    	resetThread = new ResetThread(resetHandler);
	                    	resetThread.start();
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
	        	resetProgressDialog = new ProgressDialog(this);
	        	resetProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        	resetProgressDialog.setMessage("Resetting DB...");
	        	resetProgressDialog.setCancelable(false);
	        	resetProgressDialog.setMax(40);
	            return resetProgressDialog;
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

    /** Nested class that performs progress calculations (counting) */
    private class ResetThread extends Thread {
        Handler mHandler;
        int total;
       
        ResetThread(Handler h) {
            mHandler = h;
        }
        
        public void incrementTotal() {
        	Message msg = mHandler.obtainMessage();
            msg.arg1 = total;
            mHandler.sendMessage(msg);
            total++;
        }
       
        public void run() { 
            total = 2;
            incrementTotal();
            teamInfoDb.createTeam(87,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
            incrementTotal();
			teamInfoDb.createTeam(224,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(225,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(272,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(293,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(341,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(357,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(486,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(708,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(709,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(714,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(834,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(869,1,8,1,false,2,4,0,0,0,false,false,false,false,true,true,"Defender");
			incrementTotal();
			teamInfoDb.createTeam(1143,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1168,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1218,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1391,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1495,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1640,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1647,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1712,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1791,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(1923,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2016,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2229,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2234,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2539,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2590,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2600,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(2607,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(3123,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(3167,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(3607,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(3637,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(4285,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(4342,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(4361,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
			teamInfoDb.createTeam(4373,0,0,1,false,0,0,0,0,0,false,false,false,false,false,false,"");
			incrementTotal();
        }
    }
}
