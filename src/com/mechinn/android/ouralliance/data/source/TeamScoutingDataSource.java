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

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class TeamScoutingDataSource {
	private static final String TAG = "TeamScoutingDataSource";
	// Database fields
	private SQLiteDatabase database;
	private Database db;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;

	public TeamScoutingDataSource(Context context) {
		db = new Database(context);
		teamData = new TeamDataSource(context);
		seasonData = new SeasonDataSource(context);
	}
	
	public void open() throws SQLException {
		database = db.getWritableDatabase();
		teamData.open();
		seasonData.open();
	}
	
	public void close() {
		seasonData.close();
		teamData.close();
		db.close();
	}

	public TeamScouting insert(TeamScouting teamScouting) throws IllegalArgumentException, OurAllianceException {
		this.open();
		long id = database.insert(TeamScouting.TABLE, null, teamScouting.toCV());
		this.close();
		if(id!=-1) {
			Log.d(TAG, "insert "+teamScouting);
		} else {
			Log.d(TAG, "did not insert "+teamScouting);
		}
		return get(id);
	}
	
	public int update(TeamScouting teamScouting) {
		this.open();
		int count = database.update(TeamScouting.TABLE, teamScouting.toCV(), BaseColumns._ID + " = " + teamScouting.getId(), null);
		this.close();
		Log.d(TAG, "updated "+count+" from "+teamScouting);
		return count;
	}

	public int delete(TeamScouting teamScouting) {
		this.open();
		int count = database.delete(TeamScouting.TABLE, BaseColumns._ID + " = " + teamScouting.getId(), null);
		this.close();
		Log.d(TAG, "delete "+count+" from "+teamScouting);
		return count;
	}
	
	public TeamScouting get(long id) throws IllegalArgumentException, OurAllianceException {
		return getWhere(BaseColumns._ID + " = " + id);
	}
	
	public TeamScouting get(Team team, Season season) throws IllegalArgumentException, OurAllianceException {
		return getWhere(TeamScouting.TEAM + " = " + team.getId() + " AND " + TeamScouting.SEASON + " = " + season.getId());
	}
	
	private TeamScouting getWhere(String where) throws IllegalArgumentException, OurAllianceException {
		TeamScouting scouting;
		this.open();
		Cursor cursor = database.query(TeamScouting.TABLE, TeamScouting.ALLCOLUMNS, where, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			scouting = cursorToTeamScouting(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Team scouting not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		this.close();
		return scouting;
	}

	public List<TeamScouting> getAll() throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(null);
	}
	
	public List<Team> getAllTeams(Season season) throws IllegalArgumentException, OurAllianceException {
		List<TeamScouting> scouting = getAllWhere(TeamScouting.SEASON + " = " + season.getId());
		List<Team> teams = new ArrayList<Team>();
		for(TeamScouting each : scouting) {
			teams.add(each.getTeam());
		}
		return teams;
	}
	
	private List<TeamScouting> getAllWhere(String where) throws IllegalArgumentException, OurAllianceException {
		List<TeamScouting> comments = new ArrayList<TeamScouting>();
		this.open();
		Cursor cursor = database.query(TeamScouting.TABLE, TeamScouting.ALLCOLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TeamScouting team = cursorToTeamScouting(cursor);
			comments.add(team);
			cursor.moveToNext();
		}
		if(comments.isEmpty()) {
			throw new OurAllianceException(TAG,"No team scouting in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return comments;
	}

	private TeamScouting cursorToTeamScouting(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		TeamScouting team = new TeamScouting();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			team.setSeason(seasonData.get(cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.SEASON))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup season referenced in this team scouting");
		}
		try {
			team.setTeam(teamData.get(cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.TEAM))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup team referenced in this team scouting");
		}
		team.setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.ORIENTATION)));
		team.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.WIDTH)));
		team.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.LENGTH)));
		team.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.HEIGHT)));
		team.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.NOTES)));
		return team;
	}
}
