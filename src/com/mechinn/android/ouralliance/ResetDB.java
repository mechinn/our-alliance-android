package com.mechinn.android.ouralliance;

import java.util.HashSet;

import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/** Nested class that performs progress calculations (counting) */
public class ResetDB extends ProgressDialog {
	private final String TAG = "ResetDB";
 	private Handler resetHandler;
    private TeamScoutingInterface teamInfo;
    private Prefs prefs;
    private Activity activity;
    private DatabaseConnection db;
    boolean initial;
    private final int total = 1;
    
    public ResetDB(Activity act) {
    	this(act,false);
    }

	public ResetDB(Activity act, boolean init) {
		super(act);
		activity = act;
        initial = init;
        db = new DatabaseConnection(activity);
        prefs = new Prefs(act);
        teamInfo = new TeamScoutingInterface(act);
		resetHandler = new Handler() {
	 		public void handleMessage(Message msg) {
	 			int current = msg.arg1;
	 			ResetDB.this.setProgress(current);
	 			if (current >= total){
	 				prefs.setDBVersion(db.getVersion());
	 				ResetDB.this.dismiss();
	 			}
	 		}
	 	};
    	this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	this.setMessage("Setup database...");
    	this.setCancelable(false);
    	this.setMax(total);
    	new ResetThread().execute(resetHandler);
	}

	private class ResetThread extends AsyncTask<Handler, Void, Void> {
	    Handler mHandler;
	    int total;
	    
	    private void incrementTotal() {
	    	incrementTotal(1);
	    }
	    
	    private void incrementTotal(int count) {
	        total += count;
	    	Message msg = mHandler.obtainMessage();
	        msg.arg1 = total;
	        mHandler.sendMessage(msg);
	    }
	    
	    private void setTotal(int count) {
	    	total = count;
	    	Message msg = mHandler.obtainMessage();
	        msg.arg1 = total;
	        mHandler.sendMessage(msg);
	    }
	    
	    private void addBlankTeam(int team) {
	    	HashSet<String> comps = new HashSet<String>();
	    	for(String comp : TeamScoutingProvider.COMPETITIONS) {
	    		comps.add(comp);
	    	}
	        teamInfo.createTeam(comps,team);
	        incrementTotal();
	    }

		@Override
		protected Void doInBackground(Handler... params) {
			mHandler = params[0];
			if(!initial) {
				db.reset();
				incrementTotal();
	    	} else {
	    		setTotal(1);
	    	}
	        return null;
		}
	}
}