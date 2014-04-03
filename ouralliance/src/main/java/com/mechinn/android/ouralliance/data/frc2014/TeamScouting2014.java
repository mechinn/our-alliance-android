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

import java.util.ArrayList;
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
    public static final String SHOOTINGDISTANCE = "shootingDistance";
    public static final String PASSGROUND = "passGround";
    public static final String PASSAIR = "passAir";
    public static final String PASSTRUSS = "passTruss";
    public static final String PICKUPGROUND = "pickupGround";
    public static final String PICKUPCATCH = "pickupCatch";
    public static final String PUSHER = "pusher";
    public static final String BLOCKER = "blocker";
    public static final String HUMANPLAYER = "humanPlayer";
    public static final String NOAUTO = "noAuto";
    public static final String DRIVEAUTO = "driveAuto";
    public static final String LOWAUTO = "lowAuto";
    public static final String HIGHAUTO = "highAuto";
    public static final String HOTAUTO = "hotAuto";
	
	public static final int maxPerimeter = 112;
	public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

    public static final int NONE = 1;
    public static final int DUMPER = 2;
    public static final int SHOOTER = 3;

    public static final Map shootersWrite;
    public static final Map shootersRead;

    static {
        int[] shooterIds = {
                NONE
                ,DUMPER
                ,SHOOTER
        };
        String[] shooterText = {
                "None"
                ,"Dumper"
                ,"Shooter"
        };
        Map shootersWriteTemp;
        Map shootersReadTemp;
        shootersWriteTemp = new ArrayMap();
        shootersReadTemp = new ArrayMap();
        for(int i=0;i<shooterIds.length;++i) {
            shootersWriteTemp.put(shooterIds[i],shooterText[i]);
            shootersReadTemp.put(shooterText[i],shooterIds[i]);
        }
        shootersWrite = shootersWriteTemp;
        shootersRead = shootersReadTemp;

    }

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
    @Column(NOAUTO)
    private boolean noAuto;
    @Column(DRIVEAUTO)
    private boolean driveAuto;
    @Column(LOWAUTO)
    private boolean lowAuto;
    @Column(HIGHAUTO)
    private boolean highAuto;
    @Column(HOTAUTO)
    private boolean hotAuto;

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
    public boolean isNoAuto() {
        return noAuto;
    }
    public void setNoAuto(boolean noAuto) {
        this.noAuto = noAuto;
    }
    public boolean isDriveAuto() {
        return driveAuto;
    }
    public void setDriveAuto(boolean driveAuto) {
        this.driveAuto = driveAuto;
    }
    public boolean isLowAuto() {
        return lowAuto;
    }
    public void setLowAuto(boolean lowAuto) {
        this.lowAuto = lowAuto;
    }
    public boolean isHighAuto() {
        return highAuto;
    }
    public void setHighAuto(boolean highAuto) {
        this.highAuto = highAuto;
    }
    public boolean isHotAuto() {
        return hotAuto;
    }
    public void setHotAuto(boolean hotAuto) {
        this.hotAuto = hotAuto;
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
                " Shooting Distance: "+this.getShootingDistance()+
                " Pass Ground: "+this.isPassGround()+
                " Pass Air: "+this.isPassAir()+
                " Pass Truss: "+this.isPassTruss()+
                " Pickup Ground: "+this.isPickupGround()+
                " Pickup Catch: "+this.isPickupCatch()+
                " Pusher: "+this.isPusher()+
				" Blocker: "+this.isBlocker()+
                " Human Player: "+this.getHumanPlayer()+
                " No Autonomous Mode: "+this.isNoAuto()+
                " Drive Autonomous Mode: "+this.isDriveAuto()+
                " Low Autonomous Mode: "+this.isLowAuto()+
                " High Autonomous Mode: "+this.isHighAuto()+
                " Hot Autonomous Mode: "+this.isHotAuto();
	}
	public boolean equals(Object data) {
        if(!(data instanceof TeamScouting2014)) {
            return false;
        }
		return super.equals(data) &&
				getOrientation().equals(((TeamScouting2014)data).getOrientation()) &&
				getDriveTrain().equals(((TeamScouting2014)data).getDriveTrain()) &&
				getWidth()==((TeamScouting2014)data).getWidth() &&
				getLength()==((TeamScouting2014)data).getLength() &&
				getHeightShooter()==((TeamScouting2014)data).getHeightShooter() &&
				getHeightMax()==((TeamScouting2014)data).getHeightMax() &&
				getShooterType()==((TeamScouting2014)data).getShooterType() &&
				isLowGoal()==((TeamScouting2014)data).isLowGoal() &&
				isHighGoal()==((TeamScouting2014)data).isHighGoal() &&
                getShootingDistance()==((TeamScouting2014)data).getShootingDistance() &&
                isPassGround()==((TeamScouting2014)data).isPassGround() &&
                isPassAir()==((TeamScouting2014)data).isPassAir() &&
                isPassTruss()==((TeamScouting2014)data).isPassTruss() &&
                isPickupGround()==((TeamScouting2014)data).isPickupGround() &&
                isPickupCatch()==((TeamScouting2014)data).isPickupCatch() &&
                isPusher()==((TeamScouting2014)data).isPusher() &&
				isBlocker()==((TeamScouting2014)data).isBlocker() &&
                getHumanPlayer()==((TeamScouting2014)data).getHumanPlayer() &&
                isNoAuto()==((TeamScouting2014)data).isNoAuto() &&
                isDriveAuto()==((TeamScouting2014)data).isDriveAuto() &&
                isLowAuto()==((TeamScouting2014)data).isLowAuto() &&
                isHighAuto()==((TeamScouting2014)data).isHighAuto() &&
                isHotAuto()==((TeamScouting2014)data).isHotAuto();
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
                && getShootingDistance()==0
                && isPassGround()==false
                && isPassAir()==false
                && isPassTruss()==false
                && isPickupGround()==false
                && isPickupCatch()==false
                && isPusher()==false
                && isBlocker()==false
                && getHumanPlayer()==0
                && isNoAuto()==false
                && isDriveAuto()==false
                && isLowAuto()==false
                && isHighAuto()==false
                && isHotAuto()==false;
    }
}
