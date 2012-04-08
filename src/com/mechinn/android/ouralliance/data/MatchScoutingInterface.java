package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MatchScoutingInterface {
	private final String TAG = "MatchScoutingInterface";
	private Activity activity;
	
	public MatchScoutingInterface(Activity act) {
		activity = act;
	}
	
	public int deleteMatch(String comp, int matchNum) {
		return activity.getContentResolver().delete(MatchScoutingProvider.URI, MatchScoutingProvider.KEY_COMPETITION+" = '"+comp+"' AND "+MatchScoutingProvider.KEY_MATCH_NUM+" = "+matchNum, null);
	}

    public Uri createMatch(String competition, int matchNum, int team, String slot, boolean broke, boolean autoBridge, boolean autoShooter, boolean balanced, 
    		int shooterType, int top, int mid, int bot, int miss, int topAuto, int midAuto, int botAuto, int missAuto, String notes) {
    	
        ContentValues initialValues = putVals(true, -1, competition, matchNum, team, slot, broke, 
				autoBridge, autoShooter, balanced, shooterType, top, mid, bot, miss, topAuto, midAuto, botAuto, missAuto, notes);
        
        return activity.getContentResolver().insert(MatchScoutingProvider.URI, initialValues);
    }

    public Cursor fetchAllMatches() {
    	
    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.URI, MatchScoutingProvider.SCHEMA_ARRAY.toArray(),
        		null, null, MatchScoutingProvider.KEY_TEAM);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMatch(String competition, int matchNum, int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.URI, MatchScoutingProvider.SCHEMA_ARRAY.toArray(),
    			MatchScoutingProvider.KEY_COMPETITION + " = '" + competition + "' AND " + MatchScoutingProvider.KEY_MATCH_NUM + " = " + matchNum + " AND " + MatchScoutingProvider.KEY_TEAM + " = " + team, null, MatchScoutingProvider.KEY_TEAM);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchTeam(int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchScoutingProvider.URI, MatchScoutingProvider.SCHEMA_ARRAY.toArray(),
    			MatchScoutingProvider.KEY_TEAM + " = " + team, null, MatchScoutingProvider.KEY_TEAM);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public int updateMatch(String competition, int matchNum, int team, String slot, boolean broke, boolean autoBridge, boolean autoShooter, boolean balanced, 
    		int shooterType, int top, int mid, int bot, int miss, int topAuto, int midAuto, int botAuto, int missAuto, String notes) {
    	ContentValues args = putVals(false, -1, competition, matchNum, team, slot, broke, autoBridge, autoShooter, balanced,
    			shooterType, top, mid, bot, miss, topAuto, midAuto, botAuto, missAuto, notes);
        
        return activity.getContentResolver().update(MatchScoutingProvider.URI, 
        		args,MatchScoutingProvider.KEY_COMPETITION + " = '" + competition + "' AND " + MatchScoutingProvider.KEY_MATCH_NUM + " = " + matchNum + " AND " + MatchScoutingProvider.KEY_TEAM + " = " + team,
        		null);
    }

    public int updateMatch(int lastMod, String competition, int matchNum, int team, String slot, int broke, int autoBridge, int autoShooter, int balanced, 
    		int shooterType, int top, int mid, int bot, int miss, int topAuto, int midAuto, int botAuto, int missAuto, String notes) {
    	ContentValues args = putVals(false,
    			lastMod, 
    			competition, 
    			matchNum, 
    			team, 
    			slot,
	    		broke==0?false:true, 
				autoBridge==0?false:true, 
				autoShooter==0?false:true, 
	    		balanced==0?false:true, 
	    		shooterType, 
	    		top,
				mid, 
				bot, 
				miss,
	    		topAuto,
				midAuto, 
				botAuto, 
				missAuto,
				notes);
        
        return activity.getContentResolver().update(MatchScoutingProvider.URI,
        		args,MatchScoutingProvider.KEY_COMPETITION + " = " + competition + " AND " + MatchScoutingProvider.KEY_MATCH_NUM + " = " + matchNum + " AND " + MatchScoutingProvider.KEY_TEAM + " = " + team,
        		null);
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int matchNum, int team, String slot, boolean broke, boolean autoBridge, boolean autoShooter, boolean balanced, 
    		int shooterType, int top, int mid, int bot, int miss, int topAuto, int midAuto, int botAuto, int missAuto, String notes) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(MatchScoutingProvider.KEY_COMPETITION, competition);
	    		cv.put(MatchScoutingProvider.KEY_MATCH_NUM, matchNum);
	    		cv.put(MatchScoutingProvider.KEY_TEAM, team);
	    		cv.put(MatchScoutingProvider.KEY_SLOT, slot);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(MatchScoutingProvider.KEY_BROKE, broke);
    	cv.put(MatchScoutingProvider.KEY_AUTO_BRIDGE, autoBridge);
    	cv.put(MatchScoutingProvider.KEY_AUTO_SHOOTER, autoShooter);
    	cv.put(MatchScoutingProvider.KEY_BALANCE, balanced);
    	cv.put(MatchScoutingProvider.KEY_SHOOTER, shooterType);
    	cv.put(MatchScoutingProvider.KEY_TOP, top);
    	cv.put(MatchScoutingProvider.KEY_MID, mid);
    	cv.put(MatchScoutingProvider.KEY_BOT, bot);
    	cv.put(MatchScoutingProvider.KEY_MISS, miss);
    	cv.put(MatchScoutingProvider.KEY_TOP_AUTO, topAuto);
    	cv.put(MatchScoutingProvider.KEY_MID_AUTO, midAuto);
    	cv.put(MatchScoutingProvider.KEY_BOT_AUTO, botAuto);
    	cv.put(MatchScoutingProvider.KEY_MISS_AUTO, missAuto);
        cv.put(MatchScoutingProvider.KEY_NOTES, notes);
        return cv;
    }
}