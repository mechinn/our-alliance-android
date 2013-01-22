package com.mechinn.android.ouralliance.data.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class CompetitionDataSource {
	private static final String TAG = "CompetitionDataSource";
	// Database fields
	private Context context;
	private ContentResolver data;

	public CompetitionDataSource(Context context) {
		this.context = context;
		data = context.getContentResolver();
	}

	public Competition insert(Competition competition) throws OurAllianceException {
		Uri newRow = data.insert(Competition.URI, competition.toCV());
		Log.d(TAG, "insert "+competition);
		Cursor cursor = data.query(newRow, Competition.VIEWCOLUMNS, null, null, null);
		return getCompetition(cursor);
	}
	
	public int update(Competition competition) throws OurAllianceException {
		int count = data.update(Competition.uriFromId(competition), competition.toCV(), null, null);
		Log.d(TAG, "updated "+count+" from "+competition);
		if(count>1) {
			throw new OurAllianceException(TAG,"Updated multiple teams from "+competition+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not update "+competition);
		}
		return count;
	}

	public int delete(Competition competition) throws OurAllianceException {
		int count = data.delete(Competition.uriFromId(competition), null, null);
		Log.d(TAG, "delete "+count+" from "+competition);
		if(count>1) {
			throw new OurAllianceException(TAG,"Deleted multiple teams from "+competition+", please contact developer.");
		} else if(count<1) {
			throw new OurAllianceException(TAG,"Could not delete "+competition);
		}
		return count;
	}
	
	public Competition get(long id) throws OurAllianceException {
		Cursor cursor = data.query(Competition.uriFromId(id), Competition.VIEWCOLUMNS, null, null, null);
		return getCompetition(cursor);
	}
	
	public Competition get(String code) throws OurAllianceException {
		Cursor cursor = data.query(Competition.uriFromCode(code), Competition.VIEWCOLUMNS, null, null, null);
		return getCompetition(cursor);
	}
	
	public List<Competition> getAll() throws OurAllianceException {
		Cursor cursor = data.query(Competition.URI, Competition.VIEWCOLUMNS, null, null, Competition.VIEW_NAME);
		return getCompetitions(cursor);
	}
	
	public CursorLoader getAllCompetitions(Season season) {
		return getAllComps(season.getId());
	}
	
	public CursorLoader getAllCompetitions(long season) {
		return getAllComps(season);
	}
	
	private CursorLoader getAllComps(long season) {
		return new CursorLoader(context, Competition.uriFromSeason(season), Competition.VIEWCOLUMNS, null, null, Competition.VIEW_NAME);
	}
	
	public static Competition getCompetition(Cursor cursor) throws OurAllianceException {
		Competition competition;
		if(cursor.getCount()==1) {
			cursor.moveToFirst();
			competition = cursorToCompetition(cursor);
			Log.d(TAG, "get "+competition);
		} else if(cursor.getCount()==0) {
			throw new OurAllianceException(TAG,"Competition not found in db.",new NoObjectsThrowable());
		} else {
			throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
		}
		cursor.close();
		return competition;
	}

	public static List<Competition> getCompetitions(Cursor cursor) throws OurAllianceException {
		List<Competition> comps = new ArrayList<Competition>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Competition competition = cursorToCompetition(cursor);
			Log.d(TAG, "get "+competition);
			comps.add(competition);
			cursor.moveToNext();
		}
		if(comps.isEmpty()) {
			throw new OurAllianceException(TAG,"No competitions in db.",new NoObjectsThrowable());
		}
		// Make sure to close the cursor
		cursor.close();
		return comps;
	}

	public static Competition cursorToCompetition(Cursor cursor) {
		Competition competition = new Competition();
		competition.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Competition.VIEW_ID)));
		competition.setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Competition.VIEW_MODIFIED))));
		int seasonId = cursor.getInt(cursor.getColumnIndexOrThrow(Competition.VIEW_SEASON));
		Date seasonMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Season.VIEW_MODIFIED)));
		int seasonYear = cursor.getInt(cursor.getColumnIndexOrThrow(Season.VIEW_YEAR));
		String seasonTitle = cursor.getString(cursor.getColumnIndexOrThrow(Season.VIEW_TITLE));
		competition.setSeason(new Season(seasonId, seasonMod, seasonYear, seasonTitle));
		competition.setName(cursor.getString(cursor.getColumnIndexOrThrow(Competition.VIEW_NAME)));
		competition.setCode(cursor.getString(cursor.getColumnIndexOrThrow(Competition.VIEW_CODE)));
		return competition;
	}
}
