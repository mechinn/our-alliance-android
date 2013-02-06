package com.mechinn.android.ouralliance.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;

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
	private static final int CODE_COMPETITIONTEAMS = 11;
	private static final int CODE_COMPETITIONTEAMS_ID = 12;
	private static final int CODE_COMPETITIONTEAMS_TEAM = 13;
	private static final int CODE_COMPETITIONTEAMS_COMP = 14;
	private static final int CODE_TEAMSCOUTINGWHEELS = 15;
	private static final int CODE_TEAMSCOUTINGWHEEL_ID = 16;
	private static final int CODE_TEAMSCOUTINGWHEEL_SCOUTING = 17;
	private static final int CODE_2013TEAMSCOUTINGS = 18;
	private static final int CODE_2013TEAMSCOUTING_ID = 19;
	private static final int CODE_2013TEAMSCOUTING_SEASON = 20;
	private static final int CODE_2013TEAMSCOUTING_SEASONTEAM = 21;
	
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
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.PATH, CODE_COMPETITIONTEAMS);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.IDPATH + "#", CODE_COMPETITIONTEAMS_ID);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.TEAMPATH + "#", CODE_COMPETITIONTEAMS_TEAM);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.COMPPATH + "#", CODE_COMPETITIONTEAMS_COMP);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.PATH, CODE_TEAMSCOUTINGWHEELS);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.IDPATH + "#", CODE_TEAMSCOUTINGWHEEL_ID);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.SEASONPATH + "#" + TeamScoutingWheel.TEAMADDON + "#", CODE_TEAMSCOUTINGWHEEL_SCOUTING);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.PATH, CODE_2013TEAMSCOUTINGS);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.IDPATH + "#", CODE_2013TEAMSCOUTING_ID);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.SEASONPATH + "#", CODE_2013TEAMSCOUTING_SEASON);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.SEASONPATH + "#" + TeamScouting2013.TEAMADDON + "#", CODE_2013TEAMSCOUTING_SEASONTEAM);
	}
	
	@Override
	public boolean onCreate() {
		database = new Database(this.getContext());
		db = database.getWritableDatabase();
		return db.isDatabaseIntegrityOk();
	}
	
	private Map<String,String> getSeasonTeam(Uri uri) {
		Map<String,String> scouting = new HashMap<String,String>();
		// Adding the ID to the original query
		List<String> segments = uri.getPathSegments();
		for(String each : segments) {
			Log.d(TAG, each);
		}
		String season = segments.get(segments.size()-3);
		Log.d(TAG, season);
		scouting.put(Season.CLASS, season);
		String team = segments.get(segments.size()-1);
		Log.d(TAG, team);
		scouting.put(Team.CLASS, team);
		return scouting;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, uri.toString());
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		Map<String,String> scouting = null;
		String where = null;
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
			case CODE_TEAMSCOUTINGWHEELS:
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScoutingWheel.VIEWCOLUMNS, TeamScoutingWheel.VIEW, null);
				break;
			case CODE_TEAMSCOUTINGWHEEL_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, TeamScoutingWheel.VIEWCOLUMNS, TeamScoutingWheel.VIEW, BaseColumns._ID);
				break;
			case CODE_TEAMSCOUTINGWHEEL_SCOUTING:
				scouting = getSeasonTeam(uri);
				where = TeamScoutingWheel.SEASON+"="+scouting.get(Season.CLASS)+" and "+TeamScoutingWheel.TEAM+"="+scouting.get(Team.CLASS);
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScoutingWheel.VIEWCOLUMNS, TeamScoutingWheel.VIEW, where);
				break;
			case CODE_2013TEAMSCOUTINGS:
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScouting2013.VIEWCOLUMNS, TeamScouting2013.VIEW, null);
				break;
			case CODE_2013TEAMSCOUTING_ID:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, TeamScouting2013.VIEWCOLUMNS, TeamScouting2013.VIEW, BaseColumns._ID);
				break;
			case CODE_2013TEAMSCOUTING_SEASON:
				queryBuilder = buildMyQuery(queryBuilder, uri, projection, TeamScouting2013.VIEWCOLUMNS, TeamScouting2013.VIEW, TeamScouting2013.SEASON);
				break;
			case CODE_2013TEAMSCOUTING_SEASONTEAM:
				scouting = getSeasonTeam(uri);
				where = TeamScouting2013.SEASON+"="+scouting.get(Season.CLASS)+" and "+TeamScouting2013.TEAM+"="+scouting.get(Team.CLASS);
				queryBuilder = buildMyQuery(queryBuilder, projection, TeamScouting2013.VIEWCOLUMNS, TeamScouting2013.VIEW, where);
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
		Log.d(TAG, uri.toString());
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
			case CODE_COMPETITIONTEAMS:
			case CODE_COMPETITIONTEAMS_COMP:
				return CompetitionTeam.DIRTYPE;
			case CODE_COMPETITIONTEAMS_ID:
			case CODE_COMPETITIONTEAMS_TEAM:
				return CompetitionTeam.ITEMTYPE;
			case CODE_TEAMSCOUTINGWHEELS:
			case CODE_TEAMSCOUTINGWHEEL_SCOUTING:
				return TeamScoutingWheel.DIRTYPE;
			case CODE_TEAMSCOUTINGWHEEL_ID:
				return TeamScoutingWheel.ITEMTYPE;
			case CODE_2013TEAMSCOUTINGS:
			case CODE_2013TEAMSCOUTING_SEASON:
				return TeamScouting2013.DIRTYPE;
			case CODE_2013TEAMSCOUTING_ID:
			case CODE_2013TEAMSCOUTING_SEASONTEAM:
				return TeamScouting2013.ITEMTYPE;
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
			case CODE_TEAMS:
				id = db.insert(Team.TABLE, null, values);
				newUri = Team.uriFromId(id);
				break;
			case CODE_SEASONS:
				id = db.insert(Season.TABLE, null, values);
				newUri = Season.uriFromId(id);
				break;
			case CODE_COMPETITIONS:
				id = db.insert(Competition.TABLE, null, values);
				newUri = Competition.uriFromId(id);
				break;
			case CODE_COMPETITIONTEAMS:
				id = db.insert(CompetitionTeam.TABLE, null, values);
				newUri = CompetitionTeam.uriFromId(id);
				break;
			case CODE_TEAMSCOUTINGWHEELS:
				id = db.insert(TeamScoutingWheel.TABLE, null, values);
				newUri = TeamScoutingWheel.uriFromId(id);
				break;
			case CODE_2013TEAMSCOUTINGS:
				id = db.insert(TeamScouting2013.TABLE, null, values);
				newUri = TeamScouting2013.uriFromId(id);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
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
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		int rows = 0;
		Map<String,String> scouting = null;
		String where = null;
		switch (uriType) {
			case CODE_TEAMS:
				rows = deleteOrUpdate(Team.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_TEAM_ID:
				rows = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAM_NUMBER:
				rows = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, Team.NUMBER);
				break;
			case CODE_SEASONS:
				rows = deleteOrUpdate(Season.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_SEASON_ID:
				rows = deleteOrUpdate(uri, Season.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_SEASON_YEAR:
				rows = deleteOrUpdate(uri, Season.TABLE, values, selection, selectionArgs, Season.YEAR);
				break;
			case CODE_COMPETITIONS:
				rows = deleteOrUpdate(Competition.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_COMPETITION_ID:
				rows = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITION_SEASON:
				rows = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, Competition.SEASON);
				break;
			case CODE_COMPETITION_CODE:
				rows = deleteOrUpdate(uri, Competition.TABLE, values, selection, selectionArgs, Competition.CODE);
				break;
			case CODE_COMPETITIONTEAMS:
				rows = deleteOrUpdate(Team.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_COMPETITIONTEAMS_ID:
				rows = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_COMPETITIONTEAMS_TEAM:
				rows = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, CompetitionTeam.TEAM);
				break;
			case CODE_COMPETITIONTEAMS_COMP:
				rows = deleteOrUpdate(uri, Team.TABLE, values, selection, selectionArgs, CompetitionTeam.COMPETITION);
				break;
			case CODE_TEAMSCOUTINGWHEELS:
				rows = deleteOrUpdate(TeamScoutingWheel.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_TEAMSCOUTINGWHEEL_ID:
				rows = deleteOrUpdate(uri, TeamScoutingWheel.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_TEAMSCOUTINGWHEEL_SCOUTING:
				scouting = getSeasonTeam(uri);
				where = TeamScoutingWheel.SEASON+"="+scouting.get(Season.CLASS)+" and "+TeamScoutingWheel.TEAM+"="+scouting.get(Team.CLASS);
				rows = deleteOrUpdate(uri, TeamScoutingWheel.TABLE, values, selection, selectionArgs, where);
				break;
			case CODE_2013TEAMSCOUTINGS:
				rows = deleteOrUpdate(TeamScouting2013.TABLE, values, selection, selectionArgs, null);
				break;
			case CODE_2013TEAMSCOUTING_ID:
				rows = deleteOrUpdate(uri, TeamScouting2013.TABLE, values, selection, selectionArgs, BaseColumns._ID);
				break;
			case CODE_2013TEAMSCOUTING_SEASON:
				rows = deleteOrUpdate(uri, TeamScouting2013.TABLE, values, selection, selectionArgs, TeamScouting2013.SEASON);
				break;
			case CODE_2013TEAMSCOUTING_SEASONTEAM:
				scouting = getSeasonTeam(uri);
				where = TeamScouting2013.SEASON+"="+scouting.get(Season.CLASS)+" and "+TeamScouting2013.TEAM+"="+scouting.get(Team.CLASS);
				rows = deleteOrUpdate(uri, TeamScouting2013.TABLE, values, selection, selectionArgs, where);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rows;
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
		Log.d(TAG, table+" "+selection);
		if(values!=null) {
			return db.update(table, values, selection, selectionArgs);
		}
		return db.delete(table, selection, selectionArgs);
	}
}
