package com.mechinn.android.ouralliance.providers;

import java.util.HashMap;

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

import com.mechinn.android.ouralliance.DatabaseConnection;

public class TeamRankingsProvider extends ContentProvider {
    public static final String DBTable = "teamRankings";
    
    public static final String keyCompetition = "competition";
    public static final String keyRank = "rank";
    public static final String keyTeam = "team";
    public static final String keyQS = "qualifyscore";
    public static final String keyHybrid = "hybrid";
    public static final String keyBridge = "bridge";
    public static final String keyTeleop = "teleop";
    public static final String keyCoop = "coop";
    public static final String keyRecord = "record";
    public static final String keyDQ = "disqualified";
    public static final String keyPlayed = "played";
    
    public static final String[] schemaArray = {DatabaseConnection._ID, DatabaseConnection._LASTMOD, keyCompetition, keyRank, keyTeam, keyQS, 
    	keyHybrid, keyBridge, keyTeleop, keyCoop, keyRecord, keyDQ, keyPlayed};

    private static final String logTag = "TeamRankingsProvider";
    private static final String authority = "com.mechinn.android.ouralliance.providers."+logTag;
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+DBTable;
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    private static final int sig = 1;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> projectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" int not null, "+
			keyCompetition+" text not null, " +
			keyRank+" int, " +
			keyTeam+" int, " +
			keyQS+" FLOAT, " +
			keyHybrid+" FLOAT, " +
			keyBridge+" FLOAT, " +
			keyTeleop+" FLOAT, " +
			keyCoop+" int, " +
			keyRecord+" text, " +
			keyDQ+" int, " +
			keyPlayed+" int);";

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
                qb.setProjectionMap(projectionMap);
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
        
        projectionMap = new HashMap<String, String>();
        projectionMap.put(DatabaseConnection._ID, DatabaseConnection._ID);
        projectionMap.put(DatabaseConnection._LASTMOD, DatabaseConnection._LASTMOD);
        projectionMap.put(keyCompetition, keyCompetition);
        projectionMap.put(keyRank, keyRank);
        projectionMap.put(keyTeam, keyTeam);
        projectionMap.put(keyQS, keyQS);
        projectionMap.put(keyHybrid, keyHybrid);
        projectionMap.put(keyBridge, keyBridge);
        projectionMap.put(keyTeleop, keyTeleop);
        projectionMap.put(keyCoop, keyCoop);
        projectionMap.put(keyRecord, keyRecord);
        projectionMap.put(keyDQ, keyDQ);
        projectionMap.put(keyPlayed, keyPlayed);

    }
}
