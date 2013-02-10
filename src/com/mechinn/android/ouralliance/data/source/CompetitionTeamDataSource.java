package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionTeamDataSource extends AOurAllianceDataSource<CompetitionTeam>  {
	private static final String TAG = "CompetitionTeamDataSource";
	public CompetitionTeamDataSource(Context context) {
		super(context);
	}

	@Override
	public CompetitionTeam insert(CompetitionTeam competitionTeam) throws OurAllianceException, SQLException {
		return insert(CompetitionTeam.URI, competitionTeam);
	}

	@Override
	public int update(CompetitionTeam data, String selection) throws OurAllianceException, SQLException {
		return update(CompetitionTeam.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(CompetitionTeam.URI, selection);
	}

	@Override
	public CursorLoader query(String selection, String order) {
		return query(CompetitionTeam.URI,CompetitionTeam.VIEWCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(CompetitionTeam.RANK+", "+Team.VIEW_NUMBER);
	}
	
	public CursorLoader getTeam(Team team) {
		return getTeam(team.getId());
	}
	
	public CursorLoader getTeam(long team) {
		return get(CompetitionTeam.TEAM, Long.toString(team));
	}
	
	public CursorLoader getAllTeams(Competition comp) {
		return getAllTeams(comp.getId());
	}
	
	public CursorLoader getAllTeams(long comp) {
		return get(CompetitionTeam.COMPETITION, Long.toString(comp), CompetitionTeam.RANK+", "+Team.VIEW_NUMBER);
	}
	
	public static CompetitionTeam getSingle(Cursor cursor) throws OurAllianceException {
		CompetitionTeam competitionTeam;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competitionTeam = CompetitionTeam.newFromCursor(cursor);
			Log.d(TAG, "get "+competitionTeam);
		} else if(cursor.getCount()<1) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		return competitionTeam;
	}
	
	public static List<CompetitionTeam> getList(Cursor cursor) throws OurAllianceException {
		List<CompetitionTeam> compTeams = new ArrayList<CompetitionTeam>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CompetitionTeam competitionTeam = CompetitionTeam.newFromCursor(cursor);
			Log.d(TAG, "get "+competitionTeam);
			compTeams.add(competitionTeam);
			cursor.moveToNext();
		}
		if(compTeams.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
		}
		return compTeams;
	}
	
	public static boolean contains(Cursor cursor, CompetitionTeam team) {
		cursor.moveToFirst();
		boolean exists = false;
		while (!cursor.isAfterLast()) {
			CompetitionTeam check = CompetitionTeam.newFromCursor(cursor);
			if(team.equals(check)) {
				exists = true;
			}
			cursor.moveToNext();
		}
		return exists;
	}
	
	public static boolean contains(Cursor cursor, Team team) {
		cursor.moveToFirst();
		boolean exists = false;
		while (!cursor.isAfterLast()) {
			CompetitionTeam check = CompetitionTeam.newFromCursor(cursor);
			if(team.equals(check.getTeam())) {
				exists = true;
			}
			cursor.moveToNext();
		}
		return exists;
	}
}
