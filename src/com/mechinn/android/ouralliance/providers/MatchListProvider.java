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

public class MatchListProvider extends ContentProvider {
    public static final String TABLE = "matchList";
    
    public static final String KEY_COMPETITON = "competition";
    public static final String KEY_MATCH_NUM = "matchNum";
    public static final String KEY_TIME = "matchTime";
    public static final String KEY_RED1 = "red1";
    public static final String KEY_RED2 = "red2";
    public static final String KEY_RED3 = "red3";
    public static final String KEY_BLUE1 = "blue1";
    public static final String KEY_BLUE2 = "blue2";
    public static final String KEY_BLUE3 = "blue3";
    
    public static final SchemaArray SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, KEY_COMPETITON, KEY_MATCH_NUM, KEY_TIME, KEY_RED1, 
			KEY_RED2, KEY_RED3, KEY_BLUE1, KEY_BLUE2, KEY_BLUE3});

    private static final String TAG = "MatchListProvider";
    private static final String AUTHORITY = "com.mechinn.android.ouralliance.providers."+TAG;
    private static final String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+TABLE;
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    private static final int SIG = 1;
    private static final UriMatcher sUriMatcher;
    private static final HashMap<String, String> matchesProjectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE +" ("+
    		DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" DATE NOT NULL, "+
			KEY_COMPETITON+" TEXT NOT NULL, " +
			KEY_MATCH_NUM+" INTEGER, " +
			KEY_TIME+" DATE, " +
			KEY_RED1+" INTEGER, " +
			KEY_RED2+" INTEGER, " +
			KEY_RED3+" INTEGER, " +
			KEY_BLUE1+" INTEGER, " +
			KEY_BLUE2+" INTEGER, " +
			KEY_BLUE3+" INTEGER);";

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
