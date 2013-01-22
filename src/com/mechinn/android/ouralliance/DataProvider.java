package com.mechinn.android.ouralliance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class DataProvider extends ContentProvider {
	private static final String TAG = "DataProvider";
	
	// database
	private Database database;
	private SQLiteDatabase db;
	
	public static final String AUTHORITY = "com.mechinn.android.ouralliance.data";
	public static final String BASE_URI_STRING = "content://" + AUTHORITY + "/";
	
	public static final String BASE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd."+AUTHORITY+".";
	public static final String BASE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd."+AUTHORITY+".";

	private static final int CODE_TEAMS = 1;
	private static final int CODE_TEAM_ID = 2;
	private static final int CODE_TEAM_NUMBER = 3;
	private static final int CODE_SEASONS = 4;
	private static final int CODE_SEASON_ID = 5;
	private static final int CODE_SEASON_YEAR = 6;
	private static final int CODE_COMPETITIONS = 7;
	private static final int CODE_COMPETITION_ID = 8;
	private static final int CODE_COMPETITION_SEASON = 9;
	private static final int CODE_COMPETITION_CODE = 10;
	private static final int CODE_TEAMSCOUTINGS = 11;
	private static final int CODE_TEAMSCOUTING_ID = 12;
	private static final int CODE_TEAMSCOUTING_SEASON = 13;
	private static final int CODE_TEAMSCOUTING_SEASONTEAM = 14;
	private static final int CODE_COMPETITIONTEAMS = 15;
	private static final int CODE_COMPETITIONTEAMS_ID = 16;
	private static final int CODE_COMPETITIONTEAMS_TEAM = 17;
	private static final int CODE_COMPETITIONTEAMS_COMP = 18;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, Team.PATH, CODE_TEAMS);
		sURIMatcher.addURI(AUTHORITY, Team.IDPATH + "#", CODE_TEAM_ID);
		sURIMatcher.addURI(AUTHORITY, Team.NUMPATH + "#", CODE_TEAM_NUMBER);
		sURIMatcher.addURI(AUTHORITY, Season.PATH, CODE_SEASONS);
		sURIMatcher.addURI(AUTHORITY, Season.IDPATH + "#", CODE_SEASON_ID);
		sURIMatcher.addURI(AUTHORITY, Season.YEARPATH + "#", CODE_SEASON_YEAR);
		sURIMatcher.addURI(AUTHORITY, Competition.PATH, CODE_COMPETITIONS);
		sURIMatcher.addURI(AUTHORITY, Competition.IDPATH + "#", CODE_COMPETITION_ID);
		sURIMatcher.addURI(AUTHORITY, Competition.SEASONPATH + "#", CODE_COMPETITION_SEASON);
		sURIMatcher.addURI(AUTHORITY, Competition.CODEPATH + "*", CODE_COMPETITION_CODE);
		sURIMatcher.addURI(AUTHORITY, TeamScouting.PATH, CODE_TEAMSCOUTINGS);
		sURIMatcher.addURI(AUTHORITY, TeamScouting.IDPATH + "#", CODE_TEAMSCOUTING_ID);
		sURIMatcher.addURI(AUTHORITY, TeamScouting.SEASONPATH + "#", CODE_TEAMSCOUTING_SEASON);
		sURIMatcher.addURI(AUTHORITY, TeamScouting.SEASONPATH + "#" + TeamScouting.TEAMADDON + "#", CODE_TEAMSCOUTING_SEASONTEAM);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.PATH, CODE_COMPETITIONTEAMS);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.IDPATH + "#", CODE_COMPETITIONTEAMS_ID);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.TEAMPATH + "#", CODE_COMPETITIONTEAMS_TEAM);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.COMPPATH + "#", CODE_COMPETITIONTEAMS_COMP);
	}
	
	@Override
	public boolean onCreate() {
		database = new Database(this.getContext());
		db = database.getWritableDatabase();
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAMS:
				checkColumns(projection, Team.ALLCOLUMNS);
				queryBuilder.setTables(Team.TABLE);
				break;
			case CODE_TEAM_ID:
				// Adding the ID to the original query
				checkColumns(projection, Team.ALLCOLUMNS);
				queryBuilder.setTables(Team.TABLE);
				queryBuilder.appendWhere(BaseColumns._ID+"="+uri.getLastPathSegment());
				break;
			case CODE_TEAM_NUMBER:
				// Adding the ID to the original query
				checkColumns(projection, Team.ALLCOLUMNS);
				queryBuilder.setTables(Team.TABLE);
				queryBuilder.appendWhere(Team.NUMBER+"="+uri.getLastPathSegment());
				break;
			case CODE_SEASONS:
				checkColumns(projection, Season.ALLCOLUMNS);
				queryBuilder.setTables(Season.TABLE);
				break;
			case CODE_SEASON_ID:
				// Adding the ID to the original query
				checkColumns(projection, Season.ALLCOLUMNS);
				queryBuilder.setTables(Season.TABLE);
				queryBuilder.appendWhere(BaseColumns._ID+"="+uri.getLastPathSegment());
				break;
			case CODE_SEASON_YEAR:
				// Adding the ID to the original query
				checkColumns(projection, Season.ALLCOLUMNS);
				queryBuilder.setTables(Season.TABLE);
				queryBuilder.appendWhere(Season.YEAR+"="+uri.getLastPathSegment());
				break;
			case CODE_COMPETITIONS:
				checkColumns(projection, Competition.VIEWCOLUMNS);
				queryBuilder.setTables(Competition.VIEW);
				break;
			case CODE_COMPETITION_ID:
				// Adding the ID to the original query
				checkColumns(projection, Competition.VIEWCOLUMNS);
				queryBuilder.setTables(Competition.VIEW);
				queryBuilder.appendWhere(Competition.VIEW_ID+"="+uri.getLastPathSegment());
				break;
			case CODE_COMPETITION_SEASON:
				// Adding the ID to the original query
				checkColumns(projection, Competition.VIEWCOLUMNS);
				queryBuilder.setTables(Competition.VIEW);
				queryBuilder.appendWhere(Competition.VIEW_SEASON+"="+uri.getLastPathSegment());
				break;
			case CODE_COMPETITION_CODE:
				// Adding the ID to the original query
				checkColumns(projection, Competition.VIEWCOLUMNS);
				queryBuilder.setTables(Competition.VIEW);
				queryBuilder.appendWhere(Competition.VIEW_CODE+"="+uri.getLastPathSegment());
				break;
			case CODE_TEAMSCOUTINGS:
				checkColumns(projection, TeamScouting.VIEWCOLUMNS);
				queryBuilder.setTables(TeamScouting.VIEW);
				break;
			case CODE_TEAMSCOUTING_ID:
				// Adding the ID to the original query
				checkColumns(projection, TeamScouting.VIEWCOLUMNS);
				queryBuilder.setTables(TeamScouting.VIEW);
				queryBuilder.appendWhere(TeamScouting.VIEW_ID+"="+uri.getLastPathSegment());
				break;
			case CODE_TEAMSCOUTING_SEASON:
				// Adding the ID to the original query
				checkColumns(projection, TeamScouting.VIEWCOLUMNS);
				queryBuilder.setTables(TeamScouting.VIEW);
				queryBuilder.appendWhere(TeamScouting.VIEW_SEASON+"="+uri.getLastPathSegment());
				break;
			case CODE_TEAMSCOUTING_SEASONTEAM:
				// Adding the ID to the original query
				checkColumns(projection, TeamScouting.VIEWCOLUMNS);
				queryBuilder.setTables(TeamScouting.VIEW);
				List<String> segments = uri.getPathSegments();
				for(String each : segments) {
					Log.d(TAG, each);
				}
				String season = segments.get(segments.size()-3);
				Log.d(TAG, season);
				String team = segments.get(segments.size()-1);
				Log.d(TAG, team);
				queryBuilder.appendWhere(TeamScouting.VIEW_SEASON+"="+season+" and "+TeamScouting.VIEW_TEAM+"="+team);
				break;
			case CODE_COMPETITIONTEAMS:
				checkColumns(projection, CompetitionTeam.VIEWCOLUMNS);
				queryBuilder.setTables(CompetitionTeam.VIEW);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				// Adding the ID to the original query
				checkColumns(projection, CompetitionTeam.VIEWCOLUMNS);
				queryBuilder.setTables(CompetitionTeam.VIEW);
				queryBuilder.appendWhere(CompetitionTeam.VIEW_ID+"="+uri.getLastPathSegment());
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				// Adding the ID to the original query
				checkColumns(projection, CompetitionTeam.VIEWCOLUMNS);
				queryBuilder.setTables(CompetitionTeam.VIEW);
				queryBuilder.appendWhere(CompetitionTeam.VIEW_TEAM+"="+uri.getLastPathSegment());
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				// Adding the ID to the original query
				checkColumns(projection, CompetitionTeam.VIEWCOLUMNS);
				queryBuilder.setTables(CompetitionTeam.VIEW);
				queryBuilder.appendWhere(CompetitionTeam.VIEW_COMPETITION+"="+uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAMS:
				return Team.DIRTYPE;
			case CODE_TEAM_ID:
			case CODE_TEAM_NUMBER:
				return Team.ITEMTYPE;
			case CODE_SEASONS:
				return Season.DIRTYPE;
			case CODE_SEASON_ID:
			case CODE_SEASON_YEAR:
				return Season.ITEMTYPE;
			case CODE_COMPETITIONS:
			case CODE_COMPETITION_SEASON:
				return Competition.DIRTYPE;
			case CODE_COMPETITION_ID:
			case CODE_COMPETITION_CODE:
				return Competition.ITEMTYPE;
			case CODE_TEAMSCOUTINGS:
			case CODE_TEAMSCOUTING_SEASON:
				return TeamScouting.DIRTYPE;
			case CODE_TEAMSCOUTING_ID:
			case CODE_TEAMSCOUTING_SEASONTEAM:
				return TeamScouting.ITEMTYPE;
			case CODE_COMPETITIONTEAMS:
			case CODE_COMPETITIONTEAMS_COMP:
				return CompetitionTeam.DIRTYPE;
			case CODE_COMPETITIONTEAMS_ID:
			case CODE_COMPETITIONTEAMS_TEAM:
				return CompetitionTeam.ITEMTYPE;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
			case CODE_TEAMS:
				id = db.insert(Team.TABLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return Team.uriFromId(id);
			case CODE_SEASONS:
				id = db.insert(Season.TABLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return Season.uriFromId(id);
			case CODE_COMPETITIONS:
				id = db.insert(Competition.TABLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return Competition.uriFromId(id);
			case CODE_TEAMSCOUTINGS:
				id = db.insert(TeamScouting.TABLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return TeamScouting.uriFromId(id);
			case CODE_COMPETITIONTEAMS:
				id = db.insert(CompetitionTeam.TABLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return CompetitionTeam.uriFromId(id);
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		int rowsDeleted = 0;
		String where;
		switch (uriType) {
			case CODE_TEAMS:
				rowsDeleted = db.delete(Team.TABLE, selection, selectionArgs);
				break;
			case CODE_TEAM_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Team.TABLE,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Team.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAM_NUMBER:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Team.TABLE,
						Team.NUMBER + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Team.TABLE,
						Team.NUMBER + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_SEASONS:
				rowsDeleted = db.delete(Season.TABLE, selection, selectionArgs);
				break;
			case CODE_SEASON_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Season.TABLE,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Season.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_SEASON_YEAR:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Season.TABLE,
						Season.YEAR + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Season.TABLE,
						Season.YEAR + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONS:
				rowsDeleted = db.delete(Competition.TABLE, selection, selectionArgs);
				break;
			case CODE_COMPETITION_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Competition.TABLE,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Competition.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITION_SEASON:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Competition.TABLE,
						Competition.SEASON + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Competition.TABLE,
						Competition.SEASON + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITION_CODE:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						Competition.TABLE,
						Competition.CODE + "=" + where+"'", 
						null);
				} else {
					rowsDeleted = db.delete(
						Competition.TABLE,
						Competition.CODE + "='" + where + "' and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTINGS:
				rowsDeleted = db.delete(TeamScouting.TABLE, selection, selectionArgs);
				break;
			case CODE_TEAMSCOUTING_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTING_SEASON:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						TeamScouting.SEASON + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						TeamScouting.SEASON + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTING_SEASONTEAM:
				List<String> segments = uri.getPathSegments();
				for(String each : segments) {
					Log.d(TAG, each);
				}
				String season = segments.get(segments.size()-3);
				Log.d(TAG, season);
				String team = segments.get(segments.size()-1);
				Log.d(TAG, team);
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team, 
						null);
				} else {
					rowsDeleted = db.delete(
						TeamScouting.TABLE,
						TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS:
				rowsDeleted = db.delete(CompetitionTeam.TABLE, selection, selectionArgs);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						CompetitionTeam.TEAM + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						CompetitionTeam.TEAM + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						CompetitionTeam.COMPETITION + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						CompetitionTeam.TABLE,
						CompetitionTeam.COMPETITION + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		int rowsUpdated = 0;
		String where;
		switch (uriType) {
			case CODE_TEAMS:
				rowsUpdated = db.update(
					Team.TABLE, 
					values, 
					selection,
					selectionArgs);
				break;
			case CODE_TEAM_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Team.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Team.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAM_NUMBER:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Team.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Team.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_SEASONS:
				rowsUpdated = db.update(
					Season.TABLE, 
					values, 
					selection,
					selectionArgs);
				break;
			case CODE_SEASON_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Season.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Season.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_SEASON_YEAR:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Season.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Season.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONS:
				rowsUpdated = db.update(
					Competition.TABLE, 
					values, 
					selection,
					selectionArgs);
				break;
			case CODE_COMPETITION_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITION_SEASON:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						Competition.SEASON + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						Competition.SEASON + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITION_CODE:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						Competition.CODE + "='" + where+"'", 
						null);
				} else {
					rowsUpdated = db.update(
						Competition.TABLE, 
						values,
						Competition.CODE + "='" + where + "' and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTINGS:
				rowsUpdated = db.update(
					TeamScouting.TABLE, 
					values, 
					selection,
					selectionArgs);
				break;
			case CODE_TEAMSCOUTING_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTING_SEASON:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						TeamScouting.SEASON + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						TeamScouting.SEASON + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_TEAMSCOUTING_SEASONTEAM:
				List<String> segments = uri.getPathSegments();
				for(String each : segments) {
					Log.d(TAG, each);
				}
				String season = segments.get(segments.size()-3);
				Log.d(TAG, season);
				String team = segments.get(segments.size()-1);
				Log.d(TAG, team);
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team, 
						null);
				} else {
					rowsUpdated = db.update(
						TeamScouting.TABLE, 
						values,
						TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS:
				rowsUpdated = db.update(
					CompetitionTeam.TABLE, 
					values, 
					selection,
					selectionArgs);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						BaseColumns._ID + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						CompetitionTeam.TEAM + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						CompetitionTeam.TEAM + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				where = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						CompetitionTeam.COMPETITION + "=" + where, 
						null);
				} else {
					rowsUpdated = db.update(
						CompetitionTeam.TABLE, 
						values,
						CompetitionTeam.COMPETITION + "=" + where + " and " + selection,
						selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
	
	private void checkColumns(String[] projection, String[] expected) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(expected));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
