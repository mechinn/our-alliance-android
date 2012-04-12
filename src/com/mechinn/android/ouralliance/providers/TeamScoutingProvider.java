package com.mechinn.android.ouralliance.providers;

import java.util.HashMap;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.SchemaArray;

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

public class TeamScoutingProvider extends ContentProvider {
    public static final String TABLE = "teamScouting";
    
    public static final String KEY_TEAM = "team";
    public static final String KEY_ORIENTATION = "orientation";
    public static final String KEY_NUM_WHEELS = "numWheels";
    public static final String KEY_WHEEL_TYPES = "wheelTypes";
    public static final String KEY_DEAD_WHEEL = "deadWheel";
    public static final String KEY_WHEEL1_TYPE = "wheel1Type";
    public static final String KEY_WHEEL1_DIAMETER = "wheel1Diameter";
    public static final String KEY_WHEEL2_TYPE = "wheel2Type";
    public static final String KEY_WHEEL2_DIAMETER = "wheel2Diameter";
    public static final String KEY_DEAD_WHEEL_TYPE = "deadWheelType";
    public static final String KEY_TURRET = "turret";
    public static final String KEY_TRACKING = "tracking";
    public static final String KEY_FENDER_SHOOTER = "fendershooter";
    public static final String KEY_KEY_SHOOTER = "keyshooter";
    public static final String KEY_BARRIER = "barrier";
    public static final String KEY_CLIMB = "climb";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_THIS = "This";
    
    public static final String KEY_AUTONOMOUS = "autonomous";
    public static final String KEY_AVG_HOOPS = "avghoops";
    public static final String KEY_AVG_BALANCE = "avgbalance";
    public static final String KEY_AVG_BROKE = "avgbroke";
    public static final String KEY_RANK = "rank";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_AUTO_BRIDGE = "autoBridge";
    public static final String KEY_AUTO_SHOOTER = "autoShooter";
    public static final String KEY_SHOOTING_RATING = "shooting";
    public static final String KEY_BALANCING_RATING = "balancing";
    public static final String KEY_AVG_AUTO = "avgAuto";
    
    public static final String[] COMPETITIONS = new String[] {"CT"};
    
    public static final SchemaArray V5_SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		KEY_TEAM, KEY_ORIENTATION, KEY_NUM_WHEELS, KEY_WHEEL_TYPES, 
		KEY_DEAD_WHEEL, KEY_WHEEL1_TYPE, KEY_WHEEL1_DIAMETER, 
		KEY_WHEEL2_TYPE, KEY_WHEEL2_DIAMETER, KEY_DEAD_WHEEL_TYPE, KEY_TRACKING, KEY_FENDER_SHOOTER, KEY_KEY_SHOOTER, 
		KEY_BARRIER, KEY_CLIMB, KEY_NOTES, KEY_AVG_HOOPS, KEY_AVG_BALANCE, KEY_AVG_BROKE}, COMPETITIONS);
    
    public static final SchemaArray SCHEMA_ARRAY = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
    		KEY_TEAM, KEY_RANK, KEY_ORIENTATION, KEY_WIDTH, KEY_HEIGHT, KEY_NUM_WHEELS, KEY_WHEEL_TYPES, 
    		KEY_DEAD_WHEEL, KEY_WHEEL1_TYPE, KEY_WHEEL1_DIAMETER, KEY_WHEEL2_TYPE, KEY_WHEEL2_DIAMETER, KEY_DEAD_WHEEL_TYPE,
    		KEY_TURRET, KEY_TRACKING, KEY_FENDER_SHOOTER, KEY_KEY_SHOOTER, KEY_BARRIER, KEY_CLIMB, KEY_NOTES, KEY_AUTO_BRIDGE,
    		KEY_AUTO_SHOOTER, KEY_SHOOTING_RATING, KEY_BALANCING_RATING, KEY_AVG_AUTO, KEY_AVG_HOOPS, KEY_AVG_BALANCE, KEY_AVG_BROKE}, COMPETITIONS);

    private static final String TAG = "TeamScoutingProvider";
    private static final String AUTHORITY = "com.mechinn.android.ouralliance.providers."+TAG;
    private static final String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+TABLE;
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    private static final int SIG = 1;
    private static final UriMatcher sUriMatcher;
    private static final HashMap<String, String> teamInfoProjectionMap;
    
    static {
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE, SIG);

        teamInfoProjectionMap = new HashMap<String, String>();
        for(String schema : SCHEMA_ARRAY) {
        	teamInfoProjectionMap.put(schema, schema);
        }
    }
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE +" ("+DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" INTEGER NOT NULL, "+
			KEY_TEAM+" INTEGER NOT NULL UNIQUE, " +
			KEY_ORIENTATION+" TEXT, " +
			KEY_NUM_WHEELS+" INTEGER, " +
			KEY_WHEEL_TYPES+" INTEGER, " +
			KEY_DEAD_WHEEL+" INTEGER, " +
			KEY_WHEEL1_TYPE+" TEXT, " +
			KEY_WHEEL1_DIAMETER+" INTEGER, " +
			KEY_WHEEL2_TYPE+" TEXT, " +
			KEY_WHEEL2_DIAMETER+" INTEGER, " +
			KEY_DEAD_WHEEL_TYPE+" TEXT, " +
			KEY_TURRET+" INTEGER, " +
			KEY_TRACKING+" INTEGER, " +
			KEY_FENDER_SHOOTER+" INTEGER, " +
			KEY_KEY_SHOOTER+" INTEGER, " +
			KEY_BARRIER+" INTEGER, " +
			KEY_CLIMB+" INTEGER, " +
			KEY_NOTES+" TEXT);";

    private DatabaseConnection mDB;

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
