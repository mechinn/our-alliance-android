package com.mechinn.android.ouralliance.data;

import java.util.HashSet;
import java.util.List;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

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
	
	public Uri createTeam(String competition, int team) {
		HashSet<String> competitions = new HashSet<String>();
		competitions.add(competition);
		ContentValues initialValues = putVals(true, team, -1,"None",0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,"",false, competitions);
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
	}
	
	public Uri createTeam(HashSet<String> competitions, int team) {
		ContentValues initialValues = putVals(true, team, -1,"None",0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,"",false, competitions);
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
	}

    public Uri createTeam(int team, String orientation, int numWheels, int wheelTypes, boolean deadWheel,
    		String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous, HashSet<String> competitions) {
    	
        ContentValues initialValues = putVals(true, team, -1, orientation, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, turret, tracking, fender,
    			key, barrier, climb, notes, autonomous, competitions);
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
    }
    
    public Cursor fetchAllTeams() {
        return fetchAllTeams(TeamScoutingProvider.keyTeam, false);
    }

    public Cursor fetchAllTeams(String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
        		null, null, orderBy+(desc?" DESC":""));
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchCompetitionTeams(String competition) {
        return fetchCompetitionTeams(competition, TeamScoutingProvider.keyTeam, false);
    }
    
    public Cursor fetchCompetitionTeams(String competition, String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
    			competition + " = 1", null, orderBy+(desc?" DESC":""));
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTeam(int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
    			TeamScoutingProvider.keyTeam + " = " + team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchTeam(String competition, int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray,
    			TeamScoutingProvider.keyTeam + " = " + team + " AND " + competition + " = 1", null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchAllTeamNums() throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, new String[] {DatabaseConnection._ID, TeamScoutingProvider.keyTeam}, null, null, null);
		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchCompetitionTeamNums(String competition) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, new String[] {DatabaseConnection._ID, TeamScoutingProvider.keyTeam}, 
    			competition + " = 1", null, null);
		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateTeam(int team, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous, HashSet<String> competitions) {
    	ContentValues args = putVals(false, team, -1, orientation, numWheels, wheelTypes, 
    		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
			deadWheelType, turret, tracking, fender,
			key, barrier, climb, notes, autonomous, competitions);
        
        return activity.getContentResolver().update(TeamScoutingProvider.mUri, args,MatchScoutingProvider.keyTeam + " = '" + team + "'",null) > 0;
    }
    
    private ContentValues putVals(boolean create, int team, int lastMod, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous, HashSet<String> competitions) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamScoutingProvider.keyTeam, team);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
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
        for(String allCompetitions : TeamScoutingProvider.competitions) {
        	for(String competition : competitions) {
	        	if(competition.equals(allCompetitions)) {
	        		cv.put(competition, 1);
	        	} else {
	        		cv.put(competition, 0);
	        	}
        	}
        }
        
        return cv;
    }
}