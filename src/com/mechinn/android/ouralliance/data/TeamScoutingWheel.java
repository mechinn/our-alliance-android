package com.mechinn.android.ouralliance.data;

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class TeamScoutingWheel extends AOurAllianceData {
	public static final String CLASS = "TeamScoutingWheel";
	public static final String TABLE = "teamscoutingwheel";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String TYPE = "type";
    public static final String SIZE = "size";
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, SEASON, TEAM, TYPE, SIZE };
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_TEAM = TABLE+TEAM;
    public static final String VIEW_TYPE = TABLE+TYPE;
    public static final String VIEW_WHEEL = TABLE+SIZE;
    public static final String[] VIEWCOLUMNSBASE = { Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
		Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME };
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(ALLCOLUMNS, VIEWCOLUMNSBASE);
	
	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String SEASONPATH = PATH+Season.TABLE+"/";
	public static final String TEAMADDON = "/"+Team.TABLE+"/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_SEASON = DataProvider.BASE_URI_STRING+SEASONPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private Season season;
	private Team team;
	private CharSequence type;
	private int size;
	public TeamScoutingWheel() {
		super();
	}
	public TeamScoutingWheel(Season season, Team team, CharSequence type, int size) {
		this.setData(season, team, type, size);
	}
	public TeamScoutingWheel(long id, Date mod, Season season, Team team, CharSequence type, int size) {
		super(id, mod);
		this.setData(season, team, type, size);
	}
	public void setData(Season season, Team team, CharSequence type, int size) {
		this.setSeason(season);
		this.setTeam(team);
		this.setType(type);
		this.setSize(size);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(TeamScoutingWheel id) {
		return uriFromId(id.getId());
	}
	public static Uri uriFromTeamScouting(Season season, Team team) {
		return uriFromTeamScouting(season.getId(), team.getId());
	}
	public static Uri uriFromTeamScouting(long season, long team) {
		return Uri.parse(URI_SEASON + season + TEAMADDON + team);
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
	public int getSize() {
		return size;
	}
	public void setSize(CharSequence size) {
		try {
			setSize(Integer.parseInt(size.toString()));
		} catch (Exception e) {
			setSize(0);
		}
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String toString() {
		return getSeason()+" "+getTeam()+": "+getType()+" | "+getSize();
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(TeamScoutingWheel.SEASON, this.getSeason().getId());
		values.put(TeamScoutingWheel.TEAM, this.getTeam().getId());
		if(TextUtils.isEmpty(this.getType())){
			values.putNull(TeamScoutingWheel.TYPE);
		} else {
			values.put(TeamScoutingWheel.TYPE, this.getType().toString());
		}
		values.put(TeamScoutingWheel.SIZE, this.getSize());
		return values;
	}
	public int compareTo(TeamScoutingWheel another) {
		return this.getTeam().compareTo(another.getTeam());
	}
	@Override
	public boolean update() throws OurAllianceException {
		boolean good = super.update();
		if(TextUtils.isEmpty(this.getType())) {
			throw new OurAllianceException("type");
		}
		if(0==this.getSize()) {
			throw new OurAllianceException("size");
		}
		return good;
	}
}
