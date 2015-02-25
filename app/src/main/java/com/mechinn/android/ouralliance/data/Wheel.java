package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public abstract class Wheel<Scouting extends TeamScouting> extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Wheel>, java.io.Serializable {
    public final static String TAG = "Wheel";
    public final static String TEAM_SCOUTING = TeamScouting.TAG;
    public final static String WHEEL_TYPE = "wheelType";
    public final static String WHEEL_SIZE = "wheelSize";
    public final static String WHEEL_COUNT = "wheelCount";
    @Column(name=TEAM_SCOUTING, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Scouting teamScouting;
    @Column(name=WHEEL_TYPE, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private String wheelType;
    @Column(name=WHEEL_SIZE, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Double wheelSize;
    @Column(name=WHEEL_COUNT)
    private Integer wheelCount;
    public String getWheelType() {
        return wheelType;
    }
    public void setWheelType(String wheelType) {
        this.wheelType = wheelType;
    }
    public Double getWheelSize() {
        return wheelSize;
    }
    public void setWheelSize(Double wheelSize) {
        this.wheelSize = wheelSize;
    }
    public Integer getWheelCount() {
        return wheelCount;
    }
    public void setWheelCount(Integer wheelCount) {
        this.wheelCount = wheelCount;
    }
    public Scouting getTeamScouting() {
        return teamScouting;
    }
    public void setTeamScouting(Scouting teamScouting) {
        this.teamScouting = teamScouting;
    }
    public String toString() {
        return getTeamScouting()+": "+ getWheelType()+" | "+ getWheelSize()+" | "+ getWheelCount();
    }
    public boolean equals(Object data) {
        if(!(data instanceof Wheel)) {
            return false;
        }
        return  getTeamScouting().equals(((Wheel)data).getTeamScouting()) &&
                getWheelType().equals(((Wheel)data).getWheelType()) &&
                getWheelSize()==((Wheel)data).getWheelSize() &&
                getWheelCount()==((Wheel)data).getWheelCount();
    }
    public int compareTo(Wheel another) {
        int compare = this.getWheelType().compareTo(another.getWheelType());
        if(0==compare) {
            compare = this.getTeamScouting().compareTo(another.getTeamScouting());
        }
        return compare;
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Wheel.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Wheel.this);
            }
        });
    }
}
