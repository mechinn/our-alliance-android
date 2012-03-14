package com.mechinn.android.myalliance;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/** Nested class that performs progress calculations (counting) */
public class ResetDB extends ProgressDialog {
 	private Handler resetHandler;
    private TeamInfoDB teamInfoDb;
    boolean initial;
    
    public ResetDB(Context context) {
    	this(context,false);
    }

	public ResetDB(Context context, boolean init) {
		super(context);
        initial = init;
		teamInfoDb = new TeamInfoDB(context, true);
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
    	new ResetThread().execute(resetHandler);
	}

	private class ResetThread extends AsyncTask<Handler, Void, Void> {
	    Handler mHandler;
	    int total;
	    
	    private void incrementTotal() {
	    	Message msg = mHandler.obtainMessage();
	        msg.arg1 = total;
	        mHandler.sendMessage(msg);
	        total++;
	    }
	    
	    private void addBlankTeam(int team) {
	        incrementTotal();
	        teamInfoDb.createTeam(team,"None",0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,"",false);
	    }

		@Override
		protected Void doInBackground(Handler... params) {
			mHandler = params[0];
			if(!initial) {
	    		teamInfoDb.reset();
	    	}
	        total = 1;
	        int[] teams = {11, 41, 56, 75, 102, 136, 203, 219, 223, 224, 303, 555, 613, 752, 869, 1089, 1143, 1168, 1228, 1279,
	        1302, 1367, 1370, 1403, 1626, 1672, 1676, 1811, 1881, 1989, 2016, 2458, 2554, 2577, 3142, 3314,
	        3340, 3637, 4128, 4281, 4347 };
	        Log.d("reset","adding blank teams");
	        for(int i=0;i<teams.length;++i) {
	        	addBlankTeam(teams[i]);
	        }
	        incrementTotal();
	        Log.d("reset","updating 869");
	        teamInfoDb.updateTeam(869, "Long", 8, 1, false, "Traction", 4, "None", 0, "None", false, false, false, false, true, true, "Our robot", false);
	        teamInfoDb.close();
	        return null;
		}
	}
}