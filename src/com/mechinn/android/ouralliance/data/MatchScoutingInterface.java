package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MatchScoutingInterface {
	private Activity activity;
	
	public MatchScoutingInterface(Activity act) {
		activity = act;
	}
	
	public int deleteMatch(String comp, int matchNum) {
		return activity.getContentResolver().delete(MatchScoutingProvider.mUri, MatchScoutingProvider.keyCompetition+" = '"+comp+"' AND "+MatchScoutingProvider.keyMatchNum+" = '"+matchNum+"'", null);
	}

    public Uri createMatch(String competition, int matchNum, int team, String slot, boolean broke, boolean auto, boolean balanced, 
    		int shooterType, int top, int mid, int bot, String notes) {
    	
        ContentValues initialValues = putVals(true, -1, competition, matchNum, team, slot, broke, auto, balanced,
    			shooterType, top, mid, bot, notes);
        
        return activity.getContentResolver().insert(MatchScoutingProvider.mUri, initialValues);
    }

    public Cursor fetchAllMatches() {
    	
    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.mUri, MatchScoutingProvider.schemaArray,
        		null, null, MatchScoutingProvider.keyTeam);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMatch(String competition, int matchNum, int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.mUri, MatchScoutingProvider.schemaArray,
    			MatchScoutingProvider.keyCompetition + " = '" + competition + "' AND " + MatchScoutingProvider.keyMatchNum + " = '" + matchNum + "' AND " + MatchScoutingProvider.keyTeam + " = '" + team + "'", null, MatchScoutingProvider.keyTeam);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchTeam(int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.mUri, MatchScoutingProvider.schemaArray,
    			MatchScoutingProvider.keyTeam + " = '" + team + "'", null, MatchScoutingProvider.keyTeam);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateMatch(String competition, int matchNum, int team, String slot, boolean broke, boolean auto, boolean balanced, 
    		int shooterType, int top, int mid, int bot, String notes) {
    	ContentValues args = putVals(false, -1, competition, matchNum, team, slot, broke, auto, balanced,
    			shooterType, top, mid, bot, notes);
        
        return activity.getContentResolver().update(MatchScoutingProvider.mUri, args,MatchScoutingProvider.keyCompetition + " = '" + competition + "' AND " + MatchScoutingProvider.keyMatchNum + " = '" + matchNum + "' AND " + MatchScoutingProvider.keyTeam + " = '" + team + "'",null) > 0;
    }

    public boolean updateMatch(int lastMod, String competition, int matchNum, int team, String slot, int broke, int auto, int balanced, 
    		int shooterType, int top, int mid, int bot, String notes) {
    	ContentValues args = putVals(false,
    			lastMod, 
    			competition, 
    			matchNum, 
    			team, 
    			slot,
	    		broke==0?false:true, 
	    		auto==0?false:true, 
	    		balanced==0?false:true, 
	    		shooterType, 
	    		top,
				mid, 
				bot, 
				notes);
        
        return activity.getContentResolver().update(MatchScoutingProvider.mUri, args,MatchScoutingProvider.keyCompetition + " = " + competition + " AND " + MatchScoutingProvider.keyMatchNum + " = " + matchNum + " AND " + MatchScoutingProvider.keyTeam + " = " + team,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int matchNum, int team, String slot, boolean broke, boolean auto, boolean balanced, 
    		int shooterType, int top, int mid, int bot, String notes) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(MatchScoutingProvider.keyCompetition, competition);
	    		cv.put(MatchScoutingProvider.keyMatchNum, matchNum);
	    		cv.put(MatchScoutingProvider.keyTeam, team);
	    		cv.put(MatchScoutingProvider.keySlot, slot);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(MatchScoutingProvider.keyBroke, broke);
    	cv.put(MatchScoutingProvider.keyAuto, auto);
    	cv.put(MatchScoutingProvider.keyBalance, balanced);
    	cv.put(MatchScoutingProvider.keyShooter, shooterType);
    	cv.put(MatchScoutingProvider.keyTop, top);
    	cv.put(MatchScoutingProvider.keyMid, mid);
    	cv.put(MatchScoutingProvider.keyBot, bot);
        cv.put(MatchScoutingProvider.keyNotes, notes);
        return cv;
    }
}