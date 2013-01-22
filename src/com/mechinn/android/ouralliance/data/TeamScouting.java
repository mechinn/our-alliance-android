package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

public class TeamScouting extends AOurAllianceData implements Serializable {
	public static final String CLASS = "TeamScouting";
	public static final String TABLE = "teamscouting";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String ORIENTATION = "orientation";
    public static final String WIDTH = "width";
    public static final String LENGTH = "length";
    public static final String HEIGHT = "height";
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, TEAM, ORIENTATION, WIDTH, LENGTH, HEIGHT, NOTES };
    
    public static final String VIEW = "teamscoutingview";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_TEAM = TABLE+TEAM;
    public static final String VIEW_ORIENTATION = TABLE+ORIENTATION;
    public static final String VIEW_WIDTH = TABLE+WIDTH;
    public static final String VIEW_LENGTH = TABLE+LENGTH;
    public static final String VIEW_HEIGHT = TABLE+HEIGHT;
    public static final String VIEW_NOTES = TABLE+NOTES;
	public static final String[] VIEWCOLUMNS = { VIEW_ID, VIEW_MODIFIED, VIEW_SEASON, VIEW_TEAM, VIEW_ORIENTATION, VIEW_WIDTH, VIEW_LENGTH, VIEW_HEIGHT, VIEW_NOTES,
		Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
		Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String SEASONPATH = PATH+"season/";
	public static final String TEAMADDON = "/team/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_SEASON = DataProvider.BASE_URI_STRING+SEASONPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private Season season;
	private Team team;
	private String orientation;
	private int width;
	private int length;
	private int height;
	private String notes;
	public TeamScouting() {
		super();
	}
	public TeamScouting(Season season, Team team) {
		this.setData(season, team);
	}
	public TeamScouting(long id, Date mod, Season season, Team team, String orientation, int width, int length, int height, String notes) {
		super(id, mod);
		this.setData(season, team);
		this.setOrientation(orientation);
		this.setWidth(width);
		this.setLength(length);
		this.setHeight(height);
		this.setNotes(notes);
	}
	public void setData(Season season, Team team) {
		this.setSeason(season);
		this.setTeam(team);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(TeamScouting id) {
		return Uri.parse(URI_ID + id.getId());
	}
	public static Uri uriFromSeason(Season season) {
		return Uri.parse(URI_SEASON + season.getId());
	}
	public static Uri uriFromSeasonTeam(Season season, Team team) {
		return Uri.parse(URI_SEASON + season.getId() + TEAMADDON + team.getId());
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
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(TeamScouting.SEASON, this.getSeason().getId());
		values.put(TeamScouting.TEAM, this.getTeam().getId());
		values.put(TeamScouting.ORIENTATION, this.getOrientation());
		values.put(TeamScouting.WIDTH, this.getWidth());
		values.put(TeamScouting.LENGTH, this.getLength());
		values.put(TeamScouting.HEIGHT, this.getHeight());
		values.put(TeamScouting.NOTES, this.getNotes());
		return values;
	}
}
