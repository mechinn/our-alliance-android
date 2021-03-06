package com.mechinn.android.ouralliance.data.frc2014;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = TeamScouting2014.TAG, id = TeamScouting2014.ID)
public class TeamScouting2014 extends TeamScouting {
    public final static String TAG = "TeamScouting2014";
    public final static String ORIENTATION = "orientation";
    public final static String DRIVE_TRAIN = "driveTrain";
    public final static String WIDTH = "width";
    public final static String LENGTH = "length";
    public final static String HEIGHT_SHOOTER = "heightShooter";
    public final static String HEIGHT_MAX = "heightMax";
    public final static String SHOOTER_TYPE = "shooterType";
    public enum ShooterType {
        UNKNOWN(null),NONE(0),DUMPER(1),SHOOTER(2);
        private Integer value;
        private ShooterType(Integer value) {
            this.value = value;
        }
        public Integer getValue() {
            return value;
        }
        public static ShooterType getType(Integer value) {
            if(null!=value) {
                switch(value) {
                    case 0:
                        return NONE;
                    case 1:
                        return DUMPER;
                    case 2:
                        return SHOOTER;
                }
            }
            return UNKNOWN;
        }
    }
    public final static String LOW_GOAL = "lowGoal";
    public final static String HIGH_GOAL = "highGoal";
    public final static String SHOOTING_DISTANCE = "shootingDistance";
    public final static String PASS_GROUND = "passGround";
    public final static String PASS_AIR = "passAir";
    public final static String PASS_TRUSS = "passTruss";
    public final static String PICKUP_GROUND = "pickupGround";
    public final static String PICKUP_CATCH = "pickupCatch";
    public final static String PUSHER = "pusher";
    public final static String BLOCKER = "blocker";
    public final static String HUMAN_PLAYER = "humanPlayer";
    public final static String NO_AUTO = "noAuto";
    public final static String DRIVE_AUTO = "driveAuto";
    public final static String LOW_AUTO = "lowAuto";
    public final static String HIGH_AUTO = "highAuto";
    public final static String HOT_AUTO = "hotAuto";
    @Column(name=ORIENTATION)
    private String orientation;
    @Column(name=DRIVE_TRAIN)
    private String driveTrain;
    @Column(name=WIDTH)
    private Double width;
    @Column(name=LENGTH)
    private Double length;
    @Column(name=HEIGHT_SHOOTER)
    private Double heightShooter;
    @Column(name=HEIGHT_MAX)
    private Double heightMax;
    @Column(name=SHOOTER_TYPE)
    private Integer shooterType;
    @Column(name=LOW_GOAL)
    private Boolean lowGoal;
    @Column(name=HIGH_GOAL)
    private Boolean highGoal;
    @Column(name=SHOOTING_DISTANCE)
    private Double shootingDistance;
    @Column(name=PASS_GROUND)
    private Boolean passGround;
    @Column(name=PASS_AIR)
    private Boolean passAir;
    @Column(name=PASS_TRUSS)
    private Boolean passTruss;
    @Column(name=PICKUP_GROUND)
    private Boolean pickupGround;
    @Column(name=PICKUP_CATCH)
    private Boolean pickupCatch;
    @Column(name=PUSHER)
    private Boolean pusher;
    @Column(name=BLOCKER)
    private Boolean blocker;
    @Column(name=HUMAN_PLAYER)
    private Float humanPlayer;
    @Column(name=NO_AUTO)
    private Boolean noAuto;
    @Column(name=DRIVE_AUTO)
    private Boolean driveAuto;
    @Column(name=LOW_AUTO)
    private Boolean lowAuto;
    @Column(name=HIGH_AUTO)
    private Boolean highAuto;
    @Column(name=HOT_AUTO)
    private Boolean hotAuto;
    public TeamScouting2014() {}
    public TeamScouting2014(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public String getDriveTrain() {
        return driveTrain;
    }
    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }
    public Double getWidth() {
        return width;
    }
    public void setWidth(Double width) {
        this.width = width;
    }
    public Double getLength() {
        return length;
    }
    public void setLength(Double length) {
        this.length = length;
    }
    public Double getHeightShooter() {
        return heightShooter;
    }
    public void setHeightShooter(Double heightShooter) {
        this.heightShooter = heightShooter;
    }
    public Double getHeightMax() {
        return heightMax;
    }
    public void setHeightMax(Double heightMax) {
        this.heightMax = heightMax;
    }
    public Integer getShooter() {
        return shooterType;
    }
    public void setShooter(Integer shooterType) {
        this.shooterType = shooterType;
    }
    public ShooterType getShooterType() {
        return ShooterType.getType(shooterType);
    }
    public void setShooterType(ShooterType shooterType) {
        this.shooterType = shooterType.getValue();
    }
    public Boolean getLowGoal() {
        return lowGoal;
    }
    public void setLowGoal(Boolean lowGoal) {
        this.lowGoal = lowGoal;
    }
    public Boolean getHighGoal() {
        return highGoal;
    }
    public void setHighGoal(Boolean highGoal) {
        this.highGoal = highGoal;
    }
    public Double getShootingDistance() {
        return shootingDistance;
    }
    public void setShootingDistance(Double shootingDistance) {
        this.shootingDistance = shootingDistance;
    }
    public Boolean getPassGround() {
        return passGround;
    }
    public void setPassGround(Boolean passGround) {
        this.passGround = passGround;
    }
    public Boolean getPassAir() {
        return passAir;
    }
    public void setPassAir(Boolean passAir) {
        this.passAir = passAir;
    }
    public Boolean getPassTruss() {
        return passTruss;
    }
    public void setPassTruss(Boolean passTruss) {
        this.passTruss = passTruss;
    }
    public Boolean getPickupGround() {
        return pickupGround;
    }
    public void setPickupGround(Boolean pickupGround) {
        this.pickupGround = pickupGround;
    }
    public Boolean getPickupCatch() {
        return pickupCatch;
    }
    public void setPickupCatch(Boolean pickupCatch) {
        this.pickupCatch = pickupCatch;
    }
    public Boolean getPusher() {
        return pusher;
    }
    public void setPusher(Boolean pusher) {
        this.pusher = pusher;
    }
    public Boolean getBlocker() {
        return blocker;
    }
    public void setBlocker(Boolean blocker) {
        this.blocker = blocker;
    }
    public Float getHumanPlayer() {
        return humanPlayer;
    }
    public void setHumanPlayer(Float humanPlayer) {
        this.humanPlayer = humanPlayer;
    }
    public Boolean getNoAuto() {
        return noAuto;
    }
    public void setNoAuto(Boolean noAuto) {
        this.noAuto = noAuto;
    }
    public Boolean getDriveAuto() {
        return driveAuto;
    }
    public void setDriveAuto(Boolean driveAuto) {
        this.driveAuto = driveAuto;
    }
    public Boolean getLowAuto() {
        return lowAuto;
    }
    public void setLowAuto(Boolean lowAuto) {
        this.lowAuto = lowAuto;
    }
    public Boolean getHighAuto() {
        return highAuto;
    }
    public void setHighAuto(Boolean highAuto) {
        this.highAuto = highAuto;
    }
    public Boolean getHotAuto() {
        return hotAuto;
    }
    public void setHotAuto(Boolean hotAuto) {
        this.hotAuto = hotAuto;
    }
    public List<? extends MatchScouting> getMatches() {
        return getMany(MatchScouting2014.class, super.TAG);
    }
    public List<? extends Wheel> getWheels() {
        return getMany(Wheel2014.class, TAG);
    }
    public String toString() {
        return	super.toString()+
                " Orientation: "+this.getOrientation()+
                " Drive Train: "+this.getDriveTrain()+
                " Width: "+this.getWidth()+
                " Length: "+this.getLength()+
                " Height Shooter: "+this.getHeightShooter()+
                " Height Max: "+this.getHeightMax()+
                " Shooter Type: "+this.getShooterType()+
                " Low Goal: "+this.getLowGoal()+
                " High Goal: "+this.getHighGoal()+
                " Shooting Distance: "+this.getShootingDistance()+
                " Pass Ground: "+this.getPassGround()+
                " Pass Air: "+this.getPassAir()+
                " Pass Truss: "+this.getPassTruss()+
                " Pickup Ground: "+this.getPickupGround()+
                " Pickup Catch: "+this.getPickupCatch()+
                " Pusher: "+this.getPusher()+
                " Blocker: "+this.getBlocker()+
                " Human Player: "+this.getHumanPlayer()+
                " No Autonomous Mode: "+this.getNoAuto()+
                " Drive Autonomous Mode: "+this.getDriveAuto()+
                " Low Autonomous Mode: "+this.getLowAuto()+
                " High Autonomous Mode: "+this.getHighAuto()+
                " Hot Autonomous Mode: "+this.getHotAuto();
    }
    public static TeamScouting2014 load(long teamId) {
        return new Select().from(TeamScouting2014.class).where(TeamScouting2014.TEAM+"=?",teamId).executeSingle();
    }
    public boolean equals(Object data) {
        if(!(data instanceof TeamScouting2014)) {
            return false;
        }
        return  super.equals(data) &&
                getOrientation().equals(((TeamScouting2014)data).getOrientation()) &&
                getDriveTrain().equals(((TeamScouting2014)data).getDriveTrain()) &&
                getWidth()==((TeamScouting2014)data).getWidth() &&
                getLength()==((TeamScouting2014)data).getLength() &&
                getHeightShooter()==((TeamScouting2014)data).getHeightShooter() &&
                getHeightMax()==((TeamScouting2014)data).getHeightMax() &&
                getShooterType()==((TeamScouting2014)data).getShooterType() &&
                getLowGoal()==((TeamScouting2014)data).getLowGoal() &&
                getHighGoal()==((TeamScouting2014)data).getHighGoal() &&
                getShootingDistance()==((TeamScouting2014)data).getShootingDistance() &&
                getPassGround()==((TeamScouting2014)data).getPassGround() &&
                getPassAir()==((TeamScouting2014)data).getPassAir() &&
                getPassTruss()==((TeamScouting2014)data).getPassTruss() &&
                getPickupGround()==((TeamScouting2014)data).getPickupGround() &&
                getPickupCatch()==((TeamScouting2014)data).getPickupCatch() &&
                getPusher()==((TeamScouting2014)data).getPusher() &&
                getBlocker()==((TeamScouting2014)data).getBlocker() &&
                getHumanPlayer()==((TeamScouting2014)data).getHumanPlayer() &&
                getNoAuto()==((TeamScouting2014)data).getNoAuto() &&
                getDriveAuto()==((TeamScouting2014)data).getDriveAuto() &&
                getLowAuto()==((TeamScouting2014)data).getLowAuto() &&
                getHighAuto()==((TeamScouting2014)data).getHighAuto() &&
                getHotAuto()==((TeamScouting2014)data).getHotAuto();
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(TeamScouting2014.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(TeamScouting2014.this);
            }
        });
    }
}
