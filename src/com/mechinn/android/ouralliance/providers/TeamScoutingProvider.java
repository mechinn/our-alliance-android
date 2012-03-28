package com.mechinn.android.ouralliance.providers;

import java.util.HashMap;

import com.mechinn.android.ouralliance.DatabaseConnection;

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

public class TeamScoutingProvider extends ContentProvider {
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
    public static final String keyAvgHoops = "avghoops";
    public static final String keyAvgBalance = "avgbalance";
    public static final String keyAvgBroke = "avgbroke";
    
    public static final String[] competitions = new String[] {"CT"};
    
    public static final String[] mostSchema = {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		keyTeam, keyOrientation, keyNumWheels, keyWheelTypes, 
		keyDeadWheel, keyWheel1Type, keyWheel1Diameter, 
		keyWheel2Type, keyWheel2Diameter, keyDeadWheelType, 
		keyTurret, keyTracking, keyFenderShooter, keyKeyShooter, 
		keyBarrier, keyClimb, keyNotes, keyAutonomous, keyAvgHoops, keyAvgBalance, keyAvgBroke};
    
    public static String[] schemaArray;

    private static final String logTag = "TeamScoutingProvider";
    private static final String authority = "com.mechinn.android.ouralliance.providers."+logTag;
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+DBTable;
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    private static final int sig = 1;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> teamInfoProjectionMap;
    
    static {
    	schemaArray = new String[competitions.length+mostSchema.length];
		System.arraycopy(competitions, 0, schemaArray, 0, competitions.length);
		System.arraycopy(mostSchema, 0, schemaArray, competitions.length, mostSchema.length);
    	
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(authority, DBTable, sig);

        teamInfoProjectionMap = new HashMap<String, String>();
        for(String schema : schemaArray) {
        	teamInfoProjectionMap.put(schema, schema);
        }
    }
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" INTEGER NOT NULL, "+
			keyTeam+" INTEGER NOT NULL UNIQUE, " +
			keyOrientation+" TEXT, " +
			keyNumWheels+" INTEGER, " +
			keyWheelTypes+" INTEGER, " +
			keyDeadWheel+" INTEGER, " +
			keyWheel1Type+" TEXT, " +
			keyWheel1Diameter+" INTEGER, " +
			keyWheel2Type+" TEXT, " +
			keyWheel2Diameter+" INTEGER, " +
			keyDeadWheelType+" TEXT, " +
			keyTurret+" INTEGER, " +
			keyTracking+" INTEGER, " +
			keyFenderShooter+" INTEGER, " +
			keyKeyShooter+" INTEGER, " +
			keyBarrier+" INTEGER, " +
			keyClimb+" INTEGER, " +
			keyNotes+" TEXT);";

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
}
