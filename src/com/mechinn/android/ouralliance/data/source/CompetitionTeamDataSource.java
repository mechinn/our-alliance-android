package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
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

	public CompetitionTeam editCompetitionTeam(CompetitionTeam competitionTeam) throws IllegalArgumentException, OurAllianceException {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(CompetitionTeam.COMPETITION, competitionTeam.getCompetition().getId());
		values.put(CompetitionTeam.TEAM, competitionTeam.getTeam().getId());
		long id;
		if(competitionTeam.getId()==0) {
			id = database.insert(CompetitionTeam.TABLE, null, values);
		} else {
			id = database.update(CompetitionTeam.TABLE, values, BaseColumns._ID + " = " + competitionTeam.getId(), null);
		}
		return getCompetitionTeam(id);
	}

	public void deleteCompetitionTeam(CompetitionTeam competitionTeam) {
		long id = competitionTeam.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(CompetitionTeam.TABLE, BaseColumns._ID + " = " + id, null);
	}
	
	public CompetitionTeam getCompetitionTeam(long id) throws IllegalArgumentException, OurAllianceException {
		CompetitionTeam competitionTeam;
		Cursor cursor = database.query(CompetitionTeam.TABLE, CompetitionTeam.ALLCOLUMNS, BaseColumns._ID + " = " + id, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competitionTeam = cursorToCompetitionTeam(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return competitionTeam;
	}

	public List<CompetitionTeam> getAllCompetitionTeams() throws IllegalArgumentException, OurAllianceException {
		List<CompetitionTeam> compTeams = new ArrayList<CompetitionTeam>();
	
		Cursor cursor = database.query(CompetitionTeam.TABLE, CompetitionTeam.ALLCOLUMNS, null, null, null, null, null);
	
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
		return compTeams;
	}

	private CompetitionTeam cursorToCompetitionTeam(Cursor cursor) throws IllegalArgumentException, OurAllianceException {
		CompetitionTeam competitionTeam = new CompetitionTeam();
		competitionTeam.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		competitionTeam.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		try {
			competitionTeam.setTeam(teamData.getTeam(cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.TEAM))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup team referenced in this team scouting");
		}
		try {
			competitionTeam.setCompetition(competitionData.getCompetition(cursor.getLong(cursor.getColumnIndexOrThrow(CompetitionTeam.COMPETITION))));
		} catch (Exception e) {
			throw new OurAllianceException("Unable to lookup competition referenced in this team scouting");
		}
		return competitionTeam;
	}
}
