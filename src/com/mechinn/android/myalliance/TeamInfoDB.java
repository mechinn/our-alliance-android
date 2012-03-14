package com.mechinn.android.myalliance;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
public class TeamInfoDB extends GeneralDB {
    public static final String DBTable = "teams";
    private static final int DBVersion = 2;
    
    public static final String KEY_TEAM = "team";
    public static final String KEY_ORIENTATION = "orientation";
    public static final String KEY_NUMWHEELS = "numWheels";
    public static final String KEY_WHEELTYPES = "wheelTypes";
    public static final String KEY_DEADWHEEL = "deadWheel";
    public static final String KEY_WHEEL1TYPE = "wheel1Type";
    public static final String KEY_WHEEL1DIAMETER = "wheel1Diameter";
    public static final String KEY_WHEEL2TYPE = "wheel2Type";
    public static final String KEY_WHEEL2DIAMETER = "wheel2Diameter";
    public static final String KEY_DEADWHEELTYPE = "deadWheelType";
    public static final String KEY_TURRET = "turret";
    public static final String KEY_TRACKING = "tracking";
    public static final String KEY_FENDER = "fendershooter";
    public static final String KEY_KEY = "keyshooter";
    public static final String KEY_BARRIER = "barrier";
    public static final String KEY_CLIMB = "climb";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_AUTONOMOUS = "autonomous";
    private static final String TAG = "TeamInfoDb";

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table teams ("+keyRowID+" integer primary key autoincrement, "+
        		keyLastMod+" int not null, "+
        		KEY_TEAM+" int not null unique, " +
        		KEY_ORIENTATION+" text, " +
        		KEY_NUMWHEELS+" int, " +
        		KEY_WHEELTYPES+" int, " +
        		KEY_DEADWHEEL+" int, " +
        		KEY_WHEEL1TYPE+" text, " +
        		KEY_WHEEL1DIAMETER+" int, " +
        		KEY_WHEEL2TYPE+" text, " +
        		KEY_WHEEL2DIAMETER+" int, " +
        		KEY_DEADWHEELTYPE+" text, " +
        		KEY_TURRET+" int, " +
        		KEY_TRACKING+" int, " +
        		KEY_FENDER+" int, " +
        		KEY_KEY+" int, " +
        		KEY_BARRIER+" int, " +
		        KEY_CLIMB+" int, " +
		        KEY_NOTES+" text);";
    
    public TeamInfoDB(Context context, boolean writable) {
    	super(context, DBName, null, DBVersion);
    	if(writable) {
    		mDB = getWritableDatabase();
    	} else {
    		mDB = getReadableDatabase();
    	}
	}
    
    public TeamInfoDB(Context context) {
    	this(context,false);
	}

    public void onCreate(SQLiteDatabase db) {
    	//setup original db and upgrade to latest
    	Log.d("onCreate",DATABASE_CREATE);
    	db.execSQL(DATABASE_CREATE);
    	onUpgrade(db,1,DBVersion);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(TAG, "v1 original team info table");
	    		db.execSQL("DROP TABLE IF EXISTS "+DBTable);
	    		Log.d("onCreate",DATABASE_CREATE);
	    		db.execSQL(DATABASE_CREATE);
    		case 2:
    			Log.i(TAG, "v2 added autonomous column");
        		db.execSQL("alter table "+DBTable+" add column "+KEY_AUTONOMOUS+" int;");
    		case 3:
//    			Log.i(TAG, "v3 ");
    	}
    }

    public void close() {
    	mDB.close();
    }
    
    public void reset() {
    	onUpgrade(mDB,0,1);
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
    public long createTeam(int team, String orientation, int numWheels, int wheelTypes, boolean deadWheel,
    		String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	
        ContentValues initialValues = putVals(true, team, -1, orientation, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, turret, tracking, fender,
    			key, barrier, climb, notes, autonomous);

        return mDB.insert(DBTable, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTeam(long rowId) {

        return mDB.delete(DBTable, keyRowID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllTeams() {

        return mDB.query(DBTable, new String[] {keyRowID, keyLastMod, KEY_TEAM, 
        		KEY_ORIENTATION, KEY_NUMWHEELS, KEY_WHEELTYPES, KEY_DEADWHEEL, KEY_WHEEL1TYPE, 
        		KEY_WHEEL1DIAMETER, KEY_WHEEL2TYPE, KEY_WHEEL2DIAMETER, KEY_DEADWHEELTYPE, 
        		KEY_TURRET, KEY_TRACKING, KEY_FENDER, KEY_KEY, KEY_BARRIER, KEY_CLIMB, KEY_NOTES, KEY_AUTONOMOUS}, 
        		null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchTeam(int team) throws SQLException {

        Cursor mCursor = mDB.query(true, DBTable, new String[] {keyRowID, keyLastMod, KEY_TEAM, 
            		KEY_ORIENTATION, KEY_NUMWHEELS, KEY_WHEELTYPES, KEY_DEADWHEEL, KEY_WHEEL1TYPE, 
            		KEY_WHEEL1DIAMETER, KEY_WHEEL2TYPE, KEY_WHEEL2DIAMETER, KEY_DEADWHEELTYPE, 
            		KEY_TURRET, KEY_TRACKING, KEY_FENDER, KEY_KEY, KEY_BARRIER, KEY_CLIMB, KEY_NOTES, KEY_AUTONOMOUS}, KEY_TEAM + "=" + team, null,
                    null, null, null, null);
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

    	return mDB.query(true, DBTable, new String[] {keyRowID, KEY_TEAM}, null, null,
                    null, null, null, null);

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

        return mDB.update(DBTable, args, KEY_TEAM + "=" + team, null) > 0;
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

        return mDB.update(DBTable, args, KEY_TEAM + "=" + team, null) > 0;
    }
    
    private ContentValues putVals(boolean create, int team, int lastMod, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues cv = new ContentValues();
    	if(lastMod<0) {
	    	if(create) {
	    		cv.put(KEY_TEAM, team);
	    		cv.put(keyLastMod, 0);
	    	} else {
	    		cv.put(keyLastMod, System.currentTimeMillis()/1000);
	    	}
    	} else {
    		cv.put(keyLastMod, lastMod);
    	}
    	cv.put(KEY_ORIENTATION, orientation);
    	cv.put(KEY_NUMWHEELS, numWheels);
    	cv.put(KEY_WHEELTYPES, wheelTypes);
    	cv.put(KEY_DEADWHEEL, deadWheel);
    	cv.put(KEY_WHEEL1TYPE, wheel1Type);
    	cv.put(KEY_WHEEL1DIAMETER, wheel1Diameter);
    	cv.put(KEY_WHEEL2TYPE, wheel2Type);
    	cv.put(KEY_WHEEL2DIAMETER, wheel2Diameter);
        cv.put(KEY_DEADWHEELTYPE, deadWheelType);
        cv.put(KEY_TURRET, turret);
        cv.put(KEY_TRACKING, tracking);
        cv.put(KEY_FENDER, fender);
        cv.put(KEY_KEY, key);
        cv.put(KEY_BARRIER, barrier);
        cv.put(KEY_CLIMB, climb);
        cv.put(KEY_NOTES, notes);
        cv.put(KEY_AUTONOMOUS, autonomous);
        return cv;
    }
}