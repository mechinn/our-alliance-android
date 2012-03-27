package com.mechinn.android.ouralliance;

import java.util.HashSet;

import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.MatchScoutingInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamRankingsInterface;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/** Nested class that performs progress calculations (counting) */
public class ResetDB extends ProgressDialog {
 	private Handler resetHandler;
    private TeamScoutingInterface teamInfo;
    private TeamRankingsInterface rankings;
    private MatchListInterface matchList;
    private MatchScoutingInterface matchScore;
    private String[] competitions;
    private Prefs prefs;
    private Activity activity;
    private DatabaseConnection db;
    boolean initial;
    private final int total = 276;
    
    public ResetDB(Activity act) {
    	this(act,false);
    }

	public ResetDB(Activity act, boolean init) {
		super(act);
		activity = act;
        initial = init;
        db = new DatabaseConnection(activity);
        prefs = new Prefs(act);
        competitions = act.getResources().getStringArray(R.array.competitions);
        teamInfo = new TeamScoutingInterface(act);
        rankings = new TeamRankingsInterface(act);
        matchList = new MatchListInterface(act);
        matchScore = new MatchScoutingInterface(act);
		resetHandler = new Handler() {
	 		public void handleMessage(Message msg) {
	 			int current = msg.arg1;
	 			ResetDB.this.setProgress(current);
	 			if (current >= total){
	 				prefs.setSetupDB(false);
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
	    	comps.add("CT");
	        teamInfo.createTeam(comps,team);
	        incrementTotal();
	    }
	    
	    private void addExMatch(int matchNum) {
	    	matchList.addMatch(activity, competitions[0],matchNum,0,9999,9999,9999,9999,9999,9999);
	    }

		@Override
		protected Void doInBackground(Handler... params) {
			mHandler = params[0];
			if(!initial) {
				db.reset();
				incrementTotal();
	    	} else {
	    		setTotal(4);
	    	}
			//put in static stuff for now
	        int[] teams = {20, 95, 118, 126, 155, 173, 175, 176, 177, 178, 181, 195, 228, 229, 230, 236, 237, 250, 549, 558, 
	        		571, 663, 694, 743, 839, 869, 999, 1027, 1071, 1073, 1099, 1124, 1493, 1511, 1559, 1665, 1699, 1740, 
	        		1784, 1880, 1922, 1991, 2067, 2168, 2170, 2785, 2791, 2836, 3017, 3104, 3146, 3182, 3461, 3464, 3467, 
	        		3525, 3555, 3634, 3654, 3718, 3719, 4055, 4122, 4134};
	        Log.d("reset","adding blank teams");
	        for(int i=0;i<teams.length;++i) {
	        	addBlankTeam(teams[i]);
	        }
	        incrementTotal();
	        Log.d("reset","updating 869");
	    	HashSet<String> comps = new HashSet<String>();
	    	comps.add("CT");
	        teamInfo.updateTeam(869, "Long", 8, 1, false, "Traction", 4, "None", 0, "None", false, false, false, false, true, true, "Our robot", false, comps);
	        
	        for(int i=1;i<31;++i) {
	        	addExMatch(i);
	        }
	        
	        return null;
		}
	}
}