package com.mechinn.android.ouralliance.greenDao;

import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.dao.MultimediaDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table MULTIMEDIA.
 */
public class Multimedia extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Multimedia>, java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    private int teamNumber;
    private String type;
    private String key;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MultimediaDao myDao;


    // KEEP FIELDS - put your custom fields here
    public final static String TAG = "Multimedia";
    // KEEP FIELDS END

    public Multimedia() {
    }

    public Multimedia(Long id) {
        this.id = id;
    }

    public Multimedia(Long id, java.util.Date modified, int teamNumber, String type, String key) {
        this.id = id;
        this.modified = modified;
        this.teamNumber = teamNumber;
        this.type = type;
        this.key = key;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMultimediaDao() : null;
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

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
