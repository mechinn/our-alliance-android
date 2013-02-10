package com.mechinn.android.ouralliance.provider;

import java.util.Arrays;
import java.util.HashSet;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class DataProvider extends ContentProvider {
	private static final String TAG = "DataProvider";
	
	// database
	private Database database;
	private SQLiteDatabase db;
	
	public static final String AUTHORITY = "com.mechinn.android.ouralliance.data";
	public static final String BASE_URI_STRING = "content://" + AUTHORITY + "/";

	private static final int CODE_TEAM = 1;
	private static final int CODE_SEASON = 2;
	private static final int CODE_COMPETITION = 3;
	private static final int CODE_COMPETITIONTEAM = 4;
	private static final int CODE_TEAMSCOUTINGWHEEL = 5;
	private static final int CODE_2013TEAMSCOUTING = 6;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, Team.TABLE, CODE_TEAM);
		sURIMatcher.addURI(AUTHORITY, Season.TABLE, CODE_SEASON);
		sURIMatcher.addURI(AUTHORITY, Competition.TABLE, CODE_COMPETITION);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.TABLE, CODE_COMPETITIONTEAM);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.TABLE, CODE_TEAMSCOUTINGWHEEL);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.TABLE, CODE_2013TEAMSCOUTING);
	}
	
	private void checkColumns(String[] projection, String[] expected) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(expected));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				String message = "";
				for(String each : requestedColumns) {
					if(!availableColumns.contains(each)) {
						message += each+", ";
					}
				}
				throw new IllegalArgumentException("Unknown columns in projection: "+message);
			}
		}
	}
	
	@Override
	public boolean onCreate() {
		database = new Database(this.getContext());
		db = database.getWritableDatabase();
		return db.isDatabaseIntegrityOk();
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, uri.toString());
		String table = null;
		String[] cols = null;
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAM:
				cols = Team.ALLCOLUMNS;
				table = Team.TABLE;
				break;
			case CODE_SEASON:
				cols = Season.ALLCOLUMNS;
				table = Season.TABLE;
				break;
			case CODE_COMPETITION:
				cols = Competition.VIEWCOLUMNS;
				table = Competition.VIEW;
				break;
			case CODE_COMPETITIONTEAM:
				cols = CompetitionTeam.VIEWCOLUMNS;
				table = CompetitionTeam.VIEW;
				break;
			case CODE_TEAMSCOUTINGWHEEL:
				cols = TeamScoutingWheel.VIEWCOLUMNS;
				table = TeamScoutingWheel.VIEW;
				break;
			case CODE_2013TEAMSCOUTING:
				cols = TeamScouting2013.VIEWCOLUMNS;
				table = TeamScouting2013.VIEW;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		db = database.getWritableDatabase();
		if(null!=table) {
			Log.d(TAG, "table: "+table);
			if(null!=projection) {
				for(String each : projection) {
					Log.d(TAG, "projection: "+each);
				}
			}
			if(null!=selection) {
				Log.d(TAG, "selection: "+selection);
			}
			if(null!=selectionArgs) {
				for(String each : selectionArgs) {
					Log.d(TAG, "selectionArgs: "+each);
				}
			}
			if(null!=sortOrder) {
				Log.d(TAG, "sortOrder: "+sortOrder);
			}
		} else {
			Log.d(TAG, "no table");
		}
		checkColumns(projection, cols);
		queryBuilder.setTables(table);
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		Log.d(TAG, "query: "+uri.toString());
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		Log.d(TAG, uri.toString());
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAM:
				return Team.URITYPE;
			case CODE_SEASON:
				return Season.URITYPE;
			case CODE_COMPETITION:
				return Competition.URITYPE;
			case CODE_COMPETITIONTEAM:
				return CompetitionTeam.URITYPE;
			case CODE_TEAMSCOUTINGWHEEL:
				return TeamScoutingWheel.URITYPE;
			case CODE_2013TEAMSCOUTING:
				return TeamScouting2013.URITYPE;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, uri.toString());
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		long id = 0;
		Uri newUri;
		switch (uriType) {
			case CODE_TEAM:
				id = db.insert(Team.TABLE, null, values);
				newUri = Uri.parse(Team.URI+"/"+id);
				break;
			case CODE_SEASON:
				id = db.insert(Season.TABLE, null, values);
				newUri = Uri.parse(Season.URI+"/"+id);
				break;
			case CODE_COMPETITION:
				id = db.insert(Competition.TABLE, null, values);
				newUri = Uri.parse(Competition.URI+"/"+id);
				break;
			case CODE_COMPETITIONTEAM:
				id = db.insert(CompetitionTeam.TABLE, null, values);
				newUri = Uri.parse(CompetitionTeam.URI+"/"+id);
				break;
			case CODE_TEAMSCOUTINGWHEEL:
				id = db.insert(TeamScoutingWheel.TABLE, null, values);
				newUri = Uri.parse(TeamScoutingWheel.URI+"/"+id);
				break;
			case CODE_2013TEAMSCOUTING:
				id = db.insert(TeamScouting2013.TABLE, null, values);
				newUri = Uri.parse(TeamScouting2013.URI+"/"+id);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		Log.d(TAG, "insert: "+uri.toString());
		getContext().getContentResolver().notifyChange(uri, null);
		return newUri;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return update(uri, null, selection, selectionArgs);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, uri.toString());
		int rows = 0;
		String table;
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAM:
				table = Team.TABLE;
				break;
			case CODE_SEASON:
				table = Season.TABLE;
				break;
			case CODE_COMPETITION:
				table = Competition.TABLE;
				break;
			case CODE_COMPETITIONTEAM:
				table = CompetitionTeam.TABLE;
				break;
			case CODE_TEAMSCOUTINGWHEEL:
				table = TeamScoutingWheel.TABLE;
				break;
			case CODE_2013TEAMSCOUTING:
				table = TeamScouting2013.TABLE;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		db = database.getWritableDatabase();
		if(values!=null) {
			rows = db.update(table, values, selection, selectionArgs);
			switch (uriType) {
				case CODE_TEAM:
					getContext().getContentResolver().notifyChange(Team.URI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					break;
				case CODE_SEASON:
					getContext().getContentResolver().notifyChange(Season.URI, null);
					getContext().getContentResolver().notifyChange(Competition.URI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					break;
				case CODE_COMPETITION:
					getContext().getContentResolver().notifyChange(Competition.URI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					break;
				case CODE_COMPETITIONTEAM:
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					break;
				case CODE_TEAMSCOUTINGWHEEL:
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					break;
				case CODE_2013TEAMSCOUTING:
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		} else {
			rows = db.delete(table, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		Log.d(TAG, "delete/update: "+uri.toString());
		return rows;
	}
}
