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
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.Database;

public class TeamScouting2013DataSource implements IOurAllianceDataSource<TeamScouting2013> {
	private static final String TAG = "TeamScouting2013DataSource";
	private Context context;
	private ContentResolver data;

	public TeamScouting2013DataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(TeamScouting2013 teamScouting) {
		return data.insert(TeamScouting2013.URI, teamScouting.toCV());
	}
	
	public int update(TeamScouting2013 teamScouting) throws OurAllianceException {
		int count = data.update(TeamScouting2013.uriFromId(teamScouting), teamScouting.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+teamScouting);
		}
		return count;
	}

	public int delete(TeamScouting2013 teamScouting) throws OurAllianceException {
		int count = data.delete(TeamScouting2013.uriFromId(teamScouting), null, null);
		Log.d(TAG, "delete "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+teamScouting);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, TeamScouting2013.VIEWCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(TeamScouting2013 teamScouting) {
		return get(teamScouting.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, TeamScouting2013.uriFromId(id), TeamScouting2013.VIEWCOLUMNS, null, null, null);
	}

	public CursorLoader get(Season season, Team team) {
		Log.d(TAG, season+" "+team);
		Log.d(TAG, season.getId()+" "+team.getId());
		return get(season.getId(), team.getId());
	}
	
	public CursorLoader get(long season, long team) {
		Log.d(TAG, season+" "+team);
		return new CursorLoader(context, TeamScouting2013.uriFromSeasonTeam(season, team), TeamScouting2013.VIEWCOLUMNS, null, null, null);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, TeamScouting2013.URI, TeamScouting2013.VIEWCOLUMNS, null, null, Team.VIEW_NUMBER);
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
		team.setAutonomous(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting2013.AUTONOMOUS)));
		team.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting2013.NOTES)));
		return team;
	}
}
