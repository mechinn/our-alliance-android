package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
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

public class TeamScoutingWheelDataSource {
	private static final String TAG = "TeamScoutingWheelDataSource";
	private Context context;
	private ContentResolver data;

	public TeamScoutingWheelDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(TeamScoutingWheel teamScouting) {
		Log.d(TAG, "inserted "+teamScouting);
		return data.insert(TeamScoutingWheel.URI, teamScouting.toCV());
	}
	
	public int update(TeamScoutingWheel wheel) throws OurAllianceException {
		int count = data.update(TeamScoutingWheel.uriFromId(wheel), wheel.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+wheel);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple wheels from "+wheel+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+wheel);
		}
		return count;
	}

	public int delete(TeamScoutingWheel wheel) throws OurAllianceException {
		int count = data.delete(TeamScoutingWheel.uriFromId(wheel), null, null);
		Log.d(TAG, "delete "+count+" from "+wheel);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple wheels from "+wheel+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+wheel);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, TeamScoutingWheel.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(TeamScoutingWheel teamScouting) {
		return get(teamScouting.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, TeamScoutingWheel.uriFromId(id), TeamScoutingWheel.VIEWCOLUMNS, null, null, null);
	}

	public CursorLoader getScouting(Season season, Team team) {
		return getScouting(season.getId(), team.getId());
	}
	
	public CursorLoader getScouting(long season, long team) {
		return new CursorLoader(context, TeamScoutingWheel.uriFromTeamScouting(season, team), TeamScoutingWheel.VIEWCOLUMNS, null, null, TeamScoutingWheel.TYPE);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, TeamScoutingWheel.URI, TeamScoutingWheel.VIEWCOLUMNS, null, null, Team.VIEW_NUMBER);
	}
	
	public static TeamScoutingWheel getSingle(Cursor cursor) throws OurAllianceException {
		TeamScoutingWheel scouting;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			scouting = fromCursor(cursor);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Wheel not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return scouting;
	}
	
	public static List<TeamScoutingWheel> getList(Cursor cursor) throws OurAllianceException {
		List<TeamScoutingWheel> scoutings = new ArrayList<TeamScoutingWheel>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TeamScoutingWheel team = fromCursor(cursor);
			scoutings.add(team);
			cursor.moveToNext();
		}
		if(scoutings.isEmpty()) {
			throw new OurAllianceException(TAG,"No wheels in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return scoutings;
	}

	public static TeamScoutingWheel fromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED)));
		
		long seasonId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScoutingWheel.SEASON));
		Date seasonMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Season.VIEW_MODIFIED)));
		int seasonYear = cursor.getInt(cursor.getColumnIndexOrThrow(Season.VIEW_YEAR));
		String seasonTitle = cursor.getString(cursor.getColumnIndexOrThrow(Season.VIEW_TITLE));
		Season season = new Season(seasonId, seasonMod, seasonYear, seasonTitle);
		
		long teamId = cursor.getLong(cursor.getColumnIndexOrThrow(TeamScoutingWheel.TEAM));
		Date teamMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Team.VIEW_MODIFIED)));
		int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Team.VIEW_NUMBER));
		String teamName = cursor.getString(cursor.getColumnIndexOrThrow(Team.VIEW_NAME));
		Team team = new Team(teamId, teamMod, teamNumber, teamName);
		
		String type = cursor.getString(cursor.getColumnIndexOrThrow(TeamScoutingWheel.TYPE));
		int size = cursor.getInt(cursor.getColumnIndexOrThrow(TeamScoutingWheel.SIZE));
		return new TeamScoutingWheel(id, mod, season, team, type, size);
	}
}
