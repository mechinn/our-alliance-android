package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

public class TeamScouting implements Serializable {
	public static final String CLASS = "TeamScouting";
	public static final String TABLE = "teamscouting";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String RANK = "rank";
    public static final String ORIENTATION = "orientation";
    public static final String WIDTH = "width";
    public static final String LENGTH = "length";
    public static final String HEIGHT = "height";
    public static final String NOTES = "notes";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, TEAM, RANK, ORIENTATION, WIDTH, LENGTH, HEIGHT, NOTES };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String RANKPATH = PATH+"rank/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final Uri URI_ID = Uri.parse(DataProvider.BASE_URI_STRING+IDPATH);
	public static final Uri URI_RANK = Uri.parse(DataProvider.BASE_URI_STRING+RANKPATH);

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private long id;
	private Date modified;
	private Season season;
	private Team team;
	private int rank;
	private String orientation;
	private int width;
	private int length;
	private int height;
	private String notes;
	public TeamScouting() {
	}
	public TeamScouting(Season season, Team team) {
		this.season = season;
		this.team = team;
	}
	public TeamScouting(int id, Date mod, Season season, Team team, int rank, String orientation, int width, int length, int height, String notes) {
		this.id = id;
		this.modified = mod;
		this.season = season;
		this.team = team;
		this.rank = rank;
		this.orientation = orientation;
		this.width = width;
		this.length = length;
		this.height = height;
		this.notes = notes;
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
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String toString() {
		return this.season+" - "+this.team;
	}
	
}
