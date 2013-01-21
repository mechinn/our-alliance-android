package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class Team implements Serializable, Comparable<Team> {
	public static final String CLASS = "Team";
	public static final String TABLE = "team";
	public static final String NUMBER = "number";
	public static final String NAME = "name";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, NUMBER, NAME };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String NUMPATH = PATH+"num/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final Uri URI_ID = Uri.parse(DataProvider.BASE_URI_STRING+IDPATH);
	public static final Uri URI_NUM = Uri.parse(DataProvider.BASE_URI_STRING+NUMPATH);

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private long id;
	private Date modified;
	private int number;
	private String name;
	public Team() {
	}
	public Team(int number, String name) {
		this.number = number;
		this.name = name;
	}
	public Team(int id, Date mod, int number, String name) {
		this.id = id;
		this.modified = mod;
		this.number = number;
		this.name = name;
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return this.number+": "+name;
	}
	public int compareTo(Team another) {
		return this.getNumber() - another.getNumber();
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Team.NUMBER, this.getNumber());
		values.put(Team.NAME, this.getName());
		return values;
	}
}
