package com.mechinn.android.ouralliance;

import com.mechinn.android.ouralliance.providers.MatchListProvider;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;
import com.mechinn.android.ouralliance.providers.TeamRankingsProvider;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseConnection extends SQLiteOpenHelper {
	public static final String _ID = "_id";
	public static final String _COUNT = "_count";
	public static final String _LASTMOD = "_lastmod";
	private static final String NAME = "ourAlliance.sqlite";
	private static final int VERSION = 5;
	private final String TAG = "DatabaseConnection";

	public DatabaseConnection(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	onUpgrade(db,0,VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	String query;
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(TAG, "v1 original tables.");
	    		String drop = "DROP TABLE IF EXISTS ";
    			try {
    				String matchList = drop+MatchListProvider.TABLE;
    	    		Log.d(TAG,matchList);
    	    		db.execSQL(matchList);
    	    		Log.d(TAG,MatchListProvider.DATABASE_CREATE);
    	    		db.execSQL(MatchListProvider.DATABASE_CREATE);

    	    		String matchScouting = drop+MatchScoutingProvider.TABLE;
    	    		Log.d(TAG,matchScouting);
    	    		db.execSQL(matchScouting);
    	    		Log.d(TAG,MatchScoutingProvider.DATABASE_CREATE);
    	    		db.execSQL(MatchScoutingProvider.DATABASE_CREATE);
    	    		
    	    		String teamRankings = drop+TeamRankingsProvider.TABLE;
    	    		Log.d(TAG,teamRankings);
    	    		db.execSQL(teamRankings);
    	    		Log.d(TAG,TeamRankingsProvider.DATABASE_CREATE);
    	    		db.execSQL(TeamRankingsProvider.DATABASE_CREATE);

    	    		String teamScouting = drop+TeamScoutingProvider.TABLE;
    	    		Log.d(TAG,teamScouting);
    	    		db.execSQL(teamScouting);
    	    		Log.d(TAG,TeamScoutingProvider.DATABASE_CREATE);
    	    		db.execSQL(TeamScoutingProvider.DATABASE_CREATE);
    	    		db.beginTransaction();
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 2:
    			Log.i(TAG, "v2 added "+TeamScoutingProvider.KEY_AUTONOMOUS+" column to the "+TeamScoutingProvider.TABLE+" table.");
    			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AUTONOMOUS+" INTEGER;";
    			Log.d(TAG,query);
        		db.execSQL(query);
    		case 3:
    			Log.i(TAG, "v3 added competition columns to the "+TeamScoutingProvider.TABLE+" table.");
    			db.beginTransaction();
    			try {
    				for(String comp : TeamScoutingProvider.COMPETITIONS) {
    	    			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+comp+" INTEGER;";
    	    			Log.d(TAG,query);
    	        		db.execSQL(query);
        			}
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 4:
    			Log.i(TAG, "v4 added "+TeamScoutingProvider.KEY_AVG_HOOPS+", "+TeamScoutingProvider.KEY_AVG_BALANCE+", "+TeamScoutingProvider.KEY_AVG_BROKE+" column to the "+TeamScoutingProvider.TABLE+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AVG_HOOPS+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
        			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AVG_BALANCE+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
        			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AVG_BROKE+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 5:
    			Log.i(TAG, "v5 removed "+MatchScoutingProvider.KEY_AUTO+" from the "+MatchScoutingProvider.TABLE+" table.");
    			db.beginTransaction();
    			try {
    				query = "CREATE TEMPORARY TABLE "+MatchScoutingProvider.TABLE+"_backup("+MatchScoutingProvider.V5_SCHEMA_ARRAY+");";
    				Log.d(TAG,query);
            		db.execSQL(query);

					query = "INSERT INTO "+MatchScoutingProvider.TABLE+"_backup SELECT "+MatchScoutingProvider.V5_SCHEMA_ARRAY+" FROM "+MatchScoutingProvider.TABLE+";";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+MatchScoutingProvider.TABLE+";";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "CREATE TABLE "+MatchScoutingProvider.TABLE+"("+MatchScoutingProvider.V5_SCHEMA_ARRAY+");";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "INSERT INTO "+MatchScoutingProvider.TABLE+" SELECT "+MatchScoutingProvider.V5_SCHEMA_ARRAY+" FROM "+MatchScoutingProvider.TABLE+"_backup;";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+MatchScoutingProvider.TABLE+"_backup;";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			Log.i(TAG, "v5 added "+MatchScoutingProvider.KEY_AUTO_BRIDGE+", "+MatchScoutingProvider.KEY_AUTO_SHOOTER+", "+
    					MatchScoutingProvider.KEY_MISS+", "+MatchScoutingProvider.KEY_TOP_AUTO+", "+MatchScoutingProvider.KEY_MID_AUTO+", "+MatchScoutingProvider.KEY_BOT_AUTO+", "+
    					MatchScoutingProvider.KEY_MISS_AUTO+" column to the "+MatchScoutingProvider.TABLE+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_AUTO_BRIDGE+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_AUTO_SHOOTER+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_MISS+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_TOP_AUTO+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_MID_AUTO+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_BOT_AUTO+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.TABLE+" ADD COLUMN "+MatchScoutingProvider.KEY_MISS_AUTO+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			
    			Log.i(TAG, "v5 removed "+TeamScoutingProvider.KEY_AUTONOMOUS+" from the "+TeamScoutingProvider.TABLE+" table.");
    			db.beginTransaction();
    			try {
    				query = "CREATE TEMPORARY TABLE "+TeamScoutingProvider.TABLE+"_backup("+TeamScoutingProvider.V5_SCHEMA_ARRAY+");";
    				Log.d(TAG,query);
            		db.execSQL(query);

					query = "INSERT INTO "+TeamScoutingProvider.TABLE+"_backup SELECT "+TeamScoutingProvider.V5_SCHEMA_ARRAY+" FROM "+TeamScoutingProvider.TABLE+";";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+TeamScoutingProvider.TABLE+";";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "CREATE TABLE "+TeamScoutingProvider.TABLE+"("+TeamScoutingProvider.V5_SCHEMA_ARRAY+");";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "INSERT INTO "+TeamScoutingProvider.TABLE+" SELECT "+TeamScoutingProvider.V5_SCHEMA_ARRAY+" FROM "+TeamScoutingProvider.TABLE+"_backup;";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+TeamScoutingProvider.TABLE+"_backup;";
    				Log.d(TAG,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			Log.i(TAG, "v5 added "+TeamScoutingProvider.KEY_RANK+", "+TeamScoutingProvider.KEY_WIDTH+", "+TeamScoutingProvider.KEY_HEIGHT+", "+
    					TeamScoutingProvider.KEY_AUTO_BRIDGE+", "+TeamScoutingProvider.KEY_AUTO_SHOOTER+", "+TeamScoutingProvider.KEY_SHOOTING_RATING+", "+TeamScoutingProvider.KEY_BALANCING_RATING+", "+
    					TeamScoutingProvider.KEY_AVG_AUTO+" column to the "+TeamScoutingProvider.TABLE+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_RANK+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_WIDTH+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_HEIGHT+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AUTO_BRIDGE+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AUTO_SHOOTER+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_SHOOTING_RATING+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_BALANCING_RATING+" INTEGER;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AVG_AUTO+" REAL;";
        			Log.d(TAG,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(TAG,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 6:
//    			Log.i(TAG, "v2 added "+TeamScoutingProvider.keyAutonomous+" column to the "+TeamScoutingProvider.DBTable+" table.");
//    			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutonomous+" INTEGER;";
//    			Log.d(TAG,query);
//        		db.execSQL(query);
    	}
    }
    
    public void reset() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	onUpgrade(db,0,1);
    	db.close();
    }
    
    public int getVersion() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	int version = db.getVersion();
    	db.close();
    	return version;
    }
}