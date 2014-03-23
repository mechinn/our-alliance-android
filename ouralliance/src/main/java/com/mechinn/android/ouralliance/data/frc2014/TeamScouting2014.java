package com.mechinn.android.ouralliance.data.frc2014;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;

import com.mechinn.android.ouralliance.processor.FmtTeam;
import com.mechinn.android.ouralliance.processor.ParseTeam;
import org.apache.commons.lang3.ArrayUtils;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

import java.util.Collections;
import java.util.Map;

@Table(TeamScouting2014.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class TeamScouting2014 extends TeamScouting implements Comparable<TeamScouting>  {
    public static final String TAG = "TeamScouting2014";
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
	
	public static final int maxPerimeter = 112;
	public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

    @Column(ORIENTATION)
	private String orientation;
    @Column(DRIVETRAIN)
	private String driveTrain;
    @Column(WIDTH)
	private double width;
    @Column(LENGTH)
	private double length;
    @Column(HEIGHTSHOOTER)
	private double heightShooter;
    @Column(HEIGHTMAX)
	private double heightMax;
    @Column(SHOOTERTYPE)
	private int shooterType;
    @Column(LOWGOAL)
	private boolean lowGoal;
    @Column(HIGHGOAL)
	private boolean highGoal;
    @Column(HOTGOAL)
    private boolean hotGoal;
    @Column(SHOOTINGDISTANCE)
    private double shootingDistance;
    @Column(PASSGROUND)
    private boolean passGround;
    @Column(PASSAIR)
    private boolean passAir;
    @Column(PASSTRUSS)
    private boolean passTruss;
    @Column(PICKUPGROUND)
    private boolean pickupGround;
    @Column(PICKUPCATCH)
    private boolean pickupCatch;
    @Column(PUSHER)
    private boolean pusher;
    @Column(BLOCKER)
	private boolean blocker;
    @Column(HUMANPLAYER)
    private double humanPlayer;
    @Column(AUTOMODE)
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
	public String getOrientation() {
        if(null==orientation) {
            return "";
        }
        return orientation;
    }
	public void setOrientation(String orientation) {
        if(null==orientation) {
            this.orientation="";
        } else {
            this.orientation=orientation;
        }
	}
    public void setOrientation(CharSequence orientation) {
        if(null==orientation) {
            setOrientation("");
        } else {
            setOrientation(orientation.toString());
        }
    }
	public String getDriveTrain() {
        if(null==driveTrain) {
            return "";
        }
        return driveTrain;
    }
	public void setDriveTrain(String driveTrain) {
        if(null==driveTrain) {
            this.driveTrain="";
        } else {
            this.driveTrain=driveTrain;
        }
	}
    public void setDriveTrain(CharSequence driveTrain) {
        if(null==driveTrain) {
            setDriveTrain("");
        } else {
            setDriveTrain(driveTrain.toString());
        }
    }
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getHeightShooter() {
		return heightShooter;
	}
	public void setHeightShooter(double heightShooter) {
		this.heightShooter = heightShooter;
	}
	public double getHeightMax() {
		return heightMax;
	}
	public void setHeightMax(double heightMax) {
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
    public double getShootingDistance() {
        return shootingDistance;
    }
    public void setShootingDistance(double shootingDistance) {
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
    public double getHumanPlayer() {
        return humanPlayer;
    }
    public void setHumanPlayer(double humanPlayer) {
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
    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        TeamScouting2014 item = Query.one(TeamScouting2014.class,"SELECT * FROM "+TAG+" WHERE "+TEAM+"=? LIMIT 1",getTeam().getId()).get();
        if(null!=item) {
            this.setId(item.getId());
            Log.d(TAG, "item: "+item+" is empty: "+item.empty()+" is equal: "+this.equals(item));
            Log.d(TAG, "import mod: " + item.getModified()+" sql mod: "+this.getModified()+" after: "+this.getModified().before(item.getModified()));
            if((this.getModified().before(item.getModified()) && !item.empty()) || this.equals(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean empty() {
        return super.empty()
                && getOrientation()==""
                && getDriveTrain()==""
                && getWidth()==0
                && getLength()==0
                && getHeightShooter()==0
                && getHeightMax()==0
                && getShooterType()==0
                && isLowGoal()==false
                && isHighGoal()==false
                && isHotGoal()==false
                && getShootingDistance()==0
                && isPassGround()==false
                && isPassAir()==false
                && isPassTruss()==false
                && isPickupGround()==false
                && isPickupCatch()==false
                && isPusher()==false
                && isBlocker()==false
                && getHumanPlayer()==0
                && getAutoMode()==0;
    }
}
