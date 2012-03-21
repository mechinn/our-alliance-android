package com.mechinn.android.myalliance.providers;

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

import com.mechinn.android.myalliance.GeneralSchema;

public class MatchListProvider extends ContentProvider implements GeneralSchema {
    public static final String DBTable = "matchList";
    public static final String DBTableReset = "matchListReset";
    
    public static final String keyCompetition = "competition";
    public static final String keyTime = "matchTime";
    public static final String keyMatchNum = "matchNum";
    public static final String keyRed1 = "red1";
    public static final String keyRed2 = "red2";
    public static final String keyRed3 = "red3";
    public static final String keyBlue1 = "blue1";
    public static final String keyBlue2 = "blue2";
    public static final String keyBlue3 = "blue3";
    
    public static final String[] schemaArray = {_ID, keyLastMod, keyCompetition, keyTime, keyMatchNum, keyRed1, 
			keyRed2, keyRed3, keyBlue1, keyBlue2, keyBlue3};

    private static final String logTag = "MatchListProvider";
    private static final String authority = "com.mechinn.android.myalliance.providers."+logTag;
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+DBTable;
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    public static final Uri mUriReset = Uri.parse("content://" + authority + "/"+DBTableReset);
    private static final int sig = 1;
    private static final int resetSig = 2;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> matchesProjectionMap;
    
    private static class MatchListDB extends SQLiteOpenHelper {
    	private static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+
    			_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    			keyLastMod+" int not null, "+
    			keyCompetition+" text not null, " +
    			keyTime+" int, " +
    			keyMatchNum+" int, " +
    			keyRed1+" int, " +
    			keyRed2+" int, " +
    			keyRed3+" int, " +
    			keyBlue1+" int, " +
    			keyBlue2+" int, " +
    			keyBlue3+" int);";

    	MatchListDB(Context context) {
            super(context, DBName, null, DBVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	//setup original db and upgrade to latest
        	Log.d("onCreate",DATABASE_CREATE);
        	db.execSQL(DATABASE_CREATE);
        	onUpgrade(db,1,DBVersion);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	Log.w(logTag, "Upgrading database from version " + oldVersion + " to " + newVersion);
        	switch(oldVersion+1){
    	    	default: //case 1
    	    		Log.i(logTag, "v1 original match info table");
    	    		db.execSQL("DROP TABLE IF EXISTS "+DBTable);
    	    		Log.d("onCreate",DATABASE_CREATE);
    	    		db.execSQL(DATABASE_CREATE);
        		case 2:
        			//upgraded team scouting
    	    		break;
        	}
        }
    }

    private MatchListDB mDB;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case sig:
                count = db.delete(DBTable, where, whereArgs);
                break;
            case resetSig:
                qb.setTables(DBTable);
                qb.setProjectionMap(matchesProjectionMap);
                try{
                	count = qb.query(db, null, null, null, null, null, null).getCount();
                } catch (SQLiteException e) {
                	Log.d(logTag, "No table/rows", e);
                }
            	mDB.onUpgrade(db,0,1);
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
    	mDB = new MatchListDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case sig:
                qb.setTables(DBTable);
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
        sUriMatcher.addURI(authority, DBTableReset, resetSig);

        matchesProjectionMap = new HashMap<String, String>();
        matchesProjectionMap.put(_ID, _ID);
        matchesProjectionMap.put(keyLastMod, keyLastMod);
        matchesProjectionMap.put(keyCompetition, keyCompetition);
        matchesProjectionMap.put(keyTime, keyTime);
        matchesProjectionMap.put(keyMatchNum, keyMatchNum);
        matchesProjectionMap.put(keyRed1, keyRed1);
        matchesProjectionMap.put(keyRed2, keyRed2);
        matchesProjectionMap.put(keyRed3, keyRed3);
        matchesProjectionMap.put(keyBlue1, keyBlue1);
        matchesProjectionMap.put(keyBlue2, keyBlue2);
        matchesProjectionMap.put(keyBlue3, keyBlue3);
    }
}
