package com.mechinn.android.myalliance.data;

import com.mechinn.android.myalliance.providers.TeamRankingsProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class TeamRankingsInterface {
	Activity activity;
	
	public TeamRankingsInterface(Activity act) {
		activity = act;
	}

    public Uri createTeam(String competition, int team, int rank, float qs, float hybrid,
    		float bridge, float teleop, int coop, String record, int dq, int played) {
    	
        ContentValues initialValues = putVals(true, -1, competition, team, rank, 
    			qs, hybrid, bridge, teleop, coop, record, dq, dq);
        
        return activity.getContentResolver().insert(TeamRankingsProvider.mUri, initialValues);
    }

    public Cursor fetchAllTeams() {
    	return activity.managedQuery(TeamRankingsProvider.mUri, TeamRankingsProvider.schemaArray, null, null, null);
    }

    public Cursor fetchTeam(String competition, int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamRankingsProvider.mUri, TeamRankingsProvider.schemaArray, 
    			TeamRankingsProvider.keyCompetition + " = '" + competition + "' AND " + TeamRankingsProvider.keyTeam + " = '" + team + "'", null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateTeam(String competition, int team, int rank, float qs, float hybrid,
    		float bridge, float teleop, int coop, String record, int dq, int played) {
    	ContentValues args = putVals(false, -1, competition, team, rank, 
    			qs, hybrid, bridge, teleop, coop, record, dq, dq);
        
        return activity.getContentResolver().update(TeamRankingsProvider.mUri, args,null,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int team, int rank, float qs, float hybrid,
    		float bridge, float teleop, int coop, String record, int dq, int played) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamRankingsProvider.keyCompetition, competition);
	    		cv.put(TeamRankingsProvider.keyTeam, team);
	    		cv.put(TeamRankingsProvider.keyLastMod, 0);
	    	} else {
	    		cv.put(TeamRankingsProvider.keyLastMod, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(TeamRankingsProvider.keyLastMod, lastMod);
    	}
    	cv.put(TeamRankingsProvider.keyRank, rank);
    	cv.put(TeamRankingsProvider.keyQS, qs);
    	cv.put(TeamRankingsProvider.keyHybrid, hybrid);
    	cv.put(TeamRankingsProvider.keyBridge, bridge);
    	cv.put(TeamRankingsProvider.keyTeleop, teleop);
    	cv.put(TeamRankingsProvider.keyCoop, coop);
    	cv.put(TeamRankingsProvider.keyRecord, record);
    	cv.put(TeamRankingsProvider.keyDQ, dq);
        cv.put(TeamRankingsProvider.keyPlayed, played);
        return cv;
    }
}