package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class Team extends AOurAllianceData implements Serializable, Comparable<Team> {
	public static final String CLASS = "Team";
	public static final String TABLE = "team";
	public static final String NUMBER = "number";
	public static final String NAME = "name";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, NUMBER, NAME };

    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_NUMBER = TABLE+NUMBER;
    public static final String VIEW_NAME = TABLE+NAME;

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String NUMPATH = PATH+"num/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_NUM = DataProvider.BASE_URI_STRING+NUMPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private int number;
	private String name;
	public Team() {
		super();
	}
	public Team(int number, String name) {
		setData(number, name);
	}
	public Team(long id, Date mod, int number, String name) {
		super(id, mod);
		setData(number, name);
	}
	private void setData(int number, String name) {
		this.setNumber(number);
		this.setName(name);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(Team id) {
		return Uri.parse(URI_ID + id.getId());
	}
	public static Uri uriFromNum(int id) {
		return Uri.parse(URI_NUM + id);
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
		return this.getNumber()+": "+this.getName();
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
