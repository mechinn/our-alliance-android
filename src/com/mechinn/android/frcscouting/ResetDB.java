package com.mechinn.android.frcscouting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/** Nested class that performs progress calculations (counting) */
public class ResetDB extends ProgressDialog {
 	private Handler resetHandler;
 	private ResetThread thread;
    private TeamInfoDb teamInfoDb;

	public ResetDB(Context context) {
		super(context);
		teamInfoDb = new TeamInfoDb(context, true);
		resetHandler = new Handler() {
	 		public void handleMessage(Message msg) {
	 			int total = msg.arg1;
	 			ResetDB.this.setProgress(total);
	 			if (total >= 40){
	 				ResetDB.this.dismiss();
	 			}
	 		}
	 	};
    	this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	this.setMessage("Setup database...");
    	this.setCancelable(false);
    	this.setMax(40);
    	thread = new ResetThread(resetHandler);
    	thread.start();
	}

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
	    	teamInfoDb.reset();
	        total = 1;
	        int[] teams = {11, 41, 56, 75, 102, 136, 203, 219, 223, 224, 303, 555, 613, 752, 869, 1089, 1143, 1168, 1228, 1279,
	        1302, 1367, 1370, 1403, 1626, 1672, 1676, 1811, 1881, 1989, 2016, 2458, 2554, 2577, 3142, 3314,
	        3340, 3637, 4128, 4281, 4347 };
	        for(int i=0;i<teams.length;++i) {
	        	addBlankTeam(teams[i]);
	        }
	        incrementTotal();
	        teamInfoDb.updateTeam(869, "Long", 8, 1, false, "Traction", 4, "None", 0, "None", false, false, false, false, true, true, "Our robot", false);
	    }
	    
	    private void addBlankTeam(int team) {
	        incrementTotal();
	        teamInfoDb.createTeam(team,"None",0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,"",false);
	    }
	}
}