package com.mechinn.android.ouralliance.data.frc2015;

import android.database.Cursor;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = MatchScouting2015.TAG, id = MatchScouting2015.ID)
public class MatchScouting2015 extends MatchScouting {
    public final static String TAG = "MatchScouting2015";
    public final static String TEAM = TeamScouting2015.TAG;
    public final static String AUTO_STACKED = "autoStacked";
    public final static String AUTO_TOTES = "autoTotes";
    public final static String AUTO_CONTAINERS = "autoContainers";
    public final static String AUTO_LANDFILL = "autoLandfill";
    public final static String AUTO_MOVE = "autoMove";
    public final static String COOP = "coop";
    public final static String TOTES = "totes";
    public final static String CONTAINERS = "containers";
    public final static String LITTER = "litter";
    public final static String FOWLS = "fowls";
    public final static String HUMAN_ATTEMPT = "humanAttempt";
    public final static String HUMAN_SUCCESS = "humanSuccess";
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {MatchScouting.TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScouting2015 teamScouting2015;
    @Column(name=AUTO_STACKED)
    private Boolean autoStacked;
    @Column(name=AUTO_TOTES)
    private Integer autoTotes;
    @Column(name=AUTO_CONTAINERS)
    private Integer autoContainers;
    @Column(name=AUTO_LANDFILL)
    private Integer autoLandfill;
    @Column(name=AUTO_MOVE)
    private Float autoMove;
    @Column(name=COOP)
    private Boolean coop;
    @Column(name=TOTES)
    private Integer totes;
    @Column(name=CONTAINERS)
    private Integer containers;
    @Column(name=LITTER)
    private Integer litter;
    @Column(name=FOWLS)
    private Integer fowls;
    @Column(name=HUMAN_ATTEMPT)
    private Integer humanAttempt;
    @Column(name=HUMAN_SUCCESS)
    private Integer humanSuccess;
    public MatchScouting2015() {}
    public MatchScouting2015(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public TeamScouting2015 getTeamScouting2015() {
        return teamScouting2015;
    }
    public void setTeamScouting2015(TeamScouting2015 teamScouting2015) {
        if(null==teamScouting2015 && null!=this.teamScouting2015 || null!=teamScouting2015 && !teamScouting2015.equals(this.teamScouting2015)) {
            this.teamScouting2015 = teamScouting2015;
            changedData();
        }
    }
    public void replaceTeamScouting2015(TeamScouting2015 teamScouting2015) {
        this.teamScouting2015 = teamScouting2015;
    }
    public TeamScouting getTeamScouting() {
        return getTeamScouting2015();
    }
    public void setTeamScouting(TeamScouting teamScouting) {
        setTeamScouting2015((TeamScouting2015) teamScouting);
    }

    @Override
    protected void saveTeamScouting() {
        this.getTeamScouting2015().saveMod();
        if(-1==this.getTeamScouting2015().getId()) {
            this.replaceTeamScouting2015(TeamScouting2015.load(this.getTeamScouting2015().getTeam().getId()));
        }
    }

    public Boolean getAutoStacked() {
        return autoStacked;
    }
    public void setAutoStacked(Boolean autoStacked) {
        if(null==autoStacked && null!=this.autoStacked || null!=autoStacked && !autoStacked.equals(this.autoStacked)) {
            this.autoStacked = autoStacked;
            changedData();
        }
    }
    public Integer getAutoTotes() {
        return autoTotes;
    }
    public void setAutoTotes(Integer autoTotes) {
        if(null==autoTotes && null!=this.autoTotes || null!=autoTotes && !autoTotes.equals(this.autoTotes)) {
            this.autoTotes = autoTotes;
            changedData();
        }
    }
    public Integer getAutoContainers() {
        return autoContainers;
    }
    public void setAutoContainers(Integer autoContainers) {
        if(null==autoContainers && null!=this.autoContainers || null!=autoContainers && !autoContainers.equals(this.autoContainers)) {
            this.autoContainers = autoContainers;
            changedData();
        }
    }
    public Integer getAutoLandfill() {
        return autoLandfill;
    }
    public void setAutoLandfill(Integer autoLandfill) {
        if(null==autoLandfill && null!=this.autoLandfill || null!=autoLandfill && !autoLandfill.equals(this.autoLandfill)) {
            this.autoLandfill = autoLandfill;
            changedData();
        }
    }
    public Float getAutoMove() {
        return autoMove;
    }
    public void setAutoMove(Float autoMove) {
        if(null==autoMove && null!=this.autoMove || null!=autoMove && !autoMove.equals(this.autoMove)) {
            this.autoMove = autoMove;
            changedData();
        }
    }
    public Boolean getCoop() {
        return coop;
    }
    public void setCoop(Boolean coop) {
        if(null==coop && null!=this.coop || null!=coop && !coop.equals(this.coop)) {
            this.coop = coop;
            changedData();
        }
    }
    public Integer getTotes() {
        return totes;
    }
    public void setTotes(Integer totes) {
        if(null==totes && null!=this.totes || null!=totes && !totes.equals(this.totes)) {
            this.totes = totes;
            changedData();
        }
    }
    public Integer getContainers() {
        return containers;
    }
    public void setContainers(Integer containers) {
        if(null==containers && null!=this.containers || null!=containers && !containers.equals(this.containers)) {
            this.containers = containers;
            changedData();
        }
    }
    public Integer getLitter() {
        return litter;
    }
    public void setLitter(Integer litter) {
        if(null==litter && null!=this.litter || null!=litter && !litter.equals(this.litter)) {
            this.litter = litter;
            changedData();
        }
    }
    public Integer getFowls() {
        return fowls;
    }
    public void setFowls(Integer fowls) {
        if(null==fowls && null!=this.fowls || null!=fowls && !fowls.equals(this.fowls)) {
            this.fowls = fowls;
            changedData();
        }
    }
    public Integer getHumanAttempt() {
        return humanAttempt;
    }
    public void setHumanAttempt(Integer humanAttempt) {
        if(null==humanAttempt && null!=this.humanAttempt || null!=humanAttempt && !humanAttempt.equals(this.humanAttempt)) {
            this.humanAttempt = humanAttempt;
            changedData();
        }
    }
    public Integer getHumanSuccess() {
        return humanSuccess;
    }
    public void setHumanSuccess(Integer humanSuccess) {
        if(null==humanSuccess && null!=this.humanSuccess || null!=humanSuccess && !humanSuccess.equals(this.humanSuccess)) {
            this.humanSuccess = humanSuccess;
            changedData();
        }
    }
    public boolean copy(MatchScouting2015 data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setAutoStacked(data.getAutoStacked());
            this.setAutoTotes(data.getAutoTotes());
            this.setAutoContainers(data.getAutoContainers());
            this.setAutoLandfill(data.getAutoLandfill());
            this.setAutoMove(data.getAutoMove());
            this.setCoop(data.getCoop());
            this.setTotes(data.getTotes());
            this.setContainers(data.getContainers());
            this.setLitter(data.getLitter());
            this.setFowls(data.getFowls());
            this.setHumanAttempt(data.getHumanAttempt());
            this.setHumanSuccess(data.getHumanSuccess());
            return true;
        }
        return false;
    }
    public boolean equals(Object data) {
        if (!(data instanceof MatchScouting2015)) {
            return false;
        }
        try {
            return super.equals(data) &&
                    this.getTeamScouting2015().equals(((MatchScouting2015) data).getTeamScouting2015());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(MatchScouting2015.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(MatchScouting2015.this);
            }
        });
    }
}
