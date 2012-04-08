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
	private final String TAG = "TeamScoutingInterface";
	private Activity activity;
	
	public TeamScoutingInterface(Activity act) {
		activity = act;
	}
	
	public int deleteTeam(int team) {
		return activity.getContentResolver().delete(TeamScoutingProvider.URI, TeamScoutingProvider.KEY_TEAM+" = '"+team+"'", null);
	}
	
	public Uri createTeam(String competition, int team) {
		HashSet<String> competitions = new HashSet<String>();
		competitions.add(competition);
		return createTeam(competitions,team);
	}
	
	public Uri createTeam(HashSet<String> competitions, int team) {
		ContentValues initialValues = putVals(true,-1,team,competitions,0,"None",0,0,0,1,false,"None",0,"None",0,"None",false,false,false,false,false,false,false,0,0,0,0,0,0,"");
        
        return activity.getContentResolver().insert(TeamScoutingProvider.URI, initialValues);
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
        
        return activity.getContentResolver().insert(TeamScoutingProvider.URI, initialValues);
    }
    
    public Cursor fetchAllTeams() {
        return fetchAllTeams(TeamScoutingProvider.KEY_TEAM, false);
    }

    public Cursor fetchAllTeams(String orderBy, boolean desc) {
    	return fetchAllTeams(TeamScoutingProvider.SCHEMA_ARRAY.toArray(),orderBy,desc);
    }
    
    public Cursor fetchAllTeams(String[] from,String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, from,
        		null, null, orderBy+(desc?" DESC":""));
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchCompetitionTeams(String competition) {
        return fetchCompetitionTeams(competition, TeamScoutingProvider.KEY_TEAM, false);
    }
    
    public Cursor fetchCompetitionTeams(String competition, String orderBy, boolean desc) {
        return fetchCompetitionTeams(competition, TeamScoutingProvider.SCHEMA_ARRAY.toArray(), orderBy, desc);
    }
    
    public Cursor fetchCompetitionTeams(String competition, String[] from, String orderBy, boolean desc) {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, from,
    			competition + " = 1", null, orderBy+(desc?" DESC":""));
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTeam(int team) throws SQLException {
    	for(String s : TeamScoutingProvider.SCHEMA_ARRAY.toArray()) {
    		Log.d(TAG,s);
    	}
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, TeamScoutingProvider.SCHEMA_ARRAY.toArray(),
    			TeamScoutingProvider.KEY_TEAM + " = "+ team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchTeam(String competition, int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, TeamScoutingProvider.SCHEMA_ARRAY.toArray(),
    			TeamScoutingProvider.KEY_TEAM + " = "+ team +" AND " + competition + " = 1", null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchTeamNotes(int team) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, new String[] {TeamScoutingProvider.KEY_NOTES},
    			TeamScoutingProvider.KEY_TEAM + " = "+ team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchAllTeamNums() throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, new String[] {DatabaseConnection._ID, TeamScoutingProvider.KEY_TEAM}, null, null, null);
		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchCompetitionTeamNums(String competition) throws SQLException {
    	Cursor mCursor = activity.managedQuery(TeamScoutingProvider.URI, new String[] {DatabaseConnection._ID, TeamScoutingProvider.KEY_TEAM}, 
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
        
        return activity.getContentResolver().update(TeamScoutingProvider.URI, args,MatchScoutingProvider.KEY_TEAM + " = " + team,null);
    }

    public int updateTeam(int team, boolean fender, boolean key, boolean autoBridge, boolean autoShooter, double avgAuto, double avgHoops, double avgBalance, double avgBroke) {
    	ContentValues args = new ContentValues();
    	args.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
    	args.put(TeamScoutingProvider.KEY_FENDER_SHOOTER, fender);
    	args.put(TeamScoutingProvider.KEY_KEY_SHOOTER, key);
    	args.put(TeamScoutingProvider.KEY_AUTO_BRIDGE, autoBridge);
    	args.put(TeamScoutingProvider.KEY_AUTO_SHOOTER, autoShooter);
    	args.put(TeamScoutingProvider.KEY_AVG_AUTO, avgAuto);
    	args.put(TeamScoutingProvider.KEY_AVG_HOOPS, avgHoops);
    	args.put(TeamScoutingProvider.KEY_AVG_BALANCE, avgBalance);
    	args.put(TeamScoutingProvider.KEY_AVG_BROKE, avgBroke);
        return activity.getContentResolver().update(TeamScoutingProvider.URI, args, MatchScoutingProvider.KEY_TEAM + " = " + team,null);
    }
    
    private ContentValues putVals(boolean create, int lastMod, int team, HashSet<String> competitions, int rank, String orientation,
    		double width, double height, int numWheels, int wheelTypes, boolean deadWheel, String wheel1Type, double wheel1Diameter,
    		String wheel2Type, double wheel2Diameter, String deadWheelType, boolean tracking, boolean fender, boolean key,
    		boolean barrier, boolean climb, boolean autoBridge, boolean autoShooter, float shooting, float balancing, double avgAuto,
    		double avgHoops, double avgBalance, double avgBroke, String notes) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamScoutingProvider.KEY_TEAM, team);
	    		cv.put(DatabaseConnection._LASTMOD, 0);
	    	} else {
	    		cv.put(DatabaseConnection._LASTMOD, System.currentTimeMillis());
	    	}
    	} else {
    		cv.put(DatabaseConnection._LASTMOD, lastMod);
    	}
    	cv.put(TeamScoutingProvider.KEY_RANK, rank);
    	cv.put(TeamScoutingProvider.KEY_ORIENTATION, orientation);
    	cv.put(TeamScoutingProvider.KEY_WIDTH, width);
    	cv.put(TeamScoutingProvider.KEY_HEIGHT, height);
    	cv.put(TeamScoutingProvider.KEY_NUM_WHEELS, numWheels);
    	cv.put(TeamScoutingProvider.KEY_WHEEL_TYPES, wheelTypes);
    	cv.put(TeamScoutingProvider.KEY_DEAD_WHEEL, deadWheel);
    	cv.put(TeamScoutingProvider.KEY_WHEEL1_TYPE, wheel1Type);
    	cv.put(TeamScoutingProvider.KEY_WHEEL1_DIAMETER, wheel1Diameter);
    	cv.put(TeamScoutingProvider.KEY_WHEEL2_TYPE, wheel2Type);
    	cv.put(TeamScoutingProvider.KEY_WHEEL2_DIAMETER, wheel2Diameter);
        cv.put(TeamScoutingProvider.KEY_DEAD_WHEEL_TYPE, deadWheelType);
        cv.put(TeamScoutingProvider.KEY_TRACKING, tracking);
        cv.put(TeamScoutingProvider.KEY_FENDER_SHOOTER, fender);
        cv.put(TeamScoutingProvider.KEY_KEY_SHOOTER, key);
        cv.put(TeamScoutingProvider.KEY_BARRIER, barrier);
        cv.put(TeamScoutingProvider.KEY_CLIMB, climb);
        cv.put(TeamScoutingProvider.KEY_AUTO_BRIDGE, autoBridge);
        cv.put(TeamScoutingProvider.KEY_AUTO_SHOOTER, autoShooter);
        cv.put(TeamScoutingProvider.KEY_SHOOTING_RATING, shooting);
        cv.put(TeamScoutingProvider.KEY_BALANCING_RATING, balancing);
        cv.put(TeamScoutingProvider.KEY_AVG_AUTO, avgAuto);
        cv.put(TeamScoutingProvider.KEY_AVG_HOOPS, avgHoops);
        cv.put(TeamScoutingProvider.KEY_AVG_BALANCE, avgBalance);
        cv.put(TeamScoutingProvider.KEY_AVG_BROKE, avgBroke);
        cv.put(TeamScoutingProvider.KEY_NOTES, notes);
        
        for(String allCompetitions : TeamScoutingProvider.COMPETITIONS) {
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