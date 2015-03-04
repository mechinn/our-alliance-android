package com.mechinn.android.ouralliance.data;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = Match.TAG, id = Match.ID)
public class Match extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Match>, java.io.Serializable {
    public final static String TAG = "Match";

    public final static String EVENT = Event.TAG;
    public final static String COMPETITION_LEVEL = "compLevel";
    public final static String MATCH_NUMBER = "matchNum";
    public final static String SET_NUMBER = "setNumber";
    public final static String TIME = "time";
    public final static String RED_SCORE = "redScore";
    public final static String BLUE_SCORE = "blueScore";
    public final static String EIGHTH_FINALS = "ef";
    public final static String QUARTER_FINALS = "qf";
    public final static String SEMI_FINALS = "sf";
    public final static String FINALS = "f";
    public final static String QUALIFIER = "qm";
    public final static String PRACTICE = "p";
    @Column(name=EVENT, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Event event;
    @Column(name=COMPETITION_LEVEL, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private String compLevel;
    @Column(name=MATCH_NUMBER, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int matchNumber;
    @Column(name=SET_NUMBER, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Integer setNumber;
    @Column(name=TIME)
    private Long time;
    @Column(name=RED_SCORE)
    private Integer redScore;
    @Column(name=BLUE_SCORE)
    private Integer blueScore;
    private Alliances alliances;
    public Match() {}
    public Match(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public String getCompLevel() {
        return compLevel;
    }
    public String compLevelString() {
        if(getCompLevel().equals(EIGHTH_FINALS)) {
            return "Eigtht Finals";
        } else if(getCompLevel().equals(QUARTER_FINALS)) {
            return "Quarter Finals";
        } else if(getCompLevel().equals(SEMI_FINALS)) {
            return "Semi Finals";
        } else if(getCompLevel().equals(FINALS)) {
            return "Finals";
        } else if(getCompLevel().equals(PRACTICE)) {
            return "Practice";
        } else {
            return "Qualifier";
        }
    }
    public int getCompLevelValue() {
        if(getCompLevel().equals(EIGHTH_FINALS)) {
            return 40;
        } else if(getCompLevel().equals(QUARTER_FINALS)) {
            return 60;
        } else if(getCompLevel().equals(SEMI_FINALS)) {
            return 80;
        } else if(getCompLevel().equals(FINALS)) {
            return 100;
        } else if(getCompLevel().equals(PRACTICE)) {
            return 0;
        } else {
            return 20;
        }
    }
    public void setCompLevel(String compLevel) {
        this.compLevel = compLevel;
    }
    public int getMatchNumber() {
        return matchNumber;
    }
    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }
    public Integer getSetNumber() {
        return setNumber;
    }
    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }
    public Date getTime() {
        return new Date(time);
    }
    public void setTime(Date time) {
        this.time = time.getTime();
    }
    public Integer getRedScore() {
        return redScore;
    }
    public void setRedScore(Integer redScore) {
        this.redScore = redScore;
    }
    public Integer getBlueScore() {
        return blueScore;
    }
    public void setBlueScore(Integer blueScore) {
        this.blueScore = blueScore;
    }
    public Alliances getAlliances() {
        return alliances;
    }
    public List<MatchScouting> getTeams() {
        return getMany(MatchScouting.class, TAG);
    }
    public String toString() {
        String string = compLevelString()+": ";
        if(getCompLevel().equals(EIGHTH_FINALS) || getCompLevel().equals(QUARTER_FINALS) || getCompLevel().equals(SEMI_FINALS)) {
            string += getSetNumber()+" Match: ";
        }
        return string+this.getMatchNumber();
    }
    public boolean equals(Object data) {
        if(!(data instanceof Match)) {
            return false;
        }
        return  getEvent().equals(((Match)data).getEvent()) &&
                getCompLevel().equals(((Match)data).getCompLevel()) &&
                getSetNumber()==((Match)data).getSetNumber() &&
                getTime().equals(((Match)data).getTime()) &&
                getRedScore()==((Match)data).getRedScore() &&
                getBlueScore()==((Match)data).getBlueScore() &&
                getMatchNumber()==((Match)data).getMatchNumber();
    }
    public int compareTo(Match another) {
        int compare = this.getCompLevelValue() - another.getCompLevelValue();
        if(0==compare) {
            compare = this.getMatchNumber() - another.getMatchNumber();
        }
        return compare;
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Match.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Match.this);
            }
        });
    }
}
