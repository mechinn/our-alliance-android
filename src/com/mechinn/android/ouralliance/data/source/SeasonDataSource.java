package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class SeasonDataSource {
	private static final String TAG = "SeasonDataSource";
	// Database fields
	private SQLiteDatabase database;
	private Database db;

	public SeasonDataSource(Context context) {
		db = new Database(context);
	}
	
	public void open() throws SQLException {
		database = db.getWritableDatabase();
	}
	
	public void close() {
		db.close();
	}

	public Season insert(Season season) throws IllegalArgumentException, OurAllianceException {
		this.open();
		long id = database.insert(Season.TABLE, null, season.toCV());
		this.close();
		if(id!=-1) {
			Log.d(TAG, "insert "+season);
		} else {
			Log.d(TAG, "did not insert "+season);
		}
		return get(id);
	}
	
	public int update(Season season) {
		this.open();
		int count = database.update(Season.TABLE, season.toCV(), BaseColumns._ID + " = " + season.getId(), null);
		this.close();
		Log.d(TAG, "update "+count+" from "+season);
		return count;
	}

	public int delete(Season season) {
		this.open();
		int count = database.delete(Season.TABLE, BaseColumns._ID + " = " + season.getId(), null);
		this.close();
		Log.d(TAG, "delete "+count+" from "+season);
		return count;
	}
	public Season get(long id) throws IllegalArgumentException, OurAllianceException {
		return getWhere(BaseColumns._ID + " = " + id);
	}
	
	public Season get(int year) throws IllegalArgumentException, OurAllianceException {
		return getWhere(Season.YEAR + " = " + year);
	}
	
	private Season getWhere(String where) throws IllegalArgumentException, OurAllianceException {
		Season season;
		this.open();
		Cursor cursor = database.query(Season.TABLE, Season.ALLCOLUMNS, where, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			season = cursorToSeason(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Season not found in db where "+where,new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result where "+where+" please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		this.close();
		return season;
	}

	public List<Season> getAll() throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(null);
	}
	
	private List<Season> getAllWhere(String where) throws IllegalArgumentException, OurAllianceException {
		List<Season> comments = new ArrayList<Season>();
		this.open();
		Cursor cursor = database.query(Season.TABLE, Season.ALLCOLUMNS, where, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Season season = cursorToSeason(cursor);
			comments.add(season);
			cursor.moveToNext();
		}
		if(comments.isEmpty()) {
			throw new OurAllianceException(TAG,"No seasons in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return comments;
	}

	private Season cursorToSeason(Cursor cursor) throws IllegalArgumentException {
		Season season = new Season();
		season.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		season.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		season.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(Season.YEAR)));
		season.setCompetition(cursor.getString(cursor.getColumnIndexOrThrow(Season.COMPETITION)));
		return season;
	}
}
