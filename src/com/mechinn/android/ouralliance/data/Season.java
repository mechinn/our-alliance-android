package com.mechinn.android.ouralliance.data;

import java.util.Date;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class Season extends AOurAllianceData implements Comparable<Season> {
	public static final String CLASS = "Season";
	public static final String TABLE = "season";
	public static final String YEAR = "year";
	public static final String TITLE = "title";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, YEAR, TITLE };

    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_YEAR = TABLE+YEAR;
    public static final String VIEW_TITLE = TABLE+TITLE;

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String YEARPATH = PATH+"year/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_YEAR = DataProvider.BASE_URI_STRING+YEARPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private int year;
	private CharSequence title;
	public Season() {
		super();
	}
	public Season(int year, CharSequence title) {
		setData(year, title);
	}
	public Season(long id, Date mod, int year, CharSequence title) {
		super(id, mod);
		setData(year, title);
	}
	private void setData(int year, CharSequence title) {
		this.setYear(year);
		this.setTitle(title);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(Season id) {
		return Uri.parse(URI_ID + id.getId());
	}
	public static Uri uriFromYear(int year) {
		return Uri.parse(URI_YEAR + year);
	}
	public int getYear() {
		return year;
	}
	public void setYear(CharSequence year) {
		try {
			setYear(Integer.parseInt(year.toString()));
		} catch (Exception e) {
			setYear(0);
		}
	}
	public void setYear(int year) {
		this.year = year;
	}
	public CharSequence getTitle() {
		return title;
	}
	public void setTitle(CharSequence title) {
		this.title = title;
	}
	public String toString() {
		return getYear()+": "+getTitle();
	}
	public int compareTo(Season another) {
		return this.getYear() - another.getYear();
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Season.YEAR, this.getYear());
		if(TextUtils.isEmpty(this.getTitle())) {
			values.putNull(Season.TITLE);
		} else {
			values.put(Season.TITLE, this.getTitle().toString());
		}
		return values;
	}
}
