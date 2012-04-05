package com.mechinn.android.ouralliance.data;

import java.util.HashSet;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class TeamScoutingInterface {
	private final String logTag = "TeamScoutingInterface";
	private Activity activity;
	
	public TeamScoutingInterface(Activity act) {
		activity = act;
	}
	
	public int deleteTeam(int team) {
		return activity.getContentResolver().delete(TeamScoutingProvider.mUri, TeamScoutingProvider.keyTeam+" = '"+team+"'", null);
	}
	
	public Uri createTeam(String competition, int team) {
		HashSet<String> competitions = new HashSet<String>();
		competitions.add(competition);
		return createTeam(competitions,team);
	}
	
	public Uri createTeam(HashSet<String> competitions, int team) {
		ContentValues initialValues = putVals(true,-1,team,competitions,0,"None",0,0,0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,false,0,0,0,0,0,0,"");
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
	}

    public Uri createTeam(int team, HashSet<String> competitions, int rank, String orientation, double width, double height, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, double wheel1Diameter, String wheel2Type, double wheel2Diameter,
    		String deadWheelType, boolean tracking, boolean fender, boolean key, boolean barrier, boolean climb,
    		boolean autoBridge, boolean autoShooter, float shooting, float balancing, double avgAuto,
    		double avgHoops, double avgBalance, double avgBroke, String notes) {
    	
        ContentValues initialValues = putVals(true, -1, team, competitions, rank, orientation, width, height, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, tracking, fender, key, barrier, climb, autoBridge, autoShooter, shooting, balancing,
    			avgAuto, avgHoops, avgBalance, avgBroke, notes);
        
        return activity.getContentResolver().insert(TeamScoutingProvider.mUri, initialValues);
    }
    
    public Cursor fetchAllTeams() {
        return fetchAllTeams(TeamScoutingProvider.keyTeam, false);
    }

    public Cursor fetchAllTeams(String orderBy, boolean desc) {
    	return fetchAllTeams(TeamScoutingProvider.schemaArray.toArray(),orderBy,desc);
    }
    
    public Cursor fetchAllTeams(String[] from,String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, from,
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
        return fetchCompetitionTeams(competition, TeamScoutingProvider.schemaArray.toArray(), orderBy, desc);
    }
    
    public Cursor fetchCompetitionTeams(String competition, String[] from, String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, from,
    			competition + " = 1", null, orderBy+(desc?" DESC":""));
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTeam(int team) throws SQLException {
    	for(String s : TeamScoutingProvider.schemaArray.toArray()) {
    		Log.d(logTag,s);
    	}
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray.toArray(),
    			TeamScoutingProvider.keyTeam + " = "+ team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchTeam(String competition, int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, TeamScoutingProvider.schemaArray.toArray(),
    			TeamScoutingProvider.keyTeam + " = "+ team +" AND " + competition + " = 1", null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchTeamNotes(int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.mUri, new String[] {TeamScoutingProvider.keyNotes},
    			TeamScoutingProvider.keyTeam + " = "+ team, null, null);
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
    
    public int updateTeam(int team, HashSet<String> competitions, int rank, String orientation, double width, double height, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, double wheel1Diameter, String wheel2Type, double wheel2Diameter,
    		String deadWheelType, boolean tracking, boolean fender, boolean key, boolean barrier, boolean climb,
    		boolean autoBridge, boolean autoShooter, float shooting, float balancing, double avgAuto,
    		double avgHoops, double avgBalance, double avgBroke, String notes) {
    	ContentValues args = putVals(false, -1, team, competitions, rank, orientation, width, height, numWheels, wheelTypes, 
    		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
			deadWheelType, tracking, fender,
			key, barrier, climb, autoBridge, autoShooter, shooting, balancing,
			avgAuto, avgHoops, avgBalance, avgBroke, notes);
        
        return activity.getContentResolver().update(TeamScoutingProvider.mUri, args,MatchScoutingProvider.keyTeam + " = " + team,null);
    }

    public int updateTeam(int team, boolean fender, boolean key, boolean autoBridge, boolean autoShooter, double avgAuto, double avgHoops, double avgBalance, double avgBroke) {
    	ContentValues args = new ContentValues();
    	args.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
    	args.put(TeamScoutingProvider.keyFenderShooter, fender);
    	args.put(TeamScoutingProvider.keyKeyShooter, key);
    	args.put(TeamScoutingProvider.keyAutoBridge, autoBridge);
    	args.put(TeamScoutingProvider.keyAutoShooter, autoShooter);
    	args.put(TeamScoutingProvider.keyAvgAuto, avgAuto);
    	args.put(TeamScoutingProvider.keyAvgHoops, avgHoops);
    	args.put(TeamScoutingProvider.keyAvgBalance, avgBalance);
    	args.put(TeamScoutingProvider.keyAvgBroke, avgBroke);
        return activity.getContentResolver().update(TeamScoutingProvider.mUri, args, MatchScoutingProvider.keyTeam + " = " + team,null);
    }
    
    private ContentValues putVals(boolean create, int lastMod, int team, HashSet<String> competitions, int rank, String orientation,
    		double width, double height, int numWheels, int wheelTypes, boolean deadWheel, String wheel1Type, double wheel1Diameter,
    		String wheel2Type, double wheel2Diameter, String deadWheelType, boolean tracking, boolean fender, boolean key,
    		boolean barrier, boolean climb, boolean autoBridge, boolean autoShooter, float shooting, float balancing, double avgAuto,
    		double avgHoops, double avgBalance, double avgBroke, String notes) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamScoutingProvider.keyTeam, team);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(TeamScoutingProvider.keyRank, rank);
    	cv.put(TeamScoutingProvider.keyOrientation, orientation);
    	cv.put(TeamScoutingProvider.keyWidth, width);
    	cv.put(TeamScoutingProvider.keyHeight, height);
    	cv.put(TeamScoutingProvider.keyNumWheels, numWheels);
    	cv.put(TeamScoutingProvider.keyWheelTypes, wheelTypes);
    	cv.put(TeamScoutingProvider.keyDeadWheel, deadWheel);
    	cv.put(TeamScoutingProvider.keyWheel1Type, wheel1Type);
    	cv.put(TeamScoutingProvider.keyWheel1Diameter, wheel1Diameter);
    	cv.put(TeamScoutingProvider.keyWheel2Type, wheel2Type);
    	cv.put(TeamScoutingProvider.keyWheel2Diameter, wheel2Diameter);
        cv.put(TeamScoutingProvider.keyDeadWheelType, deadWheelType);
        cv.put(TeamScoutingProvider.keyTracking, tracking);
        cv.put(TeamScoutingProvider.keyFenderShooter, fender);
        cv.put(TeamScoutingProvider.keyKeyShooter, key);
        cv.put(TeamScoutingProvider.keyBarrier, barrier);
        cv.put(TeamScoutingProvider.keyClimb, climb);
        cv.put(TeamScoutingProvider.keyAutoBridge, autoBridge);
        cv.put(TeamScoutingProvider.keyAutoShooter, autoShooter);
        cv.put(TeamScoutingProvider.keyShootingRating, shooting);
        cv.put(TeamScoutingProvider.keyBalancingRating, balancing);
        cv.put(TeamScoutingProvider.keyAvgAuto, avgAuto);
        cv.put(TeamScoutingProvider.keyAvgHoops, avgHoops);
        cv.put(TeamScoutingProvider.keyAvgBalance, avgBalance);
        cv.put(TeamScoutingProvider.keyAvgBroke, avgBroke);
        cv.put(TeamScoutingProvider.keyNotes, notes);
        
        for(String allCompetitions : TeamScoutingProvider.competitions) {
        	if(competitions.contains("All")){
        		cv.put(allCompetitions, 1);
        	} else {
        		for(String competition : competitions) {
    	        	if(competition.equals(allCompetitions)) {
    	        		cv.put(competition, 1);
    	        	} else {
    	        		cv.put(competition, 0);
    	        	}
            	}
        	}
        }
        
        return cv;
    }
}