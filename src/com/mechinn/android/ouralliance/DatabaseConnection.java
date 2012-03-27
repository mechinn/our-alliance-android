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
	private static final int DBVersion = 2;
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
    		case 2:
    			Log.i(logTag, "v2 added autonomous column to the "+TeamScoutingProvider.DBTable+" table.");
    			query = "ALTER TABLE "+TeamScoutingProvider.DBTable+" ADD COLUMN "+TeamScoutingProvider.keyAutonomous+" INTEGER;";
    			Log.d(logTag,query);
        		db.execSQL(query);
    	}
    }
    
    public void reset() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	onUpgrade(db,0,1);
    	db.close();
    }
}