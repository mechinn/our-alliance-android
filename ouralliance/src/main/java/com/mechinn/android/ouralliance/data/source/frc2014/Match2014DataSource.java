package com.mechinn.android.ouralliance.data.source.frc2014;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2014.Match2014;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class Match2014DataSource extends AOurAllianceDataSource<Match2014>  {
	public static final String TAG = Match2014DataSource.class.getSimpleName();
	public Prefs prefs;
	public Match2014DataSource(Context context) {
		super(context);
		prefs = new Prefs(context);
	}

	@Override
	public Match2014 insert(Match2014 match) throws OurAllianceException, SQLException {
		Log.d(TAG, "comp: "+match.getCompetition().getId()+" number: "+match.getNumber());
		return insert(Match2014.URI, match);
	}
	
	public Match2014 insertBase(Match match) throws OurAllianceException, SQLException {
		Log.d(TAG, "comp: "+match.getCompetition().getId()+" number: "+match.getNumber());
		return insert(new Match2014(match));
	}

	@Override
	public int update(Match2014 data, String selection) throws OurAllianceException, SQLException {
		return update(Match2014.URI, data, selection);
	}

	public int updateBase(Match match) throws OurAllianceException, SQLException {
		Log.d(TAG, "comp: "+match.getCompetition().getId()+" number: "+match.getNumber());
		return update(new Match2014(match));
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(Match2014.URI, selection);
	}

	@Override
	public CursorLoader get(String selection, String order) {
		if(selection==null || selection.isEmpty()) {
			selection = "";
		} else {
			selection += " AND ";
		}
		if(prefs.getPractice()) {
			selection += Match.SELECTPRACTICE;
		} else {
			selection += Match.SELECTNOTPRACTICE;
		}
		return get(Match2014.URI,Match2014.VIEWCOLUMNS, selection, order);
	}

	@Override
	public Cursor query(String selection, String order) {
		return query(Match2014.URI,Match2014.VIEWCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getDistinct(String[] projection, String selection, String order) {
		return get(Match2014.DISTINCTURI, projection, selection, order);
	}

	@Override
	public Cursor queryDistinct(String[] projection, String selection, String order) {
		return query(Match2014.DISTINCTURI, projection, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Match2014.NUMBER);
	}

	@Override
	public Cursor queryAll() {
		return queryAll(Match2014.NUMBER);
	}
	
	public CursorLoader getAllMatches(Competition comp) {
		return getAllMatches(comp.getId());
	}
	
	public Cursor queryAllMatches(Competition comp) {
		return queryAllMatches(comp.getId());
	}
	
	public CursorLoader getAllMatches(long comp) {
		return get(Match2014.COMPETITION, comp, Match2014.NUMBER);
	}
	
	public Cursor queryAllMatches(long comp) {
		return query(Match2014.COMPETITION, comp, Match2014.NUMBER);
	}
	
	public static Match2014 getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return Match2014.newFromCursor(cursor);
			} else if(cursor.getCount()<1) {
				throw new OurAllianceException(TAG,"Match not found in db.");
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.");
			}
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<Match2014> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<Match2014> compmatchs = new ArrayList<Match2014>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Match2014 match = Match2014.newFromCursor(cursor);
				Log.d(TAG, "get "+match);
				compmatchs.add(match);
				cursor.moveToNext();
			}
			if(compmatchs.isEmpty()) {
				throw new OurAllianceException(TAG,"No Matchs in db.");
			}
			return compmatchs;
		}
		throw new SQLException("Cursor is null");
	}
	
	public static boolean contains(Cursor cursor, Match2014 match) {
		if(null!=cursor) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Match2014 check = Match2014.newFromCursor(cursor);
				if(match.equals(check)) {
					return true;
				}
				cursor.moveToNext();
			}
		}
		return false;
	}
}
