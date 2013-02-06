package com.mechinn.android.ouralliance.data;

import java.util.Date;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class Competition extends AOurAllianceData implements Comparable<Competition> {
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

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String SEASONPATH = PATH+"season/";
	public static final String CODEPATH = PATH+"code/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_SEASON = DataProvider.BASE_URI_STRING+SEASONPATH;
	public static final String URI_CODE = DataProvider.BASE_URI_STRING+CODEPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
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
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(Competition id) {
		return uriFromId(id.getId());
	}
	public static Uri uriFromSeason(long id) {
		return Uri.parse(URI_SEASON + id);
	}
	public static Uri uriFromSeason(Season id) {
		return uriFromSeason(id.getId());
	}
	public static Uri uriFromCode(CharSequence code) {
		return Uri.parse(URI_CODE + code);
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
}