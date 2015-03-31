package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public abstract class TeamScouting extends OurAllianceObject implements Comparable<TeamScouting>, java.io.Serializable {
    public final static String TAG = "TeamScouting";
    public final static String TEAM = Team.TAG;
    public final static String NOTES = "notes";
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, unique = true, onUniqueConflict = Column.ConflictAction.FAIL)
    private Team team;
    @Column(name=NOTES)
    private String notes;
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        if(!team.equals(this.team)) {
            this.team = team;
            changedData();
        }
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        if(!notes.equals(this.notes)) {
            this.notes = notes;
            changedData();
        }
    }
    public abstract List<? extends Wheel> getWheels();
    public abstract List<? extends MatchScouting> getMatches();
    public String toString() {
        return "ID: "+this.getId()+
                " Mod: "+this.getModified()+
                " Notes: "+this.getNotes();
    }
    public int compareTo(TeamScouting another) {
        return this.getTeam().compareTo(another.getTeam());
    }
    public boolean copy(TeamScouting data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setNotes(data.getNotes());
            return true;
        }
        return false;
    }
    public void saveMod() {
        if (null == this.getId()) {
            this.getTeam().saveMod();
            if(-1==this.getTeam().getId()) {
                this.setTeam(Team.load(this.getTeam().getTeamNumber()));
            }
        }
        super.saveMod();
    }
    public void saveEvent() {
        EventBus.getDefault().post(this.getTeam());
        super.saveEvent();
    }
    public boolean equals(Object data) {
        if (!(data instanceof TeamScouting)) {
            return false;
        }
        try {
            return getTeam().equals(((TeamScouting) data).getTeam());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(TeamScouting.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(TeamScouting.this);
            }
        });
    }
}
