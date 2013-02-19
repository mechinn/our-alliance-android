package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class TeamScoutingWheelDataSource extends AOurAllianceDataSource<TeamScoutingWheel> {
	private static final String TAG = "TeamScoutingWheelDataSource";

	public TeamScoutingWheelDataSource(Context context) {
		super(context);
	}

	@Override
	public TeamScoutingWheel insert(TeamScoutingWheel competitionTeam) throws OurAllianceException, SQLException {
		return insert(TeamScoutingWheel.URI, competitionTeam);
	}

	@Override
	public int update(TeamScoutingWheel data, String selection) throws OurAllianceException, SQLException {
		return update(TeamScoutingWheel.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(TeamScoutingWheel.URI, selection);
	}

	@Override
	public CursorLoader get(String selection, String order) {
		return get(TeamScoutingWheel.URI,TeamScoutingWheel.VIEWCOLUMNS, selection, order);
	}

	@Override
	public Cursor query(String selection, String order) {
		return query(TeamScoutingWheel.URI,TeamScoutingWheel.VIEWCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Team.VIEW_NUMBER);
	}

	@Override
	public Cursor queryAll() {
		return queryAll(Team.VIEW_NUMBER);
	}
	
	public static TeamScoutingWheel getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return TeamScoutingWheel.newFromCursor(cursor);
			} else if(cursor.getCount()==0) {
				throw new OurAllianceException(TAG,"Wheel not found in db.",new NoObjectsThrowable());
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
			}
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<TeamScoutingWheel> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<TeamScoutingWheel> scoutings = new ArrayList<TeamScoutingWheel>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				TeamScoutingWheel team = TeamScoutingWheel.newFromCursor(cursor);
				scoutings.add(team);
				cursor.moveToNext();
			}
			if(scoutings.isEmpty()) {
				throw new OurAllianceException(TAG,"No wheels in db.",new NoObjectsThrowable());
			}
			return scoutings;
		}
		throw new SQLException("Cursor is null");
	}
}
