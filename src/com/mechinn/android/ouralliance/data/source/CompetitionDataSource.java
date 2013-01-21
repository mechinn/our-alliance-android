package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

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

	public Competition insert(Competition competition) throws IllegalArgumentException, OurAllianceException {
		this.open();
		long id = database.insert(Competition.TABLE, null, competition.toCV());
		this.close();
		if(id!=-1) {
			Log.d(TAG, "insert "+competition);
		} else {
			Log.d(TAG, "did not insert "+competition);
		}
		return get(id);
	}
	
	public int update(Competition competition) throws IllegalArgumentException, OurAllianceException {
		this.open();
		int count = database.update(Competition.TABLE, competition.toCV(), BaseColumns._ID + " = " + competition.getId(), null);
		this.close();
		Log.d(TAG, "updated "+count+" competitions from "+competition);
		return count;
	}

	public int delete(Competition competition) {
		this.open();
		int count = database.delete(Competition.TABLE, BaseColumns._ID + " = " + competition.getId(), null);
		this.close();
		Log.d(TAG, "deleted "+count+" from "+competition);
		return count;
	}
	
	public Competition get(long id) throws IllegalArgumentException, OurAllianceException {
		return getWhere(BaseColumns._ID + " = " + id);
	}
	
	public Competition get(String code) throws IllegalArgumentException, OurAllianceException {
		return getWhere(Competition.CODE + " = '" + code + "'");
	}
	
	private Competition getWhere(String where) throws IllegalArgumentException, OurAllianceException {
		Competition competition;
		this.open();
		Cursor cursor = database.query(Competition.TABLE, Competition.ALLCOLUMNS, where, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competition = cursorToCompetition(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Competition not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		this.close();
		return competition;
	}
	
	public List<Competition> getAll() throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(null);
	}
	
	public List<Competition> getAll(Competition comp) throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(Competition.SEASON + " = " + comp.getId());
	}

	private List<Competition> getAllWhere(String where) throws IllegalArgumentException, OurAllianceException {
		List<Competition> comps = new ArrayList<Competition>();
		this.open();
		Cursor cursor = database.query(Competition.TABLE, Competition.ALLCOLUMNS, where, null, null, null, null);
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
		this.close();
		return comps;
	}

	private Competition cursorToCompetition(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		Competition competition = new Competition();
		competition.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		competition.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			competition.setSeason(seasonData.get(cursor.getLong(cursor.getColumnIndexOrThrow(Competition.SEASON))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup season referenced in this team scouting");
		}
		competition.setName(cursor.getString(cursor.getColumnIndexOrThrow(Competition.NAME)));
		competition.setCode(cursor.getString(cursor.getColumnIndexOrThrow(Competition.CODE)));
		return competition;
	}
}
