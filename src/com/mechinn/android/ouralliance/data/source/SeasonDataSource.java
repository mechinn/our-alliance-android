package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.data.Season;
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

public class SeasonDataSource {
	private static final String TAG = "SeasonDataSource";
	private Context context;
	private ContentResolver data;

	public SeasonDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Uri insert(Season season) {
		return data.insert(Season.URI, season.toCV());
	}
	
	public int update(Season season) throws OurAllianceException {
		int count = data.update(Season.uriFromId(season), season.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+season);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+season+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+season);
		}
		return count;
	}

	public int delete(Season season) throws OurAllianceException {
		int count = data.delete(Season.uriFromId(season), null, null);
		Log.d(TAG, "delete "+count+" from "+season);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+season+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+season);
		}
		return count;
	}
	
	public CursorLoader get(Uri uri) {
		return new CursorLoader(context, uri, Season.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(Season id) {
		return get(id.getId());
	}
	
	public CursorLoader get(long id) {
		return new CursorLoader(context, Season.uriFromId(id), Season.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader get(int year) {
		return new CursorLoader(context, Season.uriFromYear(year), Season.ALLCOLUMNS, null, null, null);
	}
	
	public CursorLoader getAll() {
		return new CursorLoader(context, Season.URI, Season.ALLCOLUMNS, null, null, Season.YEAR+" DESC");
	}
	
	public static Season getSeason(Cursor cursor) throws OurAllianceException {
		Season season;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			season = cursorToSeason(cursor);
			Log.d(TAG, "get "+season);
		} else if(cursor.getCount()<1) {
			throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return season;
	}
	
	public static List<Season> getSeasons(Cursor cursor) throws OurAllianceException {
		List<Season> comments = new ArrayList<Season>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Season season = cursorToSeason(cursor);
			Log.d(TAG, "get "+season);
			comments.add(season);
			cursor.moveToNext();
		}
		if(comments.isEmpty()) {
			throw new OurAllianceException(TAG,"No seasons in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}

	public static Season cursorToSeason(Cursor cursor) {
		Season season = new Season();
		season.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		season.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
		season.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(Season.YEAR)));
		season.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Season.TITLE)));
		return season;
	}
}
