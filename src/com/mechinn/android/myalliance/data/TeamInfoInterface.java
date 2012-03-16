package com.mechinn.android.myalliance.data;

import java.util.Date;

import com.mechinn.android.myalliance.providers.TeamInfoProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class TeamInfoInterface {
	Activity activity;
	
	public TeamInfoInterface(Activity act) {
		activity = act;
	}
	
	public void reset() {
		activity.getContentResolver().delete(TeamInfoProvider.CONTENT_URI_RESET, null, null);
	}

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public Uri createTeam(int team, String orientation, int numWheels, int wheelTypes, boolean deadWheel,
    		String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	
        ContentValues initialValues = putVals(true, team, -1, orientation, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, turret, tracking, fender,
    			key, barrier, climb, notes, autonomous);
        
        return activity.getContentResolver().insert(TeamInfoProvider.CONTENT_URI, initialValues);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllTeams() {
    	
    	return activity.managedQuery(TeamInfoProvider.CONTENT_URI, new String[] {TeamInfoProvider._ID, TeamInfoProvider.keyLastMod, 
    			TeamInfoProvider.keyTeam, TeamInfoProvider.keyOrientation, TeamInfoProvider.keyNumWheels, TeamInfoProvider.keyWheelTypes, 
    			TeamInfoProvider.keyDeadWheel, TeamInfoProvider.keyWheel1Type, TeamInfoProvider.keyWheel1Diameter, 
    			TeamInfoProvider.keyWheel2Type, TeamInfoProvider.keyWheel2Diameter, TeamInfoProvider.keyDeadWheelType, 
    			TeamInfoProvider.keyTurret, TeamInfoProvider.keyTracking, TeamInfoProvider.keyFenderShooter, TeamInfoProvider.keyKeyShooter, 
    			TeamInfoProvider.keyBarrier, TeamInfoProvider.keyClimb, TeamInfoProvider.keyNotes, TeamInfoProvider.keyAutonomous},
        		null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchTeam(int team) throws SQLException {

    	Cursor mCursor = activity.managedQuery(TeamInfoProvider.CONTENT_URI, new String[] {TeamInfoProvider._ID, TeamInfoProvider.keyLastMod, 
    			TeamInfoProvider.keyTeam, TeamInfoProvider.keyOrientation, TeamInfoProvider.keyNumWheels, TeamInfoProvider.keyWheelTypes, 
    			TeamInfoProvider.keyDeadWheel, TeamInfoProvider.keyWheel1Type, TeamInfoProvider.keyWheel1Diameter, 
    			TeamInfoProvider.keyWheel2Type, TeamInfoProvider.keyWheel2Diameter, TeamInfoProvider.keyDeadWheelType, 
    			TeamInfoProvider.keyTurret, TeamInfoProvider.keyTracking, TeamInfoProvider.keyFenderShooter, TeamInfoProvider.keyKeyShooter, 
    			TeamInfoProvider.keyBarrier, TeamInfoProvider.keyClimb, TeamInfoProvider.keyNotes, TeamInfoProvider.keyAutonomous},
    			TeamInfoProvider.keyTeam + " = " + team, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchTeamNums() throws SQLException {
		return activity.managedQuery(TeamInfoProvider.CONTENT_URI, new String[] {TeamInfoProvider._ID, TeamInfoProvider.keyTeam}, null, null, null);
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateTeam(int team, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues args = putVals(false, team, -1, orientation, numWheels, wheelTypes, 
    		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
			deadWheelType, turret, tracking, fender,
			key, barrier, climb, notes, autonomous);
        
        return activity.getContentResolver().update(TeamInfoProvider.CONTENT_URI, args,null,null) > 0;
    }
    
    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
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
        
        return activity.getContentResolver().update(TeamInfoProvider.CONTENT_URI, args,TeamInfoProvider.keyTeam + "=" + team,null) > 0;
    }
    
    private ContentValues putVals(boolean create, int team, int lastMod, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(TeamInfoProvider.keyTeam, team);
	    		cv.put(TeamInfoProvider.keyLastMod, 0);
	    	} else {
	    		cv.put(TeamInfoProvider.keyLastMod, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(TeamInfoProvider.keyLastMod, lastMod);
    	}
    	cv.put(TeamInfoProvider.keyOrientation, orientation);
    	cv.put(TeamInfoProvider.keyNumWheels, numWheels);
    	cv.put(TeamInfoProvider.keyWheelTypes, wheelTypes);
    	cv.put(TeamInfoProvider.keyDeadWheel, deadWheel);
    	cv.put(TeamInfoProvider.keyWheel1Type, wheel1Type);
    	cv.put(TeamInfoProvider.keyWheel1Diameter, wheel1Diameter);
    	cv.put(TeamInfoProvider.keyWheel2Type, wheel2Type);
    	cv.put(TeamInfoProvider.keyWheel2Diameter, wheel2Diameter);
        cv.put(TeamInfoProvider.keyDeadWheelType, deadWheelType);
        cv.put(TeamInfoProvider.keyTurret, turret);
        cv.put(TeamInfoProvider.keyTracking, tracking);
        cv.put(TeamInfoProvider.keyFenderShooter, fender);
        cv.put(TeamInfoProvider.keyKeyShooter, key);
        cv.put(TeamInfoProvider.keyBarrier, barrier);
        cv.put(TeamInfoProvider.keyClimb, climb);
        cv.put(TeamInfoProvider.keyNotes, notes);
        cv.put(TeamInfoProvider.keyAutonomous, autonomous);
        return cv;
    }
}