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

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class TeamScouting2014 extends TeamScouting implements Comparable<TeamScouting>  {
	public static final String TAG = TeamScouting2014.class.getSimpleName();
	private static final long serialVersionUID = 675330724134779728L;
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
	
	public static final int maxPerimeter = 112;
	public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

    @Column
	private CharSequence orientation;
    @Column
	private CharSequence driveTrain;
    @Column
	private float width;
    @Column
	private float length;
    @Column
	private float heightShooter;
    @Column
	private float heightMax;
    @Column
	private int shooterType;
    @Column
	private boolean lowGoal;
    @Column
	private boolean highGoal;
    @Column
    private boolean hotGoal;
    @Column
    private float shootingDistance;
    @Column
    private boolean passGround;
    @Column
    private boolean passAir;
    @Column
    private boolean passTruss;
    @Column
    private boolean pickupGround;
    @Column
    private boolean pickupCatch;
    @Column
    private boolean pusher;
    @Column
	private boolean blocker;
    @Column
    private float humanPlayer;
    @Column
    private int autoMode;

	public TeamScouting2014() {
		super();
	}
    public TeamScouting2014(long id) {
        super(id);
    }
	public TeamScouting2014(Team team) {
		super(team);
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
		super(id, mod, team, notes);
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
}
