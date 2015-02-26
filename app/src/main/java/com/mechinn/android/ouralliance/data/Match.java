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
    @Column(name=EVENT, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private Event event;
    @Column(name=COMPETITION_LEVEL, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private CompetitionLevel compLevel;
    @Column(name=MATCH_NUMBER, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int matchNum;
    @Column(name=SET_NUMBER)
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
    public CompetitionLevel getCompLevel() {
        return compLevel;
    }
    public void setCompLevel(CompetitionLevel compLevel) {
        this.compLevel = compLevel;
    }
    public int getMatchNum() {
        return matchNum;
    }
    public void setMatchNum(int matchNum) {
        this.matchNum = matchNum;
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
    public enum CompetitionLevel {
        EIGHTH_FINALS(8),
        QUARTER_FINALS(4),
        SEMI_FINALS(2),
        FINALS(1),
        QUALIFIER(0),
        PRACTICE(-1);
        private int value;
        private CompetitionLevel(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
        public String getAbbr() {
            switch(this) {
                case EIGHTH_FINALS:
                    return "ef";
                case QUARTER_FINALS:
                    return "qf";
                case SEMI_FINALS:
                    return "sf";
                case FINALS:
                    return "f";
                case PRACTICE:
                    return "p";
                default:
                    return "qm";
            }
        }
        public String toString() {
            switch(this) {
                case EIGHTH_FINALS:
                    return "Eigtht Finals";
                case QUARTER_FINALS:
                    return "Quarter Finals";
                case SEMI_FINALS:
                    return "Semi Finals";
                case FINALS:
                    return "Finals";
                case PRACTICE:
                    return "Practice";
                default:
                    return "Qualifier";
            }
        }
        public static CompetitionLevel fromValue(int value) {
            switch(value) {
                case 8:
                    return EIGHTH_FINALS;
                case 4:
                    return QUARTER_FINALS;
                case 2:
                    return SEMI_FINALS;
                case 1:
                    return FINALS;
                case -1:
                    return PRACTICE;
                default:
                    return QUALIFIER;
            }
        }
    }
    public String toString() {
        String string = getCompLevel().toString()+": ";
        switch(getCompLevel()) {
            case QUARTER_FINALS:
            case SEMI_FINALS:
                string += getSetNumber()+" Match: ";
                break;
        }
        return string+this.getMatchNum();
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
                getMatchNum()==((Match)data).getMatchNum();
    }
    public int compareTo(Match another) {
        return this.getMatchNum() - another.getMatchNum();
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
