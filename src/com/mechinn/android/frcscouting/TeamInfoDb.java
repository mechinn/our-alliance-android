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

    private static final String TAG = "TeamInfoDb";
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table teams (_id integer primary key autoincrement, "
        + "_lastMod datetime not null, " +
        "team int not null unique, " +
        "orientation int, " +
        "numWheels int, " +
        "wheelTypes int, " +
        "deadWheel int, " +
        "wheel1Type int, " +
        "wheel1Diameter int, " +
        "wheel2Type int, " +
        "wheel2Diameter int, " +
        "deadWheelType int, " +
        "turret int, " +
        "tracking int, " +
        "fender int, " +
        "key int, " +
        "barrier int, " +
        "climb int, " +
        "notes text );";

    private static final String DATABASE_NAME = "frcscoutingdb";
    private static final String DATABASE_TABLE = "teams";
    private static final int DATABASE_VERSION = 1;
    
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
    	db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
    	reset();
    }

    public void close() {
    	mDb.close();
    }
    
    public void reset() {
    	mDb.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
    	onCreate(mDb);
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
    public long createTeam(int team, int orientation, int numWheels, int wheelTypes, boolean deadWheel,
			int wheel1Type, int wheel1Diameter, int wheel2Type, int wheel2Diameter,
			int deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LASTMOD, 0);
        initialValues.put(KEY_TEAM, team);
        initialValues.put(KEY_ORIENTATION, orientation);
        initialValues.put(KEY_NUMWHEELS, numWheels);
        initialValues.put(KEY_WHEELTYPES, wheelTypes);
        initialValues.put(KEY_DEADWHEEL, deadWheel);
        initialValues.put(KEY_WHEEL1TYPE, wheel1Type);
        initialValues.put(KEY_WHEEL1DIAMETER, wheel1Diameter);
        initialValues.put(KEY_WHEEL2TYPE, wheel2Type);
        initialValues.put(KEY_WHEEL2DIAMETER, wheel2Diameter);
        initialValues.put(KEY_DEADWHEELTYPE, deadWheelType);
        initialValues.put(KEY_TURRET, turret);
        initialValues.put(KEY_TRACKING, tracking);
        initialValues.put(KEY_FENDER, fender);
        initialValues.put(KEY_KEY, key);
        initialValues.put(KEY_BARRIER, barrier);
        initialValues.put(KEY_CLIMB, climb);
        initialValues.put(KEY_NOTES, notes);

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
        		KEY_TURRET, KEY_TRACKING, KEY_FENDER, KEY_KEY, KEY_BARRIER, KEY_CLIMB, KEY_NOTES}, 
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
            		KEY_TURRET, KEY_TRACKING, KEY_FENDER, KEY_KEY, KEY_BARRIER, KEY_CLIMB, KEY_NOTES}, KEY_TEAM + "=" + team, null,
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
    public boolean updateTeam(int team, int orientation, int numWheels, int wheelTypes, 
    		boolean deadWheel, int wheel1Type, int wheel1Diameter, int wheel2Type, int wheel2Diameter,
			int deadWheelType, boolean turret, boolean tracking, boolean fender,
			boolean key, boolean barrier, boolean climb, String notes) {
        ContentValues args = new ContentValues();
        args.put(KEY_LASTMOD, new Date().getTime());
        args.put(KEY_ORIENTATION, orientation);
        args.put(KEY_NUMWHEELS, numWheels);
        args.put(KEY_WHEELTYPES, wheelTypes);
        args.put(KEY_DEADWHEEL, deadWheel);
        args.put(KEY_WHEEL1TYPE, wheel1Type);
        args.put(KEY_WHEEL1DIAMETER, wheel1Diameter);
        args.put(KEY_WHEEL2TYPE, wheel2Type);
        args.put(KEY_WHEEL2DIAMETER, wheel2Diameter);
        args.put(KEY_DEADWHEELTYPE, deadWheelType);
        args.put(KEY_TURRET, turret);
        args.put(KEY_TRACKING, tracking);
        args.put(KEY_FENDER, fender);
        args.put(KEY_KEY, key);
        args.put(KEY_BARRIER, barrier);
        args.put(KEY_CLIMB, climb);
        args.put(KEY_NOTES, notes);

        return mDb.update(DATABASE_TABLE, args, KEY_TEAM + "=" + team, null) > 0;
    }
}