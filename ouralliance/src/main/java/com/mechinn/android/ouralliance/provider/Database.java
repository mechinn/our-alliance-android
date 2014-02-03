package com.mechinn.android.ouralliance.provider;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2014.Match2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	public static final String TAG = Database.class.getSimpleName();
	public static final String NAME = "ourAlliance.db";
	public static final String MODIFIED = "modified";
	public static final String[] COLUMNSBASE = new String[] {BaseColumns._ID, Database.MODIFIED};
	public static final int VERSION = 5;
	
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
		String schema;
		if(db.isReadOnly()) {
            Log.e(TAG,"Read only database!");
        } else {
			switch(currentVersion+1) {
				case 0:
                case 1:
                case 2:
                case 3:
                case 4:
					Log.i(TAG,"Clear Database");
                    db.execSQL("DROP VIEW IF EXISTS match2013view");
                    db.execSQL("DROP TABLE IF EXISTS match2013");
                    db.execSQL("DROP VIEW IF EXISTS teamscouting2013view");
                    db.execSQL("DROP TABLE IF EXISTS teamscouting2013");
					db.execSQL("DROP VIEW IF EXISTS "+Match2014.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+Match2014.TABLE);
					db.execSQL("DROP VIEW IF EXISTS "+TeamScouting2014.VIEW);
					db.execSQL("DROP TABLE IF EXISTS "+TeamScouting2014.TABLE);
                    db.execSQL("DROP VIEW IF EXISTS "+TeamScoutingWheel.VIEW);
                    db.execSQL("DROP TABLE IF EXISTS "+TeamScoutingWheel.TABLE);
                    db.execSQL("DROP VIEW IF EXISTS "+CompetitionTeam.VIEW);
                    db.execSQL("DROP TABLE IF EXISTS "+CompetitionTeam.TABLE);
                    db.execSQL("DROP VIEW IF EXISTS "+Competition.VIEW);
                    db.execSQL("DROP TABLE IF EXISTS "+Competition.TABLE);
                    db.execSQL("DROP TABLE IF EXISTS "+Season.TABLE);
                    db.execSQL("DROP TABLE IF EXISTS "+Team.TABLE);
				case 5:
					Log.i(TAG,"Upgrade to version 5");
					
					schema = "CREATE TABLE "+Team.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Team.NUMBER+" INTEGER NOT NULL," +
							Team.NAME+" TEXT," +
							" UNIQUE ("+Team.NUMBER+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE TABLE "+Season.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Season.YEAR+" INTEGER NOT NULL," +
							Season.TITLE+" TEXT," +
							" UNIQUE ("+Season.YEAR+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE TABLE "+Competition.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Competition.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							Competition.NAME+" TEXT NOT NULL," +
							Competition.CODE+" TEXT,"+
							" UNIQUE ("+Competition.SEASON+","+Competition.NAME+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE VIEW "+Competition.VIEW+" AS " +
							"SELECT "+
							Competition.TAG+"."+BaseColumns._ID+"," +
							Competition.TAG+"."+Database.MODIFIED+"," +
							Competition.TAG+"."+Competition.SEASON+"," +
							Competition.TAG+"."+Competition.NAME+"," +
							Competition.TAG+"."+Competition.CODE+"," +
							Season.TAG+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.TAG+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.TAG+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.TAG+"."+Season.TITLE+" AS "+Season.VIEW_TITLE +
							" FROM " +
							Competition.TABLE+" "+Competition.TAG+"," +
							Season.TABLE+" "+Season.TAG +
							" WHERE " +
							Competition.SEASON+"="+Season.VIEW_ID+
							";";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE TABLE "+CompetitionTeam.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							CompetitionTeam.COMPETITION+" INTEGER NOT NULL"+foreignCompetition+"," +
							CompetitionTeam.TEAM+" INTEGER NOT NULL"+foreignTeam+","+
							CompetitionTeam.RANK+" INTEGER NOT NULL,"+
                            CompetitionTeam.SCOUTED+" INTEGER,"+
							" UNIQUE ("+CompetitionTeam.COMPETITION+","+CompetitionTeam.TEAM+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);

                    schema = "CREATE VIEW "+CompetitionTeam.VIEW+" AS " +
                            "SELECT "+
                            CompetitionTeam.TAG+"."+BaseColumns._ID+"," +
                            CompetitionTeam.TAG+"."+Database.MODIFIED+"," +
                            CompetitionTeam.TAG+"."+CompetitionTeam.COMPETITION+"," +
                            CompetitionTeam.TAG+"."+CompetitionTeam.TEAM+"," +
                            CompetitionTeam.TAG+"."+CompetitionTeam.RANK+"," +
                            CompetitionTeam.TAG+"."+CompetitionTeam.SCOUTED+"," +
                            Competition.TAG+"."+BaseColumns._ID+" AS "+Competition.VIEW_ID+"," +
                            Competition.TAG+"."+Database.MODIFIED+" AS "+Competition.VIEW_MODIFIED+"," +
                            Competition.TAG+"."+Competition.SEASON+" AS "+Competition.VIEW_SEASON+"," +
                            Competition.TAG+"."+Competition.NAME+" AS "+Competition.VIEW_NAME+"," +
                            Competition.TAG+"."+Competition.CODE+" AS "+Competition.VIEW_CODE+"," +
                            Season.TAG+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
                            Season.TAG+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
                            Season.TAG+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
                            Season.TAG+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
                            Team.TAG+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
                            Team.TAG+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
                            Team.TAG+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
                            Team.TAG+"."+Team.NAME+" AS "+Team.VIEW_NAME +
                            " FROM " +
                            CompetitionTeam.TABLE+" "+CompetitionTeam.TAG+"," +
                            Competition.TABLE+" "+Competition.TAG+"," +
                            Season.TABLE+" "+Season.TAG+"," +
                            Team.TABLE+" "+Team.TAG+
                            " WHERE " +
                            CompetitionTeam.COMPETITION+"="+Competition.VIEW_ID+
                            " AND "+
                            Competition.VIEW_SEASON+"="+Season.VIEW_ID+
                            " AND "+
                            CompetitionTeam.TEAM+"="+Team.VIEW_ID+
                            ";";
                    Log.i(TAG,schema);
                    db.execSQL(schema);
					
					schema = "CREATE TABLE "+TeamScoutingWheel.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							TeamScoutingWheel.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							TeamScoutingWheel.TEAM+" INTEGER NOT NULL"+foreignTeam+"," +
							TeamScoutingWheel.TYPE+" TEXT NOT NULL,"+
							TeamScoutingWheel.SIZE+" INTEGER NOT NULL,"+
							TeamScoutingWheel.COUNT+" INTEGER NOT NULL,"+
							" UNIQUE ("+TeamScoutingWheel.SEASON+","+TeamScoutingWheel.TEAM+","+TeamScoutingWheel.TYPE+","+TeamScoutingWheel.SIZE+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema ="CREATE VIEW "+TeamScoutingWheel.VIEW+" AS " +
							"SELECT "+
							TeamScoutingWheel.TAG+"."+BaseColumns._ID+"," +
							TeamScoutingWheel.TAG+"."+Database.MODIFIED+"," +
							TeamScoutingWheel.TAG+"."+TeamScoutingWheel.SEASON+"," +
							TeamScoutingWheel.TAG+"."+TeamScoutingWheel.TEAM+"," +
							TeamScoutingWheel.TAG+"."+TeamScoutingWheel.TYPE+"," +
							TeamScoutingWheel.TAG+"."+TeamScoutingWheel.SIZE+"," +
							TeamScoutingWheel.TAG+"."+TeamScoutingWheel.COUNT+"," +
							Season.TAG+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.TAG+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.TAG+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.TAG+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Team.TAG+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
							Team.TAG+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
							Team.TAG+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
							Team.TAG+"."+Team.NAME+" AS "+Team.VIEW_NAME +
							" FROM " +
							TeamScoutingWheel.TABLE+" "+TeamScoutingWheel.TAG+"," +
							Season.TABLE+" "+Season.TAG+"," +
							Team.TABLE+" "+Team.TAG+
							" WHERE " +
							TeamScoutingWheel.SEASON+"="+Season.VIEW_ID+
							" AND "+
							TeamScoutingWheel.TEAM+"="+Team.VIEW_ID+
							";";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE TABLE "+TeamScouting2014.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							TeamScouting.SEASON+" INTEGER NOT NULL"+foreignSeason+"," +
							TeamScouting.TEAM+" INTEGER NOT NULL"+foreignTeam+"," +
							TeamScouting.NOTES+" TEXT," +
							TeamScouting2014.ORIENTATION+" TEXT," +
							TeamScouting2014.DRIVETRAIN+" TEXT," +
							TeamScouting2014.WIDTH+" REAL," +
							TeamScouting2014.LENGTH+" REAL," +
							TeamScouting2014.HEIGHTSHOOTER+" REAL," +
							TeamScouting2014.HEIGHTMAX+" REAL," +
							TeamScouting2014.SHOOTERTYPE+" INTEGER," +
							TeamScouting2014.LOWGOAL+" INTEGER," +
							TeamScouting2014.HIGHGOAL+" INTEGER," +
                            TeamScouting2014.HOTGOAL+" INTEGER," +
                            TeamScouting2014.SHOOTINGDISTANCE+" REAL," +
                            TeamScouting2014.PASSGROUND+" INTEGER," +
                            TeamScouting2014.PASSAIR+" INTEGER," +
                            TeamScouting2014.PASSTRUSS+" INTEGER," +
                            TeamScouting2014.PICKUPGROUND+" INTEGER," +
                            TeamScouting2014.PICKUPCATCH+" INTEGER," +
                            TeamScouting2014.PUSHER+" INTEGER," +
                            TeamScouting2014.BLOCKER+" INTEGER," +
                            TeamScouting2014.HUMANPLAYER+" REAL," +
                            TeamScouting2014.AUTOMODE+" INTEGER," +
							" UNIQUE ("+TeamScouting2014.SEASON+","+TeamScouting2014.TEAM+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE VIEW "+TeamScouting2014.VIEW+" AS " +
							"SELECT "+
							TeamScouting2014.TAG+"."+BaseColumns._ID+"," +
							TeamScouting2014.TAG+"."+Database.MODIFIED+"," +
							TeamScouting2014.TAG+"."+TeamScouting.SEASON+"," +
							TeamScouting2014.TAG+"."+TeamScouting.TEAM+"," +
							TeamScouting2014.TAG+"."+TeamScouting.NOTES+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.ORIENTATION+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.DRIVETRAIN+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.WIDTH+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.LENGTH+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHTSHOOTER+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHTMAX+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTERTYPE+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.LOWGOAL+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.HIGHGOAL+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.HOTGOAL+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTINGDISTANCE+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PASSGROUND+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PASSAIR+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PASSTRUSS+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PICKUPGROUND+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PICKUPCATCH+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.PUSHER+"," +
							TeamScouting2014.TAG+"."+TeamScouting2014.BLOCKER+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.HUMANPLAYER+"," +
                            TeamScouting2014.TAG+"."+TeamScouting2014.AUTOMODE+"," +
							Season.TAG+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.TAG+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.TAG+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.TAG+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Team.TAG+"."+BaseColumns._ID+" AS "+Team.VIEW_ID+"," +
							Team.TAG+"."+Database.MODIFIED+" AS "+Team.VIEW_MODIFIED+"," +
							Team.TAG+"."+Team.NUMBER+" AS "+Team.VIEW_NUMBER+"," +
							Team.TAG+"."+Team.NAME+" AS "+Team.VIEW_NAME +
							" FROM " +
							TeamScouting2014.TABLE+" "+TeamScouting2014.TAG+"," +
							Season.TABLE+" "+Season.TAG+"," +
							Team.TABLE+" "+Team.TAG+
							" WHERE " +
							TeamScouting2014.SEASON+"="+Season.VIEW_ID+
							" AND "+
							TeamScouting2014.TEAM+"="+Team.VIEW_ID+
							";";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE TABLE "+Match2014.TABLE+" ( "+
							BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
							MODIFIED+" DATE NOT NULL," +
							Match.COMPETITION+" INTEGER NOT NULL"+foreignCompetition+"," +
							Match.NUMBER+" INTEGER NOT NULL,"+
							Match.RED1+" INTEGER NOT NULL"+foreignTeam+","+
							Match.RED2+" INTEGER NOT NULL"+foreignTeam+","+
							Match.RED3+" INTEGER NOT NULL"+foreignTeam+","+
							Match.BLUE1+" INTEGER NOT NULL"+foreignTeam+","+
							Match.BLUE2+" INTEGER NOT NULL"+foreignTeam+","+
							Match.BLUE3+" INTEGER NOT NULL"+foreignTeam+","+
							Match.REDSCORE+" INTEGER,"+
							Match.BLUESCORE+" INTEGER,"+
							Match.TYPE+" INTEGER NOT NULL,"+
							Match.SET+" INTEGER,"+
							Match.OF+" INTEGER,"+
							" UNIQUE ("+Match.COMPETITION+","+Match.NUMBER+") ON CONFLICT IGNORE"+
							" );";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					schema = "CREATE VIEW "+Match2014.VIEW+" AS " +
							"SELECT "+
							Match2014.TAG+"."+BaseColumns._ID+"," +
							Match2014.TAG+"."+Database.MODIFIED+"," +
							Match2014.TAG+"."+Match.COMPETITION+"," +
							Match2014.TAG+"."+Match.NUMBER+"," +
							Match2014.TAG+"."+Match.RED1+"," +
							Match2014.TAG+"."+Match.RED2+"," +
							Match2014.TAG+"."+Match.RED3+"," +
							Match2014.TAG+"."+Match.BLUE1+"," +
							Match2014.TAG+"."+Match.BLUE2+"," +
							Match2014.TAG+"."+Match.BLUE3+"," +
							Match2014.TAG+"."+Match.REDSCORE+"," +
							Match2014.TAG+"."+Match.BLUESCORE+"," +
							Match2014.TAG+"."+Match.TYPE+"," +
							Match2014.TAG+"."+Match.SET+"," +
							Match2014.TAG+"."+Match.OF+"," +
							Competition.TAG+"."+BaseColumns._ID+" AS "+Competition.VIEW_ID+"," +
							Competition.TAG+"."+Database.MODIFIED+" AS "+Competition.VIEW_MODIFIED+"," +
							Competition.TAG+"."+Competition.SEASON+" AS "+Competition.VIEW_SEASON+"," +
							Competition.TAG+"."+Competition.NAME+" AS "+Competition.VIEW_NAME+"," +
							Competition.TAG+"."+Competition.CODE+" AS "+Competition.VIEW_CODE+"," +
							Season.TAG+"."+BaseColumns._ID+" AS "+Season.VIEW_ID+"," +
							Season.TAG+"."+Database.MODIFIED+" AS "+Season.VIEW_MODIFIED+"," +
							Season.TAG+"."+Season.YEAR+" AS "+Season.VIEW_YEAR+"," +
							Season.TAG+"."+Season.TITLE+" AS "+Season.VIEW_TITLE+"," +
							Match.RED1+Team.TAG+"."+BaseColumns._ID+" AS "+Match.RED1+Team.VIEW_ID+"," +
							Match.RED1+Team.TAG+"."+Database.MODIFIED+" AS "+Match.RED1+Team.VIEW_MODIFIED+"," +
							Match.RED1+Team.TAG+"."+Team.NUMBER+" AS "+Match.RED1+Team.VIEW_NUMBER+"," +
							Match.RED1+Team.TAG+"."+Team.NAME+" AS "+Match.RED1+Team.VIEW_NAME+"," +
							Match.RED2+Team.TAG+"."+BaseColumns._ID+" AS "+Match.RED2+Team.VIEW_ID+"," +
							Match.RED2+Team.TAG+"."+Database.MODIFIED+" AS "+Match.RED2+Team.VIEW_MODIFIED+"," +
							Match.RED2+Team.TAG+"."+Team.NUMBER+" AS "+Match.RED2+Team.VIEW_NUMBER+"," +
							Match.RED2+Team.TAG+"."+Team.NAME+" AS "+Match.RED2+Team.VIEW_NAME+"," +
							Match.RED3+Team.TAG+"."+BaseColumns._ID+" AS "+Match.RED3+Team.VIEW_ID+"," +
							Match.RED3+Team.TAG+"."+Database.MODIFIED+" AS "+Match.RED3+Team.VIEW_MODIFIED+"," +
							Match.RED3+Team.TAG+"."+Team.NUMBER+" AS "+Match.RED3+Team.VIEW_NUMBER+"," +
							Match.RED3+Team.TAG+"."+Team.NAME+" AS "+Match.RED3+Team.VIEW_NAME+"," +
							Match.BLUE1+Team.TAG+"."+BaseColumns._ID+" AS "+Match.BLUE1+Team.VIEW_ID+"," +
							Match.BLUE1+Team.TAG+"."+Database.MODIFIED+" AS "+Match.BLUE1+Team.VIEW_MODIFIED+"," +
							Match.BLUE1+Team.TAG+"."+Team.NUMBER+" AS "+Match.BLUE1+Team.VIEW_NUMBER+"," +
							Match.BLUE1+Team.TAG+"."+Team.NAME+" AS "+Match.BLUE1+Team.VIEW_NAME+"," +
							Match.BLUE2+Team.TAG+"."+BaseColumns._ID+" AS "+Match.BLUE2+Team.VIEW_ID+"," +
							Match.BLUE2+Team.TAG+"."+Database.MODIFIED+" AS "+Match.BLUE2+Team.VIEW_MODIFIED+"," +
							Match.BLUE2+Team.TAG+"."+Team.NUMBER+" AS "+Match.BLUE2+Team.VIEW_NUMBER+"," +
							Match.BLUE2+Team.TAG+"."+Team.NAME+" AS "+Match.BLUE2+Team.VIEW_NAME+"," +
							Match.BLUE3+Team.TAG+"."+BaseColumns._ID+" AS "+Match.BLUE3+Team.VIEW_ID+"," +
							Match.BLUE3+Team.TAG+"."+Database.MODIFIED+" AS "+Match.BLUE3+Team.VIEW_MODIFIED+"," +
							Match.BLUE3+Team.TAG+"."+Team.NUMBER+" AS "+Match.BLUE3+Team.VIEW_NUMBER+"," +
							Match.BLUE3+Team.TAG+"."+Team.NAME+" AS "+Match.BLUE3+Team.VIEW_NAME +
							" FROM " +
							Match2014.TABLE+" "+Match2014.TAG+"," +
							Competition.TABLE+" "+Competition.TAG+"," +
							Season.TABLE+" "+Season.TAG+"," +
							Team.TABLE+" "+Match.RED1+Team.TAG+"," +
							Team.TABLE+" "+Match.RED2+Team.TAG+"," +
							Team.TABLE+" "+Match.RED3+Team.TAG+"," +
							Team.TABLE+" "+Match.BLUE1+Team.TAG+"," +
							Team.TABLE+" "+Match.BLUE2+Team.TAG+"," +
							Team.TABLE+" "+Match.BLUE3+Team.TAG+
							" WHERE " +
							Match.COMPETITION+"="+Competition.VIEW_ID+
							" AND "+
							Competition.VIEW_SEASON+"="+Season.VIEW_ID+
							" AND "+
							Match.RED1+"="+Match.RED1+Team.VIEW_ID+
							" AND "+
							Match.RED2+"="+Match.RED2+Team.VIEW_ID+
							" AND "+
							Match.RED3+"="+Match.RED3+Team.VIEW_ID+
							" AND "+
							Match.BLUE1+"="+Match.BLUE1+Team.VIEW_ID+
							" AND "+
							Match.BLUE2+"="+Match.BLUE2+Team.VIEW_ID+
							" AND "+
							Match.BLUE3+"="+Match.BLUE3+Team.VIEW_ID+
							";";
					Log.i(TAG,schema);
					db.execSQL(schema);
					
					Log.i(TAG,"At version 5");
				default:
			}
		}
	}
}
