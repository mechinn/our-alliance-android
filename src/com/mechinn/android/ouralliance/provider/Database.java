package com.mechinn.android.ouralliance.provider;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	public static final String TAG = Database.class.getName();
	public static final String NAME = "ourAlliance.db";
	public static final String MODIFIED = "modified";
	public static final String[] COLUMNSBASE = new String[] {BaseColumns._ID, Database.MODIFIED};
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
		//if we open a writable db make sure we enable foreign key constraints
		if (!db.isReadOnly()) {
			Log.i(TAG,"Enable Foreign Keys");
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG,"Create Database");
		setup(db,0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG,"Upgrade Database from "+oldVersion);
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
		Log.i(TAG,"Reset Database");
		setup(db,-1);
	}
	
	public void setup(SQLiteDatabase db, int currentVersion) {
		Log.i(TAG,"Updating Database from "+currentVersion+" to "+VERSION);
		if(!db.isReadOnly()) {
			switch(currentVersion+1) {
				case 0:
					Log.i(TAG,"Clear Database");
					db.execSQL("DROP VIEW IF EXISTS "+TeamScouting2013.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+TeamScouting2013.TABLE);
					db.execSQL("DROP VIEW IF EXISTS "+TeamScoutingWheel.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+TeamScoutingWheel.TABLE);
					db.execSQL("DROP VIEW IF EXISTS "+CompetitionTeam.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+CompetitionTeam.TABLE);
					db.execSQL("DROP VIEW IF EXISTS "+Competition.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+Competition.TABLE);
					db.execSQL("DROP TABLE IF EXISTS "+Season.TABLE);
					db.execSQL("DROP TABLE IF EXISTS "+Team.TABLE);
				case 1:
					Log.i(TAG,"Upgrade to version 1");
					
					String teamSchema = "CREATE TABLE "+Team.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Team.NUMBER+" INTEGER NOT NULL," +
							Team.NAME+" TEXT," +
							" UNIQUE ("+Team.NUMBER+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,teamSchema);
					db.execSQL(teamSchema);
					
					String seasonSchema = "CREATE TABLE "+Season.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Season.YEAR+" INTEGER NOT NULL," +
							Season.TITLE+" TEXT," +
							" UNIQUE ("+Season.YEAR+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,seasonSchema);
					db.execSQL(seasonSchema);
					
					String competitionSchema = "CREATE TABLE "+Competition.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Competition.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							Competition.NAME+" TEXT NOT NULL," +
							Competition.CODE+" TEXT,"+
							" UNIQUE ("+Competition.SEASON+","+Competition.NAME+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,competitionSchema);
					db.execSQL(competitionSchema);
					
					String competitionView = "CREATE VIEW "+Competition.VIEW+" AS " +
							"SELECT "+
							Competition.CLASS+"."+BaseColumns._ID+"," +
							Competition.CLASS+"."+Database.MODIFIED+"," +
							Competition.CLASS+"."+Competition.SEASON+"," +
							Competition.CLASS+"."+Competition.NAME+"," +
							Competition.CLASS+"."+Competition.CODE+"," +
							Season.CLASS+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.CLASS+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.CLASS+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.CLASS+"."+Season.TITLE+" AS "+Season.VIEW_TITLE +
							" FROM " +
							Competition.TABLE+" "+Competition.CLASS+"," +
							Season.TABLE+" "+Season.CLASS +
							" WHERE " +
							Competition.SEASON+"="+Season.VIEW_ID+
							";";
					Log.i(TAG,competitionView);
					db.execSQL(competitionView);
					
					String competitionTeamSchema = "CREATE TABLE "+CompetitionTeam.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							CompetitionTeam.COMPETITION+" INTEGER NOT NULL"+foreignCompetition+"," +
							CompetitionTeam.TEAM+" INTEGER NOT NULL"+foreignTeam+","+
							CompetitionTeam.RANK+" INTEGER NOT NULL,"+
							" UNIQUE ("+CompetitionTeam.COMPETITION+","+CompetitionTeam.TEAM+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,competitionTeamSchema);
					db.execSQL(competitionTeamSchema);
					
					String competitionTeamView = "CREATE VIEW "+CompetitionTeam.VIEW+" AS " +
							"SELECT "+
							CompetitionTeam.CLASS+"."+BaseColumns._ID+"," +
							CompetitionTeam.CLASS+"."+Database.MODIFIED+"," +
							CompetitionTeam.CLASS+"."+CompetitionTeam.COMPETITION+"," +
							CompetitionTeam.CLASS+"."+CompetitionTeam.TEAM+"," +
							CompetitionTeam.CLASS+"."+CompetitionTeam.RANK+"," +
							Competition.CLASS+"."+BaseColumns._ID+" AS "+Competition.VIEW_ID+"," +
							Competition.CLASS+"."+Database.MODIFIED+" AS "+Competition.VIEW_MODIFIED+"," +
							Competition.CLASS+"."+Competition.SEASON+" AS "+Competition.VIEW_SEASON+"," +
							Competition.CLASS+"."+Competition.NAME+" AS "+Competition.VIEW_NAME+"," +
							Competition.CLASS+"."+Competition.CODE+" AS "+Competition.VIEW_CODE+"," +
							Season.CLASS+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.CLASS+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.CLASS+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.CLASS+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Team.CLASS+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
							Team.CLASS+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
							Team.CLASS+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
							Team.CLASS+"."+Team.NAME+" AS "+Team.VIEW_NAME +
							" FROM " +
							CompetitionTeam.TABLE+" "+CompetitionTeam.CLASS+"," +
							Competition.TABLE+" "+Competition.CLASS+"," +
							Season.TABLE+" "+Season.CLASS+"," +
							Team.TABLE+" "+Team.CLASS+
							" WHERE " +
							CompetitionTeam.COMPETITION+"="+Competition.VIEW_ID+
							" AND "+
							Competition.VIEW_SEASON+"="+Season.VIEW_ID+
							" AND "+
							CompetitionTeam.TEAM+"="+Team.VIEW_ID+
							";";
					Log.i(TAG,competitionTeamView);
					db.execSQL(competitionTeamView);
					
					String teamScoutingWheelSchema = "CREATE TABLE "+TeamScoutingWheel.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							TeamScoutingWheel.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							TeamScoutingWheel.TEAM+" INTEGER NOT NULL"+foreignTeam+"," +
							TeamScoutingWheel.TYPE+" TEXT NOT NULL,"+
							TeamScoutingWheel.SIZE+" INTEGER NOT NULL,"+
							TeamScoutingWheel.COUNT+" INTEGER NOT NULL,"+
							" UNIQUE ("+TeamScoutingWheel.SEASON+","+TeamScoutingWheel.TEAM+","+TeamScoutingWheel.TYPE+","+TeamScoutingWheel.SIZE+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,teamScoutingWheelSchema);
					db.execSQL(teamScoutingWheelSchema);
					
					String teamScoutingWheelView ="CREATE VIEW "+TeamScoutingWheel.VIEW+" AS " +
							"SELECT "+
							TeamScoutingWheel.CLASS+"."+BaseColumns._ID+"," +
							TeamScoutingWheel.CLASS+"."+Database.MODIFIED+"," +
							TeamScoutingWheel.CLASS+"."+TeamScoutingWheel.SEASON+"," +
							TeamScoutingWheel.CLASS+"."+TeamScoutingWheel.TEAM+"," +
							TeamScoutingWheel.CLASS+"."+TeamScoutingWheel.TYPE+"," +
							TeamScoutingWheel.CLASS+"."+TeamScoutingWheel.SIZE+"," +
							TeamScoutingWheel.CLASS+"."+TeamScoutingWheel.COUNT+"," +
							Season.CLASS+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.CLASS+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.CLASS+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.CLASS+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Team.CLASS+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
							Team.CLASS+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
							Team.CLASS+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
							Team.CLASS+"."+Team.NAME+" AS "+Team.VIEW_NAME +
							" FROM " +
							TeamScoutingWheel.TABLE+" "+TeamScoutingWheel.CLASS+"," +
							Season.TABLE+" "+Season.CLASS+"," +
							Team.TABLE+" "+Team.CLASS+
							" WHERE " +
							TeamScoutingWheel.SEASON+"="+Season.VIEW_ID+
							" AND "+
							TeamScoutingWheel.TEAM+"="+Team.VIEW_ID+
							";";
					Log.i(TAG,teamScoutingWheelView);
					db.execSQL(teamScoutingWheelView);
					
					String teamScouting2013Schema = "CREATE TABLE "+TeamScouting2013.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							TeamScouting.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							TeamScouting.TEAM+" INTEGER NOT NULL"+foreignTeam+"," +
							TeamScouting.NOTES+" TEXT," +
							TeamScouting2013.ORIENTATION+" TEXT," +
							TeamScouting2013.DRIVETRAIN+" TEXT," +
							TeamScouting2013.HUMANPLAYER+" REAL," +
							TeamScouting2013.WIDTH+" INTEGER," +
							TeamScouting2013.LENGTH+" INTEGER," +
							TeamScouting2013.HEIGHTSHOOTER+" INTEGER," +
							TeamScouting2013.HEIGHTMAX+" INTEGER," +
							TeamScouting2013.MAXCLIMB+" INTEGER," +
							TeamScouting2013.CLIMBTIME+" INTEGER," +
							TeamScouting2013.SHOOTERTYPE+" INTEGER," +
							TeamScouting2013.CONTINUOUSSHOOTING+" INTEGER," +
							TeamScouting2013.LOWGOAL+" INTEGER," +
							TeamScouting2013.MIDGOAL+" INTEGER," +
							TeamScouting2013.HIGHGOAL+" INTEGER," +
							TeamScouting2013.PYRAMIDGOAL+" INTEGER," +
							TeamScouting2013.AUTOMODE+" INTEGER," +
							TeamScouting2013.SLOT+" INTEGER," +
							TeamScouting2013.GROUND+" INTEGER," +
							TeamScouting2013.AUTOPICKUP+" INTEGER," +
							TeamScouting2013.RELOADSPEED+" REAL," +
							TeamScouting2013.SAFESHOOTER+" INTEGER," +
							TeamScouting2013.LOADERSHOOTER+" INTEGER," +
							TeamScouting2013.BLOCKER+" INTEGER," +
							" UNIQUE ("+TeamScouting2013.SEASON+","+TeamScouting2013.TEAM+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,teamScouting2013Schema);
					db.execSQL(teamScouting2013Schema);
					
					String teamScouting2013View = "CREATE VIEW "+TeamScouting2013.VIEW+" AS " +
							"SELECT "+
							TeamScouting2013.CLASS+"."+BaseColumns._ID+"," +
							TeamScouting2013.CLASS+"."+Database.MODIFIED+"," +
							TeamScouting2013.CLASS+"."+TeamScouting.SEASON+"," +
							TeamScouting2013.CLASS+"."+TeamScouting.TEAM+"," +
							TeamScouting2013.CLASS+"."+TeamScouting.NOTES+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.ORIENTATION+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.DRIVETRAIN+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.HUMANPLAYER+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.WIDTH+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.LENGTH+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.HEIGHTSHOOTER+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.HEIGHTMAX+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.MAXCLIMB+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.CLIMBTIME+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.SHOOTERTYPE+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.CONTINUOUSSHOOTING+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.LOWGOAL+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.MIDGOAL+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.HIGHGOAL+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.PYRAMIDGOAL+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.AUTOMODE+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.SLOT+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.GROUND+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.AUTOPICKUP+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.RELOADSPEED+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.SAFESHOOTER+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.LOADERSHOOTER+"," +
							TeamScouting2013.CLASS+"."+TeamScouting2013.BLOCKER+"," +
							Season.CLASS+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.CLASS+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.CLASS+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.CLASS+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Team.CLASS+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
							Team.CLASS+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
							Team.CLASS+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
							Team.CLASS+"."+Team.NAME+" AS "+Team.VIEW_NAME +
							" FROM " +
							TeamScouting2013.TABLE+" "+TeamScouting2013.CLASS+"," +
							Season.TABLE+" "+Season.CLASS+"," +
							Team.TABLE+" "+Team.CLASS+
							" WHERE " +
							TeamScouting2013.SEASON+"="+Season.VIEW_ID+
							" AND "+
							TeamScouting2013.TEAM+"="+Team.VIEW_ID+
							";";
					Log.i(TAG,teamScouting2013View);
					db.execSQL(teamScouting2013View);
					
					Log.i(TAG,"At version 1");
//				case 2:
//					Log.i(TAG,"Upgrade to version 2");
//					
//					Log.i(TAG,"At version 2");
				default:
			}
		} else {
			Log.e(TAG,"Read only database!");
		}
	}
}
