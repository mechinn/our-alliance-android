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
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.mechinn.android.myalliance.GeneralSchema;

public class FIRSTrankingsProvider extends ContentProvider implements GeneralSchema  {
    public static final String DBTable = "FIRSTrankings";
    public static final String DBTableReset = "FIRSTrankingsReset";
    private static final int DBVersion = 1;
    
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

    private static final String logTag = "FIRSTrankingsProvider";
    private static final String authority = "com.mechinn.android.myalliance.providers.FIRSTrankingsProvider";
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn.teaminfo";
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    public static final Uri mUriReset = Uri.parse("content://" + authority + "/"+DBTableReset);
    private static final int sig = 1;
    private static final int resetSig = 2;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> projectionMap;
    
    private static class TeamInfoDB extends SQLiteOpenHelper {
    	private static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    			keyLastMod+" int not null, "+
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

    	TeamInfoDB(Context context) {
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
    	    		Log.i(logTag, "v1 original team info table");
    	    		db.execSQL("DROP TABLE IF EXISTS "+DBTable);
    	    		Log.d("onCreate",DATABASE_CREATE);
    	    		db.execSQL(DATABASE_CREATE);
        		case 2:
//        			Log.i(logTag, "v2 ");
        	}
        }
    }

    private TeamInfoDB mDB;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int count;
        switch (sUriMatcher.match(uri)) {
            case sig:
                count = db.delete(DBTable, where, whereArgs);
                break;
            case resetSig:
                qb.setTables(DBTable);
                qb.setProjectionMap(projectionMap);
                count = qb.query(db, null, null, null, null, null, null).getCount();
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
    	mDB = new TeamInfoDB(getContext());
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
        sUriMatcher.addURI(authority, DBTableReset, resetSig);

        projectionMap = new HashMap<String, String>();
        projectionMap.put(_ID, _ID);
        projectionMap.put(keyLastMod, keyLastMod);
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
