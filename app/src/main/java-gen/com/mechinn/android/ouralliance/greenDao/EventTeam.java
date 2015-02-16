package com.mechinn.android.ouralliance.greenDao;

import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.dao.EventDao;
import com.mechinn.android.ouralliance.greenDao.dao.EventTeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table EVENT_TEAM.
 */
public class EventTeam extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<EventTeam>, java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    private long eventId;
    private long teamId;
    private Integer rank;
    private Boolean scouted;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EventTeamDao myDao;

    private Event event;
    private Long event__resolvedKey;

    private Team team;
    private Long team__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    public final static String TAG = "EventTeam";
    // KEEP FIELDS END

    public EventTeam() {
    }

    public EventTeam(Long id) {
        this.id = id;
    }

    public EventTeam(Long id, java.util.Date modified, long eventId, long teamId, Integer rank, Boolean scouted) {
        this.id = id;
        this.modified = modified;
        this.eventId = eventId;
        this.teamId = teamId;
        this.rank = rank;
        this.scouted = scouted;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventTeamDao() : null;
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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getScouted() {
        return scouted;
    }

    public void setScouted(Boolean scouted) {
        this.scouted = scouted;
    }

    /** To-one relationship, resolved on first access. */
    public Event getEvent() {
        long __key = this.eventId;
        if (event__resolvedKey == null || !event__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventDao targetDao = daoSession.getEventDao();
            Event eventNew = targetDao.load(__key);
            synchronized (this) {
                event = eventNew;
            	event__resolvedKey = __key;
            }
        }
        return event;
    }

    public void setEvent(Event event) {
        if (event == null) {
            throw new DaoException("To-one property 'eventId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.event = event;
            eventId = event.getId();
            event__resolvedKey = eventId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Team getTeam() {
        long __key = this.teamId;
        if (team__resolvedKey == null || !team__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeamDao targetDao = daoSession.getTeamDao();
            Team teamNew = targetDao.load(__key);
            synchronized (this) {
                team = teamNew;
            	team__resolvedKey = __key;
            }
        }
        return team;
    }

    public void setTeam(Team team) {
        if (team == null) {
            throw new DaoException("To-one property 'teamId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.team = team;
            teamId = team.getId();
            team__resolvedKey = teamId;
        }
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
                getScouted() == ((EventTeam)data).getScouted();
    }
    public int compareTo(EventTeam another) {
        int compare = this.getRank() - another.getRank();
        if(0==compare) {
            compare = this.getTeam().compareTo(another.getTeam());
        }
        return compare;
    }
    // KEEP METHODS END

}
