package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

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

	public Team insert(Team team) throws IllegalArgumentException, OurAllianceException {
		this.open();
		long id = database.insert(Team.TABLE, null, team.toCV());
		this.close();
		if(id!=-1) {
			Log.d(TAG,"insert "+team);
		} else {
			Log.d(TAG,"did not insert "+team);
		}
		return get(id);
	}
	
	public int update(Team team) {
		this.open();
		int count = database.update(Team.TABLE, team.toCV(), BaseColumns._ID + " = " + team.getId(), null);
		this.close();
		Log.d(TAG, "updated "+count+" from "+team);
		return count;
	}

	public int delete(Team team) {
		this.open();
		int count = database.delete(Team.TABLE, BaseColumns._ID + " = " + team.getId(), null);
		this.close();
		Log.d(TAG, "delete "+count+" from "+team);
		return count;
	}
	
	public Team get(long id) throws IllegalArgumentException, OurAllianceException {
		return getWhere(BaseColumns._ID + " = " + id);
	}
	
	public Team getNum(int num) throws IllegalArgumentException, OurAllianceException {
		return getWhere(Team.NUMBER + " = " + num);
	}
	
	private Team getWhere(String where) throws IllegalArgumentException, OurAllianceException {
		Team team;
		this.open();
		Cursor cursor = database.query(Team.TABLE, Team.ALLCOLUMNS, where, null, null, null, null, null);
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			team = cursorToTeam(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Team not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		this.close();
		return team;
	}

	public List<Team> getAll() throws IllegalArgumentException, OurAllianceException {
		return getAllWhere(null);
	}
	
	private List<Team> getAllWhere(String where) throws IllegalArgumentException, OurAllianceException {
		List<Team> comments = new ArrayList<Team>();
		this.open();
		Cursor cursor = database.query(Team.TABLE, Team.ALLCOLUMNS, where, null, null, null, null);
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
		this.close();
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
