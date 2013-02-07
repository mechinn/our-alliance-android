package com.mechinn.android.ouralliance.data.source.frc2013;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.IOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.Database;

public class TeamScouting2013DataSource extends TeamScoutingDataSource<TeamScouting2013> {
	public TeamScouting2013DataSource(Context context) {
		super(context);
	}

	private static final String TAG = "TeamScouting2013DataSource";

	@Override
	public Uri getUri() {
		return TeamScouting2013.URI;
	}

	@Override
	public Uri getUriFromId(TeamScouting2013 teamScouting) {
		return getUriFromId(teamScouting.getId());
	}

	@Override
	public Uri getUriFromId(long teamScouting) {
		return TeamScouting2013.uriFromId(teamScouting);
	}

	@Override
	public Uri getUriFromSeasonTeam(long season, long team) {
		return TeamScouting2013.uriFromSeasonTeam(season, team);
	}

	@Override
	public String[] getViewCols() {
		return TeamScouting2013.VIEWCOLUMNS;
	}
	
	public static TeamScouting2013 getSingle(Cursor cursor) throws OurAllianceException {
		TeamScouting2013 scouting;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			scouting = fromCursor(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Team scouting not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return scouting;
	}
	
	public static List<TeamScouting2013> getList(Cursor cursor) throws OurAllianceException {
		List<TeamScouting2013> scoutings = new ArrayList<TeamScouting2013>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TeamScouting2013 team = fromCursor(cursor);
			scoutings.add(team);
			cursor.moveToNext();
		}
		if(scoutings.isEmpty()) {
			throw new OurAllianceException(TAG,"No team scouting in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return scoutings;
	}
	
	public static List<Team> getAllTeams(Cursor cursor) throws OurAllianceException {
		List<Team> teams = new ArrayList<Team>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TeamScouting2013 competitionTeam = fromCursor(cursor);
			Log.d(TAG, "get "+competitionTeam);
			teams.add(competitionTeam.getTeam());
			cursor.moveToNext();
		}
		if(teams.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return teams;
	}

	public static TeamScouting2013 fromCursor(Cursor cursor) {
		TeamScouting2013 team = new TeamScouting2013();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		
		long seasonId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting2013.SEASON));
		Date seasonMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Season.VIEW_MODIFIED)));
		int seasonYear = cursor.getInt(cursor.getColumnIndexOrThrow(Season.VIEW_YEAR));
		String seasonTitle = cursor.getString(cursor.getColumnIndexOrThrow(Season.VIEW_TITLE));
		team.setSeason(new Season(seasonId, seasonMod, seasonYear, seasonTitle));
		
		long teamId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting2013.TEAM));
		Date teamMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Team.VIEW_MODIFIED)));
		int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Team.VIEW_NUMBER));
		String teamName = cursor.getString(cursor.getColumnIndexOrThrow(Team.VIEW_NAME));
		team.setTeam(new Team(teamId, teamMod, teamNumber, teamName));
		
		team.setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting2013.ORIENTATION)));
		team.setDriveTrain(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting2013.DRIVETRAIN)));
		team.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting2013.WIDTH)));
		team.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting2013.LENGTH)));
		team.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting2013.HEIGHT)));
		team.setAutonomous(cursor.getFloat(cursor.getColumnIndexOrThrow(TeamScouting2013.AUTONOMOUS)));
		team.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting2013.NOTES)));
		return team;
	}
}
