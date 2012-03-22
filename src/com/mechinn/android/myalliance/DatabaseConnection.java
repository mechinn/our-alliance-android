package com.mechinn.android.myalliance;

import com.mechinn.android.myalliance.providers.MatchListProvider;
import com.mechinn.android.myalliance.providers.MatchScoutingProvider;
import com.mechinn.android.myalliance.providers.TeamRankingsProvider;
import com.mechinn.android.myalliance.providers.TeamScoutingProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseConnection extends SQLiteOpenHelper {
	public static final String _ID = "_id";
	public static final String _COUNT = "_count";
	public static final String keyLastMod = "_lastmod";
	private static final String DBName = "myAlliance.db";
	private static final int DBVersion = 2;
	private final String logTag = "DatabaseConnection";

	public DatabaseConnection(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	//setup original db and upgrade to latest
    	creatTables(db);
    	onUpgrade(db,1,DBVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(logTag, "Upgrading database from version " + oldVersion + " to " + newVersion);
    	switch(oldVersion+1){
	    	default: //case 1
	    		Log.i(logTag, "v1 original match info table");
	    		db.execSQL("DROP TABLE IF EXISTS "+MatchListProvider.DBTable);
	    		db.execSQL("DROP TABLE IF EXISTS "+MatchScoutingProvider.DBTable);
	    		db.execSQL("DROP TABLE IF EXISTS "+TeamRankingsProvider.DBTable);
	    		db.execSQL("DROP TABLE IF EXISTS "+TeamScoutingProvider.DBTable);
	    		creatTables(db);
    		case 2:
    			Log.i(logTag, "v2 added autonomous column");
        		db.execSQL("alter table "+TeamScoutingProvider.DBTable+" add column "+TeamScoutingProvider.keyAutonomous+" int;");
    	}
    }
    
    public void creatTables(SQLiteDatabase db) {
    	Log.d("onCreate",MatchListProvider.DATABASE_CREATE);
		db.execSQL(MatchListProvider.DATABASE_CREATE);
		
		Log.d("onCreate",MatchScoutingProvider.DATABASE_CREATE);
		db.execSQL(MatchScoutingProvider.DATABASE_CREATE);
		
		Log.d("onCreate",TeamRankingsProvider.DATABASE_CREATE);
		db.execSQL(TeamRankingsProvider.DATABASE_CREATE);
		
		Log.d("onCreate",TeamScoutingProvider.DATABASE_CREATE);
		db.execSQL(TeamScoutingProvider.DATABASE_CREATE);
    }
    
    public void reset() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	onUpgrade(db,0,1);
    	db.close();
    }
}