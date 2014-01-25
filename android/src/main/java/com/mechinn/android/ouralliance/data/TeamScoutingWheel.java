package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class TeamScoutingWheel extends AOurAllianceData {
	public static final String TAG = TeamScoutingWheel.class.getSimpleName();
	private static final long serialVersionUID = -8710760990028670121L;
	public static final String CLASS = "TeamScoutingWheel";
	public static final String TABLE = "teamscoutingwheel";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String TYPE = "type";
    public static final String SIZE = "size";
    public static final String COUNT = "count";
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, TEAM, TYPE, SIZE, COUNT };
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_TEAM = TABLE+TEAM;
    public static final String VIEW_TYPE = TABLE+TYPE;
    public static final String VIEW_SIZE = TABLE+SIZE;
    public static final String VIEW_COUNT = TABLE+COUNT;
    public static final String[] VIEWCOLUMNSBASE = { Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
		Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME };
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(ALLCOLUMNS, VIEWCOLUMNSBASE);

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);

	public static final String TITLE_TYPE = "Wheel Type";
	public static final String TITLE_SIZE = "Wheel Size";
	public static final String TITLE_COUNT = "Wheel Count";
	
	public static final int FIELD_TYPE = 1;
	public static final int FIELD_SIZE = 3;
	public static final int FIELD_COUNT = 6;
	public static final int FIELD_DELETE = 7;
	
	private Season season;
	private Team team;
	private CharSequence type;
	private float size;
	private int count;
	public TeamScoutingWheel() {
		super();
	}
	public TeamScoutingWheel(Season season, Team team, CharSequence type, float size, int count) {
		this.setData(season, team, type, size, count);
	}
	public TeamScoutingWheel(long id, Date mod, Season season, Team team, CharSequence type, float size, int count) {
		super(id, mod);
		this.setData(season, team, type, size, count);
	}
	public void setData(Season season, Team team, CharSequence type, float size, int count) {
		this.setSeason(season);
		this.setTeam(team);
		this.setType(type);
		this.setSize(size);
		this.setCount(count);
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
	public CharSequence getType() {
		return type;
	}
	public void setType(CharSequence type) {
		this.type = type;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String toString() {
		return getSeason()+" "+getTeam()+": "+getType()+" | "+getSize()+" | "+getCount();
	}
	public boolean equals(TeamScoutingWheel data) {
		return super.equals(data) &&
				getSeason().equals(data.getSeason()) &&
				getTeam().equals(data.getTeam()) &&
				getType().equals(data.getType()) &&
				getSize()==data.getSize() &&
				getCount()==data.getCount();
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(SEASON, this.getSeason().getId());
		values.put(TEAM, this.getTeam().getId());
		if(TextUtils.isEmpty(this.getType())){
			values.putNull(TYPE);
		} else {
			values.put(TYPE, this.getType().toString());
		}
		values.put(SIZE, this.getSize());
		values.put(COUNT, this.getCount());
		return values;
	}
	public int compareTo(TeamScoutingWheel another) {
		return this.getTeam().compareTo(another.getTeam());
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==getSeason()) {
			error.add(SEASON);
		}
		if(null==getTeam()) {
			error.add(TEAM);
		}
		if(TextUtils.isEmpty(this.getType())) {
			error.add(TYPE);
		}
		if(0==this.getSize()) {
//			error.add(SIZE);
		}
		if(0==this.getCount()) {
//			error.add(COUNT);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		for(String col : cursor.getColumnNames()) {
			System.out.println(col);
		}
		super.fromCursor(cursor);
		setSeason(Season.newFromViewCursor(cursor));
		setTeam(Team.newFromViewCursor(cursor));
		setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
		setSize(cursor.getFloat(cursor.getColumnIndexOrThrow(SIZE)));
		setCount(cursor.getInt(cursor.getColumnIndexOrThrow(COUNT)));
	}

	@Override
	public String[] toStringArray() {
		String[] links = ArrayUtils.addAll(getSeason().toStringArray(),
				getTeam().toStringArray());
		return ArrayUtils.addAll(links,
				getType().toString(),
				Float.toString(getSize()),
				Integer.toString(getCount()));
	}
	
	public static TeamScoutingWheel newFromCursor(Cursor cursor) {
		TeamScoutingWheel data = new TeamScoutingWheel();
		data.fromCursor(cursor);
		return data;
	}
	
	public static TeamScoutingWheel newFromViewCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		Season season = Season.newFromViewCursor(cursor);
		Team team = Team.newFromViewCursor(cursor);
		String type = cursor.getString(cursor.getColumnIndexOrThrow(VIEW_TYPE));
		float size = cursor.getFloat(cursor.getColumnIndexOrThrow(VIEW_SIZE));
		int count = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_COUNT));
		return new TeamScoutingWheel(id, mod, season, team, type, size, count);
	}
}