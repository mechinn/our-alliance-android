package com.mechinn.android.ouralliance.data.frc2015;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.csv.FmtTeam;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;

import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

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
    private Float driverExperience;
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
        if(!orientation.equals(this.orientation)) {
            this.orientation = orientation;
            changedData();
        }
    }
    public String getDriveTrain() {
        return driveTrain;
    }
    public void setDriveTrain(String driveTrain) {
        if(!driveTrain.equals(this.driveTrain)) {
            this.driveTrain = driveTrain;
            changedData();
        }
    }
    public Double getWidth() {
        return width;
    }
    public void setWidth(Double width) {
        if(!width.equals(this.width)) {
            this.width = width;
            changedData();
        }
    }
    public Double getLength() {
        return length;
    }
    public void setLength(Double length) {
        if(!length.equals(this.length)) {
            this.length = length;
            changedData();
        }
    }
    public Double getHeight() {
        return height;
    }
    public void setHeight(Double height) {
        if(!height.equals(this.height)) {
            this.height = height;
            changedData();
        }
    }
    public Boolean getCoop() {
        return coop;
    }
    public void setCoop(Boolean coop) {
        if(!coop.equals(this.coop)) {
            this.coop = coop;
            changedData();
        }
    }
    public Float getDriverExperience() {
        return driverExperience;
    }
    public void setDriverExperience(Float driverExperience) {
        if(!driverExperience.equals(this.driverExperience)) {
            this.driverExperience = driverExperience;
            changedData();
        }
    }
    public String getPickupMechanism() {
        return pickupMechanism;
    }
    public void setPickupMechanism(String pickupMechanism) {
        if(!pickupMechanism.equals(this.pickupMechanism)) {
            this.pickupMechanism = pickupMechanism;
            changedData();
        }
    }
    public Integer getMaxToteStack() {
        return maxToteStack;
    }
    public void setMaxToteStack(Integer maxToteStack) {
        if(!maxToteStack.equals(this.maxToteStack)) {
            this.maxToteStack = maxToteStack;
            changedData();
        }
    }
    public Integer getMaxTotesStackContainer() {
        return maxTotesStackContainer;
    }
    public void setMaxTotesStackContainer(Integer maxTotesStackContainer) {
        if(!maxTotesStackContainer.equals(this.maxTotesStackContainer)) {
            this.maxTotesStackContainer = maxTotesStackContainer;
            changedData();
        }
    }
    public Integer getMaxTotesAndContainerLitter() {
        return maxTotesAndContainerLitter;
    }
    public void setMaxTotesAndContainerLitter(Integer maxTotesAndContainerLitter) {
        if(!maxTotesAndContainerLitter.equals(this.maxTotesAndContainerLitter)) {
            this.maxTotesAndContainerLitter = maxTotesAndContainerLitter;
            changedData();
        }
    }
    public Float getHumanPlayer() {
        return humanPlayer;
    }
    public void setHumanPlayer(Float humanPlayer) {
        if(!humanPlayer.equals(this.humanPlayer)) {
            this.humanPlayer = humanPlayer;
            changedData();
        }
    }
    public Boolean getNoAuto() {
        return noAuto;
    }
    public void setNoAuto(Boolean noAuto) {
        if(!noAuto.equals(this.noAuto)) {
            this.noAuto = noAuto;
            changedData();
        }
    }
    public Boolean getDriveAuto() {
        return driveAuto;
    }
    public void setDriveAuto(Boolean driveAuto) {
        if(!driveAuto.equals(this.driveAuto)) {
            this.driveAuto = driveAuto;
            changedData();
        }
    }
    public Boolean getToteAuto() {
        return toteAuto;
    }
    public void setToteAuto(Boolean toteAuto) {
        if(!toteAuto.equals(this.toteAuto)) {
            this.toteAuto = toteAuto;
            changedData();
        }
    }
    public Boolean getContainerAuto() {
        return containerAuto;
    }
    public void setContainerAuto(Boolean containerAuto) {
        if(!containerAuto.equals(this.containerAuto)) {
            this.containerAuto = containerAuto;
            changedData();
        }
    }
    public Boolean getStackedAuto() {
        return stackedAuto;
    }
    public void setStackedAuto(Boolean stackedAuto) {
        if(!stackedAuto.equals(this.stackedAuto)) {
            this.stackedAuto = stackedAuto;
            changedData();
        }
    }
    public Integer getLandfillAuto() {
        return landfillAuto;
    }
    public void setLandfillAuto(Integer landfillAuto) {
        if(!landfillAuto.equals(this.landfillAuto)) {
            this.landfillAuto = landfillAuto;
            changedData();
        }
    }
    public static TeamScouting2015 load(long teamId) {
        Timber.d("TeamScouting2015"+teamId);
        return new Select().from(TeamScouting2015.class).where(TeamScouting2015.TEAM+"=?",teamId).executeSingle();
    }
    public List<? extends MatchScouting> getMatches() {
        return getMany(MatchScouting2015.class, super.TAG);
    }
    public EventTeam getEventTeam(Event event) {
        return getEventTeam(event.getId());
    }
    public EventTeam getEventTeam(long eventId) {
        return new Select().from(EventTeam.class).where(EventTeam.EVENT+"=?",eventId).and(EventTeam.TEAM+"=?",getTeam().getId()).executeSingle();
    }
    public List<? extends Wheel> getWheels() {
        return getMany(Wheel2015.class, TAG);
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
    public boolean copy(TeamScouting2015 data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setOrientation(data.getOrientation());
            this.setDriveTrain(data.getDriveTrain());
            this.setWidth(data.getWidth());
            this.setLength(data.getLength());
            this.setHeight(data.getHeight());
            this.setCoop(data.getCoop());
            this.setDriverExperience(data.getDriverExperience());
            this.setPickupMechanism(data.getPickupMechanism());
            this.setMaxToteStack(data.getMaxToteStack());
            this.setMaxTotesStackContainer(data.getMaxTotesStackContainer());
            this.setMaxTotesAndContainerLitter(data.getMaxTotesAndContainerLitter());
            this.setHumanPlayer(data.getHumanPlayer());
            this.setNoAuto(data.getNoAuto());
            this.setDriveAuto(data.getDriveAuto());
            this.setToteAuto(data.getToteAuto());
            this.setContainerAuto(data.getContainerAuto());
            this.setStackedAuto(data.getStackedAuto());
            this.setLandfillAuto(data.getLandfillAuto());
            return true;
        }
        return false;
    }
    public boolean equals(Object data) {
        if(!(data instanceof TeamScouting2015)) {
            return false;
        }
        try {
            return  super.equals(data);
        } catch (NullPointerException e) {
            return false;
        }
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
