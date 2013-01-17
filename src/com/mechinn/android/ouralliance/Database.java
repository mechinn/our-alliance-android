package com.mechinn.android.ouralliance;


import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	private static final String tag = "Database";
	public static final String NAME = "ouralliance.sqlite";
	public static final String MODIFIED = "modified";
	public static final int VERSION = 1;

	public Database(Context context) {
		super(context, NAME, null, VERSION);
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

	public void reset() {
		Log.i(tag,"Reset Database");
		setup(this.getWritableDatabase(),-1);
	}
	
	public void setup(SQLiteDatabase db, int currentVersion) {
		Log.i(tag,"Updating Database from "+currentVersion+" to "+VERSION);
		switch(currentVersion+1) {
			default:
				Log.i(tag,"Clear Database");
				db.execSQL("DROP TABLE IF EXISTS "+Team.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+Season.TABLE);
				db.execSQL("DROP TABLE IF EXISTS "+TeamScouting.TABLE);
			case 1:
				Log.i(tag,"Upgrade to version 1");
				
				Log.i(tag,"Enable Foreign Keys");
				db.execSQL("PRAGMA foreign_keys=ON;");
				
				Log.i(tag,"create Team table");
				db.execSQL("CREATE TABLE "+Team.TABLE+" ( "+
					BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
					MODIFIED+" DATE NOT NULL," +
					Team.NUMBER+" INTEGER NOT NULL UNIQUE," +
					Team.NAME+" TEXT" +
					" );");
				
				Log.i(tag,"create Team table");
				db.execSQL("CREATE TABLE "+Season.TABLE+" ( "+
					BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
					MODIFIED+" DATE NOT NULL," +
					Season.YEAR+" INTEGER NOT NULL UNIQUE," +
					Season.COMPETITION+" TEXT" +
					" );");
				
				Log.i(tag,"create Team table");
				db.execSQL("CREATE TABLE "+TeamScouting.TABLE+" ( "+
					BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
					MODIFIED+" DATE NOT NULL," +
					TeamScouting.SEASON+" FOREIGN KEY NOT NULL UNIQUE," +
					TeamScouting.TEAM+" FOREIGN KEY NOT NULL UNIQUE," +
					TeamScouting.RANK+" INTEGER NOT NULL," +
					TeamScouting.ORIENTATION+" TEXT," +
					TeamScouting.WIDTH+" INTEGER," +
					TeamScouting.LENGTH+" INTEGER," +
					TeamScouting.HEIGHT+" INTEGER," +
					TeamScouting.NOTES+" TEXT" +
					" );");
				
				Log.i(tag,"At version 1");
//			case 2:
//				Log.i(tag,"Upgrade to version 2");
//				
//				Log.i(tag,"At version 2");
		}
	}
}
