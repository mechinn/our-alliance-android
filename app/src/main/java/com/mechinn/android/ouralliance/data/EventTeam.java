package com.mechinn.android.ouralliance.data;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
        this.event = event;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public Integer getRank() {
        return rank;
    }
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    public boolean isScouted() {
        return scouted;
    }
    public void setScouted(boolean scouted) {
        this.scouted = scouted;
    }
    public String toString() {
        return this.getEvent()+" # "+this.rank+" "+this.team;
    }
    public boolean equals(Object data) {
        if(!(data instanceof EventTeam)) {
            return false;
        }
        return  getEvent().equals(((EventTeam)data).getEvent()) &&
                getTeam().equals(((EventTeam)data).getTeam()) &&
                getRank() == ((EventTeam)data).getRank() &&
                isScouted() == ((EventTeam)data).isScouted();
    }
    public int compareTo(EventTeam another) {
        int compare = this.getRank() - another.getRank();
        if(0==compare) {
            compare = this.getTeam().compareTo(another.getTeam());
        }
        return compare;
    }
}
