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