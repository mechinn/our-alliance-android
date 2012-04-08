package com.mechinn.android.ouralliance.providers;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.SchemaArray;

public class TeamRankingsProvider extends ContentProvider {
    public static final String TABLE = "teamRankings";
    
    public static final String KEY_COMPETITION = "competition";
    public static final String KEY_RANK = "rank";
    public static final String KEY_TEAM = "team";
    public static final String KEY_QS = "qualifyscore";
    public static final String KEY_HYBRID = "hybrid";
    public static final String KEY_BRIDGE = "bridge";
    public static final String KEY_TELEOP = "teleop";
    public static final String KEY_COOP = "coop";
    public static final String KEY_RECORD = "record";
    public static final String KEY_DQ = "disqualified";
    public static final String KEY_PLAYED = "played";
    
    public static final SchemaArray SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, KEY_COMPETITION, KEY_RANK, KEY_TEAM, KEY_QS, 
    	KEY_HYBRID, KEY_BRIDGE, KEY_TELEOP, KEY_COOP, KEY_RECORD, KEY_DQ, KEY_PLAYED});

    private static final String TAG = "TeamRankingsProvider";
    private static final String AUTHORITY = "com.mechinn.android.ouralliance.providers."+TAG;
    private static final String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+TABLE;
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    private static final int SIG = 1;
    private static final UriMatcher sUriMatcher;
    private static final HashMap<String, String> projectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE +" ("+DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" INTEGER NOT NULL, "+
			KEY_COMPETITION+" TEXT NOT NULL, " +
			KEY_RANK+" INTEGER, " +
			KEY_TEAM+" INTEGER, " +
			KEY_QS+" REAL, " +
			KEY_HYBRID+" REAL, " +
			KEY_BRIDGE+" REAL, " +
			KEY_TELEOP+" REAL, " +
			KEY_COOP+" INTEGER, " +
			KEY_RECORD+" TEXT, " +
			KEY_DQ+" INTEGER, " +
			KEY_PLAYED+" INTEGER);";

    private DatabaseConnection mDB;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE, SIG);
        
        projectionMap = new HashMap<String, String>();
        projectionMap.put(DatabaseConnection._ID, DatabaseConnection._ID);
        projectionMap.put(DatabaseConnection._LASTMOD, DatabaseConnection._LASTMOD);
        projectionMap.put(KEY_COMPETITION, KEY_COMPETITION);
        projectionMap.put(KEY_RANK, KEY_RANK);
        projectionMap.put(KEY_TEAM, KEY_TEAM);
        projectionMap.put(KEY_QS, KEY_QS);
        projectionMap.put(KEY_HYBRID, KEY_HYBRID);
        projectionMap.put(KEY_BRIDGE, KEY_BRIDGE);
        projectionMap.put(KEY_TELEOP, KEY_TELEOP);
        projectionMap.put(KEY_COOP, KEY_COOP);
        projectionMap.put(KEY_RECORD, KEY_RECORD);
        projectionMap.put(KEY_DQ, KEY_DQ);
        projectionMap.put(KEY_PLAYED, KEY_PLAYED);

    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case SIG:
                count = db.delete(TABLE, where, whereArgs);
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
            case SIG:
                return TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
    	switch(sUriMatcher.match(uri)) {
    		case SIG: {
    			ContentValues values;
    	        if (initialValues != null) {
    	            values = new ContentValues(initialValues);
    	        } else {
    	            values = new ContentValues();
    	        }

    	        SQLiteDatabase db = mDB.getWritableDatabase();
    	        long rowId = db.insert(TABLE, null, values);
    	        if (rowId > 0) {
    	            Uri teamUri = ContentUris.withAppendedId(URI, rowId);
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
            case SIG:
                qb.setTables(TABLE);
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
            case SIG:
                count = db.update(TABLE, values, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
