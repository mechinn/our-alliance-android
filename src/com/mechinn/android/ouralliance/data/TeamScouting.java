package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.Database;

public abstract class TeamScouting extends AOurAllianceData implements Comparable<TeamScouting>  {
	private static final long serialVersionUID = 2234995463512680398L;
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
	private float autonomous;
	private CharSequence notes;
	public TeamScouting() {
		super();
	}
	public TeamScouting(Season season, Team team) {
		this.setData(season, team);
	}
	public TeamScouting(long id, Date mod, Season season, Team team, CharSequence orientation, CharSequence driveTrain, int width, int length, int height, float autonomous, CharSequence notes) {
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
	public float getAutonomous() {
		return autonomous;
	}
	public void setAutonomous(CharSequence autonomous) {
		try {
			setAutonomous(Float.parseFloat(autonomous.toString()));
		} catch (Exception e) {
			setAutonomous(0);
		}
	}
	public void setAutonomous(float autonomous) {
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
	public boolean equals(TeamScouting data) {
		return super.equals(data) &&
				getSeason().equals(data.getSeason()) &&
				getTeam().equals(data.getTeam()) &&
				getOrientation().equals(data.getOrientation()) &&
				getDriveTrain().equals(data.getDriveTrain()) &&
				getWidth()==data.getWidth() &&
				getLength()==data.getLength() &&
				getHeight()==data.getHeight() &&
				getAutonomous()==data.getAutonomous() &&
				getNotes().equals(data.getNotes());
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(SEASON, this.getSeason().getId());
		values.put(TEAM, this.getTeam().getId());
		if(TextUtils.isEmpty(this.getOrientation())){
			values.putNull(ORIENTATION);
		} else {
			values.put(ORIENTATION, this.getOrientation().toString());
		}
		if(TextUtils.isEmpty(this.getDriveTrain())){
			values.putNull(DRIVETRAIN);
		} else {
			values.put(DRIVETRAIN, this.getDriveTrain().toString());
		}
		values.put(WIDTH, this.getWidth());
		values.put(LENGTH, this.getLength());
		values.put(HEIGHT, this.getHeight());
		values.put(AUTONOMOUS, this.getAutonomous());
		if(TextUtils.isEmpty(this.getNotes())){
			values.putNull(NOTES);
		} else {
			values.put(NOTES, this.getNotes().toString());
		}
		return values;
	}
	public int compareTo(TeamScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==this.getSeason()) {
			error.add(SEASON);
		}
		if(null==this.getTeam()) {
			error.add(TEAM);
		}
		if(TextUtils.isEmpty(this.getOrientation())) {
//			error.add(ORIENTATION);
		}
		if(TextUtils.isEmpty(this.getDriveTrain())) {
//			error.add(DRIVETRAIN);
		}
		if(0==this.getWidth()) {
//			error.add(WIDTH);
		}
		if(0==this.getLength()) {
//			error.add(LENGTH);
		}
		if(0==this.getHeight()) {
//			error.add(HEIGHT);
		}
		if(0==this.getAutonomous()) {
//			error.add(AUTONOMOUS);
		}
		if(TextUtils.isEmpty(this.getNotes())) {
//			error.add(NOTES);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setSeason(Season.newFromViewCursor(cursor));
		setTeam(Team.newFromViewCursor(cursor));
		setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(ORIENTATION)));
		setDriveTrain(cursor.getString(cursor.getColumnIndexOrThrow(DRIVETRAIN)));
		setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(WIDTH)));
		setLength(cursor.getInt(cursor.getColumnIndexOrThrow(LENGTH)));
		setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(HEIGHT)));
		setAutonomous(cursor.getFloat(cursor.getColumnIndexOrThrow(AUTONOMOUS)));
		setNotes(cursor.getString(cursor.getColumnIndexOrThrow(NOTES)));
	}
	
	public static String orientationFromViewCursor(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(VIEW_ORIENTATION));
	}
	
	public static String driveTrainFromViewCursor(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(VIEW_DRIVETRAIN));
	}
	
	public static int widthFromViewCursor(Cursor cursor) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_WIDTH));
	}
	
	public static int lengthFromViewCursor(Cursor cursor) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_LENGTH));
	}
	
	public static int heightFromViewCursor(Cursor cursor) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_HEIGHT));
	}
	
	public static float autonomousFromViewCursor(Cursor cursor) {
		return cursor.getFloat(cursor.getColumnIndexOrThrow(VIEW_AUTONOMOUS));
	}
	
	public static String notesFromViewCursor(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(VIEW_NOTES));
	}
}
