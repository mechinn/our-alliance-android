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
	public static final String TABLE = TeamScouting.TABLE+"2014";
    public static final String ORIENTATION = "orientation";
    public static final String DRIVETRAIN = "driveTrain";
    public static final String WIDTH = "width";
    public static final String LENGTH = "length";
    public static final String HEIGHTSHOOTER = "heightShooter";
    public static final String HEIGHTMAX = "heightMax";
    public static final String SHOOTERTYPE = "shooterType";
    public static final String LOWGOAL = "lowGoal";
    public static final String HIGHGOAL = "highGoal";
    public static final String HOTGOAL = "hotGoal";
    public static final String SHOOTINGDISTANCE = "shootingDistance";
    public static final String PASSGROUND = "passGround";
    public static final String PASSAIR = "passAir";
    public static final String PASSTRUSS = "passTruss";
    public static final String PICKUPGROUND = "pickupGround";
    public static final String PICKUPCATCH = "pickupCatch";
    public static final String PUSHER = "pusher";
    public static final String BLOCKER = "blocker";
    public static final String HUMANPLAYER = "humanPlayer";
    public static final String AUTOMODE = "autoMode";
	public static final String[] ALLCOLUMNS2014 = { ORIENTATION, DRIVETRAIN, WIDTH, LENGTH,
		HEIGHTSHOOTER, HEIGHTMAX, SHOOTERTYPE, LOWGOAL, HIGHGOAL, HOTGOAL, SHOOTINGDISTANCE, PASSGROUND,
            PASSAIR, PASSTRUSS, PICKUPGROUND, PICKUPCATCH, PUSHER, BLOCKER, HUMANPLAYER, AUTOMODE };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(TeamScouting.ALLCOLUMNS, ALLCOLUMNS2014);
    
	public static final String VIEW = TABLE+"view";
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(TeamScouting.VIEWCOLUMNS, ALLCOLUMNS2014);

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);
	
	public static final int maxPerimeter = 112;
	public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

	private CharSequence orientation;
	private CharSequence driveTrain;
	private float width;
	private float length;
	private float heightShooter;
	private float heightMax;
	private int shooterType;
	private boolean lowGoal;
	private boolean highGoal;
    private boolean hotGoal;
    private float shootingDistance;
    private boolean passGround;
    private boolean passAir;
    private boolean passTruss;
    private boolean pickupGround;
    private boolean pickupCatch;
    private boolean pusher;
	private boolean blocker;
    private float humanPlayer;
    private int autoMode;
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
            float width,
            float length,
            float heightShooter,
            float heightMax,
            int shooterType,
            boolean lowGoal,
            boolean highGoal,
            boolean hotGoal,
            float shootingDistance,
            boolean passGround,
            boolean passAir,
            boolean passTruss,
            boolean pickupGround,
            boolean pickupCatch,
            boolean pusher,
            boolean blocker,
            float humanPlayer,
            int autoMode
    ) {
		super(id, mod, season, team, notes);
		setOrientation(orientation);
		setDriveTrain(driveTrain);
		setWidth(width);
		setLength(length);
		setHeightShooter(heightShooter);
		setHeightMax(heightMax);
		setShooterType(shooterType);
		setLowGoal(lowGoal);
		setHighGoal(highGoal);
        setHotGoal(hotGoal);
        setShootingDistance(shootingDistance);
        setPassGround(passGround);
        setPassAir(passAir);
        setPassTruss(passTruss);
        setPickupGround(pickupGround);
        setPickupCatch(pickupCatch);
        setPusher(pusher);
		setBlocker(blocker);
        setHumanPlayer(humanPlayer);
        setAutoMode(autoMode);
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
	public int getShooterType() {
		return shooterType;
	}
	public void setShooterType(int shooterType) {
		this.shooterType = shooterType;
	}
	public boolean isLowGoal() {
		return lowGoal;
	}
	public void setLowGoal(boolean lowGoal) {
		this.lowGoal = lowGoal;
	}
	public boolean isHighGoal() {
		return highGoal;
	}
	public void setHighGoal(boolean highGoal) {
		this.highGoal = highGoal;
	}
    public boolean isHotGoal() {
        return hotGoal;
    }
    public void setHotGoal(boolean hotGoal) {
        this.hotGoal = hotGoal;
    }
    public float getShootingDistance() {
        return shootingDistance;
    }
    public void setShootingDistance(float shootingDistance) {
        this.shootingDistance = shootingDistance;
    }
	public boolean isPassGround() {
		return passGround;
	}
	public void setPassGround(boolean passGround) {
		this.passGround = passGround;
	}
    public boolean isPassAir() {
        return passAir;
    }
    public void setPassAir(boolean passAir) {
        this.passAir = passAir;
    }
    public boolean isPassTruss() {
        return passTruss;
    }
    public void setPassTruss(boolean passTruss) {
        this.passTruss = passTruss;
    }
    public boolean isPickupGround() {
        return pickupGround;
    }
    public void setPickupGround(boolean pickupGround) {
        this.pickupGround = pickupGround;
    }
    public boolean isPickupCatch() {
        return pickupCatch;
    }
    public void setPickupCatch(boolean pickupCatch) {
        this.pickupCatch = pickupCatch;
    }
    public boolean isPusher() {
        return pusher;
    }
    public void setPusher(boolean pusher) {
        this.pusher = pusher;
    }
    public boolean isBlocker() {
        return blocker;
    }
    public void setBlocker(boolean blocker) {
        this.blocker = blocker;
    }
    public float getHumanPlayer() {
        return humanPlayer;
    }
    public void setHumanPlayer(float humanPlayer) {
        this.humanPlayer = humanPlayer;
    }
    public int getAutoMode() {
        return autoMode;
    }
    public void setAutoMode(int autoMode) {
        this.autoMode = autoMode;
    }
	public String toString() {
		return	"ID: "+this.getId()+
				" Mod: "+this.getModified()+
				" Notes: "+this.getNotes()+
				" Orientation: "+this.getOrientation()+
				" Drive Train: "+this.getDriveTrain()+
				" Width: "+this.getWidth()+
				" Length: "+this.getLength()+
				" Height Shooter: "+this.getHeightShooter()+
				" Height Max: "+this.getHeightMax()+
                " Shooter Type: "+this.getShooterType()+
                " Low Goal: "+this.isLowGoal()+
                " High Goal: "+this.isHighGoal()+
                " Hot Goal: "+this.isHotGoal()+
                " Shooting Distance: "+this.getShootingDistance()+
                " Pass Ground: "+this.isPassGround()+
                " Pass Air: "+this.isPassAir()+
                " Pass Truss: "+this.isPassTruss()+
                " Pickup Ground: "+this.isPickupGround()+
                " Pickup Catch: "+this.isPickupCatch()+
                " Pusher: "+this.isPusher()+
				" Blocker: "+this.isBlocker()+
                " Human Player: "+this.getHumanPlayer()+
                " Autonomous Mode: "+this.getAutoMode();
	}
	public boolean equals(TeamScouting2014 data) {
		return super.equals(data) &&
				getOrientation().equals(data.getOrientation()) &&
				getDriveTrain().equals(data.getDriveTrain()) &&
				getWidth()==data.getWidth() &&
				getLength()==data.getLength() &&
				getHeightShooter()==data.getHeightShooter() &&
				getHeightMax()==data.getHeightMax() &&
				getShooterType()==data.getShooterType() &&
				isLowGoal()==data.isLowGoal() &&
				isHighGoal()==data.isHighGoal() &&
                isHotGoal()==data.isHotGoal() &&
                getShootingDistance()==data.getShootingDistance() &&
                isPassGround()==data.isPassGround() &&
                isPassAir()==data.isPassAir() &&
                isPassTruss()==data.isPassTruss() &&
                isPickupGround()==data.isPickupGround() &&
                isPickupCatch()==data.isPickupCatch() &&
                isPusher()==data.isPusher() &&
				isBlocker()==data.isBlocker() &&
                getHumanPlayer()==data.getHumanPlayer() &&
                getAutoMode()==data.getAutoMode();
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
		values.put(WIDTH, this.getWidth());
		values.put(LENGTH, this.getLength());
		values.put(HEIGHTSHOOTER, this.getHeightShooter());
		values.put(HEIGHTMAX, this.getHeightMax());
		values.put(SHOOTERTYPE, this.getShooterType());
		values.put(LOWGOAL, Utility.boolToShort(this.isLowGoal()));
		values.put(HIGHGOAL, Utility.boolToShort(this.isHighGoal()));
        values.put(HOTGOAL, Utility.boolToShort(this.isHotGoal()));
        values.put(SHOOTINGDISTANCE, this.getShootingDistance());
        values.put(PASSGROUND, Utility.boolToShort(this.isPassGround()));
        values.put(PASSAIR, Utility.boolToShort(this.isPassAir()));
        values.put(PASSTRUSS, Utility.boolToShort(this.isPassTruss()));
        values.put(PICKUPGROUND, Utility.boolToShort(this.isPickupGround()));
        values.put(PICKUPCATCH, Utility.boolToShort(this.isPickupCatch()));
        values.put(PUSHER, Utility.boolToShort(this.isPusher()));
		values.put(BLOCKER, Utility.boolToShort(this.isBlocker()));
        values.put(HUMANPLAYER, this.getHumanPlayer());
        values.put(AUTOMODE, this.getAutoMode());
		return values;
	}

	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setOrientation(cursor.getString(cursor.getColumnIndexOrThrow(ORIENTATION)));
		setDriveTrain(cursor.getString(cursor.getColumnIndexOrThrow(DRIVETRAIN)));
		setWidth(cursor.getFloat(cursor.getColumnIndexOrThrow(WIDTH)));
		setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(LENGTH)));
		setHeightShooter(cursor.getFloat(cursor.getColumnIndexOrThrow(HEIGHTSHOOTER)));
		setHeightMax(cursor.getFloat(cursor.getColumnIndexOrThrow(HEIGHTMAX)));
		setShooterType(cursor.getInt(cursor.getColumnIndexOrThrow(SHOOTERTYPE)));
		setLowGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(LOWGOAL))));
        setHotGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(HIGHGOAL))));
        setHighGoal(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(HOTGOAL))));
        setShootingDistance(cursor.getFloat(cursor.getColumnIndexOrThrow(SHOOTINGDISTANCE)));
        setPassGround(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PASSGROUND))));
        setPassAir(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PASSAIR))));
        setPassTruss(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PASSTRUSS))));
        setPickupGround(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PICKUPGROUND))));
        setPickupCatch(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PICKUPCATCH))));
        setPusher(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(PUSHER))));
		setBlocker(Utility.shortToBool(cursor.getShort(cursor.getColumnIndexOrThrow(BLOCKER))));
        setHumanPlayer(cursor.getFloat(cursor.getColumnIndexOrThrow(HUMANPLAYER)));
        setAutoMode(cursor.getInt(cursor.getColumnIndexOrThrow(AUTOMODE)));
	}

	@Override
	public String[] toStringArray() {
		return ArrayUtils.addAll(super.toStringArray(),
				getOrientation().toString(),
				getDriveTrain().toString(),
				Float.toString(getWidth()),
				Float.toString(getLength()),
				Float.toString(getHeightShooter()),
				Float.toString(getHeightMax()),
				Integer.toString(getShooterType()),
				Boolean.toString(isLowGoal()),
                Boolean.toString(isHighGoal()),
                Boolean.toString(isHotGoal()),
                Float.toString(getShootingDistance()),
                Boolean.toString(isPassGround()),
                Boolean.toString(isPassAir()),
                Boolean.toString(isPassTruss()),
                Boolean.toString(isPickupGround()),
                Boolean.toString(isPickupCatch()),
                Boolean.toString(isPusher()),
				Boolean.toString(isBlocker()),
                Float.toString(getHumanPlayer()),
                Integer.toString(getAutoMode()));
	}
	
	public static TeamScouting2014 newFromCursor(Cursor cursor) {
		TeamScouting2014 data = new TeamScouting2014();
		data.fromCursor(cursor);
		return data;
	}
}
