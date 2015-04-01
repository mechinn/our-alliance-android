package com.mechinn.android.ouralliance.data;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

@Table(name = EventTeam.TAG, id = EventTeam.ID)
public class EventTeam extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<EventTeam>, java.io.Serializable {
    public final static String TAG = "EventTeam";
    public final static String EVENT = Event.TAG;
    public final static String TEAM = Team.TAG;
    public final static String RANK = "rank";
    public final static String SCOUTED = "scouted";
    @Column(name=EVENT, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Event event;
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Team team;
    @Column(name=RANK)
    private Integer rank;
    @Column(name=SCOUTED, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private boolean scouted;
    public EventTeam() {}
    public EventTeam(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        if(!event.equals(this.event)) {
            this.event = event;
            changedData();
        }
    }
    public void replaceEvent(Event event) {
        this.event = event;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        if(!team.equals(this.team)) {
            this.team = team;
            changedData();
        }
    }
    public void replaceTeam(Team team) {
        this.team = team;
    }
    public Integer getRank() {
        return rank;
    }
    public void setRank(Integer rank) {
        if(!rank.equals(this.rank)) {
            this.rank = rank;
            changedData();
        }
    }
    public boolean isScouted() {
        return scouted;
    }
    public void setScouted(boolean scouted) {
        if(scouted!=this.scouted) {
            this.scouted = scouted;
            changedData();
        }
    }
    public String toString() {
        return this.getEvent()+" # "+this.rank+" "+this.team;
    }
    public boolean copy(EventTeam data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setRank(data.getRank());
            this.setScouted(data.isScouted());
            return true;
        }
        return false;
    }
    public static EventTeam load(long eventId, long teamId) {
        return new Select().from(EventTeam.class).where(EventTeam.EVENT+"=?",eventId).and(EventTeam.TEAM+"=?",teamId).executeSingle();
    }
    public void saveMod() {
        if (null == this.getId()) {
            this.getEvent().saveMod();
            if(-1==this.getEvent().getId()) {
                this.replaceEvent(Event.load(this.getEvent().getEventCode(),this.getEvent().getYear()));
            }
            this.getTeam().saveMod();
            Timber.d(this.getTeam() + " " + this.getTeam().getId());
            if(-1==this.getTeam().getId()) {
                this.replaceTeam(Team.load(this.getTeam().getTeamNumber()));
            }
            Timber.d(this.getTeam() + " " + this.getTeam().getId());
        }
        super.saveMod();
    }
    public void saveEvent() {
        EventBus.getDefault().post(this.getEvent());
        EventBus.getDefault().post(this.getTeam());
        super.saveEvent();
    }
    public boolean equals(Object data) {
        if(!(data instanceof EventTeam)) {
            return false;
        }
        try {
            return  getEvent().equals(((EventTeam)data).getEvent()) &&
                    getTeam().equals(((EventTeam)data).getTeam());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public int compareTo(EventTeam another) {
        int compare = this.getRank() - another.getRank();
        if(0==compare) {
            compare = this.getTeam().compareTo(another.getTeam());
        }
        return compare;
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(EventTeam.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(EventTeam.this);
            }
        });
    }
}
