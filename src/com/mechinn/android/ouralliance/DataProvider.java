package com.mechinn.android.ouralliance;

import java.util.Arrays;
import java.util.HashSet;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class DataProvider extends ContentProvider {
	private static final String tag = "DataProvider";
	
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
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, Team.PATH, CODE_TEAMS);
		sURIMatcher.addURI(AUTHORITY, Team.IDPATH + "#", CODE_TEAM_ID);
		sURIMatcher.addURI(AUTHORITY, Team.NUMPATH + "#", CODE_TEAM_NUMBER);
		sURIMatcher.addURI(AUTHORITY, Season.PATH, CODE_SEASONS);
		sURIMatcher.addURI(AUTHORITY, Season.IDPATH + "#", CODE_SEASON_ID);
		sURIMatcher.addURI(AUTHORITY, Season.YEARPATH + "#", CODE_SEASON_YEAR);
	}
	
	@Override
	public boolean onCreate() {
		database = new Database(this.getContext());
		db = database.getWritableDatabase();
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	
		// Uisng SQLiteQueryBuilder instead of query() method
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
			default:
				break;
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		db = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
			case CODE_TEAMS:
				id = db.insert(Team.TABLE, null, values);
				if(id==-1) {
					throw new SQLiteException("Did not insert row");
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return Uri.parse(Team.IDPATH + id);
			case CODE_SEASONS:
				id = db.insert(Season.TABLE, null, values);
				if(id==-1) {
					throw new SQLiteException("Did not insert row");
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return Uri.parse(Season.IDPATH + id);
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
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Team.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
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
						BaseColumns._ID + "=" + where, 
						null);
				} else {
					rowsDeleted = db.delete(
						Season.TABLE,
						BaseColumns._ID + "=" + where + " and " + selection,
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
