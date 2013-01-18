package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

public class Competition implements Serializable {
	public static final String CLASS = "Competition";
	public static final String TABLE = "competition";
	public static final String SEASON = Season.TABLE;
    public static final String NAME = "name";
    public static final String CODE = "code";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, NAME, CODE };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String CODEPATH = PATH+"code/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final Uri URI_ID = Uri.parse(DataProvider.BASE_URI_STRING+IDPATH);
	public static final Uri URI_CODE = Uri.parse(DataProvider.BASE_URI_STRING+CODEPATH);

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private long id;
	private Date modified;
	private Season season;
	private String name;
	private String code;
	public Competition() {
	}
	public Competition(Season season, String name, String code) {
		this.season = season;
		this.name = name;
		this.code = code;
	}
	public Competition(int id, Date mod, Season season, String name, String code) {
		this(season, name, code);
		this.id = id;
		this.modified = mod;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String toString() {
		return this.season+" - "+this.name;
	}
}