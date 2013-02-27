package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public abstract class AOurAllianceDataSource<A extends AOurAllianceData> {
	public static final String TAG = AOurAllianceDataSource.class.getSimpleName();
	private Context context;
	
	public AOurAllianceDataSource(Context context) {
		this.context = context;
	}
	
	public A insert(Uri uri, A data) throws OurAllianceException, SQLException {
		if(data.insert()) {
			Uri newUri = context.getContentResolver().insert(uri, data.toCV());
			Log.d(TAG, newUri.toString());
			data.setIdUri(newUri);
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
	
	public Cursor query(Season season, Team team) {
		return query(season.getId(), team.getId());
	}
	
	public CursorLoader get(long season, long team) {
		return get(TeamScouting.SEASON+" = '"+season+"' AND "+TeamScouting.TEAM+" = '"+team+"'");
	}
	
	public Cursor query(long season, long team) {
		return query(TeamScouting.SEASON+" = '"+season+"' AND "+TeamScouting.TEAM+" = '"+team+"'");
	}
	
	public CursorLoader get(A id) {
		return get(id.getId());
	}
	
	public Cursor query(A id) {
		return query(id.getId());
	}
	
	public CursorLoader get(long id) {
		return get(BaseColumns._ID, id);
	}
	
	public Cursor query(long id) {
		return query(BaseColumns._ID, id);
	}
	
	public CursorLoader get(String key, Object value) {
		return get(key, value, null);
	}
	
	public Cursor query(String key, Object value) {
		return query(key, value, null);
	}
	
	public CursorLoader get(String key, Object value, String order) {
		return get(key+" = '"+value+"'", order);
	}
	
	public Cursor query(String key, Object value, String order) {
		return query(key+" = '"+value+"'", order);
	}
	
	public CursorLoader getAll() {
		return getAll(null);
	}
	
	public Cursor queryAll() {
		return queryAll(null);
	}
	
	public CursorLoader getAll(String order) {
		return get(null, order);
	}
	
	public Cursor queryAll(String order) {
		return query(null, order);
	}
	
	public CursorLoader get(String selection) {
		return get(selection, null);
	}
	
	public Cursor query(String selection) {
		return query(selection, null);
	}
	
	public CursorLoader getAllDistinct(String column) {
		return getAllDistinct(new String[]{column}, null);
	}
	
	public Cursor queryAllDistinct(String column) {
		return queryAllDistinct(new String[]{column}, null);
	}
	
	public CursorLoader getAllDistinct(String[] projection) {
		return getAllDistinct(projection, null);
	}
	
	public Cursor queryAllDistinct(String[] projection) {
		return queryAllDistinct(projection, null);
	}
	
	public CursorLoader getAllDistinct(String[] projection, String order) {
		return getDistinct(projection, null, order);
	}
	
	public Cursor queryAllDistinct(String[] projection, String order) {
		return queryDistinct(projection, null, order);
	}
	
	public CursorLoader get(Uri uri, String[] projection, String selection, String order) {
		return new CursorLoader(context, uri, projection, selection, null, order);
	}
	
	public Cursor query(Uri uri, String[] projection, String selection, String order) {
		return context.getContentResolver().query(uri, projection, selection, null, order);
	}

	public abstract A insert(A data) throws OurAllianceException, SQLException;
	public abstract int update(A data, String selection) throws OurAllianceException, SQLException;
	public abstract int delete(String selection) throws OurAllianceException;
	public abstract CursorLoader get(String selection, String order);
	public abstract CursorLoader getDistinct(String[] projection, String selection, String order);
	public abstract Cursor query(String selection, String order);
	public abstract Cursor queryDistinct(String[] projection, String selection, String order);

	public static List<String> getStringList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<String> set = new ArrayList<String>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String value = cursor.getString(0);
				if(null!=value) {
					Log.d(TAG, "get "+value);
					set.add(value);
				}
				cursor.moveToNext();
			}
			if(set.isEmpty()) {
				throw new OurAllianceException(TAG,"None in db.");
			}
			return set;
		}
		throw new SQLException("Cursor is null");
	}
}
