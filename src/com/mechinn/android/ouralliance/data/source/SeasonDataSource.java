package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

public class SeasonDataSource extends AOurAllianceDataSource<Season> {
	private static final String TAG = "SeasonDataSource";

	public SeasonDataSource(Context context) {
		super(context);
	}

	@Override
	public Season insert(Season season) throws OurAllianceException, SQLException {
		return insert(Season.URI, season);
	}

	@Override
	public int update(Season data, String selection) throws OurAllianceException, SQLException {
		return update(Season.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(Season.URI, selection);
	}

	@Override
	public CursorLoader get(String selection, String order) {
		return get(Season.URI,Season.ALLCOLUMNS, selection, order);
	}

	@Override
	public Cursor query(String selection, String order) {
		return query(Season.URI,Season.ALLCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getDistinct(String[] projection, String selection, String order) {
		return get(Season.DISTINCTURI, projection, selection, order);
	}

	@Override
	public Cursor queryDistinct(String[] projection, String selection, String order) {
		return query(Season.DISTINCTURI, projection, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Season.YEAR+" DESC");
	}

	@Override
	public Cursor queryAll() {
		return queryAll(Season.YEAR+" DESC");
	}
	
	public CursorLoader get(int year) {
		return get(Season.YEAR,year);
	}
	
	public Cursor query(int year) {
		return query(Season.YEAR,year);
	}
	
	public static Season getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return Season.newFromCursor(cursor);
			} else if(cursor.getCount()<1) {
				throw new OurAllianceException(TAG,"Season not found in db.",new NoObjectsThrowable());
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
			}
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<Season> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<Season> comments = new ArrayList<Season>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Season season = Season.newFromCursor(cursor);
				Log.d(TAG, "get "+season);
				comments.add(season);
				cursor.moveToNext();
			}
			if(comments.isEmpty()) {
				throw new OurAllianceException(TAG,"No seasons in db.",new NoObjectsThrowable());
			}
			return comments;
		}
		throw new SQLException("Cursor is null");
	}
}
