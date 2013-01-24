package com.mechinn.android.ouralliance.provider;

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
		return db.isDatabaseIntegrityOk();
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAMS:
				queryBuilder = buildMyQuery(queryBuilder, projection, Team.ALLCOLUMNS, Team.TABLE, null);
				break;
			case CODE_TEAM_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Team.ALLCOLUMNS, Team.TABLE, BaseColumns._ID);
				break;
			case CODE_TEAM_NUMBER:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Team.ALLCOLUMNS, Team.TABLE, Team.NUMBER);
				break;
			case CODE_SEASONS:
				queryBuilder = buildMyQuery(queryBuilder, projection, Season.ALLCOLUMNS, Season.TABLE, null);
				break;
			case CODE_SEASON_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Season.ALLCOLUMNS, Season.TABLE, BaseColumns._ID);
				break;
			case CODE_SEASON_YEAR:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Season.ALLCOLUMNS, Season.TABLE, Season.YEAR);
				break;
			case CODE_COMPETITIONS:
				queryBuilder = buildMyQuery(queryBuilder, projection, Competition.VIEWCOLUMNS, Competition.VIEW, null);
				break;
			case CODE_COMPETITION_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Competition.VIEWCOLUMNS, Competition.VIEW, BaseColumns._ID);
				break;
			case CODE_COMPETITION_SEASON:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Competition.VIEWCOLUMNS, Competition.VIEW, Competition.SEASON);
				break;
			case CODE_COMPETITION_CODE:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, Competition.VIEWCOLUMNS, Competition.VIEW, Competition.CODE);
				break;
			case CODE_TEAMSCOUTINGS:
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScouting.VIEWCOLUMNS, TeamScouting.VIEW, null);
				break;
			case CODE_TEAMSCOUTING_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, TeamScouting.VIEWCOLUMNS, TeamScouting.VIEW, BaseColumns._ID);
				break;
			case CODE_TEAMSCOUTING_SEASON:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, TeamScouting.VIEWCOLUMNS, TeamScouting.VIEW, TeamScouting.SEASON);
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
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScouting.VIEWCOLUMNS, TeamScouting.VIEW, TeamScouting.SEASON+"="+season+" and "+TeamScouting.TEAM+"="+team);
				break;
			case CODE_COMPETITIONTEAMS:
				queryBuilder = buildMyQuery(queryBuilder, projection, CompetitionTeam.VIEWCOLUMNS, CompetitionTeam.VIEW, null);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, CompetitionTeam.VIEWCOLUMNS, CompetitionTeam.VIEW, BaseColumns._ID);
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, CompetitionTeam.VIEWCOLUMNS, CompetitionTeam.VIEW, CompetitionTeam.TEAM);
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, CompetitionTeam.VIEWCOLUMNS, CompetitionTeam.VIEW, CompetitionTeam.COMPETITION);
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
				rowsDeleted = deleteOrUpdate(Team.TABLE, null, selection, selectionArgs, null);
				break;
			case CODE_TEAM_ID:
				rowsDeleted = deleteOrUpdate(uri, Team.TABLE, null, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAM_NUMBER:
				rowsDeleted = deleteOrUpdate(uri, Team.TABLE, null, selection, selectionArgs, Team.NUMBER);
				break;
			case CODE_SEASONS:
				rowsDeleted = deleteOrUpdate(Season.TABLE, null, selection, selectionArgs, null);
				break;
			case CODE_SEASON_ID:
				rowsDeleted = deleteOrUpdate(uri, Season.TABLE, null, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_SEASON_YEAR:
				rowsDeleted = deleteOrUpdate(uri, Season.TABLE, null, selection, selectionArgs, Season.YEAR);
				break;
			case CODE_COMPETITIONS:
				rowsDeleted = deleteOrUpdate(Competition.TABLE, null, selection, selectionArgs, null);
				break;
			case CODE_COMPETITION_ID:
				rowsDeleted = deleteOrUpdate(uri, Competition.TABLE, null, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITION_SEASON:
				rowsDeleted = deleteOrUpdate(uri, Competition.TABLE, null, selection, selectionArgs, Competition.SEASON);
				break;
			case CODE_COMPETITION_CODE:
				rowsDeleted = deleteOrUpdate(uri, Competition.TABLE, null, selection, selectionArgs, Competition.CODE);
				break;
			case CODE_TEAMSCOUTINGS:
				rowsDeleted = deleteOrUpdate(TeamScouting.TABLE, null, selection, selectionArgs, null);
				break;
			case CODE_TEAMSCOUTING_ID:
				rowsDeleted = deleteOrUpdate(uri, TeamScouting.TABLE, null, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAMSCOUTING_SEASON:
				rowsDeleted = deleteOrUpdate(uri, TeamScouting.TABLE, null, selection, selectionArgs, TeamScouting.SEASON);
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
				rowsDeleted = deleteOrUpdate(uri, TeamScouting.TABLE, null, selection, selectionArgs, TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team);
				break;
			case CODE_COMPETITIONTEAMS:
				rowsDeleted = deleteOrUpdate(CompetitionTeam.TABLE, null, selection, selectionArgs, null);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				rowsDeleted = deleteOrUpdate(uri, CompetitionTeam.TABLE, null, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				rowsDeleted = deleteOrUpdate(uri, CompetitionTeam.TABLE, null, selection, selectionArgs, CompetitionTeam.TEAM);
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				rowsDeleted = deleteOrUpdate(uri, CompetitionTeam.TABLE, null, selection, selectionArgs, CompetitionTeam.COMPETITION);
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
				rowsUpdated = deleteOrUpdate(Team.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_TEAM_ID:
				rowsUpdated = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAM_NUMBER:
				rowsUpdated = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, Team.NUMBER);
				break;
			case CODE_SEASONS:
				rowsUpdated = deleteOrUpdate(Season.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_SEASON_ID:
				rowsUpdated = deleteOrUpdate(uri, Season.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_SEASON_YEAR:
				rowsUpdated = deleteOrUpdate(uri, Season.TABLE, values, selection, selectionArgs, Season.YEAR);
				break;
			case CODE_COMPETITIONS:
				rowsUpdated = deleteOrUpdate(Competition.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_COMPETITION_ID:
				rowsUpdated = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITION_SEASON:
				rowsUpdated = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, Competition.SEASON);
				break;
			case CODE_COMPETITION_CODE:
				rowsUpdated = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, Competition.CODE);
				break;
			case CODE_TEAMSCOUTINGS:
				rowsUpdated = deleteOrUpdate(TeamScouting.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_TEAMSCOUTING_ID:
				rowsUpdated = deleteOrUpdate(uri, TeamScouting.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAMSCOUTING_SEASON:
				rowsUpdated = deleteOrUpdate(uri, TeamScouting.TABLE, values, selection, selectionArgs, TeamScouting.SEASON);
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
				rowsUpdated = deleteOrUpdate(uri, TeamScouting.TABLE, values, selection, selectionArgs, TeamScouting.SEASON + "=" + season + " and " + TeamScouting.TEAM + "=" + team);
				break;
			case CODE_COMPETITIONTEAMS:
				rowsUpdated = deleteOrUpdate(Team.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				rowsUpdated = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				rowsUpdated = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, CompetitionTeam.TEAM);
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				rowsUpdated = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, CompetitionTeam.COMPETITION);
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
	
	private SQLiteQueryBuilder buildMyQuery(SQLiteQueryBuilder queryBuilder, Uri uri, String[] projection, String[] allCols, String table, String where) {
		if(null!=where) {
			return buildMyQuery(queryBuilder, projection, allCols, table, where+"='"+uri.getLastPathSegment()+"'");
		}
		return buildMyQuery(queryBuilder, projection, allCols, table, where);
	}
	
	private SQLiteQueryBuilder buildMyQuery(SQLiteQueryBuilder queryBuilder, String[] projection, String[] allCols, String table, String where) {
		checkColumns(projection, allCols);
		queryBuilder.setTables(table);
		if(null!=where) {
			queryBuilder.appendWhere(where);
		}
		return queryBuilder;
	}
	
	private int deleteOrUpdate(Uri uri, String table, ContentValues values, String selection, String[] selectionArgs, String where) {
		if(null!=where) {
			return deleteOrUpdate(table, values, selection, selectionArgs, where+"='"+uri.getLastPathSegment()+"'");
		}
		return deleteOrUpdate(table, values, selection, selectionArgs, where);
	}
	
	private int deleteOrUpdate(String table, ContentValues values, String selection, String[] selectionArgs, String where) {
		if(null!=where) {
			if (!TextUtils.isEmpty(selection)) {
				where +=  " and " + selection;
			}
			selection = where;
		}
		if(values!=null) {
			return db.update(table, values, selection, selectionArgs);
		}
		return db.delete(table, selection, selectionArgs);
	}
}
