package com.mechinn.android.ouralliance.data.frc2013;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class TeamScouting2013 extends TeamScouting implements Comparable<TeamScouting>  {
	private static final long serialVersionUID = 675330724134779728L;
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
    public static final String VIEW_SAFESHOOTER = TABLE+SAFESHOOTER;
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(TeamScouting.VIEWCOLUMNS, ALLCOLUMNS2013);

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);

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
		super(season, team);
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
			float autonomous, 
			CharSequence notes, 
			int maxClimb,
			int shooterType,
			int continuousShooting,
			boolean colorFrisbee,
			boolean lowGoal,
			boolean midGoal,
			boolean highGoal,
			boolean pyramidGoal,
			boolean slot,
			boolean ground,
			float reloadSpeed,
			boolean safeShooter
		) {
		super(id, mod, season, team, orientation, driveTrain, width, length, height, autonomous, notes);
		setMaxClimb(maxClimb);
		setShooterType(shooterType);
		setContinuousShooting(continuousShooting);
		setColorFrisbee(colorFrisbee);
		setLowGoal(lowGoal);
		setMidGoal(midGoal);
		setHighGoal(highGoal);
		setPyramidGoal(pyramidGoal);
		setSlot(slot);
		setGround(ground);
		setReloadSpeed(reloadSpeed);
		setSafeShooter(safeShooter);
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
		values.put(SHOOTERTYPE, this.getShooterType());
		values.put(CONTINUOUSSHOOTING, this.getContinuousShooting());
		values.put(COLORFRISBEE, Utility.boolToShort(this.isColorFrisbee()));
		values.put(MAXCLIMB, this.getMaxClimb());
		values.put(LOWGOAL, Utility.boolToShort(this.isLowGoal()));
		values.put(MIDGOAL, Utility.boolToShort(this.isMidGoal()));
		values.put(HIGHGOAL, Utility.boolToShort(this.isHighGoal()));
		values.put(PYRAMIDGOAL, Utility.boolToShort(this.isPyramidGoal()));
		values.put(RELOADSPEED, this.getReloadSpeed());
		values.put(SAFESHOOTER, Utility.boolToShort(this.isSafeShooter()));
		values.put(SLOT, Utility.boolToShort(this.isSlot()));
		values.put(GROUND, Utility.boolToShort(this.isGround()));
		return values;
	}
	@Override
	public List<String> checkNotNulls() {
		List<String> error = super.checkNotNulls();
		if(0==this.getShooterType()) {
//			throw new OurAllianceException(SHOOTERTYPE);
		}
		if(0==this.getContinuousShooting()) {
//			throw new OurAllianceException(CONTINUOUSSHOOTING);
		}
		if(!this.isColorFrisbee()) {
//			throw new OurAllianceException(COLORFRISBEE);
		}
		if(0==this.getMaxClimb()) {
//			throw new OurAllianceException(MAXCLIMB);
		}
		if(!this.isLowGoal()) {
//			throw new OurAllianceException(LOWGOAL);
		}
		if(!this.isMidGoal()) {
//			throw new OurAllianceException(MIDGOAL);
		}
		if(!this.isHighGoal()) {
//			throw new OurAllianceException(HIGHGOAL);
		}
		if(!this.isPyramidGoal()) {
//			throw new OurAllianceException(PYRAMIDGOAL);
		}
		if(0==this.getReloadSpeed()) {
//			throw new OurAllianceException(RELOADSPEED);
		}
		if(!this.isSafeShooter()) {
//			throw new OurAllianceException(SAFESHOOTER);
		}
		if(!this.isSlot()) {
//			throw new OurAllianceException(SLOT);
		}
		if(!this.isGround()) {
//			throw new OurAllianceException(GROUND);
		}
		return error;
	}

	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setMaxClimb(cursor.getInt(cursor.getColumnIndexOrThrow(MAXCLIMB)));
		setShooterType(cursor.getInt(cursor.getColumnIndexOrThrow(SHOOTERTYPE)));
		setContinuousShooting(cursor.getInt(cursor.getColumnIndexOrThrow(CONTINUOUSSHOOTING)));
		setLowGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(LOWGOAL))));
		setMidGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(MIDGOAL))));
		setHighGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(HIGHGOAL))));
		setPyramidGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PYRAMIDGOAL))));
		setSlot(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(SLOT))));
		setGround(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(GROUND))));
		setReloadSpeed(cursor.getFloat(cursor.getColumnIndexOrThrow(RELOADSPEED)));
		setSafeShooter(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(SAFESHOOTER))));
		setColorFrisbee(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(COLORFRISBEE))));
	}
	
	public static TeamScouting2013 newFromCursor(Cursor cursor) {
		TeamScouting2013 data = new TeamScouting2013();
		data.fromCursor(cursor);
		return data;
	}
	
	public static TeamScouting2013 newFromViewCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		Season season = Season.newFromViewCursor(cursor);
		Team team = Team.newFromViewCursor(cursor);
		String orientation = TeamScouting.orientationFromViewCursor(cursor);
		String driveTrain = TeamScouting.driveTrainFromViewCursor(cursor);
		int width = TeamScouting.widthFromViewCursor(cursor);
		int length = TeamScouting.lengthFromViewCursor(cursor);
		int height = TeamScouting.heightFromViewCursor(cursor);
		float autonomous = TeamScouting.autonomousFromViewCursor(cursor);
		String notes = TeamScouting.notesFromViewCursor(cursor);
		int maxClimb = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_MAXCLIMB));
		int shooterType = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_SHOOTERTYPE));
		int continuousShooting = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_CONTINUOUSSHOOTING));
		boolean lowGoal = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_LOWGOAL)));
		boolean midGoal = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_MIDGOAL)));
		boolean highGoal = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_HIGHGOAL)));
		boolean pyramidGoal = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_PYRAMIDGOAL)));
		boolean slot = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_SLOT)));
		boolean ground = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_GROUND)));
		float reloadSpeed = cursor.getFloat(cursor.getColumnIndexOrThrow(VIEW_RELOADSPEED));
		boolean safeShooter = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_SAFESHOOTER)));
		boolean colorFrisbee = Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(VIEW_COLORFRISBEE)));
		return new TeamScouting2013(id, mod, season, team, orientation, driveTrain, width, length, height, autonomous, notes, maxClimb, shooterType, continuousShooting, colorFrisbee, lowGoal, midGoal, highGoal, pyramidGoal, slot, ground, reloadSpeed, safeShooter);
	}
}
