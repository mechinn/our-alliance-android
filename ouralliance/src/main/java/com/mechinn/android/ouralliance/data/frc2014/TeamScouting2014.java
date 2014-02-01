package com.mechinn.android.ouralliance.data.frc2014;

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.provider.DataProvider;

public class TeamScouting2014 extends TeamScouting implements Comparable<TeamScouting>  {
	public static final String TAG = TeamScouting2014.class.getSimpleName();
	private static final long serialVersionUID = 675330724134779728L;
	public static final String TABLE = TeamScouting.TABLE+"2013";
    public static final String ORIENTATION = "orientation";
    public static final String DRIVETRAIN = "driveTrain";
    public static final String HUMANPLAYER = "humanPlayer";
    public static final String WIDTH = "width";
    public static final String LENGTH = "length";
    public static final String HEIGHTSHOOTER = "heightShooter";
    public static final String HEIGHTMAX = "heightMax";
    public static final String MAXCLIMB = "maxClimb";
    public static final String CLIMBTIME = "climbTime";
    public static final String SHOOTERTYPE = "shooterType";
    public static final String CONTINUOUSSHOOTING = "continuousShooting";
    public static final String LOWGOAL = "lowGoal";
    public static final String MIDGOAL = "midGoal";
    public static final String HIGHGOAL = "highGoal";
    public static final String PYRAMIDGOAL = "pyramidGoal";
    public static final String AUTOMODE = "autoMode";
    public static final String SLOT = "slot";
    public static final String GROUND = "ground";
    public static final String AUTOPICKUPPYRAMID = "autoPickupPyramid";
    public static final String AUTOPICKUPCENTER = "autoPickupCenter";
    public static final String RELOADSPEED = "reloadSpeed";
    public static final String SAFESHOOTER = "safeShooter";
    public static final String LOADERSHOOTER = "loaderShooter";
    public static final String BLOCKER = "blocker";
    public static final String AUTOAVGSCORE = "autoAvgScore";
	public static final String[] ALLCOLUMNS2013 = { ORIENTATION, DRIVETRAIN, HUMANPLAYER, WIDTH, LENGTH,
		HEIGHTSHOOTER, HEIGHTMAX, MAXCLIMB, CLIMBTIME, SHOOTERTYPE, CONTINUOUSSHOOTING, LOWGOAL, MIDGOAL,
		HIGHGOAL, PYRAMIDGOAL, AUTOMODE, SLOT, GROUND, AUTOPICKUPPYRAMID, AUTOPICKUPCENTER, RELOADSPEED, SAFESHOOTER, LOADERSHOOTER, BLOCKER, AUTOAVGSCORE };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(TeamScouting.ALLCOLUMNS, ALLCOLUMNS2013);
    
	public static final String VIEW = TABLE+"view";
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(TeamScouting.VIEWCOLUMNS, ALLCOLUMNS2013);

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);
	
//	public static final String TITLE_ORIENTATION = "Orientation";
//	public static final String TITLE_DRIVETRAIN = "Drive Train";
//	public static final String TITLE_HUMANPLAYER = "Human Player";
//	public static final String TITLE_WIDTH = "Width";
//	public static final String TITLE_LENGTH = "Length";
//	public static final String TITLE_HEIGHTSHOOTER = "Height Shooter";
//	public static final String TITLE_HEIGHTMAX = "Height Max";
//	public static final String TITLE_MAXCLIMB = "Max Climb";
//	public static final String TITLE_CLIMBTIME = "Climb Time";
//	public static final String TITLE_SHOOTERTYPE = "Shooter Type";
//	public static final String TITLE_CONTINUOUSSHOOTING = "ContinuousShooting";
//	public static final String TITLE_LOWGOAL = "Low Goal";
//	public static final String TITLE_MIDGOAL = "Mid Goal";
//	public static final String TITLE_HIGHGOAL = "High Goal";
//	public static final String TITLE_PYRAMIDGOAL = "Pyramid Goal";
//	public static final String TITLE_AUTOMODE = "Auto Mode";
//	public static final String TITLE_SLOT = "Slot";
//	public static final String TITLE_GROUND = "Ground";
//	public static final String TITLE_AUTOPICKUP = "Auto Pickup";
//	public static final String TITLE_RELOADSPEED = "Reload Speed";
//	public static final String TITLE_SAFESHOOTER = "Safe Shooter";
//	public static final String TITLE_LOADERSHOOTER = "Loader Shooter";
//	public static final String TITLE_BLOCKER = "Blocker";
	
	public static final int maxPerimeter = 112;
	public static final int maxHeight = 84;

	private CharSequence orientation;
	private CharSequence driveTrain;
	private float humanPlayer;
	private float width;
	private float length;
	private float heightShooter;
	private float heightMax;
	private int maxClimb;
	private float climbTime;
	private int shooterType;
	private int continuousShooting;
	private boolean lowGoal;
	private boolean midGoal;
	private boolean highGoal;
	private boolean pyramidGoal;
	private int autoMode;
	private boolean slot;
	private boolean ground;
	private boolean autoPickupPyramid;
	private boolean autoPickupCenter;
	private float reloadSpeed;
	private boolean safeShooter;
	private boolean loaderShooter;
	private boolean blocker;
	private float autoAvgScore;
	public TeamScouting2014() {
		super();
	}
	public TeamScouting2014(Season season, Team team) {
		super(season, team);
	}
	public TeamScouting2014(
            long id,
            Date mod,
            Season season,
            Team team,
            CharSequence notes,
            CharSequence orientation,
            CharSequence driveTrain,
            float humanPlayer,
            float width,
            float length,
            float heightShooter,
            float heightMax,
            int maxClimb,
            float climbTime,
            int shooterType,
            int continuousShooting,
            boolean lowGoal,
            boolean midGoal,
            boolean highGoal,
            boolean pyramidGoal,
            int autoMode,
            boolean slot,
            boolean ground,
            boolean autoPickupPyramid,
            boolean autoPickupCenter,
            float reloadSpeed,
            boolean safeShooter,
            boolean loaderShooter,
            boolean blocker,
            float autoAvgScore
    ) {
		super(id, mod, season, team, notes);
		setOrientation(orientation);
		setDriveTrain(driveTrain);
		setHumanPlayer(humanPlayer);
		setWidth(width);
		setLength(length);
		setHeightShooter(heightShooter);
		setHeightMax(heightMax);
		setMaxClimb(maxClimb);
		setClimbTime(climbTime);
		setShooterType(shooterType);
		setContinuousShooting(continuousShooting);
		setLowGoal(lowGoal);
		setMidGoal(midGoal);
		setHighGoal(highGoal);
		setPyramidGoal(pyramidGoal);
		setAutoMode(autoMode);
		setSlot(slot);
		setGround(ground);
		setAutoPickupPyramid(autoPickupPyramid);
		setAutoPickupCenter(autoPickupCenter);
		setReloadSpeed(reloadSpeed);
		setSafeShooter(safeShooter);
		setLoaderShooter(loaderShooter);
		setBlocker(blocker);
		setAutoAvgScore(autoAvgScore);
	}
	public CharSequence getOrientation() {
		if(null==orientation) {
			return "";
		}
		return orientation;
	}
	public void setOrientation(CharSequence orientation) {
		this.orientation = orientation;
	}
	public CharSequence getDriveTrain() {
		if(null==driveTrain) {
			return "";
		}
		return driveTrain;
	}
	public void setDriveTrain(CharSequence driveTrain) {
		this.driveTrain = driveTrain;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public float getHeightShooter() {
		return heightShooter;
	}
	public void setHeightShooter(float heightShooter) {
		this.heightShooter = heightShooter;
	}
	public float getHeightMax() {
		return heightMax;
	}
	public void setHeightMax(float heightMax) {
		this.heightMax = heightMax;
	}
	public float getHumanPlayer() {
		return humanPlayer;
	}
	public void setHumanPlayer(float humanPlayer) {
		this.humanPlayer = humanPlayer;
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
	public float getClimbTime() {
		return climbTime;
	}
	public void setClimbTime(float climbTime) {
		this.climbTime = climbTime;
	}
	public int getAutoMode() {
		return autoMode;
	}
	public void setAutoMode(int autoMode) {
		this.autoMode = autoMode;
	}
	public boolean isAutoPickupPyramid() {
		return autoPickupPyramid;
	}
	public void setAutoPickupPyramid(boolean autoPickupPyramid) {
		this.autoPickupPyramid = autoPickupPyramid;
	}
	public boolean isAutoPickupCenter() {
		return autoPickupCenter;
	}
	public void setAutoPickupCenter(boolean autoPickupCenter) {
		this.autoPickupCenter = autoPickupCenter;
	}
	public boolean isLoaderShooter() {
		return loaderShooter;
	}
	public void setLoaderShooter(boolean loaderShooter) {
		this.loaderShooter = loaderShooter;
	}
	public boolean isBlocker() {
		return blocker;
	}
	public void setBlocker(boolean blocker) {
		this.blocker = blocker;
	}
	public float getAutoAvgScore() {
		return autoAvgScore;
	}
	public void setAutoAvgScore(float autoAvgScore) {
		this.autoAvgScore = autoAvgScore;
	}
	public String toString() {
		return	"ID: "+this.getId()+
				" Mod: "+this.getModified()+
				" Notes: "+this.getNotes()+
				" Orientation: "+this.getOrientation()+
				" Drive Train: "+this.getDriveTrain()+
				" Human Player: "+this.getHumanPlayer()+
				" Width: "+this.getWidth()+
				" Length: "+this.getLength()+
				" Height Shooter: "+this.getHeightShooter()+
				" Height Max: "+this.getHeightMax()+
				" Max Climb: "+this.getMaxClimb()+
				" Climb Time: "+this.getClimbTime()+
				" Shooter Type: "+this.getShooterType()+
				" Continuous Shooter: "+this.getContinuousShooting()+
				" Low Goal: "+this.isLowGoal()+
				" Middle Goal: "+this.isMidGoal()+
				" High Goal: "+this.isHighGoal()+
				" Pyramid Goal: "+this.isPyramidGoal()+
				" Autonomous Mode: "+this.getAutoMode()+
				" Get from Slot: "+this.isSlot()+
				" Get from Ground: "+this.isGround()+
				" Autonomous Pickup Pyramid: "+this.isAutoPickupPyramid()+
				" Autonomous Pickup Center: "+this.isAutoPickupCenter()+
				" Safe zone shooter: "+this.isSafeShooter()+
				" Loading Station Shooter: "+this.isLoaderShooter()+
				" Blocker: "+this.isBlocker()+
				" Auto Avg Score: "+this.getAutoAvgScore();
	}
	public boolean equals(TeamScouting2014 data) {
		return super.equals(data) &&
				getOrientation().equals(data.getOrientation()) &&
				getDriveTrain().equals(data.getDriveTrain()) &&
				getHumanPlayer()==data.getHumanPlayer() &&
				getWidth()==data.getWidth() &&
				getLength()==data.getLength() &&
				getHeightShooter()==data.getHeightShooter() &&
				getHeightMax()==data.getHeightMax() &&
				getMaxClimb()==data.getMaxClimb() &&
				getClimbTime()==data.getClimbTime() &&
				getShooterType()==data.getShooterType() &&
				getContinuousShooting()==data.getContinuousShooting() &&
				isLowGoal()==data.isLowGoal() &&
				isMidGoal()==data.isMidGoal() &&
				isHighGoal()==data.isHighGoal() &&
				isPyramidGoal()==data.isPyramidGoal() &&
				getAutoMode()==data.getAutoMode() &&
				isSlot()==data.isSlot() &&
				isGround()==data.isGround() &&
				isAutoPickupPyramid()==data.isAutoPickupPyramid() &&
				isAutoPickupCenter()==data.isAutoPickupCenter() &&
				getReloadSpeed()==data.getReloadSpeed() &&
				isSafeShooter()==data.isSafeShooter() &&
				isLoaderShooter()==data.isLoaderShooter() &&
				isBlocker()==data.isBlocker() &&
				getAutoAvgScore()==data.getAutoAvgScore();
	}
	
	public ContentValues toCV() {
		ContentValues values = super.toCV();
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
		values.put(HUMANPLAYER, this.getHumanPlayer());
		values.put(WIDTH, this.getWidth());
		values.put(LENGTH, this.getLength());
		values.put(HEIGHTSHOOTER, this.getHeightShooter());
		values.put(HEIGHTMAX, this.getHeightMax());
		values.put(MAXCLIMB, this.getMaxClimb());
		values.put(CLIMBTIME, this.getClimbTime());
		values.put(SHOOTERTYPE, this.getShooterType());
		values.put(CONTINUOUSSHOOTING, this.getContinuousShooting());
		values.put(LOWGOAL, Utility.boolToShort(this.isLowGoal()));
		values.put(MIDGOAL, Utility.boolToShort(this.isMidGoal()));
		values.put(HIGHGOAL, Utility.boolToShort(this.isHighGoal()));
		values.put(PYRAMIDGOAL, Utility.boolToShort(this.isPyramidGoal()));
		values.put(AUTOMODE, this.getAutoMode());
		values.put(SLOT, Utility.boolToShort(this.isSlot()));
		values.put(GROUND, Utility.boolToShort(this.isGround()));
		values.put(AUTOPICKUPPYRAMID, Utility.boolToShort(this.isAutoPickupPyramid()));
		values.put(AUTOPICKUPCENTER, Utility.boolToShort(this.isAutoPickupCenter()));
		values.put(RELOADSPEED, this.getReloadSpeed());
		values.put(SAFESHOOTER, Utility.boolToShort(this.isSafeShooter()));
		values.put(LOADERSHOOTER, Utility.boolToShort(this.isLoaderShooter()));
		values.put(BLOCKER, Utility.boolToShort(this.isBlocker()));
		values.put(AUTOAVGSCORE, this.getAutoAvgScore());
		return values;
	}

	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(ORIENTATION)));
		setDriveTrain(cursor.getString(cursor.getColumnIndexOrThrow(DRIVETRAIN)));
		setHumanPlayer(cursor.getFloat(cursor.getColumnIndexOrThrow(HUMANPLAYER)));
		setWidth(cursor.getFloat(cursor.getColumnIndexOrThrow(WIDTH)));
		setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(LENGTH)));
		setHeightShooter(cursor.getFloat(cursor.getColumnIndexOrThrow(HEIGHTSHOOTER)));
		setHeightMax(cursor.getFloat(cursor.getColumnIndexOrThrow(HEIGHTMAX)));
		setMaxClimb(cursor.getInt(cursor.getColumnIndexOrThrow(MAXCLIMB)));
		setClimbTime(cursor.getFloat(cursor.getColumnIndexOrThrow(CLIMBTIME)));
		setShooterType(cursor.getInt(cursor.getColumnIndexOrThrow(SHOOTERTYPE)));
		setContinuousShooting(cursor.getInt(cursor.getColumnIndexOrThrow(CONTINUOUSSHOOTING)));
		setLowGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(LOWGOAL))));
		setMidGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(MIDGOAL))));
		setHighGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(HIGHGOAL))));
		setPyramidGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PYRAMIDGOAL))));
		setAutoMode(cursor.getInt(cursor.getColumnIndexOrThrow(AUTOMODE)));
		setSlot(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(SLOT))));
		setGround(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(GROUND))));
		setAutoPickupPyramid(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(AUTOPICKUPPYRAMID))));
		setAutoPickupCenter(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(AUTOPICKUPCENTER))));
		setReloadSpeed(cursor.getFloat(cursor.getColumnIndexOrThrow(RELOADSPEED)));
		setSafeShooter(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(SAFESHOOTER))));
		setLoaderShooter(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(LOADERSHOOTER))));
		setBlocker(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(BLOCKER))));
		setAutoAvgScore(cursor.getFloat(cursor.getColumnIndexOrThrow(AUTOAVGSCORE)));
	}

	@Override
	public String[] toStringArray() {
		return ArrayUtils.addAll(super.toStringArray(),
				getOrientation().toString(),
				getDriveTrain().toString(),
				Float.toString(getHumanPlayer()),
				Float.toString(getWidth()),
				Float.toString(getLength()),
				Float.toString(getHeightShooter()),
				Float.toString(getHeightMax()),
				Integer.toString(getMaxClimb()),
				Float.toString(getClimbTime()),
				Integer.toString(getShooterType()),
				Integer.toString(getContinuousShooting()),
				Boolean.toString(isLowGoal()),
				Boolean.toString(isMidGoal()),
				Boolean.toString(isHighGoal()),
				Boolean.toString(isPyramidGoal()),
				Integer.toString(getAutoMode()),
				Boolean.toString(isSlot()),
				Boolean.toString(isGround()),
				Boolean.toString(isAutoPickupPyramid()),
				Boolean.toString(isAutoPickupCenter()),
				Float.toString(getReloadSpeed()),
				Boolean.toString(isSafeShooter()),
				Boolean.toString(isLoaderShooter()),
				Boolean.toString(isBlocker()),
				Float.toString(getAutoAvgScore()));
	}
	
	public static TeamScouting2014 newFromCursor(Cursor cursor) {
		TeamScouting2014 data = new TeamScouting2014();
		data.fromCursor(cursor);
		return data;
	}
}
