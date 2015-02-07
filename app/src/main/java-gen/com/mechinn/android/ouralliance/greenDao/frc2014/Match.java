package com.mechinn.android.ouralliance.greenDao.frc2014;

import java.util.List;
import com.mechinn.android.ouralliance.greenDao.frc2014.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.frc2014.dao.CompetitionDao;
import com.mechinn.android.ouralliance.greenDao.frc2014.dao.MatchDao;
import com.mechinn.android.ouralliance.greenDao.frc2014.dao.MatchScoutingDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table MATCH.
 */
public class Match extends com.mechinn.android.ouralliance.OurAllianceObject  {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    private int matchType;
    private Integer matchSet;
    private Integer redScore;
    private Integer blueScore;
    /** Not-null value. */
    private String compLevel;
    private String matchNum;
    private Long competition;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MatchDao myDao;

    private Competition competition;
    private Long competition__resolvedKey;

    private List<MatchScouting> teams;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Match() {
    }

    public Match(Long id) {
        this.id = id;
    }

    public Match(Long id, java.util.Date modified, int matchType, Integer matchSet, Integer redScore, Integer blueScore, String compLevel, String matchNum, Long competition) {
        this.id = id;
        this.modified = modified;
        this.matchType = matchType;
        this.matchSet = matchSet;
        this.redScore = redScore;
        this.blueScore = blueScore;
        this.compLevel = compLevel;
        this.matchNum = matchNum;
        this.competition = competition;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMatchDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public java.util.Date getModified() {
        return modified;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setModified(java.util.Date modified) {
        this.modified = modified;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public Integer getMatchSet() {
        return matchSet;
    }

    public void setMatchSet(Integer matchSet) {
        this.matchSet = matchSet;
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

    /** Not-null value. */
    public String getCompLevel() {
        return compLevel;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCompLevel(String compLevel) {
        this.compLevel = compLevel;
    }

    public String getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(String matchNum) {
        this.matchNum = matchNum;
    }

    public Long getCompetition() {
        return competition;
    }

    public void setCompetition(Long competition) {
        this.competition = competition;
    }

    /** To-one relationship, resolved on first access. */
    public Competition getCompetition() {
        Long __key = this.competition;
        if (competition__resolvedKey == null || !competition__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CompetitionDao targetDao = daoSession.getCompetitionDao();
            Competition competitionNew = targetDao.load(__key);
            synchronized (this) {
                competition = competitionNew;
            	competition__resolvedKey = __key;
            }
        }
        return competition;
    }

    public void setCompetition(Competition competition) {
        synchronized (this) {
            this.competition = competition;
            competition = competition == null ? null : competition.getId();
            competition__resolvedKey = competition;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<MatchScouting> getTeams() {
        if (teams == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MatchScoutingDao targetDao = daoSession.getMatchScoutingDao();
            List<MatchScouting> teamsNew = targetDao._queryMatch_Teams(id);
            synchronized (this) {
                if(teams == null) {
                    teams = teamsNew;
                }
            }
        }
        return teams;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTeams() {
        teams = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
