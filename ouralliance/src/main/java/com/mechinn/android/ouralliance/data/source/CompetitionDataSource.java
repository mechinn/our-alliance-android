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
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionDataSource extends AOurAllianceDataSource<Competition> {
	public static final String TAG = CompetitionDataSource.class.getSimpleName();
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
	public CursorLoader get(String selection, String order) {
		return get(Competition.URI,Competition.VIEWCOLUMNS, selection, order);
	}

	@Override
	public Cursor query(String selection, String order) {
		return query(Competition.URI, Competition.VIEWCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getDistinct(String[] projection, String selection, String order) {
		return get(Competition.DISTINCTURI, projection, selection, order);
	}

	@Override
	public Cursor queryDistinct(String[] projection, String selection, String order) {
		return query(Competition.DISTINCTURI, projection, selection, order);
	}
	
	@Override
	public CursorLoader getAll() {
		return getAll(Competition.NAME);
	}
	
	@Override
	public Cursor queryAll() {
		return queryAll(Competition.NAME);
	}
	
	public CursorLoader get(String code) {
		return get(Competition.CODE, code, null);
	}
	
	public Cursor query(String code) {
		return query(Competition.CODE, code, null);
	}
	
	public CursorLoader getAllCompetitions(Season season) {
		return getAllCompetitions(season.getId());
	}
	
	public Cursor queryAllCompetitions(Season season) {
		return queryAllCompetitions(season.getId());
	}
	
	public CursorLoader getAllCompetitions(long season) {
		return get(Competition.SEASON, season, Competition.NAME);
	}
	
	public Cursor queryAllCompetitions(long season) {
		return query(Competition.SEASON, season, Competition.NAME);
	}
	
	public static Competition getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return Competition.newFromCursor(cursor);
			} else if(cursor.getCount()==0) {
				throw new OurAllianceException(TAG,"Competition not found in db.");
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.");
			}
		}
		throw new SQLException("Cursor is null");
	}

	public static List<Competition> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<Competition> comps = new ArrayList<Competition>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Competition competition = Competition.newFromCursor(cursor);
				Log.d(TAG, "get "+competition);
				comps.add(competition);
				cursor.moveToNext();
			}
			if(comps.isEmpty()) {
				throw new OurAllianceException(TAG,"No competitions in db.");
			}
			return comps;
		}
		throw new SQLException("Cursor is null");
	}
}
