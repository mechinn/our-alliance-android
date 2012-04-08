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

public class MatchScoutingProvider extends ContentProvider {
    public static final String TABLE = "matchScouting";
    
    public static final String KEY_COMPETITION = "competition";
    public static final String KEY_MATCH_NUM = "matchNum";
    public static final String KEY_TEAM = "team";
    public static final String KEY_SLOT = "slot";
    public static final String KEY_BROKE = "broke";
    public static final String KEY_AUTO = "autonomous";
    public static final String KEY_BALANCE = "balanced";
    public static final String KEY_SHOOTER = "shooter";
    public static final String KEY_TOP = "top";
    public static final String KEY_MID = "mid";
    public static final String KEY_BOT = "bot";
    public static final String KEY_MISS = "miss";
    public static final String KEY_NOTES = "notes";
    
    public static final String KEY_AUTO_BRIDGE = "autoBridge";
    public static final String KEY_AUTO_SHOOTER = "autoShooter";
    public static final String KEY_TOP_AUTO = "topAuto";
    public static final String KEY_MID_AUTO = "midAuto";
    public static final String KEY_BOT_AUTO = "botAuto";
    public static final String KEY_MISS_AUTO = "missAuto";
    
    public static final SchemaArray V5_SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		KEY_COMPETITION, KEY_MATCH_NUM, KEY_TEAM, KEY_SLOT, KEY_BROKE, KEY_BALANCE, KEY_SHOOTER, 
		KEY_TOP, KEY_MID, KEY_BOT, KEY_NOTES});

    public static final SchemaArray SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		KEY_COMPETITION, KEY_MATCH_NUM, KEY_TEAM, KEY_SLOT, KEY_BROKE, KEY_BALANCE, KEY_SHOOTER, 
		KEY_TOP, KEY_MID, KEY_BOT, KEY_NOTES, KEY_AUTO_BRIDGE, KEY_AUTO_SHOOTER, KEY_MISS, 
		KEY_TOP_AUTO, KEY_MID_AUTO, KEY_BOT_AUTO, KEY_MISS_AUTO});

    private static final String TAG = "MatchScoutingProvider";
    private static final String AUTHORITY = "com.mechinn.android.ouralliance.providers."+TAG;
    private static final String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+TABLE;
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    private static final int SIG = 1;
    private static final UriMatcher sUriMatcher;
    private static final HashMap<String, String> matchesProjectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE +" ("+
    		DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" INTEGER NOT NULL, "+
			KEY_COMPETITION+" TEXT NOT NULL, " +
			KEY_MATCH_NUM+" INTEGER, " +
			KEY_TEAM+" INTEGER, " +
			KEY_SLOT+" TEXT, " +
			KEY_BROKE+" INTEGER, " +
			KEY_AUTO+" INTEGER, " +
			KEY_BALANCE+" INTEGER, "+
			KEY_SHOOTER+" INTEGER, " +
			KEY_TOP+" INTEGER, " +
			KEY_MID+" INTEGER, " +
			KEY_BOT+" INTEGER, " +
			KEY_NOTES+" TEXT);";

    private DatabaseConnection mDB;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE, SIG);

        matchesProjectionMap = new HashMap<String, String>();
        for(String schema : SCHEMA_ARRAY) {
            matchesProjectionMap.put(schema, schema);
        }
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
                qb.setProjectionMap(matchesProjectionMap);
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
