package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class TeamScoutingDataSource {
	private static final String TAG = "TeamScoutingDataSource";
	// Database fields
	private Context context;
	private ContentResolver data;

	public TeamScoutingDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(TeamScouting teamScouting) {
		return data.insert(TeamScouting.URI, teamScouting.toCV());
	}
	
	public int update(TeamScouting teamScouting) throws OurAllianceException {
		int count = data.update(TeamScouting.uriFromId(teamScouting), teamScouting.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+teamScouting);
		}
		return count;
	}

	public int delete(TeamScouting teamScouting) throws OurAllianceException {
		int count = data.delete(TeamScouting.uriFromId(teamScouting), null, null);
		Log.d(TAG, "delete "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+teamScouting);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, TeamScouting.VIEWCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(TeamScouting teamScouting) {
		return get(teamScouting.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, TeamScouting.uriFromId(id), TeamScouting.VIEWCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(Season season, Team team) {
		return new CursorLoader(context, TeamScouting.uriFromSeasonTeam(season, team), TeamScouting.VIEWCOLUMNS, null, null, null);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, TeamScouting.URI, TeamScouting.VIEWCOLUMNS, null, null, Team.VIEW_NUMBER);
	}
	
	public static TeamScouting getTeamScouting(Cursor cursor) throws OurAllianceException {
		TeamScouting scouting;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			scouting = cursorToTeamScouting(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Team scouting not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return scouting;
	}
	
	public static List<TeamScouting> getTeamScoutings(Cursor cursor) throws OurAllianceException {
		List<TeamScouting> scoutings = new ArrayList<TeamScouting>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TeamScouting team = cursorToTeamScouting(cursor);
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
			TeamScouting competitionTeam = cursorToTeamScouting(cursor);
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

	public static TeamScouting cursorToTeamScouting(Cursor cursor) {
		TeamScouting team = new TeamScouting();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_MODIFIED))));
		
		long seasonId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_SEASON));
		Date seasonMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Season.VIEW_MODIFIED)));
		int seasonYear = cursor.getInt(cursor.getColumnIndexOrThrow(Season.VIEW_YEAR));
		String seasonTitle = cursor.getString(cursor.getColumnIndexOrThrow(Season.VIEW_TITLE));
		team.setSeason(new Season(seasonId, seasonMod, seasonYear, seasonTitle));
		
		long teamId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_TEAM));
		Date teamMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Team.VIEW_MODIFIED)));
		int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Team.VIEW_NUMBER));
		String teamName = cursor.getString(cursor.getColumnIndexOrThrow(Team.VIEW_NAME));
		team.setTeam(new Team(teamId, teamMod, teamNumber, teamName));
		
		team.setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_ORIENTATION)));
		team.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_WIDTH)));
		team.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_LENGTH)));
		team.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_HEIGHT)));
		team.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.VIEW_NOTES)));
		return team;
	}
}
