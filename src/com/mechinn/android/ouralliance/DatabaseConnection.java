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
	private static final String DBName = "ourAlliance.sqlite";
	private static final int DBVersion = 5;
	private final String logTag = "DatabaseConnection";

	public DatabaseConnection(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	onUpgrade(db,0,DBVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(logTag, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	String query;
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(logTag, "v1 original tables.");
	    		String drop = "DROP TABLE IF EXISTS ";
    			try {
    				String matchList = drop+MatchListProvider.DBTable;
    	    		Log.d(logTag,matchList);
    	    		db.execSQL(matchList);
    	    		Log.d(logTag,MatchListProvider.DATABASE_CREATE);
    	    		db.execSQL(MatchListProvider.DATABASE_CREATE);

    	    		String matchScouting = drop+MatchScoutingProvider.DBTable;
    	    		Log.d(logTag,matchScouting);
    	    		db.execSQL(matchScouting);
    	    		Log.d(logTag,MatchScoutingProvider.DATABASE_CREATE);
    	    		db.execSQL(MatchScoutingProvider.DATABASE_CREATE);
    	    		
    	    		String teamRankings = drop+TeamRankingsProvider.DBTable;
    	    		Log.d(logTag,teamRankings);
    	    		db.execSQL(teamRankings);
    	    		Log.d(logTag,TeamRankingsProvider.DATABASE_CREATE);
    	    		db.execSQL(TeamRankingsProvider.DATABASE_CREATE);

    	    		String teamScouting = drop+TeamScoutingProvider.DBTable;
    	    		Log.d(logTag,teamScouting);
    	    		db.execSQL(teamScouting);
    	    		Log.d(logTag,TeamScoutingProvider.DATABASE_CREATE);
    	    		db.execSQL(TeamScoutingProvider.DATABASE_CREATE);
    	    		db.beginTransaction();
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 2:
    			Log.i(logTag, "v2 added "+TeamScoutingProvider.keyAutonomous+" column to the "+TeamScoutingProvider.DBTable+" table.");
    			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutonomous+" INTEGER;";
    			Log.d(logTag,query);
        		db.execSQL(query);
    		case 3:
    			Log.i(logTag, "v3 added competition columns to the "+TeamScoutingProvider.DBTable+" table.");
    			db.beginTransaction();
    			try {
    				for(String comp : TeamScoutingProvider.competitions) {
    	    			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+comp+" INTEGER;";
    	    			Log.d(logTag,query);
    	        		db.execSQL(query);
        			}
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 4:
    			Log.i(logTag, "v4 added "+TeamScoutingProvider.keyAvgHoops+", "+TeamScoutingProvider.keyAvgBalance+", "+TeamScoutingProvider.keyAvgBroke+" column to the "+TeamScoutingProvider.DBTable+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAvgHoops+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
        			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAvgBalance+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
        			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAvgBroke+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 5:
    			Log.i(logTag, "v5 removed "+MatchScoutingProvider.keyAuto+" from the "+MatchScoutingProvider.DBTable+" table.");
    			db.beginTransaction();
    			try {
    				query = "CREATE TEMPORARY TABLE "+MatchScoutingProvider.DBTable+"_backup("+MatchScoutingProvider.v5schemaArray+");";
    				Log.d(logTag,query);
            		db.execSQL(query);

					query = "INSERT INTO "+MatchScoutingProvider.DBTable+"_backup SELECT "+MatchScoutingProvider.v5schemaArray+" FROM "+MatchScoutingProvider.DBTable+";";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+MatchScoutingProvider.DBTable+";";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "CREATE TABLE "+MatchScoutingProvider.DBTable+"("+MatchScoutingProvider.v5schemaArray+");";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "INSERT INTO "+MatchScoutingProvider.DBTable+" SELECT "+MatchScoutingProvider.v5schemaArray+" FROM "+MatchScoutingProvider.DBTable+"_backup;";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+MatchScoutingProvider.DBTable+"_backup;";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			Log.i(logTag, "v5 added "+MatchScoutingProvider.keyAutoBridge+", "+MatchScoutingProvider.keyAutoShooter+", "+
    					MatchScoutingProvider.keyMiss+", "+MatchScoutingProvider.keyTopAuto+", "+MatchScoutingProvider.keyMidAuto+", "+MatchScoutingProvider.keyBotAuto+", "+
    					MatchScoutingProvider.keyMissAuto+" column to the "+MatchScoutingProvider.DBTable+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyAutoBridge+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyAutoShooter+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyMiss+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyTopAuto+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyMidAuto+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyBotAuto+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+MatchScoutingProvider.DBTable+" ADD COLUMN "+MatchScoutingProvider.keyMissAuto+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			
    			Log.i(logTag, "v5 removed "+TeamScoutingProvider.keyAutonomous+" from the "+TeamScoutingProvider.DBTable+" table.");
    			db.beginTransaction();
    			try {
    				query = "CREATE TEMPORARY TABLE "+TeamScoutingProvider.DBTable+"_backup("+TeamScoutingProvider.v5schemaArray+");";
    				Log.d(logTag,query);
            		db.execSQL(query);

					query = "INSERT INTO "+TeamScoutingProvider.DBTable+"_backup SELECT "+TeamScoutingProvider.v5schemaArray+" FROM "+TeamScoutingProvider.DBTable+";";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+TeamScoutingProvider.DBTable+";";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "CREATE TABLE "+TeamScoutingProvider.DBTable+"("+TeamScoutingProvider.v5schemaArray+");";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "INSERT INTO "+TeamScoutingProvider.DBTable+" SELECT "+TeamScoutingProvider.v5schemaArray+" FROM "+TeamScoutingProvider.DBTable+"_backup;";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
					query = "DROP TABLE "+TeamScoutingProvider.DBTable+"_backup;";
    				Log.d(logTag,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    			Log.i(logTag, "v5 added "+TeamScoutingProvider.keyRank+", "+TeamScoutingProvider.keyWidth+", "+TeamScoutingProvider.keyHeight+", "+
    					TeamScoutingProvider.keyAutoBridge+", "+TeamScoutingProvider.keyAutoShooter+", "+TeamScoutingProvider.keyShootingRating+", "+TeamScoutingProvider.keyBalancingRating+", "+
    					TeamScoutingProvider.keyAvgAuto+" column to the "+TeamScoutingProvider.DBTable+" table.");
        		db.beginTransaction();
    			try {
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyRank+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyWidth+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyHeight+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutoBridge+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutoShooter+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyShootingRating+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyBalancingRating+" INTEGER;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAvgAuto+" REAL;";
        			Log.d(logTag,query);
            		db.execSQL(query);
            		
    				db.setTransactionSuccessful();
    				Log.d(logTag,"Transaction Successful");
    			} finally {
    				db.endTransaction();
    			}
    		case 6:
//    			Log.i(logTag, "v2 added "+TeamScoutingProvider.keyAutonomous+" column to the "+TeamScoutingProvider.DBTable+" table.");
//    			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutonomous+" INTEGER;";
//    			Log.d(logTag,query);
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