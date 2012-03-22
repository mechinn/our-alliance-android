package com.mechinn.android.myalliance.providers;

import java.util.HashMap;

import com.mechinn.android.myalliance.DatabaseConnection;
import com.mechinn.android.myalliance.GeneralSchema;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class TeamScoutingProvider extends ContentProvider implements GeneralSchema  {
    public static final String DBTable = "teamScouting";
    
    public static final String keyTeam = "team";
    public static final String keyOrientation = "orientation";
    public static final String keyNumWheels = "numWheels";
    public static final String keyWheelTypes = "wheelTypes";
    public static final String keyDeadWheel = "deadWheel";
    public static final String keyWheel1Type = "wheel1Type";
    public static final String keyWheel1Diameter = "wheel1Diameter";
    public static final String keyWheel2Type = "wheel2Type";
    public static final String keyWheel2Diameter = "wheel2Diameter";
    public static final String keyDeadWheelType = "deadWheelType";
    public static final String keyTurret = "turret";
    public static final String keyTracking = "tracking";
    public static final String keyFenderShooter = "fendershooter";
    public static final String keyKeyShooter = "keyshooter";
    public static final String keyBarrier = "barrier";
    public static final String keyClimb = "climb";
    public static final String keyNotes = "notes";
    public static final String keyAutonomous = "autonomous";
    
    public static final String[] schemaArray = {_ID, keyLastMod, 
		keyTeam, keyOrientation, keyNumWheels, keyWheelTypes, 
		keyDeadWheel, keyWheel1Type, keyWheel1Diameter, 
		keyWheel2Type, keyWheel2Diameter, keyDeadWheelType, 
		keyTurret, keyTracking, keyFenderShooter, keyKeyShooter, 
		keyBarrier, keyClimb, keyNotes, keyAutonomous};

    private static final String logTag = "TeamScoutingProvider";
    private static final String authority = "com.mechinn.android.myalliance.providers."+logTag;
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+DBTable;
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    private static final int sig = 1;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> teamInfoProjectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			keyLastMod+" int not null, "+
			keyTeam+" int not null unique, " +
			keyOrientation+" text, " +
			keyNumWheels+" int, " +
			keyWheelTypes+" int, " +
			keyDeadWheel+" int, " +
			keyWheel1Type+" text, " +
			keyWheel1Diameter+" int, " +
			keyWheel2Type+" text, " +
			keyWheel2Diameter+" int, " +
			keyDeadWheelType+" text, " +
			keyTurret+" int, " +
			keyTracking+" int, " +
			keyFenderShooter+" int, " +
			keyKeyShooter+" int, " +
			keyBarrier+" int, " +
			keyClimb+" int, " +
			keyNotes+" text);";

    private DatabaseConnection mDB;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case sig:
                count = db.delete(DBTable, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case sig:
                return type;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
    	switch(sUriMatcher.match(uri)) {
    		case sig: {
    			ContentValues values;
    	        if (initialValues != null) {
    	            values = new ContentValues(initialValues);
    	        } else {
    	            values = new ContentValues();
    	        }

    	        SQLiteDatabase db = mDB.getWritableDatabase();
    	        long rowId = db.insert(DBTable, null, values);
    	        if (rowId > 0) {
    	            Uri teamUri = ContentUris.withAppendedId(mUri, rowId);
    	            getContext().getContentResolver().notifyChange(teamUri, null);
    	            return teamUri;
    	        }

    	        throw new SQLException("Failed to insert row into " + uri);
    		}
    		default: {
    			throw new IllegalArgumentException("Unknown URI " + uri);
    		}
    	}
        
    }

    @Override
    public boolean onCreate() {
    	mDB = new DatabaseConnection(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case sig:
                qb.setTables(DBTable);
                qb.setProjectionMap(teamInfoProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDB.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case sig:
                count = db.update(DBTable, values, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(authority, DBTable, sig);

        teamInfoProjectionMap = new HashMap<String, String>();
        teamInfoProjectionMap.put(_ID, _ID);
        teamInfoProjectionMap.put(keyLastMod, keyLastMod);
        teamInfoProjectionMap.put(keyTeam, keyTeam);
        teamInfoProjectionMap.put(keyOrientation, keyOrientation);
        teamInfoProjectionMap.put(keyNumWheels, keyNumWheels);
        teamInfoProjectionMap.put(keyWheelTypes, keyWheelTypes);
        teamInfoProjectionMap.put(keyDeadWheel, keyDeadWheel);
        teamInfoProjectionMap.put(keyWheel1Type, keyWheel1Type);
        teamInfoProjectionMap.put(keyWheel1Diameter, keyWheel1Diameter);
        teamInfoProjectionMap.put(keyWheel2Type, keyWheel2Type);
        teamInfoProjectionMap.put(keyWheel2Diameter, keyWheel2Diameter);
        teamInfoProjectionMap.put(keyDeadWheelType, keyDeadWheelType);
        teamInfoProjectionMap.put(keyTurret, keyTurret);
        teamInfoProjectionMap.put(keyTracking, keyTracking);
        teamInfoProjectionMap.put(keyFenderShooter, keyFenderShooter);
        teamInfoProjectionMap.put(keyKeyShooter, keyKeyShooter);
        teamInfoProjectionMap.put(keyBarrier, keyBarrier);
        teamInfoProjectionMap.put(keyClimb, keyClimb);
        teamInfoProjectionMap.put(keyNotes, keyNotes);
        teamInfoProjectionMap.put(keyAutonomous, keyAutonomous);

    }
}
