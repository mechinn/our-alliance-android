package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public abstract class AOurAllianceDataSource<A extends AOurAllianceData> {
	private static final String TAG = "AOurAllianceDataSource";
	private Context context;
	
	public AOurAllianceDataSource(Context context) {
		this.context = context;
	}
	
	public A insert(Uri uri, A data) throws OurAllianceException, SQLException {
		if(data.insert()) {
			Uri newUri = context.getContentResolver().insert(uri, data.toCV());
			Log.d(TAG, newUri.toString());
			data.setId(newUri);
			if(!data.insert()){
				return data;
			}
		}
		throw new OurAllianceException("Did not insert");
	}
	
	public int update(A data) throws OurAllianceException, SQLException {
		return update(data, BaseColumns._ID,data.getId());
	}
	
	public int update(A data, String key, Object value) throws OurAllianceException, SQLException {
		return update(data, key+" = '"+value+"'");
	}
	
	public int update(Uri uri, A data, String selection) throws OurAllianceException, SQLException {
		data.update();
		int count = context.getContentResolver().update(uri, data.toCV(), selection, null);
		if(count>1) {
			throw new OurAllianceException("Updated multiple items from "+data+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException("Could not update "+data);
		}
		return count;
	}
	
	public int delete(A competition) throws OurAllianceException {
		return delete(competition.getId());
	}
	
	public int delete(long data) throws OurAllianceException {
		return delete(BaseColumns._ID, data);
	}
	
	public int delete(String key, Object value) throws OurAllianceException {
		return delete(key+" = '"+value+"'");
	}
	
	public int delete(Uri uri, String selection) throws OurAllianceException {
		int count = context.getContentResolver().delete(uri, selection, null);
		if(count>1) {
			throw new OurAllianceException("Deleted multiple items, please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException("Could not delete");
		}
		return count;
	}

	public CursorLoader get(Season season, Team team) {
		return get(season.getId(), team.getId());
	}
	
	public CursorLoader get(long season, long team) {
		return query(TeamScouting.SEASON+" = '"+season+"' AND "+TeamScouting.TEAM+" = '"+team+"'");
	}
	
	public CursorLoader get(A id) {
		return get(id.getId());
	}
	
	public CursorLoader get(long id) {
		return get(BaseColumns._ID, id);
	}
	
	public CursorLoader get(String key, Object value) {
		return get(key, value, null);
	}
	
	public CursorLoader get(String key, Object value, String order) {
		return query(key+" = '"+value+"'", order);
	}
	
	public CursorLoader getAll() {
		return getAll(null);
	}
	
	public CursorLoader getAll(String order) {
		return query(null, order);
	}
	
	public CursorLoader query(String selection) {
		return query(selection, null);
	}
	
	public CursorLoader query(Uri uri, String[] projection, String selection, String order) {
		return new CursorLoader(context, uri, projection, selection, null, order);
	}

	public abstract A insert(A data) throws OurAllianceException, SQLException;
	public abstract int update(A data, String selection) throws OurAllianceException, SQLException;
	public abstract int delete(String selection) throws OurAllianceException;
	public abstract CursorLoader query(String selection, String order);
}
