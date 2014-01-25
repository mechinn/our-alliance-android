package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.Database;

public abstract class TeamScouting extends AOurAllianceData implements Comparable<TeamScouting>  {
	public static final String TAG = TeamScouting.class.getSimpleName();
	private static final long serialVersionUID = 2234995463512680398L;
	public static final String CLASS = "TeamScouting";
	public static final String TABLE = "teamscouting";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNSBASE = { SEASON, TEAM, NOTES };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, ALLCOLUMNSBASE);
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_TEAM = TABLE+TEAM;
    public static final String VIEW_NOTES = TABLE+NOTES;
	public static final String[] VIEWCOLUMNSBASE = ArrayUtils.addAll(
			ALLCOLUMNSBASE, 
			new String[] { Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE, Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME }
		);
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, VIEWCOLUMNSBASE);

	public static final String TITLE_NOTES = "Scouting Notes";
	
	private Season season;
	private Team team;
	private CharSequence notes;
	public TeamScouting() {
		super();
	}
	public TeamScouting(Season season, Team team) {
		this.setData(season, team);
	}
	public TeamScouting(long id, Date mod, Season season, Team team, CharSequence notes) {
		super(id, mod);
		this.setData(season, team);
		this.setNotes(notes);
	}
	public void setData(Season season, Team team) {
		this.setSeason(season);
		this.setTeam(team);
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public CharSequence getNotes() {
		if(null==notes) {
			return "";
		}
		return notes;
	}
	public void setNotes(CharSequence notes) {
		this.notes = notes;
	}
	public String toString() {
		return this.season+" - "+this.team;
	}
	public boolean equals(TeamScouting data) {
		return super.equals(data) &&
				getSeason().equals(data.getSeason()) &&
				getTeam().equals(data.getTeam()) &&
				getNotes().equals(data.getNotes());
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(SEASON, this.getSeason().getId());
		values.put(TEAM, this.getTeam().getId());
		if(TextUtils.isEmpty(this.getNotes())){
			values.putNull(NOTES);
		} else {
			values.put(NOTES, this.getNotes().toString());
		}
		return values;
	}
	public int compareTo(TeamScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==this.getSeason()) {
			error.add(SEASON);
		}
		if(null==this.getTeam()) {
			error.add(TEAM);
		}
		if(TextUtils.isEmpty(this.getNotes())) {
//			error.add(NOTES);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setSeason(Season.newFromViewCursor(cursor));
		setTeam(Team.newFromViewCursor(cursor));
		setNotes(cursor.getString(cursor.getColumnIndexOrThrow(NOTES)));
	}

	@Override
	public String[] toStringArray() {
		String[] links = ArrayUtils.addAll(getSeason().toStringArray(),
				getTeam().toStringArray());
		return ArrayUtils.addAll(links, getNotes().toString());
	}
	
	public static String notesFromViewCursor(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(VIEW_NOTES));
	}
}
