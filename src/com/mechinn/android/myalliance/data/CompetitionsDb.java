package com.mechinn.android.myalliance.data;

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
public class CompetitionsDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "frcscoutingdb";
    private static final String DATABASE_TABLE = "competition";
    private static final int DATABASE_VERSION = 1;
    
	public static final String KEY_ROWID = "_id";
    public static final String KEY_LASTMOD = "_lastMod";
    public static final String KEY_COMPETITION = "competition";
    public static final String KEY_TEAM = "team";

    private static final String TAG = "CompetitionsDb";
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table teams ("+KEY_ROWID+" integer primary key autoincrement, "+
        		KEY_LASTMOD+" datetime not null, "+
        		KEY_COMPETITION+" text not null, "+
        		KEY_TEAM+" int not null unique);";
    
    public CompetitionsDb(Context context, boolean writable) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	if(writable) {
    		mDb = getWritableDatabase();
    	} else {
    		mDb = getReadableDatabase();
    	}
	}
    
    public CompetitionsDb(Context context) {
    	this(context,false);
	}

    public void onCreate(SQLiteDatabase db) {
    	//setup original db and upgrade to latest
    	db.execSQL(DATABASE_CREATE);
    	onUpgrade(db,1,DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(TAG, "v1 original team info table");
	    		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
				onCreate(db);
    		case 2:
//    			Log.i(TAG, "v3 ");
    	}
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
    public long createTeam(String competition, int team) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_LASTMOD, new Date().getTime());
    	initialValues.put(KEY_COMPETITION, competition);
        initialValues.put(KEY_TEAM, team);

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

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LASTMOD, KEY_COMPETITION, KEY_TEAM}, 
        		null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchTeamsAtCompetition(String competition) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LASTMOD, KEY_TEAM},
        		KEY_COMPETITION + "=" + competition, null, null, null, null, null);
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
    public Cursor fetchCompetitionsForTeam(int team) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LASTMOD, KEY_COMPETITION}, 
        		KEY_TEAM + "=" + team, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}