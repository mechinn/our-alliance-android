package com.mechinn.android.ouralliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

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

	public TeamScouting editTeam(TeamScouting teamScouting) throws IllegalArgumentException, OurAllianceException {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(TeamScouting.SEASON, teamScouting.getSeason().getId());
		values.put(TeamScouting.TEAM, teamScouting.getTeam().getId());
		values.put(TeamScouting.RANK, teamScouting.getRank());
		values.put(TeamScouting.ORIENTATION, teamScouting.getOrientation());
		values.put(TeamScouting.WIDTH, teamScouting.getWidth());
		values.put(TeamScouting.LENGTH, teamScouting.getLength());
		values.put(TeamScouting.HEIGHT, teamScouting.getHeight());
		values.put(TeamScouting.NOTES, teamScouting.getNotes());
		long id;
		if(teamScouting.getId()==0) {
			id = database.insert(TeamScouting.TABLE, null, values);
		} else {
			id = database.update(TeamScouting.TABLE, values, BaseColumns._ID + " = " + teamScouting.getId(), null);
		}
		return getTeamScouting(id);
	}

	public void deleteTeam(TeamScouting teamScouting) {
		long id = teamScouting.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(TeamScouting.TABLE, BaseColumns._ID + " = " + id, null);
	}
	public TeamScouting getTeamScouting(long id) throws IllegalArgumentException, OurAllianceException {
		TeamScouting scouting;
		Cursor cursor = database.query(TeamScouting.TABLE, TeamScouting.ALLCOLUMNS, BaseColumns._ID + " = " + id, null, null, null, null, null);
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

	public List<TeamScouting> getAllTeams() throws IllegalArgumentException, OurAllianceException {
		List<TeamScouting> comments = new ArrayList<TeamScouting>();
	
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
		return comments;
	}

	private TeamScouting cursorToTeamScouting(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		TeamScouting team = new TeamScouting();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			team.setSeason(seasonData.getSeason(cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.SEASON))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup season referenced in this team scouting");
		}
		try {
			team.setTeam(teamData.getTeam((cursor.getLong(cursor.getColumnIndexOrThrow(TeamScouting.TEAM)))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup team referenced in this team scouting");
		}
		team.setRank(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.RANK)));
		team.setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.ORIENTATION)));
		team.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.WIDTH)));
		team.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.LENGTH)));
		team.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(TeamScouting.HEIGHT)));
		team.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(TeamScouting.NOTES)));
		return team;
	}
}
