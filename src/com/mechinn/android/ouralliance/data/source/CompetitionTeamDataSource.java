package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionTeamDataSource {
	private static final String TAG = "CompetitionTeamDataSource";
	// Database fields
	private SQLiteDatabase database;
	private Database db;
	private TeamDataSource teamData;
	private CompetitionDataSource competitionData;

	public CompetitionTeamDataSource(Context context) {
		db = new Database(context);
		teamData = new TeamDataSource(context);
		competitionData = new CompetitionDataSource(context);
	}
	
	public void open() throws SQLException {
		database = db.getWritableDatabase();
		teamData.open();
		competitionData.open();
	}
	
	public void close() {
		competitionData.close();
		teamData.close();
		db.close();
	}

	public CompetitionTeam insert(CompetitionTeam competitionTeam) throws IllegalArgumentException, OurAllianceException {
		this.open();
		long id = database.insert(CompetitionTeam.TABLE, null, competitionTeam.toCV());
		this.close();
		if(id!=-1) {
			Log.d(TAG, "insert "+competitionTeam);
		} else {
			Log.d(TAG, "did not insert "+competitionTeam);
		}
		return get(id);
	}
	
	public int update(CompetitionTeam competitionTeam) {
		this.open();
		int count = database.update(CompetitionTeam.TABLE, competitionTeam.toCV(), BaseColumns._ID + " = " + competitionTeam.getId(), null);
		this.close();
		Log.d(TAG, "updated "+count+" from "+competitionTeam);
		return count;
	}

	public int delete(CompetitionTeam competitionTeam) {
		this.open();
		int count = database.delete(CompetitionTeam.TABLE, BaseColumns._ID + " = " + competitionTeam.getId(), null);
		this.close();
		Log.d(TAG, "delete "+count+" from "+competitionTeam);
		return count;
	}
	
	public CompetitionTeam get(long id) throws IllegalArgumentException, OurAllianceException {
		return getWhere(BaseColumns._ID + " = " + id);
	}
	
	public CompetitionTeam get(Team team) throws IllegalArgumentException, OurAllianceException {
		return getWhere(CompetitionTeam.TEAM + " = " + team.getId());
	}
	
	private CompetitionTeam getWhere(String where) throws IllegalArgumentException, OurAllianceException {
		CompetitionTeam competitionTeam;
		this.open();
		Cursor cursor = database.query(CompetitionTeam.TABLE, CompetitionTeam.ALLCOLUMNS, where, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competitionTeam = cursorToCompetitionTeam(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		this.close();
		return competitionTeam;
	}

	public List<CompetitionTeam> getAll() throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(null);
	}
	
	public List<Team> getAllTeams(Competition comp) throws IllegalArgumentException, OurAllianceException {
		List<CompetitionTeam> compTeams = getAllWhere(CompetitionTeam.COMPETITION + " = " + comp.getId());
		List<Team> teams = new ArrayList<Team>();
		for(CompetitionTeam each : compTeams) {
			teams.add(each.getTeam());
		}
		return teams;
	}
	
	private List<CompetitionTeam> getAllWhere(String where) throws IllegalArgumentException, OurAllianceException {
		List<CompetitionTeam> compTeams = new ArrayList<CompetitionTeam>();
		this.open();
		Cursor cursor = database.query(CompetitionTeam.TABLE, CompetitionTeam.ALLCOLUMNS, where, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CompetitionTeam competitionTeam = cursorToCompetitionTeam(cursor);
			compTeams.add(competitionTeam);
			cursor.moveToNext();
		}
		if(compTeams.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return compTeams;
	}

	private CompetitionTeam cursorToCompetitionTeam(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		CompetitionTeam competitionTeam = new CompetitionTeam();
		competitionTeam.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		competitionTeam.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			competitionTeam.setTeam(teamData.get(cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.TEAM))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup team referenced in this team scouting");
		}
		try {
			competitionTeam.setCompetition(competitionData.get(cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.COMPETITION))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup competition referenced in this team scouting");
		}
		return competitionTeam;
	}
}
