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
	private static final int VERSION = 8;
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
	    	case 2:
	    	case 3:
	    	case 4:
	    	case 5:
	    	case 6:
	    	case 7:
	    	case 8: //reset db for major changes
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
    		
//    			Log.i(TAG, "v2 added "+TeamScoutingProvider.KEY_AUTONOMOUS+" column to the "+TeamScoutingProvider.TABLE+" table.");
//    			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+TeamScoutingProvider.KEY_AUTONOMOUS+" INTEGER;";
//    			Log.d(TAG,query);
//        		db.execSQL(query);
//    		
//    			Log.i(TAG, "v3 added competition columns to the "+TeamScoutingProvider.TABLE+" table.");
//    			db.beginTransaction();
//    			try {
//    				for(String comp : TeamScoutingProvider.COMPETITIONS) {
//    	    			query = "ALTER TABLE "+TeamScoutingProvider.TABLE+" ADD COLUMN "+comp+" INTEGER;";
//    	    			Log.d(TAG,query);
//    	        		db.execSQL(query);
//        			}
//            		
//    				db.setTransactionSuccessful();
//    				Log.d(TAG,"Transaction Successful");
//    			} finally {
//    				db.endTransaction();
//    			}
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