package com.mechinn.android.ouralliance;


import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	private static final String tag = "Database";
	public static final String NAME = "ourAlliance.sqlite";
	public static final String MODIFIED = "modified";
	public static final int VERSION = 1;
	
	private static final String foreignSeason = " REFERENCES "+Season.TABLE+"("+BaseColumns._ID+") ON DELETE CASCADE";
	private static final String foreignTeam = " REFERENCES "+Team.TABLE+"("+BaseColumns._ID+") ON DELETE CASCADE";
	private static final String foreignCompetition = " REFERENCES "+Competition.TABLE+"("+BaseColumns._ID+") ON DELETE CASCADE";

	public Database(Context context) {
		super(context, NAME, null, VERSION);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			Log.i(tag,"Enable Foreign Keys");
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(tag,"Create Database");
		setup(db,0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(tag,"Upgrade Database from "+oldVersion);
		setup(db,oldVersion);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset(db);
	}

	public void reset() {
		reset(this.getWritableDatabase());
	}
	
	public void reset(SQLiteDatabase db) {
		Log.i(tag,"Reset Database");
		setup(db,-1);
	}
	
	public void setup(SQLiteDatabase db, int currentVersion) {
		Log.i(tag,"Updating Database from "+currentVersion+" to "+VERSION);
		switch(currentVersion+1) {
			case 0:
				Log.i(tag,"Clear Database");
				db.execSQL("DROP TABLE IF EXISTS "+Team.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+Season.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+Competition.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+TeamScouting.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+CompetitionTeam.TABLE);
			case 1:
				Log.i(tag,"Upgrade to version 1");
				
				String teamSchema = "CREATE TABLE "+Team.TABLE+" ( "+
						BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						MODIFIED+" DATE NOT NULL," +
						Team.NUMBER+" INTEGER NOT NULL," +
						Team.NAME+" TEXT," +
						" UNIQUE ("+Team.NUMBER+") ON CONFLICT IGNORE"+
						" );";
				Log.i(tag,teamSchema);
				db.execSQL(teamSchema);
				
				String seasonSchema = "CREATE TABLE "+Season.TABLE+" ( "+
						BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						MODIFIED+" DATE NOT NULL," +
						Season.YEAR+" INTEGER NOT NULL," +
						Season.COMPETITION+" TEXT," +
						" UNIQUE ("+Season.YEAR+") ON CONFLICT IGNORE"+
						" );";
				Log.i(tag,seasonSchema);
				db.execSQL(seasonSchema);
				
				String competitionSchema = "CREATE TABLE "+Competition.TABLE+" ( "+
						BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						MODIFIED+" DATE NOT NULL," +
						Competition.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
						Competition.NAME+" TEXT NOT NULL," +
						Competition.CODE+" TEXT NOT NULL,"+
						" UNIQUE ("+Competition.SEASON+","+Competition.CODE+") ON CONFLICT IGNORE"+
						" );";
				Log.i(tag,competitionSchema);
				db.execSQL(competitionSchema);
				
				String teamScoutingSchema = "CREATE TABLE "+TeamScouting.TABLE+" ( "+
						BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						MODIFIED+" DATE NOT NULL," +
						TeamScouting.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
						TeamScouting.TEAM+" INTEGER NOT NULL"+foreignTeam+"," +
						TeamScouting.ORIENTATION+" TEXT," +
						TeamScouting.WIDTH+" INTEGER," +
						TeamScouting.LENGTH+" INTEGER," +
						TeamScouting.HEIGHT+" INTEGER," +
						TeamScouting.NOTES+" TEXT," +
						" UNIQUE ("+TeamScouting.SEASON+","+TeamScouting.TEAM+") ON CONFLICT IGNORE"+
						" );";
				Log.i(tag,teamScoutingSchema);
				db.execSQL(teamScoutingSchema);
				
				String competitionTeamSchema = "CREATE TABLE "+CompetitionTeam.TABLE+" ( "+
						BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						MODIFIED+" DATE NOT NULL," +
						CompetitionTeam.COMPETITION+" INTEGER NOT NULL"+foreignCompetition+"," +
						CompetitionTeam.TEAM+" INTEGER NOT NULL"+foreignTeam+","+
						" UNIQUE ("+CompetitionTeam.COMPETITION+","+CompetitionTeam.TEAM+") ON CONFLICT IGNORE"+
						" );";
				Log.i(tag,competitionTeamSchema);
				db.execSQL(competitionTeamSchema);
				
				Log.i(tag,"At version 1");
//			case 2:
//				Log.i(tag,"Upgrade to version 2");
//				
//				Log.i(tag,"At version 2");
			default:
		}
	}
}
