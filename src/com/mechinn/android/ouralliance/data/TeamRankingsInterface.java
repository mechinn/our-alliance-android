package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.TeamRankingsProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class TeamRankingsInterface {
	private final String TAG = "TeamRankingsInterface";
	Activity activity;
	
	public TeamRankingsInterface(Activity act) {
		activity = act;
	}

    public Uri createTeam(String competition, int team, int rank, double qs, double hybrid,
    		double bridge, double teleop, int coop, String record, int dq, int played) {
    	
        ContentValues initialValues = putVals(true, -1, competition, team, rank, 
    			qs, hybrid, bridge, teleop, coop, record, dq, dq);
        
        return activity.getContentResolver().insert(TeamRankingsProvider.URI, initialValues);
    }

    public Cursor fetchAllTeams() {
    	Cursor mCursor = activity.managedQuery(TeamRankingsProvider.URI, TeamRankingsProvider.SCHEMA_ARRAY.toArray(), null, null, TeamRankingsProvider.KEY_RANK);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTeam(String competition, int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamRankingsProvider.URI, TeamRankingsProvider.SCHEMA_ARRAY.toArray(), 
    			TeamRankingsProvider.KEY_COMPETITION + " = '" + competition + "' AND " + TeamRankingsProvider.KEY_TEAM + " = " + team, null, TeamRankingsProvider.KEY_RANK);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateTeam(String competition, int team, int rank, double qs, double hybrid,
    		double bridge, double teleop, int coop, String record, int dq, int played) {
    	ContentValues args = putVals(false, -1, competition, team, rank, 
    			qs, hybrid, bridge, teleop, coop, record, dq, dq);
        
        return activity.getContentResolver().update(TeamRankingsProvider.URI, args,null,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int team, int rank, double qs, double hybrid,
    		double bridge, double teleop, int coop, String record, int dq, int played) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamRankingsProvider.KEY_COMPETITION, competition);
	    		cv.put(TeamRankingsProvider.KEY_TEAM, team);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(TeamRankingsProvider.KEY_RANK, rank);
    	cv.put(TeamRankingsProvider.KEY_QS, qs);
    	cv.put(TeamRankingsProvider.KEY_HYBRID, hybrid);
    	cv.put(TeamRankingsProvider.KEY_BRIDGE, bridge);
    	cv.put(TeamRankingsProvider.KEY_TELEOP, teleop);
    	cv.put(TeamRankingsProvider.KEY_COOP, coop);
    	cv.put(TeamRankingsProvider.KEY_RECORD, record);
    	cv.put(TeamRankingsProvider.KEY_DQ, dq);
        cv.put(TeamRankingsProvider.KEY_PLAYED, played);
        return cv;
    }
}