package com.mechinn.android.ouralliance.data.source;

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

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.Database;

public class CompetitionTeamDataSource {
	private static final String TAG = "CompetitionTeamDataSource";
	private Context context;
	private ContentResolver data;

	public CompetitionTeamDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(CompetitionTeam competitionTeam) {
		return data.insert(CompetitionTeam.URI, competitionTeam.toCV());
	}
	
	public int update(CompetitionTeam competitionTeam) throws OurAllianceException {
		int count = data.update(CompetitionTeam.uriFromId(competitionTeam), competitionTeam.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+competitionTeam);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+competitionTeam+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+competitionTeam);
		}
		return count;
	}

	public int delete(CompetitionTeam competitionTeam) throws OurAllianceException {
		int count = data.delete(CompetitionTeam.uriFromId(competitionTeam), null, null);
		Log.d(TAG, "delete "+count+" from "+competitionTeam);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+competitionTeam+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+competitionTeam);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, CompetitionTeam.VIEWCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(CompetitionTeam id) {
		return get(id.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, CompetitionTeam.uriFromId(id), CompetitionTeam.VIEWCOLUMNS, null, null, null);
	}
	
	public CursorLoader getTeam(Team team) {
		return getTeam(team.getId());
	}
	
	public CursorLoader getTeam(long team) {
		return new CursorLoader(context, CompetitionTeam.uriFromTeam(team), CompetitionTeam.VIEWCOLUMNS, null, null, null);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, CompetitionTeam.URI, CompetitionTeam.VIEWCOLUMNS, null, null, Team.VIEW_NUMBER);
	}
	
	public CursorLoader getAllTeams(Competition comp) {
		return getAllTeams(comp.getId());
	}
	
	public CursorLoader getAllTeams(long id) {
		return new CursorLoader(context, CompetitionTeam.uriFromComp(id), CompetitionTeam.VIEWCOLUMNS, null, null, Team.VIEW_NUMBER);
	}
	
	private CompetitionTeam getCompetitionTeam(Cursor cursor) throws OurAllianceException {
		CompetitionTeam competitionTeam;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competitionTeam = cursorToCompetitionTeam(cursor);
			Log.d(TAG, "get "+competitionTeam);
		} else if(cursor.getCount()<1) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return competitionTeam;
	}
	
	private List<CompetitionTeam> getCompetitionTeams(Cursor cursor) throws OurAllianceException {
		List<CompetitionTeam> compTeams = new ArrayList<CompetitionTeam>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CompetitionTeam competitionTeam = cursorToCompetitionTeam(cursor);
			Log.d(TAG, "get "+competitionTeam);
			compTeams.add(competitionTeam);
			cursor.moveToNext();
		}
		if(compTeams.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return compTeams;
	}

	public static CompetitionTeam cursorToCompetitionTeam(Cursor cursor) {
		CompetitionTeam competitionTeam = new CompetitionTeam();
		competitionTeam.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		competitionTeam.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		
		long teamId = cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.TEAM));
		Date teamMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Team.VIEW_MODIFIED)));
		int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Team.VIEW_NUMBER));
		String teamName = cursor.getString(cursor.getColumnIndexOrThrow(Team.VIEW_NAME));
		competitionTeam.setTeam(new Team(teamId, teamMod, teamNumber, teamName));

		long compId = cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.COMPETITION));
		Date compMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Competition.VIEW_MODIFIED)));

		int seasonId = cursor.getInt(cursor.getColumnIndexOrThrow(Competition.VIEW_SEASON));
		Date seasonMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Season.VIEW_MODIFIED)));
		int seasonYear = cursor.getInt(cursor.getColumnIndexOrThrow(Season.VIEW_YEAR));
		String seasonTitle = cursor.getString(cursor.getColumnIndexOrThrow(Season.VIEW_TITLE));
		Season season = new Season(seasonId, seasonMod, seasonYear, seasonTitle);
		
		String compName = cursor.getString(cursor.getColumnIndexOrThrow(Competition.VIEW_NAME));
		String compCode = cursor.getString(cursor.getColumnIndexOrThrow(Competition.VIEW_CODE));
		competitionTeam.setCompetition(new Competition(compId, compMod, season, compName, compCode));

		return competitionTeam;
	}
}
