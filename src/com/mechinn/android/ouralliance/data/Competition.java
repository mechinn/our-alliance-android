package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class Competition extends AOurAllianceData implements Comparable<Competition> {
	private static final long serialVersionUID = -5179493838272851750L;
	public static final String CLASS = "Competition";
	public static final String TABLE = "competition";
	public static final String SEASON = Season.TABLE;
    public static final String NAME = "name";
    public static final String CODE = "code";
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, NAME, CODE };

    public static final String VIEW = "competitionview";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_NAME = TABLE+NAME;
    public static final String VIEW_CODE = TABLE+CODE;
	public static final String[] VIEWCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, NAME, CODE,
		Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE };

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;
	
	private Season season;
	private CharSequence name;
	private CharSequence code;
	public Competition() {
		super();
	}
	public Competition(Season season, CharSequence name, CharSequence code) {
		setData(season, name, code);
	}
	public Competition(long id, Date mod, Season season, CharSequence name, CharSequence code) {
		super(id, mod);
		setData(season, name, code);
	}
	private void setData(Season season, CharSequence name, CharSequence code) {
		this.setSeason(season);
		this.setName(name);
		this.setCode(code);
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public CharSequence getName() {
		return name;
	}
	public void setName(CharSequence name) {
		this.name = name;
	}
	public CharSequence getCode() {
		return code;
	}
	public void setCode(CharSequence code) {
		this.code = code;
	}
	public String toString() {
		return this.season+" - "+this.name;
	}
	public boolean equals(Competition data) {
		return super.equals(data) && 
				getSeason().equals(data.getSeason()) && 
				getName().equals(data.getName()) && 
				getCode().equals(data.getCode());
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Competition.SEASON, this.getSeason().getId());
		if(TextUtils.isEmpty(this.getName())) {
			values.putNull(Competition.NAME);
		} else {
			values.put(Competition.NAME, this.getName().toString());
		}
		if(TextUtils.isEmpty(this.getCode())) {
			values.putNull(Competition.CODE);
		} else {
			values.put(Competition.CODE, this.getCode().toString());
		}
		return values;
	}
	public int compareTo(Competition another) {
		return this.getName().toString().compareTo(another.getName().toString());
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==this.getSeason()) {
			error.add(SEASON);
		}
		if(TextUtils.isEmpty(this.getName())) {
			error.add(NAME);
		}
		if(TextUtils.isEmpty(this.getCode())) {
			error.add(CODE);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setSeason(Season.newFromViewCursor(cursor));
		setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
		setCode(cursor.getString(cursor.getColumnIndexOrThrow(CODE)));
	}
	
	public static Competition newFromCursor(Cursor cursor) {
		Competition data = new Competition();
		data.fromCursor(cursor);
		return data;
	}
	
	public static Competition newFromViewCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		Season season = Season.newFromViewCursor(cursor);
		String name = cursor.getString(cursor.getColumnIndexOrThrow(VIEW_NAME));
		String code = cursor.getString(cursor.getColumnIndexOrThrow(VIEW_CODE));
		return new Competition(id, mod, season, name, code);
	}
}