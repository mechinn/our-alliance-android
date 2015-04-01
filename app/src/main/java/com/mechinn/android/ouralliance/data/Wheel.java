package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public abstract class Wheel extends OurAllianceObject implements Comparable<Wheel>, java.io.Serializable {
    public final static String TAG = "Wheel";
    public final static String WHEEL_TYPE = "wheelType";
    public final static String WHEEL_SIZE = "wheelSize";
    public final static String WHEEL_COUNT = "wheelCount";
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
        if(!wheelType.equals(this.wheelType)) {
            this.wheelType = wheelType;
            changedData();
        }
    }
    public Double getWheelSize() {
        return wheelSize;
    }
    public void setWheelSize(Double wheelSize) {
        if(!wheelSize.equals(this.wheelSize)) {
            this.wheelSize = wheelSize;
            changedData();
        }
    }
    public Integer getWheelCount() {
        return wheelCount;
    }
    public void setWheelCount(Integer wheelCount) {
        if(!wheelCount.equals(this.wheelCount)) {
            this.wheelCount = wheelCount;
            changedData();
        }
    }
    public abstract TeamScouting getTeamScouting();
    public abstract void setTeamScouting(TeamScouting teamScouting);
    protected abstract void saveTeamScouting();
    public String toString() {
        return getTeamScouting()+": "+ getWheelType()+" | "+ getWheelSize()+" | "+ getWheelCount();
    }
    public boolean copy(Wheel data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setWheelCount(data.getWheelCount());
            return true;
        }
        return false;
    }
    public void saveMod() {
        if (null == this.getId()) {
            saveTeamScouting();
        }
        super.saveMod();
    }
    public void saveEvent() {
        EventBus.getDefault().post(this.getTeamScouting());
        super.saveEvent();
    }
    public boolean equals(Object data) {
        if(!(data instanceof Wheel)) {
            return false;
        }
        try {
            return  getTeamScouting().equals(((Wheel)data).getTeamScouting()) &&
                    getWheelType().equals(((Wheel)data).getWheelType()) &&
                    getWheelSize()==((Wheel)data).getWheelSize();
        } catch (NullPointerException e) {
            return false;
        }
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
