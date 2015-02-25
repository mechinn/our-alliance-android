package com.mechinn.android.ouralliance.data;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 2/14/2015.
 */
public abstract class MatchScouting<TeamScout extends TeamScouting> extends OurAllianceObject implements Comparable<MatchScouting>, java.io.Serializable {
    public final static String TAG = "MatchScouting";

    public final static String MATCH = Match.TAG;
    public final static String TEAM = TeamScouting.TAG;
    public final static String ALLIANCE = "alliance";
    public final static String POSITION = "position";
    public final static String NOTES = "notes";
    @Column(name=MATCH, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Match match;
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScout teamScouting;
    @Column(name=ALLIANCE, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private boolean alliance;
    @Column(name=POSITION, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private int position;
    @Column(name=NOTES)
    private String notes;
    public Match getMatch() {
        return match;
    }
    public void setMatch(Match match) {
        this.match = match;
    }
    public TeamScout getTeamScouting() {
        return teamScouting;
    }
    public void setTeamScouting(TeamScout teamScouting) {
        this.teamScouting = teamScouting;
    }
    public boolean isAlliance() {
        return alliance;
    }
    public void setAlliance(boolean alliance) {
        this.alliance = alliance;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public int compareTo(MatchScouting another) {
        int compare = (this.isAlliance()?1:0) - (another.isAlliance()?1:0);
        if(0==compare) {
            compare = this.getTeamScouting().getTeam().compareTo(another.getTeamScouting().getTeam());
        }
        return compare;
    }
    public boolean equals(Object data) {
        if (!(data instanceof MatchScouting)) {
            return false;
        }
        return getMatch().equals(((MatchScouting) data).getMatch()) &&
                getTeamScouting().equals(((MatchScouting) data).getTeamScouting()) &&
                isAlliance() == ((MatchScouting) data).isAlliance() &&
                getNotes().equals(((MatchScouting) data).getNotes());
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(MatchScouting.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(MatchScouting.this);
            }
        });
    }
}
