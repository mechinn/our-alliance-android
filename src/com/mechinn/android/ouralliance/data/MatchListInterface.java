package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.MatchListProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MatchListInterface {
	private final String TAG = "MatchListInterface";
	private Activity activity;
	
	public MatchListInterface(Activity act) {
		activity = act;
	}
	
	public int deleteMatch(String comp, int matchNum) {
		int total = 0;
		MatchScoutingInterface matchScore = new MatchScoutingInterface(activity);
		total+=matchScore.deleteMatch(comp, matchNum);
		total+=delete(comp,matchNum);
		return total;
	}
	
	public void addMatch(String comp, int matchNum, long time, int red1, int red2, int red3, int blue1, int blue2, int blue3) {
		MatchScoutingInterface matchScore = new MatchScoutingInterface(activity);
    	createMatch(comp,matchNum,time,red1,red2,red3,blue1,blue2,blue3);
    	matchScore.createMatch(comp,matchNum,red1,"Red 1", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    	matchScore.createMatch(comp,matchNum,red2,"Red 2", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    	matchScore.createMatch(comp,matchNum,red3,"Red 3", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    	matchScore.createMatch(comp,matchNum,blue1,"Blue 1", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    	matchScore.createMatch(comp,matchNum,blue2,"Blue 2", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    	matchScore.createMatch(comp,matchNum,blue3,"Blue 3", false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
    }
	
	public int delete(String comp, int matchNum) {
		return activity.getContentResolver().delete(MatchListProvider.URI, MatchListProvider.KEY_COMPETITON+" = '"+comp+"' AND "+MatchListProvider.KEY_MATCH_NUM+" = "+matchNum, null);
	}

    public Uri createMatch(String competition, int matchNum, long time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	
        ContentValues initialValues = putVals(true, -1, competition, matchNum, 
    			time, red1, red2, red3, blue1, blue2, blue3);
        
        return activity.getContentResolver().insert(MatchListProvider.URI, initialValues);
    }

    public Cursor fetchAllMatches() {
    	Cursor mCursor = activity.managedQuery(MatchListProvider.URI, MatchListProvider.SCHEMA_ARRAY.toArray(), null, null, MatchListProvider.KEY_MATCH_NUM);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchMatches(String competition) {
    	Cursor mCursor = activity.managedQuery(MatchListProvider.URI, MatchListProvider.SCHEMA_ARRAY.toArray(),
    			MatchListProvider.KEY_COMPETITON + " = '" + competition + "'", null, MatchListProvider.KEY_MATCH_NUM);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMatch(String competition, int matchNum) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchListProvider.URI, MatchListProvider.SCHEMA_ARRAY.toArray(),
    			MatchListProvider.KEY_COMPETITON + " = '" + competition + "' AND " + MatchListProvider.KEY_MATCH_NUM + " = " + matchNum, null, MatchListProvider.KEY_MATCH_NUM);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateMatch(int lastMod, String competition, int matchNum, long time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	ContentValues args = putVals(false, lastMod, competition, matchNum, 
    			time, red1, red2, red3, blue1, blue2, blue3);
        
        return activity.getContentResolver().update(MatchListProvider.URI, args,MatchListProvider.KEY_COMPETITON + " = '" + competition + "' AND " + MatchListProvider.KEY_MATCH_NUM + " = " + matchNum,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int matchNum, long time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	        	cv.put(MatchListProvider.KEY_COMPETITON, competition);
	        	cv.put(MatchListProvider.KEY_MATCH_NUM, matchNum);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(MatchListProvider.KEY_TIME, time);
    	cv.put(MatchListProvider.KEY_RED1, red1);
    	cv.put(MatchListProvider.KEY_RED2, red2);
    	cv.put(MatchListProvider.KEY_RED3, red3);
    	cv.put(MatchListProvider.KEY_BLUE1, blue1);
    	cv.put(MatchListProvider.KEY_BLUE2, blue2);
        cv.put(MatchListProvider.KEY_BLUE3, blue3);
        return cv;
    }
}