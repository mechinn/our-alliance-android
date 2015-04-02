package com.mechinn.android.ouralliance.data.frc2014;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name=Wheel2014.TAG, id = Wheel2014.ID)
public class Wheel2014 extends Wheel {
    public final static String TAG = "Wheel2014";
    public final static String TEAM_SCOUTING = TeamScouting2014.TAG;
    @Column(name=TEAM_SCOUTING, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {Wheel.TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScouting2014 teamScouting2014;
    public Wheel2014() {}
    public Wheel2014(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public TeamScouting2014 getTeamScouting2014() {
        return teamScouting2014;
    }
    public void setTeamScouting2014(TeamScouting2014 teamScouting2014) {
        this.teamScouting2014 = teamScouting2014;
    }
    public TeamScouting getTeamScouting() {
        return getTeamScouting2014();
    }
    public void setTeamScouting(TeamScouting teamScouting) {
        setTeamScouting2014((TeamScouting2014) teamScouting);
    }

    @Override
    protected void saveTeamScouting() {
        this.getTeamScouting2014().saveMod();
        if(-1==this.getTeamScouting2014().getId()) {
            TeamScouting2014 dbTeamScouting = TeamScouting2014.load(this.getTeamScouting2014().getTeam().getId());
            this.setTeamScouting2014(dbTeamScouting);
        }
    }

    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Wheel2014.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Wheel2014.this);
            }
        });
    }
}
