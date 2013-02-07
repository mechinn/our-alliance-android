package com.mechinn.android.ouralliance.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public abstract class TeamScoutingDataSource<A extends TeamScouting> implements IOurAllianceDataSource<A> {
	private static final String TAG = "TeamScoutingDataSource";
	private Context context;
	private ContentResolver data;
	public abstract Uri getUri();
	public abstract Uri getUriFromId(A teamScouting);
	public abstract Uri getUriFromId(long teamScouting);
	public abstract Uri getUriFromSeasonTeam(long season, long team);
	public abstract String[] getViewCols();

	public TeamScoutingDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}
	
	public Uri insert(A teamScouting) {
		return data.insert(getUri(), teamScouting.toCV());
	}
	
	public int update(A teamScouting) throws OurAllianceException {
		int count = data.update(getUriFromId(teamScouting), teamScouting.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+teamScouting);
		}
		return count;
	}

	public int delete(A teamScouting) throws OurAllianceException {
		int count = data.delete(getUriFromId(teamScouting), null, null);
		Log.d(TAG, "delete "+count+" from "+teamScouting);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+teamScouting+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+teamScouting);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, getViewCols(), null, null, null);
	}
	
	public CursorLoader get(A teamScouting) {
		return get(teamScouting.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, getUriFromId(id), getViewCols(), null, null, null);
	}

	public CursorLoader get(Season season, Team team) {
		Log.d(TAG, season+" "+team);
		Log.d(TAG, season.getId()+" "+team.getId());
		return get(season.getId(), team.getId());
	}
	
	public CursorLoader get(long season, long team) {
		Log.d(TAG, season+" "+team);
		return new CursorLoader(context, getUriFromSeasonTeam(season, team), getViewCols(), null, null, null);
	}

	public CursorLoader getAll() {
		return new CursorLoader(context, getUri(), getViewCols(), null, null, Team.VIEW_NUMBER);
	}
}
