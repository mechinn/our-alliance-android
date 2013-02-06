package com.mechinn.android.ouralliance.data;

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public abstract class TeamScouting extends AOurAllianceData implements Comparable<TeamScouting>  {
	public static final String CLASS = "TeamScouting";
	public static final String TABLE = "teamscouting";
	public static final String SEASON = Season.TABLE;
	public static final String TEAM = Team.TABLE;
    public static final String ORIENTATION = "orientation";
    public static final String DRIVETRAIN = "driveTrain";
    public static final String WIDTH = "width";
    public static final String LENGTH = "length";
    public static final String HEIGHT = "height";
    public static final String AUTONOMOUS = "autonomous";
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNSBASE = { SEASON, TEAM, ORIENTATION, DRIVETRAIN, WIDTH, LENGTH, HEIGHT, AUTONOMOUS, NOTES };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, ALLCOLUMNSBASE);
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SEASON = TABLE+SEASON;
    public static final String VIEW_TEAM = TABLE+TEAM;
    public static final String VIEW_ORIENTATION = TABLE+ORIENTATION;
    public static final String VIEW_DRIVETRAIN = TABLE+DRIVETRAIN;
    public static final String VIEW_WIDTH = TABLE+WIDTH;
    public static final String VIEW_LENGTH = TABLE+LENGTH;
    public static final String VIEW_HEIGHT = TABLE+HEIGHT;
    public static final String VIEW_AUTONOMOUS = TABLE+AUTONOMOUS;
    public static final String VIEW_NOTES = TABLE+NOTES;
	public static final String[] VIEWCOLUMNSBASE = ArrayUtils.addAll(
			ALLCOLUMNSBASE, 
			new String[] { Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE, Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME }
		);
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, VIEWCOLUMNSBASE);
	
	private Season season;
	private Team team;
	private CharSequence orientation;
	private CharSequence driveTrain;
	private int width;
	private int length;
	private int height;
	private int autonomous;
	private CharSequence notes;
	public TeamScouting() {
		super();
	}
	public TeamScouting(Season season, Team team) {
		this.setData(season, team);
	}
	public TeamScouting(long id, Date mod, Season season, Team team, CharSequence orientation, CharSequence driveTrain, int width, int length, int height, int autonomous, CharSequence notes) {
		super(id, mod);
		this.setData(season, team);
		this.setOrientation(orientation);
		this.setDriveTrain(driveTrain);
		this.setWidth(width);
		this.setLength(length);
		this.setHeight(height);
		this.setAutonomous(autonomous);
		this.setNotes(notes);
	}
	public void setData(Season season, Team team) {
		this.setSeason(season);
		this.setTeam(team);
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
	public CharSequence getOrientation() {
		return orientation;
	}
	public void setOrientation(CharSequence orientation) {
		this.orientation = orientation;
	}
	public CharSequence getDriveTrain() {
		return driveTrain;
	}
	public void setDriveTrain(CharSequence driveTrain) {
		this.driveTrain = driveTrain;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(CharSequence width) {
		try {
			setWidth(Integer.parseInt(width.toString()));
		} catch (Exception e) {
			setWidth(0);
		}
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(CharSequence length) {
		try {
			setLength(Integer.parseInt(length.toString()));
		} catch (Exception e) {
			setLength(0);
		}
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(CharSequence height) {
		try {
			setHeight(Integer.parseInt(height.toString()));
		} catch (Exception e) {
			setHeight(0);
		}
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getAutonomous() {
		return autonomous;
	}
	public void setAutonomous(CharSequence autonomous) {
		try {
			setAutonomous(Integer.parseInt(autonomous.toString()));
		} catch (Exception e) {
			setAutonomous(0);
		}
	}
	public void setAutonomous(int autonomous) {
		this.autonomous = autonomous;
	}
	public CharSequence getNotes() {
		return notes;
	}
	public void setNotes(CharSequence notes) {
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
		if(TextUtils.isEmpty(this.getOrientation())){
			values.putNull(TeamScouting.ORIENTATION);
		} else {
			values.put(TeamScouting.ORIENTATION, this.getOrientation().toString());
		}
		if(TextUtils.isEmpty(this.getDriveTrain())){
			values.putNull(TeamScouting.DRIVETRAIN);
		} else {
			values.put(TeamScouting.DRIVETRAIN, this.getDriveTrain().toString());
		}
		values.put(TeamScouting.WIDTH, this.getWidth());
		values.put(TeamScouting.LENGTH, this.getLength());
		values.put(TeamScouting.HEIGHT, this.getHeight());
		if(TextUtils.isEmpty(this.getNotes())){
			values.putNull(TeamScouting.NOTES);
		} else {
			values.put(TeamScouting.NOTES, this.getNotes().toString());
		}
		return values;
	}
	public int compareTo(TeamScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
}
