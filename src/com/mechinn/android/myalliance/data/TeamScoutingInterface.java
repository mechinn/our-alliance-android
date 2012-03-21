package com.mechinn.android.myalliance.data;

import com.mechinn.android.myalliance.providers.MatchScoutingProvider;
import com.mechinn.android.myalliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class TeamScoutingInterface {
	private Activity activity;
	
	public TeamScoutingInterface(Activity act) {
		activity = act;
	}
	
	public void reset() {
		activity.getContentResolver().delete(TeamScoutingProvider.mUriReset, null, null);
	}

    public Uri createTeam(int team, String orientation, int numWheels, int wheelTypes, boolean deadWheel,
    		String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	
        ContentValues initialValues = putVals(true, team, -1, orientation, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, turret, tracking, fender,
    			key, barrier, climb, notes, autonomous);
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
    }

    public Cursor fetchAllTeams() {
    	
    	return activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
        		null, null, null);
    }

    public Cursor fetchTeam(int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
    			TeamScoutingProvider.keyTeam + " = " + team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchTeamNums() throws SQLException {
		return activity.managedQuery(TeamScoutingProvider.mUri, new String[] {TeamScoutingProvider._ID, TeamScoutingProvider.keyTeam}, null, null, null);
    }

    public boolean updateTeam(int team, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues args = putVals(false, team, -1, orientation, numWheels, wheelTypes, 
    		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
			deadWheelType, turret, tracking, fender,
			key, barrier, climb, notes, autonomous);
        
        return activity.getContentResolver().update(TeamScoutingProvider.mUri, args,MatchScoutingProvider.keyTeam + "=" + team,null) > 0;
    }

    public boolean updateTeam(int team, int lastMod, String orientation, int numWheels, int wheelTypes, 
    		int deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, int turret, int tracking, int fender,
    		int key, int barrier, int climb, String notes, int autonomous) {
    	ContentValues args = putVals(false, 
    			team, 
    			lastMod, 
    			orientation, 
    			numWheels, 
    			wheelTypes, 
	    		deadWheel==0?false:true, 
	    		wheel1Type, 
	    		wheel1Diameter, 
	    		wheel2Type, 
	    		wheel2Diameter,
				deadWheelType, 
				turret==0?false:true, 
				tracking==0?false:true, 
				fender==0?false:true,
				key==0?false:true, 
				barrier==0?false:true, 
				climb==0?false:true, 
				notes, 
				autonomous==0?false:true);
        
        return activity.getContentResolver().update(TeamScoutingProvider.mUri, args,TeamScoutingProvider.keyTeam + "=" + team,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int team, int lastMod, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamScoutingProvider.keyTeam, team);
	    		cv.put(TeamScoutingProvider.keyLastMod, 0);
	    	} else {
	    		cv.put(TeamScoutingProvider.keyLastMod, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(TeamScoutingProvider.keyLastMod, lastMod);
    	}
    	cv.put(TeamScoutingProvider.keyOrientation, orientation);
    	cv.put(TeamScoutingProvider.keyNumWheels, numWheels);
    	cv.put(TeamScoutingProvider.keyWheelTypes, wheelTypes);
    	cv.put(TeamScoutingProvider.keyDeadWheel, deadWheel);
    	cv.put(TeamScoutingProvider.keyWheel1Type, wheel1Type);
    	cv.put(TeamScoutingProvider.keyWheel1Diameter, wheel1Diameter);
    	cv.put(TeamScoutingProvider.keyWheel2Type, wheel2Type);
    	cv.put(TeamScoutingProvider.keyWheel2Diameter, wheel2Diameter);
        cv.put(TeamScoutingProvider.keyDeadWheelType, deadWheelType);
        cv.put(TeamScoutingProvider.keyTurret, turret);
        cv.put(TeamScoutingProvider.keyTracking, tracking);
        cv.put(TeamScoutingProvider.keyFenderShooter, fender);
        cv.put(TeamScoutingProvider.keyKeyShooter, key);
        cv.put(TeamScoutingProvider.keyBarrier, barrier);
        cv.put(TeamScoutingProvider.keyClimb, climb);
        cv.put(TeamScoutingProvider.keyNotes, notes);
        cv.put(TeamScoutingProvider.keyAutonomous, autonomous);
        return cv;
    }
}