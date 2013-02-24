package com.mechinn.android.ouralliance.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mechinn.android.ouralliance.Setup;
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
import android.os.Bundle;
import android.util.Log;

public class DataProvider extends ContentProvider {
	public static final String TAG = DataProvider.class.getName();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock read = lock.readLock();
	private final Lock write = lock.writeLock();
	
	public static final String AUTHORITY = "com.mechinn.android.ouralliance.data";
	public static final String BASE_URI_STRING = "content://" + AUTHORITY + "/";
	
	public static final String RESET = "reset";
	
	// database
	private Database database;
	private SQLiteDatabase db;

	private static final int CODE_RESET = 0;
	private static final int CODE_TEAM = 1;
	private static final int CODE_DISTINCT_TEAM = 2;
	private static final int CODE_SEASON = 3;
	private static final int CODE_DISTINCT_SEASON = 4;
	private static final int CODE_COMPETITION = 5;
	private static final int CODE_DISTINCT_COMPETITION = 6;
	private static final int CODE_COMPETITIONTEAM = 7;
	private static final int CODE_DISTINCT_COMPETITIONTEAM = 8;
	private static final int CODE_TEAMSCOUTINGWHEEL = 9;
	private static final int CODE_DISTINCT_TEAMSCOUTINGWHEEL = 10;
	private static final int CODE_2013TEAMSCOUTING = 11;
	private static final int CODE_DISTINCT_2013TEAMSCOUTING = 12;
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, Setup.TAG, CODE_RESET);
		sURIMatcher.addURI(AUTHORITY, Team.TABLE, CODE_TEAM);
		sURIMatcher.addURI(AUTHORITY, Team.DISTINCT, CODE_DISTINCT_TEAM);
		sURIMatcher.addURI(AUTHORITY, Season.TABLE, CODE_SEASON);
		sURIMatcher.addURI(AUTHORITY, Season.DISTINCT, CODE_DISTINCT_SEASON);
		sURIMatcher.addURI(AUTHORITY, Competition.TABLE, CODE_COMPETITION);
		sURIMatcher.addURI(AUTHORITY, Competition.DISTINCT, CODE_DISTINCT_COMPETITION);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.TABLE, CODE_COMPETITIONTEAM);
		sURIMatcher.addURI(AUTHORITY, CompetitionTeam.DISTINCT, CODE_DISTINCT_COMPETITIONTEAM);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.TABLE, CODE_TEAMSCOUTINGWHEEL);
		sURIMatcher.addURI(AUTHORITY, TeamScoutingWheel.DISTINCT, CODE_DISTINCT_TEAMSCOUTINGWHEEL);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.TABLE, CODE_2013TEAMSCOUTING);
		sURIMatcher.addURI(AUTHORITY, TeamScouting2013.DISTINCT, CODE_DISTINCT_2013TEAMSCOUTING);
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
	
//	@Override
//    public ParcelFileDescriptor openFile(Uri uri, String mode) {
//        
//        Log.d(TAG,"fetching: " + uri);
//
//        String path = getContext().getFilesDir().getAbsolutePath() + "/" + uri.getPath();
//        File file = new File(path);
//        ParcelFileDescriptor parcel = null;
//        try {
//            parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//        } catch (FileNotFoundException e) {
//            Log.e("LocalFileContentProvider", "uri " + uri.toString(), e);
//        }
//        return parcel;
//    }
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, uri.toString());
		String table = null;
		String[] cols = null;
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		boolean distinct = false;
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAM:
				cols = Team.ALLCOLUMNS;
				table = Team.TABLE;
				break;
			case CODE_DISTINCT_TEAM:
				distinct = true;
				cols = Team.ALLCOLUMNS;
				table = Team.TABLE;
				break;
			case CODE_SEASON:
				cols = Season.ALLCOLUMNS;
				table = Season.TABLE;
				break;
			case CODE_DISTINCT_SEASON:
				distinct = true;
				cols = Season.ALLCOLUMNS;
				table = Season.TABLE;
				break;
			case CODE_COMPETITION:
				cols = Competition.VIEWCOLUMNS;
				table = Competition.VIEW;
				break;
			case CODE_DISTINCT_COMPETITION:
				distinct = true;
				cols = Competition.ALLCOLUMNS;
				table = Competition.TABLE;
				break;
			case CODE_COMPETITIONTEAM:
				cols = CompetitionTeam.VIEWCOLUMNS;
				table = CompetitionTeam.VIEW;
				break;
			case CODE_DISTINCT_COMPETITIONTEAM:
				distinct = true;
				cols = CompetitionTeam.ALLCOLUMNS;
				table = CompetitionTeam.TABLE;
				break;
			case CODE_TEAMSCOUTINGWHEEL:
				cols = TeamScoutingWheel.VIEWCOLUMNS;
				table = TeamScoutingWheel.VIEW;
				break;
			case CODE_DISTINCT_TEAMSCOUTINGWHEEL:
				distinct = true;
				cols = TeamScoutingWheel.ALLCOLUMNS;
				table = TeamScoutingWheel.TABLE;
				break;
			case CODE_2013TEAMSCOUTING:
				cols = TeamScouting2013.VIEWCOLUMNS;
				table = TeamScouting2013.VIEW;
				break;
			case CODE_DISTINCT_2013TEAMSCOUTING:
				distinct = true;
				cols = TeamScouting2013.ALLCOLUMNS;
				table = TeamScouting2013.TABLE;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		db = database.getWritableDatabase();
		if(null!=table) {
			Log.d(TAG, table+" distinct: "+distinct);
			if(null!=projection) {
				for(String each : projection) {
					Log.d(TAG, table+" projection: "+each);
				}
			} else {
				Log.d(TAG, table+" no projection");
			}
			if(null!=selection) {
				Log.d(TAG, table+" selection: "+selection);
			} else {
				Log.d(TAG, table+" no selection");
			}
			if(null!=selectionArgs) {
				for(String each : selectionArgs) {
					Log.d(TAG, table+" selectionArgs: "+each);
				}
			} else {
				Log.d(TAG, table+" no selection args");
			}
			if(null!=sortOrder) {
				Log.d(TAG, table+" sortOrder: "+sortOrder);
			} else {
				Log.d(TAG, table+" no sortOrder");
			}
		} else {
			Log.d(TAG, table+" no table");
		}
		checkColumns(projection, cols);
		queryBuilder.setDistinct(distinct);
		queryBuilder.setTables(table);
		Cursor cursor = null;
		read.lock();
		try {
			cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		} finally {
			read.unlock();
		}
		Log.d(TAG, "query: "+uri.toString());
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		Log.d(TAG, uri.toString());
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case CODE_TEAM:
			case CODE_DISTINCT_TEAM:
				return Team.URITYPE;
			case CODE_SEASON:
			case CODE_DISTINCT_SEASON:
				return Season.URITYPE;
			case CODE_COMPETITION:
			case CODE_DISTINCT_COMPETITION:
				return Competition.URITYPE;
			case CODE_COMPETITIONTEAM:
			case CODE_DISTINCT_COMPETITIONTEAM:
				return CompetitionTeam.URITYPE;
			case CODE_TEAMSCOUTINGWHEEL:
			case CODE_DISTINCT_TEAMSCOUTINGWHEEL:
				return TeamScoutingWheel.URITYPE;
			case CODE_2013TEAMSCOUTING:
			case CODE_DISTINCT_2013TEAMSCOUTING:
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
		write.lock();
		try {
			switch (uriType) {
				case CODE_TEAM:
				case CODE_DISTINCT_TEAM:
					id = db.insert(Team.TABLE, null, values);
					newUri = Uri.parse(Team.URI+"/"+id);
					break;
				case CODE_SEASON:
				case CODE_DISTINCT_SEASON:
					id = db.insert(Season.TABLE, null, values);
					newUri = Uri.parse(Season.URI+"/"+id);
					break;
				case CODE_COMPETITION:
				case CODE_DISTINCT_COMPETITION:
					id = db.insert(Competition.TABLE, null, values);
					newUri = Uri.parse(Competition.URI+"/"+id);
					break;
				case CODE_COMPETITIONTEAM:
				case CODE_DISTINCT_COMPETITIONTEAM:
					id = db.insert(CompetitionTeam.TABLE, null, values);
					newUri = Uri.parse(CompetitionTeam.URI+"/"+id);
					break;
				case CODE_TEAMSCOUTINGWHEEL:
				case CODE_DISTINCT_TEAMSCOUTINGWHEEL:
					id = db.insert(TeamScoutingWheel.TABLE, null, values);
					newUri = Uri.parse(TeamScoutingWheel.URI+"/"+id);
					break;
				case CODE_2013TEAMSCOUTING:
				case CODE_DISTINCT_2013TEAMSCOUTING:
					id = db.insert(TeamScouting2013.TABLE, null, values);
					newUri = Uri.parse(TeamScouting2013.URI+"/"+id);
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		} finally {
			write.unlock();
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
			case CODE_DISTINCT_TEAM:
				table = Team.TABLE;
				break;
			case CODE_SEASON:
			case CODE_DISTINCT_SEASON:
				table = Season.TABLE;
				break;
			case CODE_COMPETITION:
			case CODE_DISTINCT_COMPETITION:
				table = Competition.TABLE;
				break;
			case CODE_COMPETITIONTEAM:
			case CODE_DISTINCT_COMPETITIONTEAM:
				table = CompetitionTeam.TABLE;
				break;
			case CODE_TEAMSCOUTINGWHEEL:
			case CODE_DISTINCT_TEAMSCOUTINGWHEEL:
				table = TeamScoutingWheel.TABLE;
				break;
			case CODE_2013TEAMSCOUTING:
			case CODE_DISTINCT_2013TEAMSCOUTING:
				table = TeamScouting2013.TABLE;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		db = database.getWritableDatabase();
		if(values!=null) {
			write.lock();
			try {
				rows = db.update(table, values, selection, selectionArgs);
	    	} finally {
	    		write.unlock();
	    	}
			switch (uriType) {
				case CODE_TEAM:
				case CODE_DISTINCT_TEAM:
					getContext().getContentResolver().notifyChange(Team.URI, null);
					getContext().getContentResolver().notifyChange(Team.DISTINCTURI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					break;
				case CODE_SEASON:
				case CODE_DISTINCT_SEASON:
					getContext().getContentResolver().notifyChange(Season.URI, null);
					getContext().getContentResolver().notifyChange(Season.DISTINCTURI, null);
					getContext().getContentResolver().notifyChange(Competition.URI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					break;
				case CODE_COMPETITION:
				case CODE_DISTINCT_COMPETITION:
					getContext().getContentResolver().notifyChange(Competition.URI, null);
					getContext().getContentResolver().notifyChange(Competition.DISTINCTURI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					break;
				case CODE_COMPETITIONTEAM:
				case CODE_DISTINCT_COMPETITIONTEAM:
					getContext().getContentResolver().notifyChange(CompetitionTeam.URI, null);
					getContext().getContentResolver().notifyChange(CompetitionTeam.DISTINCTURI, null);
					break;
				case CODE_TEAMSCOUTINGWHEEL:
				case CODE_DISTINCT_TEAMSCOUTINGWHEEL:
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.URI, null);
					getContext().getContentResolver().notifyChange(TeamScoutingWheel.DISTINCTURI, null);
					break;
				case CODE_2013TEAMSCOUTING:
				case CODE_DISTINCT_2013TEAMSCOUTING:
					getContext().getContentResolver().notifyChange(TeamScouting2013.URI, null);
					getContext().getContentResolver().notifyChange(TeamScouting2013.DISTINCTURI, null);
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		} else {
			write.lock();
			try {
				rows = db.delete(table, selection, selectionArgs);
	    	} finally {
	    		write.unlock();
	    	}
			getContext().getContentResolver().notifyChange(uri, null);
		}
		Log.d(TAG, "delete/update: "+uri.toString());
		return rows;
	}
	
	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		//run custom methods declared here
        if(method.equals(RESET)) {
        	write.lock();
        	try {
        		database.reset(db);
        	} finally {
        		write.unlock();
        	}
        }
        return null;
    }
}
