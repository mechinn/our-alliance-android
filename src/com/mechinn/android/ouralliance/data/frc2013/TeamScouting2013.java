package com.mechinn.android.ouralliance.data.frc2013;

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.Utility;
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
    public static final String LOWGOAL = "lowGoal";
    public static final String MIDGOAL = "midGoal";
    public static final String HIGHGOAL = "highGoal";
    public static final String PYRAMIDGOAL = "pyramidGoal";
    public static final String RELOADSPEED = "reloadSpeed";
    public static final String SAFESHOOTER = "safeShooter";
    public static final String SLOT = "slot";
    public static final String GROUND = "ground";
	public static final String[] ALLCOLUMNS2013 = { SHOOTERTYPE, CONTINUOUSSHOOTING, COLORFRISBEE, MAXCLIMB, LOWGOAL, MIDGOAL, HIGHGOAL, PYRAMIDGOAL, RELOADSPEED, SAFESHOOTER, SLOT, GROUND };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(TeamScouting.ALLCOLUMNS, ALLCOLUMNS2013);
    
	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_SHOOTERTYPE = TABLE+SHOOTERTYPE;
    public static final String VIEW_CONTINUOUSSHOOTING = TABLE+CONTINUOUSSHOOTING;
    public static final String VIEW_COLORFRISBEE = TABLE+COLORFRISBEE;
    public static final String VIEW_MAXCLIMB = TABLE+MAXCLIMB;
    public static final String VIEW_LOWGOAL = TABLE+LOWGOAL;
    public static final String VIEW_MIDGOAL = TABLE+MIDGOAL;
    public static final String VIEW_HIGHGOAL = TABLE+HIGHGOAL;
    public static final String VIEW_PYRAMIDGOAL = TABLE+PYRAMIDGOAL;
    public static final String VIEW_RELOADSPEED = TABLE+RELOADSPEED;
    public static final String VIEW_SLOT = TABLE+SLOT;
    public static final String VIEW_GROUND = TABLE+GROUND;
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

	private int shooterType;
	private int continuousShooting;
	private boolean colorFrisbee;
	private int maxClimb;
	private boolean lowGoal;
	private boolean midGoal;
	private boolean highGoal;
	private boolean pyramidGoal;
	private float reloadSpeed;
	private boolean safeShooter;
	private boolean slot;
	private boolean ground;
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
			int shooterType,
			int continuousShooting,
			boolean colorFrisbee,
			int maxClimb,
			boolean lowGoal,
			boolean midGoal,
			boolean highGoal,
			boolean pyramidGoal,
			float reloadSpeed,
			boolean safeShooter,
			boolean slot,
			boolean ground
		) {
		super(id, mod, season, team, orientation, driveTrain, width, length, height, autonomous, notes);
		this.setShooterType(shooterType);
		this.setContinuousShooting(continuousShooting);
		this.setColorFrisbee(colorFrisbee);
		this.setMaxClimb(maxClimb);
		this.setLowGoal(lowGoal);
		this.setMidGoal(midGoal);
		this.setHighGoal(highGoal);
		this.setPyramidGoal(pyramidGoal);
		this.setReloadSpeed(reloadSpeed);
		this.setSafeShooter(safeShooter);
		this.setSlot(slot);
		this.setGround(ground);
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
	public int getShooterType() {
		return shooterType;
	}
	public void setShooterType(int shooterType) {
		this.shooterType = shooterType;
	}
	public int getContinuousShooting() {
		return continuousShooting;
	}
	public void setContinuousShooting(int continuousShooting) {
		this.continuousShooting = continuousShooting;
	}
	public boolean isColorFrisbee() {
		return colorFrisbee;
	}
	public void setColorFrisbee(boolean colorFrisbee) {
		this.colorFrisbee = colorFrisbee;
	}
	public int getMaxClimb() {
		return maxClimb;
	}
	public void setMaxClimb(int maxClimb) {
		this.maxClimb = maxClimb;
	}
	public float getReloadSpeed() {
		return reloadSpeed;
	}
	public void setReloadSpeed(float reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}
	public boolean isSafeShooter() {
		return safeShooter;
	}
	public void setSafeShooter(boolean safeShooter) {
		this.safeShooter = safeShooter;
	}
	public boolean isLowGoal() {
		return lowGoal;
	}
	public void setLowGoal(boolean lowGoal) {
		this.lowGoal = lowGoal;
	}
	public boolean isMidGoal() {
		return midGoal;
	}
	public void setMidGoal(boolean midGoal) {
		this.midGoal = midGoal;
	}
	public boolean isHighGoal() {
		return highGoal;
	}
	public void setHighGoal(boolean highGoal) {
		this.highGoal = highGoal;
	}
	public boolean isPyramidGoal() {
		return pyramidGoal;
	}
	public void setPyramidGoal(boolean pyramidGoal) {
		this.pyramidGoal = pyramidGoal;
	}
	public boolean isSlot() {
		return slot;
	}
	public void setSlot(boolean slot) {
		this.slot = slot;
	}
	public boolean isGround() {
		return ground;
	}
	public void setGround(boolean ground) {
		this.ground = ground;
	}
	public ContentValues toCV() {
		ContentValues values = super.toCV();
		values.put(TeamScouting2013.SHOOTERTYPE, this.getShooterType());
		values.put(TeamScouting2013.CONTINUOUSSHOOTING, this.getContinuousShooting());
		values.put(TeamScouting2013.COLORFRISBEE, Utility.boolToShort(this.isColorFrisbee()));
		values.put(TeamScouting2013.MAXCLIMB, this.getMaxClimb());
		values.put(TeamScouting2013.LOWGOAL, Utility.boolToShort(this.isLowGoal()));
		values.put(TeamScouting2013.MIDGOAL, Utility.boolToShort(this.isMidGoal()));
		values.put(TeamScouting2013.HIGHGOAL, Utility.boolToShort(this.isHighGoal()));
		values.put(TeamScouting2013.PYRAMIDGOAL, Utility.boolToShort(this.isPyramidGoal()));
		values.put(TeamScouting2013.RELOADSPEED, this.getReloadSpeed());
		values.put(TeamScouting2013.SAFESHOOTER, Utility.boolToShort(this.isSafeShooter()));
		values.put(TeamScouting2013.SLOT, Utility.boolToShort(this.isSlot()));
		values.put(TeamScouting2013.GROUND, Utility.boolToShort(this.isGround()));
		return values;
	}
}
