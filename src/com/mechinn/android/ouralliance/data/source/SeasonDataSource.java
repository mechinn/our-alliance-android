package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public CursorLoader query(String selection, String order) {
		return query(Season.URI,Season.ALLCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Season.YEAR+" DESC");
	}
	
	public CursorLoader get(int year) {
		return get(Season.YEAR,Integer.toString(year));
	}
	
	public static Season getSingle(Cursor cursor) throws OurAllianceException {
		Season season;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			season = Season.newFromCursor(cursor);
			Log.d(TAG, "get "+season);
		} else if(cursor.getCount()<1) {
			throw new OurAllianceException(TAG,"Season not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return season;
	}
	
	public static List<Season> getList(Cursor cursor) throws OurAllianceException {
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
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}
}
