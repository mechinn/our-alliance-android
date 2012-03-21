package com.mechinn.android.myalliance.data;

import com.mechinn.android.myalliance.providers.MatchListProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MatchListInterface {
	private Activity activity;
	
	public MatchListInterface(Activity act) {
		activity = act;
	}
	
	public void reset() {
		activity.getContentResolver().delete(MatchListProvider.mUriReset, null, null);
	}

    public Uri createMatch(String competition, int matchNum, int time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	
        ContentValues initialValues = putVals(true, -1, competition, matchNum, 
    			time, red1, red2, red3, blue1, blue2, blue3);
        
        return activity.getContentResolver().insert(MatchListProvider.mUri, initialValues);
    }

    public Cursor fetchAllMatches() {
    	
    	return activity.managedQuery(MatchListProvider.mUri, MatchListProvider.schemaArray, null, null, null);
    }

    public Cursor fetchMatch(String competition, int matchNum) throws SQLException {

    	Cursor mCursor = activity.managedQuery(MatchListProvider.mUri, MatchListProvider.schemaArray,
    			MatchListProvider.keyCompetition + " = " + competition + " AND " + MatchListProvider.keyMatchNum + " = " + matchNum, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateMatch(int lastMod, String competition, int matchNum, int time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	ContentValues args = putVals(false, lastMod, competition, matchNum, 
    			time, red1, red2, red3, blue1, blue2, blue3);
        
        return activity.getContentResolver().update(MatchListProvider.mUri, args,MatchListProvider.keyCompetition + " = " + competition + " AND " + MatchListProvider.keyMatchNum + " = " + matchNum,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int lastMod, String competition, int matchNum, int time, 
    		int red1, int red2, int red3, int blue1, int blue2, int blue3) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	        	cv.put(MatchListProvider.keyCompetition, competition);
	        	cv.put(MatchListProvider.keyMatchNum, matchNum);
	    		cv.put(MatchListProvider.keyLastMod, 0);
	    	} else {
	    		cv.put(MatchListProvider.keyLastMod, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(MatchListProvider.keyLastMod, lastMod);
    	}
    	cv.put(MatchListProvider.keyTime, time);
    	cv.put(MatchListProvider.keyRed1, red1);
    	cv.put(MatchListProvider.keyRed2, red2);
    	cv.put(MatchListProvider.keyRed3, red3);
    	cv.put(MatchListProvider.keyBlue1, blue1);
    	cv.put(MatchListProvider.keyBlue2, blue2);
        cv.put(MatchListProvider.keyBlue3, blue3);
        return cv;
    }
}