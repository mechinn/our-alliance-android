package com.mechinn.android.ouralliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TeamDataSource {
	private static final String TAG = "TeamDataSource";
	// Database fields
	private SQLiteDatabase database;
	private Database db;

	public TeamDataSource(Context context) {
		db = new Database(context);
	}
	
	public void open() throws SQLException {
		database = db.getWritableDatabase();
	}
	
	public void close() {
		db.close();
	}

	public Team editTeam(Team team) throws IllegalArgumentException, OurAllianceException {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Team.NUMBER, team.getNumber());
		values.put(Team.NAME, team.getName());
		long id;
		if(team.getId()==0) {
			id = database.insert(Team.TABLE, null, values);
		} else {
			id = database.update(Team.TABLE, values, BaseColumns._ID + " = " + team.getId(), null);
		}
		return getTeam(id);
	}

	public void deleteTeam(Team team) {
		long id = team.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(Team.TABLE, BaseColumns._ID + " = " + id, null);
	}
	public Team getTeam(long id) throws IllegalArgumentException, OurAllianceException {
		Team team;
		Cursor cursor = database.query(Team.TABLE, Team.ALLCOLUMNS, BaseColumns._ID + " = " + id, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			team = cursorToTeam(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Team not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return team;
	}

	public List<Team> getAllTeams() throws IllegalArgumentException, OurAllianceException {
		List<Team> comments = new ArrayList<Team>();
	
		Cursor cursor = database.query(Team.TABLE, Team.ALLCOLUMNS, null, null, null, null, null);
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Team team = cursorToTeam(cursor);
			comments.add(team);
			cursor.moveToNext();
		}
		if(comments.isEmpty()) {
			throw new OurAllianceException(TAG,"No teams in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}

	private Team cursorToTeam(Cursor cursor) {
		Team team = new Team();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		team.setNumber(cursor.getInt(cursor.getColumnIndexOrThrow(Team.NUMBER)));
		team.setName(cursor.getString(cursor.getColumnIndexOrThrow(Team.NAME)));
		return team;
	}
}
