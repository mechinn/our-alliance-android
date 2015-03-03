package com.mechinn.android.ouralliance.data.frc2015;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name= Wheel2015.TAG, id = Wheel2015.ID)
public class Wheel2015 extends Wheel {
    public final static String TAG = "Wheel2015";
    public final static String TEAM_SCOUTING = TeamScouting2015.TAG;
    @Column(name=TEAM_SCOUTING, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {Wheel.TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScouting2015 teamScouting2015;
    public Wheel2015() {}
    public Wheel2015(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public TeamScouting2015 getTeamScouting2015() {
        return teamScouting2015;
    }
    public void setTeamScouting2015(TeamScouting2015 teamScouting2015) {
        this.teamScouting2015 = teamScouting2015;
    }
    public TeamScouting getTeamScouting() {
        return getTeamScouting2015();
    }
    public void setTeamScouting(TeamScouting teamScouting) {
        setTeamScouting2015((TeamScouting2015) teamScouting);
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Wheel2015.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Wheel2015.this);
            }
        });
    }
}
