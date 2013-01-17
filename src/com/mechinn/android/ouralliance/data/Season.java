package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

public class Season implements Serializable {
	public static final String CLASS = "Season";
	public static final String TABLE = "season";
	public static final String YEAR = "year";
	public static final String COMPETITION = "competition";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, YEAR, COMPETITION };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String YEARPATH = PATH+"year/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final Uri URI_ID = Uri.parse(DataProvider.BASE_URI_STRING+IDPATH);
	public static final Uri URI_YEAR = Uri.parse(DataProvider.BASE_URI_STRING+YEARPATH);

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private long id;
	private Date modified;
	private int year;
	private String competition;
	public Season() {
	}
	public Season(int year, String competition) {
		this.year = year;
		this.competition = competition;
	}
	public Season(int id, Date mod, int number, String name) {
		this.id = id;
		this.modified = mod;
		this.year = number;
		this.competition = name;
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
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getCompetition() {
		return competition;
	}
	public void setCompetition(String competition) {
		this.competition = competition;
	}
	public String toString() {
		return this.year+": "+competition;
	}
	
}
