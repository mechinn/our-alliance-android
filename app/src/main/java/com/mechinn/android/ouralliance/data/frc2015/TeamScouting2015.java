package com.mechinn.android.ouralliance.data.frc2015;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = TeamScouting2015.TAG, id = TeamScouting2015.ID)
public class TeamScouting2015 extends TeamScouting {
    public final static String TAG = "TeamScouting2015";
    public final static String ORIENTATION = "orientation";
    public final static String DRIVE_TRAIN = "driveTrain";
    public final static String WIDTH = "width";
    public final static String LENGTH = "length";
    public final static String HEIGHT = "height";
    public final static String COOP = "coop";
    public final static String DRIVER_EXPERIENCE = "driverExperience";
    public final static String PICKUP_MECHANISM = "pickupMechanism";
    public final static String MAX_TOTE_STACK = "maxToteStack";
    public final static String MAX_CONTAINER_STACK = "maxTotesStackContainer";
    public final static String MAX_TOTES_AND_CONTAINER_LITTER = "maxTotesAndContainerLitter";
    public final static String HUMAN_PLAYER = "humanPlayer";
    public final static String NO_AUTO = "noAuto";
    public final static String DRIVE_AUTO = "driveAuto";
    public final static String TOTE_AUTO = "toteAuto";
    public final static String CONTAINER_AUTO = "containerAuto";
    public final static String STACKED_AUTO = "stackedAuto";
    public final static String LANDFILL_AUTO = "landfillAuto";
    @Column(name=ORIENTATION)
    private String orientation;
    @Column(name=DRIVE_TRAIN)
    private String driveTrain;
    @Column(name=WIDTH)
    private Double width;
    @Column(name=LENGTH)
    private Double length;
    @Column(name=HEIGHT)
    private Double height;
    @Column(name=COOP)
    private Boolean coop;
    @Column(name=DRIVER_EXPERIENCE)
    private Double driverExperience;
    @Column(name=PICKUP_MECHANISM)
    private String pickupMechanism;
    @Column(name=MAX_TOTE_STACK)
    private Integer maxToteStack;
    @Column(name=MAX_CONTAINER_STACK)
    private Integer maxTotesStackContainer;
    @Column(name=MAX_TOTES_AND_CONTAINER_LITTER)
    private Integer maxTotesAndContainerLitter;
    @Column(name=HUMAN_PLAYER)
    private Float humanPlayer;
    @Column(name=NO_AUTO)
    private Boolean noAuto;
    @Column(name=DRIVE_AUTO)
    private Boolean driveAuto;
    @Column(name=TOTE_AUTO)
    private Boolean toteAuto;
    @Column(name=CONTAINER_AUTO)
    private Boolean containerAuto;
    @Column(name=STACKED_AUTO)
    private Boolean stackedAuto;
    @Column(name=LANDFILL_AUTO)
    private Integer landfillAuto;
    public TeamScouting2015() {}
    public TeamScouting2015(Cursor cursor) {
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
    public Double getHeight() {
        return height;
    }
    public void setHeight(Double height) {
        this.height = height;
    }
    public Boolean getCoop() {
        return coop;
    }
    public void setCoop(Boolean coop) {
        this.coop = coop;
    }
    public Double getDriverExperience() {
        return driverExperience;
    }
    public void setDriverExperience(Double driverExperience) {
        this.driverExperience = driverExperience;
    }
    public String getPickupMechanism() {
        return pickupMechanism;
    }
    public void setPickupMechanism(String pickupMechanism) {
        this.pickupMechanism = pickupMechanism;
    }
    public Integer getMaxToteStack() {
        return maxToteStack;
    }
    public void setMaxToteStack(Integer maxToteStack) {
        this.maxToteStack = maxToteStack;
    }
    public Integer getMaxTotesStackContainer() {
        return maxTotesStackContainer;
    }
    public void setMaxTotesStackContainer(Integer maxTotesStackContainer) {
        this.maxTotesStackContainer = maxTotesStackContainer;
    }
    public Integer getMaxTotesAndContainerLitter() {
        return maxTotesAndContainerLitter;
    }
    public void setMaxTotesAndContainerLitter(Integer maxTotesAndContainerLitter) {
        this.maxTotesAndContainerLitter = maxTotesAndContainerLitter;
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
    public Boolean getToteAuto() {
        return toteAuto;
    }
    public void setToteAuto(Boolean toteAuto) {
        this.toteAuto = toteAuto;
    }
    public Boolean getContainerAuto() {
        return containerAuto;
    }
    public void setContainerAuto(Boolean containerAuto) {
        this.containerAuto = containerAuto;
    }
    public Boolean getStackedAuto() {
        return stackedAuto;
    }
    public void setStackedAuto(Boolean stackedAuto) {
        this.stackedAuto = stackedAuto;
    }
    public Integer getLandfillAuto() {
        return landfillAuto;
    }
    public void setLandfillAuto(Integer landfillAuto) {
        this.landfillAuto = landfillAuto;
    }
    public List<? extends MatchScouting> getMatches() {
        return getMany(MatchScouting2015.class, super.TAG);
    }
    public List<? extends Wheel> getWheels() {
        return getMany(Wheel2015.class, super.TAG);
    }
    public String toString() {
        return	super.toString()+
                " Orientation: "+this.getOrientation()+
                " Drive Train: "+this.getDriveTrain()+
                " Width: "+this.getWidth()+
                " Length: "+this.getLength()+
                " Height: "+this.getHeight()+
                " Co-op: "+this.getCoop()+
                " Driver Experience: "+this.getDriverExperience()+
                " Pickup Mechanism: "+this.getPickupMechanism()+
                " Max Tote Stack: "+this.getMaxToteStack()+
                " Max Tote Stack place Container: "+this.getMaxTotesStackContainer()+
                " Max Tote Stack and Container to place Litter: "+this.getMaxTotesAndContainerLitter()+
                " Human Player: "+this.getHumanPlayer()+
                " No Autonomous Mode: "+this.getNoAuto()+
                " Drive Autonomous Mode: "+this.getDriveAuto()+
                " Tote Move Autonomous Mode: "+this.getToteAuto()+
                " Container Move Autonomous Mode: "+this.getContainerAuto()+
                " Stacked Tote Autonomous Mode: "+this.getStackedAuto()+
                " Landfill totes moved in Autonomous Mode: "+this.getLandfillAuto();
    }
    public boolean equals(Object data) {
        if(!(data instanceof TeamScouting2015)) {
            return false;
        }
        return  super.equals(data) &&
                getOrientation().equals(((TeamScouting2015)data).getOrientation()) &&
                getDriveTrain().equals(((TeamScouting2015)data).getDriveTrain()) &&
                getWidth().equals(((TeamScouting2015)data).getWidth()) &&
                getLength().equals(((TeamScouting2015)data).getLength()) &&
                getHeight().equals(((TeamScouting2015)data).getHeight()) &&
                getCoop().equals(((TeamScouting2015)data).getCoop()) &&
                getDriverExperience().equals(((TeamScouting2015)data).getDriverExperience()) &&
                getPickupMechanism().equals(((TeamScouting2015)data).getPickupMechanism()) &&
                getMaxToteStack().equals(((TeamScouting2015)data).getMaxToteStack()) &&
                getMaxTotesStackContainer().equals(((TeamScouting2015)data).getMaxTotesStackContainer()) &&
                getMaxTotesAndContainerLitter().equals(((TeamScouting2015)data).getMaxTotesAndContainerLitter()) &&
                getHumanPlayer().equals(((TeamScouting2015)data).getHumanPlayer()) &&
                getNoAuto().equals(((TeamScouting2015)data).getNoAuto()) &&
                getDriveAuto().equals(((TeamScouting2015)data).getDriveAuto()) &&
                getToteAuto().equals(((TeamScouting2015)data).getToteAuto()) &&
                getContainerAuto().equals(((TeamScouting2015)data).getContainerAuto()) &&
                getStackedAuto().equals(((TeamScouting2015)data).getStackedAuto()) &&
                getLandfillAuto().equals(((TeamScouting2015)data).getLandfillAuto());
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(TeamScouting2015.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(TeamScouting2015.this);
            }
        });
    }
}
