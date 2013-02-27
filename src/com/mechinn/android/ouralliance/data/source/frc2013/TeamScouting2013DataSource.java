package com.mechinn.android.ouralliance.data.source.frc2013;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class TeamScouting2013DataSource extends AOurAllianceDataSource<TeamScouting2013> {
	public static final String TAG = TeamScouting2013DataSource.class.getSimpleName();
	public TeamScouting2013DataSource(Context context) {
		super(context);
	}

	@Override
	public TeamScouting2013 insert(TeamScouting2013 competitionTeam) throws OurAllianceException, SQLException {
		return insert(TeamScouting2013.URI, competitionTeam);
	}

	@Override
	public int update(TeamScouting2013 data, String selection) throws OurAllianceException, SQLException {
		return update(TeamScouting2013.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(TeamScouting2013.URI, selection);
	}

	@Override
	public CursorLoader get(String selection, String order) {
		return get(TeamScouting2013.URI,TeamScouting2013.VIEWCOLUMNS, selection, order);
	}

	@Override
	public Cursor query(String selection, String order) {
		return query(TeamScouting2013.URI,TeamScouting2013.VIEWCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getDistinct(String[] projection, String selection, String order) {
		return get(TeamScouting2013.DISTINCTURI, projection, selection, order);
	}

	@Override
	public Cursor queryDistinct(String[] projection, String selection, String order) {
		return query(TeamScouting2013.DISTINCTURI, projection, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Team.VIEW_NUMBER);
	}

	@Override
	public Cursor queryAll() {
		return queryAll(Team.VIEW_NUMBER);
	}

	public CursorLoader getAllTeams(String teams) {
		return this.get(Team.VIEW_ID+" IN ("+teams+")", Team.VIEW_NUMBER);
	}

	public Cursor queryAllTeams(String teams) {
		return this.query(Team.VIEW_ID+" IN ("+teams+")", Team.VIEW_NUMBER);
	}
	
	public static TeamScouting2013 getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return TeamScouting2013.newFromCursor(cursor);
			} else if(cursor.getCount()==0) {
				throw new OurAllianceException(TAG,"Team scouting not found in db.");
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.");
			}
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<TeamScouting2013> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<TeamScouting2013> scoutings = new ArrayList<TeamScouting2013>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				TeamScouting2013 scouting = TeamScouting2013.newFromCursor(cursor);
				scoutings.add(scouting);
				cursor.moveToNext();
			}
			if(scoutings.isEmpty()) {
				throw new OurAllianceException(TAG,"No team scouting in db.");
			}
			return scoutings;
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<Team> getAllTeams(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<Team> teams = new ArrayList<Team>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				TeamScouting2013 scouting = TeamScouting2013.newFromCursor(cursor);
				Log.d(TAG, "get "+scouting);
				teams.add(scouting.getTeam());
				cursor.moveToNext();
			}
			if(teams.isEmpty()) {
				throw new OurAllianceException(TAG,"No competitionTeams in db.");
			}
			return teams;
		}
		throw new SQLException("Cursor is null");
	}
}
