package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class TeamDataSource {
	private static final String TAG = "TeamDataSource";
	private Context context;
	private ContentResolver data;

	public TeamDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(Team team) {
		return data.insert(Team.URI, team.toCV());
	}
	
	public int update(Team team) throws OurAllianceException {
		int count = data.update(Team.uriFromId(team), team.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+team);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+team+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+team);
		}
		return count;
	}

	public int delete(Team team) throws OurAllianceException {
		int count = data.delete(Team.uriFromId(team), null, null);
		Log.d(TAG, "delete "+count+" from "+team);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+team+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+team);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, Team.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(Team team) {
		return get(team.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, Team.uriFromId(id), Team.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(int num) {
		return new CursorLoader(context, Team.uriFromNum(num), Team.ALLCOLUMNS, null, null, null);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, Team.URI, Team.ALLCOLUMNS, null, null, Team.NUMBER);
	}
	
	public static Team getTeam(Cursor cursor) throws OurAllianceException {
		Team competitionTeam;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competitionTeam = cursorToTeam(cursor);
			Log.d(TAG, "get "+competitionTeam);
		} else if(cursor.getCount()<1) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return competitionTeam;
	}
	
	public static List<Team> getTeams(Cursor cursor) throws OurAllianceException {
		List<Team> teams = new ArrayList<Team>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Team competitionTeam = cursorToTeam(cursor);
			Log.d(TAG, "get "+competitionTeam);
			teams.add(competitionTeam);
			cursor.moveToNext();
		}
		if(teams.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return teams;
	}

	public static Team cursorToTeam(Cursor cursor) {
		Team team = new Team();
		team.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		team.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		team.setNumber(cursor.getInt(cursor.getColumnIndexOrThrow(Team.NUMBER)));
		team.setName(cursor.getString(cursor.getColumnIndexOrThrow(Team.NAME)));
		return team;
	}
}
