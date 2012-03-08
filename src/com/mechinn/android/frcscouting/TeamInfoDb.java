package com.mechinn.android.frcscouting;

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
public class TeamInfoDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "frcscoutingdb";
    private static final String DATABASE_TABLE = "teams";
    private static final int DATABASE_VERSION = 2;
    
	public static final String KEY_ROWID = "_id";
    public static final String KEY_LASTMOD = "_lastMod";
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
    public static final String KEY_FENDER = "fender";
    public static final String KEY_KEY = "key";
    public static final String KEY_BARRIER = "barrier";
    public static final String KEY_CLIMB = "climb";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_AUTONOMOUS = "autonomous";
    private static final String TAG = "TeamInfoDb";
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table teams ("+KEY_ROWID+" integer primary key autoincrement, "+
        		KEY_LASTMOD+" int not null, "+
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
    
    public TeamInfoDb(Context context, boolean writable) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	if(writable) {
    		mDb = getWritableDatabase();
    	} else {
    		mDb = getReadableDatabase();
    	}
	}
    
    public TeamInfoDb(Context context) {
    	this(context,false);
	}

    public void onCreate(SQLiteDatabase db) {
    	//setup original db and upgrade to latest
    	Log.d("onCreate",DATABASE_CREATE);
    	db.execSQL(DATABASE_CREATE);
    	onUpgrade(db,1,DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(TAG, "v1 original team info table");
	    		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
	    		Log.d("onCreate",DATABASE_CREATE);
	    		db.execSQL(DATABASE_CREATE);
    		case 2:
    			Log.i(TAG, "v2 added autonomous column");
        		db.execSQL("alter table "+DATABASE_TABLE+" add column "+KEY_AUTONOMOUS+" int;");
    		case 3:
//    			Log.i(TAG, "v3 ");
    	}
    }

    public void close() {
    	mDb.close();
    }
    
    public void reset() {
    	onUpgrade(mDb,0,1);
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
    	
        ContentValues initialValues = putVals(true, team, orientation, numWheels, wheelTypes, 
        		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
    			deadWheelType, turret, tracking, fender,
    			key, barrier, climb, notes, autonomous);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTeam(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllTeams() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LASTMOD, KEY_TEAM, 
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

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LASTMOD, KEY_TEAM, 
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
    public Cursor fetchTeamNums(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TEAM}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

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
    	ContentValues args = putVals(false, team, orientation, numWheels, wheelTypes, 
    		deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter,
			deadWheelType, turret, tracking, fender,
			key, barrier, climb, notes, autonomous);

        return mDb.update(DATABASE_TABLE, args, KEY_TEAM + "=" + team, null) > 0;
    }
    
    private ContentValues putVals(boolean create, int team, String orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, String wheel1Type, int wheel1Diameter, String wheel2Type, int wheel2Diameter,
    		String deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes, boolean autonomous) {
    	ContentValues cv = new ContentValues();
    	if(create) {
    		cv.put(KEY_TEAM, team);
    		cv.put(KEY_LASTMOD, 0);
    	} else {
    		cv.put(KEY_LASTMOD, System.currentTimeMillis()/1000);
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