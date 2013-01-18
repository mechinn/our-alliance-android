package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

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

	public Season editSeason(Season season) throws IllegalArgumentException, OurAllianceException {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Season.YEAR, season.getYear());
		values.put(Season.COMPETITION, season.getCompetition());
		long id;
		if(season.getId()==0) {
			id = database.insert(Season.TABLE, null, values);
		} else {
			id = database.update(Season.TABLE, values, BaseColumns._ID + " = " + season.getId(), null);
		}
		return getSeason(id);
	}

	public void deleteSeason(Season season) {
		long id = season.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(Season.TABLE, BaseColumns._ID + " = " + id, null);
	}
	public Season getSeason(long id) throws IllegalArgumentException, OurAllianceException {
		Season season;
		Cursor cursor = database.query(Season.TABLE, Season.ALLCOLUMNS, BaseColumns._ID + " = " + id, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			season = cursorToSeason(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Season not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return season;
	}
	public Season getSeason(int year) throws IllegalArgumentException, OurAllianceException {
		Season season;
		Cursor cursor = database.query(Season.TABLE, Season.ALLCOLUMNS, Season.YEAR + " = " + year, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			season = cursorToSeason(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Season not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return season;
	}

	public List<Season> getAllSeasons() throws IllegalArgumentException, OurAllianceException {
		List<Season> comments = new ArrayList<Season>();
	
		Cursor cursor = database.query(Season.TABLE, Season.ALLCOLUMNS, null, null, null, null, null);
	
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
		return comments;
	}
	
	public CharSequence[] getAllSeasonsViews() throws IllegalArgumentException, OurAllianceException {
		List<Season> list = this.getAllSeasons();
		CharSequence[] views = new CharSequence[list.size()];
		for(int i=0;i<list.size();++i) {
			views[i] = list.get(i).toString();
		}
		return views;
	}
	
	public CharSequence[] getAllSeasonsYears() throws IllegalArgumentException, OurAllianceException {
		List<Season> list = this.getAllSeasons();
		CharSequence[] years = new CharSequence[list.size()];
		for(int i=0;i<list.size();++i) {
			years[i] = Integer.toString(list.get(i).getYear());
		}
		return years;
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
