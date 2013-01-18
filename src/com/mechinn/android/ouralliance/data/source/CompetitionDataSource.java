package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionDataSource {
	private static final String TAG = "CompetitionDataSource";
	// Database fields
	private SQLiteDatabase database;
	private Database db;
	private SeasonDataSource seasonData;

	public CompetitionDataSource(Context context) {
		db = new Database(context);
		seasonData = new SeasonDataSource(context);
	}
	
	public void open() throws SQLException {
		database = db.getWritableDatabase();
		seasonData.open();
	}
	
	public void close() {
		seasonData.close();
		db.close();
	}

	public Competition editCompetition(Competition competition) throws IllegalArgumentException, OurAllianceException {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Competition.SEASON, competition.getSeason().getId());
		values.put(Competition.NAME, competition.getName());
		values.put(Competition.CODE, competition.getCode());
		long id;
		if(competition.getId()==0) {
			id = database.insert(Competition.TABLE, null, values);
		} else {
			id = database.update(Competition.TABLE, values, BaseColumns._ID + " = " + competition.getId(), null);
		}
		return getCompetition(id);
	}

	public void deleteCompetition(Competition competition) {
		long id = competition.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(Competition.TABLE, BaseColumns._ID + " = " + id, null);
	}
	
	public Competition getCompetition(long id) throws IllegalArgumentException, OurAllianceException {
		Competition competition;
		Cursor cursor = database.query(Competition.TABLE, Competition.ALLCOLUMNS, BaseColumns._ID + " = " + id, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competition = cursorToCompetition(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Competition not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return competition;
	}

	public List<Competition> getAllCompetitions() throws IllegalArgumentException, OurAllianceException {
		List<Competition> comps = new ArrayList<Competition>();
	
		Cursor cursor = database.query(Competition.TABLE, Competition.ALLCOLUMNS, null, null, null, null, null);
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Competition competition = cursorToCompetition(cursor);
			comps.add(competition);
			cursor.moveToNext();
		}
		if(comps.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitions in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return comps;
	}

	private Competition cursorToCompetition(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		Competition competition = new Competition();
		competition.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		competition.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			competition.setSeason(seasonData.getSeason(cursor.getLong(cursor.getColumnIndexOrThrow(Competition.SEASON))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup season referenced in this team scouting");
		}
		competition.setName(cursor.getString(cursor.getColumnIndexOrThrow(Competition.NAME)));
		competition.setCode(cursor.getString(cursor.getColumnIndexOrThrow(Competition.CODE)));
		return competition;
	}
}
