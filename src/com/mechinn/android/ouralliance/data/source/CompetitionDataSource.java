package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionDataSource extends AOurAllianceDataSource<Competition> {
	private static final String TAG = "CompetitionDataSource";
	public CompetitionDataSource(Context context) {
		super(context);
	}

	@Override
	public Competition insert(Competition competition) throws OurAllianceException, SQLException {
		return insert(Competition.URI, competition);
	}

	@Override
	public int update(Competition data, String selection) throws OurAllianceException, SQLException {
		return update(Competition.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(Competition.URI, selection);
	}

	@Override
	public CursorLoader query(String selection, String order) {
		return query(Competition.URI,Competition.VIEWCOLUMNS, selection, order);
	}
	
	@Override
	public CursorLoader getAll() {
		return getAll(Competition.NAME);
	}
	
	public CursorLoader get(String code) {
		return get(Competition.CODE, code);
	}
	
	public CursorLoader getAllCompetitions(Season season) {
		return getAllCompetitions(season.getId());
	}
	
	public CursorLoader getAllCompetitions(long season) {
		return get(Competition.SEASON, Long.toString(season), Competition.NAME);
	}
	
	public static Competition getSingle(Cursor cursor) throws OurAllianceException {
		Competition competition;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competition = Competition.newFromCursor(cursor);
			Log.d(TAG, "get "+competition);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Competition not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		return competition;
	}

	public static List<Competition> getList(Cursor cursor) throws OurAllianceException {
		List<Competition> comps = new ArrayList<Competition>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Competition competition = Competition.newFromCursor(cursor);
			Log.d(TAG, "get "+competition);
			comps.add(competition);
			cursor.moveToNext();
		}
		if(comps.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitions in db.",new NoObjectsThrowable());
		}
		return comps;
	}
}
