package com.mechinn.android.ouralliance.data.frc2013;

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class TeamScouting2013 extends TeamScouting implements Comparable<TeamScouting>  {
	public static final String CLASS = TeamScouting.CLASS+"2013";
	public static final String TABLE = TeamScouting.TABLE+"2013";
    public static final String SHOOTERTYPE = "shooterType";
    public static final String CONTINUOUSSHOOTING = "continuousShooting";
    public static final String COLORFRISBEE = "colorFrisbee";
    public static final String MAXCLIMB = "maxClimb";
    public static final String SHOOTABLEGOALS = "shootableGoals";
    public static final String RELOADSPEED = "reloadSpeed";
    public static final String SAFESHOOTER = "safeShooter";
    public static final String LOADSFROM = "loadsFrom";
	public static final String[] ALLCOLUMNS2013 = { SHOOTERTYPE, CONTINUOUSSHOOTING, COLORFRISBEE, MAXCLIMB, SHOOTABLEGOALS, RELOADSPEED, SAFESHOOTER, LOADSFROM };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(TeamScouting.ALLCOLUMNS, ALLCOLUMNS2013);
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SHOOTERTYPE = TABLE+SHOOTERTYPE;
    public static final String VIEW_CONTINUOUSSHOOTING = TABLE+CONTINUOUSSHOOTING;
    public static final String VIEW_COLORFRISBEE = TABLE+COLORFRISBEE;
    public static final String VIEW_MAXCLIMB = TABLE+MAXCLIMB;
    public static final String VIEW_SHOOTABLEGOALS = TABLE+SHOOTABLEGOALS;
    public static final String VIEW_RELOADSPEED = TABLE+RELOADSPEED;
    public static final String VIEW_SAFESHOOTER = TABLE+SAFESHOOTER;
    public static final String VIEW_LOADSFROM = TABLE+LOADSFROM;
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(TeamScouting.VIEWCOLUMNS, ALLCOLUMNS2013);

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String SEASONPATH = PATH+Season.TABLE+"/";
	public static final String TEAMADDON = "/"+Team.TABLE+"/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_SEASON = DataProvider.BASE_URI_STRING+SEASONPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private CharSequence shooterType;
	private CharSequence continuousShooting;
	private CharSequence colorFrisbee;
	private int maxClimb;
	private int shootableGoals;
	private int reloadSpeed;
	private CharSequence safeShooter;
	private CharSequence loadsFrom;
	public TeamScouting2013() {
		super();
	}
	public TeamScouting2013(Season season, Team team) {
		super.setData(season, team);
	}
	public TeamScouting2013(
			long id, 
			Date mod, 
			Season season, 
			Team team, 
			CharSequence orientation, 
			CharSequence driveTrain, 
			int width, 
			int length, 
			int height, 
			int autonomous, 
			CharSequence notes, 
			CharSequence shooterType,
			CharSequence continuousShooting,
			CharSequence colorFrisbee,
			int maxClimb,
			int shootableGoals,
			int reloadSpeed,
			CharSequence safeShooter,
			CharSequence loadsFrom
		) {
		super(id, mod, season, team, orientation, driveTrain, width, length, height, autonomous, notes);
		this.setShooterType(shooterType);
		this.setContinuousShooting(continuousShooting);
		this.setColorFrisbee(colorFrisbee);
		this.setMaxClimb(maxClimb);
		this.setShootableGoals(shootableGoals);
		this.setReloadSpeed(reloadSpeed);
		this.setSafeShooter(safeShooter);
		this.setLoadsFrom(loadsFrom);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(TeamScouting id) {
		return uriFromId(id.getId());
	}
	public static Uri uriFromSeason(Season season) {
		return uriFromSeason(season.getId());
	}
	public static Uri uriFromSeason(long season) {
		return Uri.parse(URI_SEASON + season);
	}
	public static Uri uriFromSeasonTeam(Season season, Team team) {
		return uriFromSeasonTeam(season.getId(), team.getId());
	}
	public static Uri uriFromSeasonTeam(long season, long team) {
		return Uri.parse(URI_SEASON + season + TEAMADDON + team);
	}
	public CharSequence getShooterType() {
		return shooterType;
	}
	public void setShooterType(CharSequence shooterType) {
		this.shooterType = shooterType;
	}
	public CharSequence getContinuousShooting() {
		return continuousShooting;
	}
	public void setContinuousShooting(CharSequence continuousShooting) {
		this.continuousShooting = continuousShooting;
	}
	public CharSequence getColorFrisbee() {
		return colorFrisbee;
	}
	public void setColorFrisbee(CharSequence colorFrisbee) {
		this.colorFrisbee = colorFrisbee;
	}
	public int getMaxClimb() {
		return maxClimb;
	}
	public void setMaxClimb(CharSequence maxClimb) {
		try {
			setMaxClimb(Integer.parseInt(maxClimb.toString()));
		} catch (Exception e) {
			setMaxClimb(0);
		}
	}
	public void setMaxClimb(int maxClimb) {
		this.maxClimb = maxClimb;
	}
	public int getShootableGoals() {
		return shootableGoals;
	}
	public void setShootableGoals(CharSequence shootableGoals) {
		try {
			setMaxClimb(Integer.parseInt(shootableGoals.toString()));
		} catch (Exception e) {
			setMaxClimb(0);
		}
	}
	public void setShootableGoals(int shootableGoals) {
		this.shootableGoals = shootableGoals;
	}
	public int getReloadSpeed() {
		return reloadSpeed;
	}
	public void setReloadSpeed(CharSequence reloadSpeed) {
		try {
			setReloadSpeed(Integer.parseInt(reloadSpeed.toString()));
		} catch (Exception e) {
			setReloadSpeed(0);
		}
	}
	public void setReloadSpeed(int reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}
	public CharSequence getSafeShooter() {
		return safeShooter;
	}
	public void setSafeShooter(CharSequence safeShooter) {
		this.safeShooter = safeShooter;
	}
	public CharSequence getLoadsFrom() {
		return loadsFrom;
	}
	public void setLoadsFrom(CharSequence loadsFrom) {
		this.loadsFrom = loadsFrom;
	}
	public ContentValues toCV() {
		ContentValues values = super.toCV();
		if(TextUtils.isEmpty(this.getShooterType())){
			values.putNull(TeamScouting2013.SHOOTERTYPE);
		} else {
			values.put(TeamScouting2013.SHOOTERTYPE, this.getShooterType().toString());
		}
		if(TextUtils.isEmpty(this.getContinuousShooting())){
			values.putNull(TeamScouting2013.CONTINUOUSSHOOTING);
		} else {
			values.put(TeamScouting2013.CONTINUOUSSHOOTING, this.getContinuousShooting().toString());
		}
		if(TextUtils.isEmpty(this.getColorFrisbee())){
			values.putNull(TeamScouting2013.COLORFRISBEE);
		} else {
			values.put(TeamScouting2013.COLORFRISBEE, this.getColorFrisbee().toString());
		}
		values.put(TeamScouting2013.MAXCLIMB, this.getMaxClimb());
		values.put(TeamScouting2013.SHOOTABLEGOALS, this.getShootableGoals());
		values.put(TeamScouting2013.RELOADSPEED, this.getReloadSpeed());
		if(TextUtils.isEmpty(this.getSafeShooter())){
			values.putNull(TeamScouting2013.SAFESHOOTER);
		} else {
			values.put(TeamScouting2013.SAFESHOOTER, this.getSafeShooter().toString());
		}
		if(TextUtils.isEmpty(this.getLoadsFrom())){
			values.putNull(TeamScouting2013.LOADSFROM);
		} else {
			values.put(TeamScouting2013.LOADSFROM, this.getLoadsFrom().toString());
		}
		return values;
	}
}
