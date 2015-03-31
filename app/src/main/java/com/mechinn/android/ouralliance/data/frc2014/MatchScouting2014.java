package com.mechinn.android.ouralliance.data.frc2014;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = MatchScouting2014.TAG, id = MatchScouting2014.ID)
public class MatchScouting2014 extends MatchScouting {
    public final static String TAG = "MatchScouting2014";
    public final static String TEAM = TeamScouting2014.TAG;
    public final static String HOTSHOTS = "hotShots";
    public final static String SHOTSMADE = "shotsMade";
    public final static String SHOTSMISSED = "shotsMissed";
    public final static String MOVEFORWARD = "moveForward";
    public final static String SHOOTER = "shooter";
    public final static String CATCHER = "catcher";
    public final static String PASSER = "passer";
    public final static String DRIVETRAINRATING = "driveTrainRating";
    public final static String BALLACCURACYRATING = "ballAccuracyRating";
    public final static String GROUND = "ground";
    public final static String OVERTRUSS = "overTruss";
    public final static String LOW = "low";
    public final static String HIGH = "high";
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {MatchScouting.TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScouting2014 teamScouting2014;
    @Column(name=HOTSHOTS)
    private Integer hotShots;
    @Column(name=SHOTSMADE)
    private Integer shotsMade;
    @Column(name=SHOTSMISSED)
    private Integer shotsMissed;
    @Column(name=MOVEFORWARD)
    private Float moveForward;
    @Column(name=SHOOTER)
    private Boolean shooter;
    @Column(name=CATCHER)
    private Boolean catcher;
    @Column(name=PASSER)
    private Boolean passer;
    @Column(name=DRIVETRAINRATING)
    private Float driveTrainRating;
    @Column(name=BALLACCURACYRATING)
    private Float ballAccuracyRating;
    @Column(name=GROUND)
    private Boolean ground;
    @Column(name=OVERTRUSS)
    private Boolean overTruss;
    @Column(name=LOW)
    private Boolean low;
    @Column(name=HIGH)
    private Boolean high;
    public MatchScouting2014() {}
    public MatchScouting2014(Cursor cursor) {
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
    public TeamScouting loadTeamScouting(long teamId) {
        return TeamScouting2014.load(teamId);
    }

    public Integer getHotShots() {
        return hotShots;
    }
    public void setHotShots(Integer hotShots) {
        this.hotShots = hotShots;
    }
    public Integer getShotsMade() {
        return shotsMade;
    }
    public void setShotsMade(Integer shotsMade) {
        this.shotsMade = shotsMade;
    }
    public Integer getShotsMissed() {
        return shotsMissed;
    }
    public void setShotsMissed(Integer shotsMissed) {
        this.shotsMissed = shotsMissed;
    }
    public Float getMoveForward() {
        return moveForward;
    }
    public void setMoveForward(Float moveForward) {
        this.moveForward = moveForward;
    }
    public Boolean getShooter() {
        return shooter;
    }
    public void setShooter(Boolean shooter) {
        this.shooter = shooter;
    }
    public Boolean getCatcher() {
        return catcher;
    }
    public void setCatcher(Boolean catcher) {
        this.catcher = catcher;
    }
    public Boolean getPasser() {
        return passer;
    }
    public void setPasser(Boolean passer) {
        this.passer = passer;
    }
    public Float getDriveTrainRating() {
        return driveTrainRating;
    }
    public void setDriveTrainRating(Float driveTrainRating) {
        this.driveTrainRating = driveTrainRating;
    }
    public Float getBallAccuracyRating() {
        return ballAccuracyRating;
    }
    public void setBallAccuracyRating(Float ballAccuracyRating) {
        this.ballAccuracyRating = ballAccuracyRating;
    }
    public Boolean getGround() {
        return ground;
    }
    public void setGround(Boolean ground) {
        this.ground = ground;
    }
    public Boolean getOverTruss() {
        return overTruss;
    }
    public void setOverTruss(Boolean overTruss) {
        this.overTruss = overTruss;
    }
    public Boolean getLow() {
        return low;
    }
    public void setLow(Boolean low) {
        this.low = low;
    }
    public Boolean getHigh() {
        return high;
    }
    public void setHigh(Boolean high) {
        this.high = high;
    }
    public boolean equals(Object data) {
        if (!(data instanceof MatchScouting2014)) {
            return false;
        }
        return super.equals(data) &&
                getHotShots()==((MatchScouting2014)data).getHotShots() &&
                getShotsMade()==((MatchScouting2014)data).getShotsMade() &&
                getShotsMissed()==((MatchScouting2014)data).getShotsMissed() &&
                getMoveForward()==((MatchScouting2014)data).getMoveForward() &&
                getShooter()==((MatchScouting2014)data).getShooter() &&
                getCatcher()==((MatchScouting2014)data).getCatcher() &&
                getPasser()==((MatchScouting2014)data).getPasser() &&
                getDriveTrainRating()==((MatchScouting2014)data).getDriveTrainRating() &&
                getBallAccuracyRating()==((MatchScouting2014)data).getBallAccuracyRating() &&
                getGround()==((MatchScouting2014)data).getGround() &&
                getOverTruss()==((MatchScouting2014)data).getOverTruss() &&
                getLow()==((MatchScouting2014)data).getLow() &&
                getHigh()==((MatchScouting2014)data).getHigh();
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(MatchScouting2014.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(MatchScouting2014.this);
            }
        });
    }
}
