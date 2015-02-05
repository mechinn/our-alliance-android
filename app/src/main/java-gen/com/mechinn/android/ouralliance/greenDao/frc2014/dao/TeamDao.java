package com.mechinn.android.ouralliance.greenDao.frc2014.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.mechinn.android.ouralliance.greenDao.frc2014.Team;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TEAM.
*/
public class TeamDao extends AbstractDao<Team, Long> {

    public static final String TABLENAME = "TEAM";

    /**
     * Properties of entity Team.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Modified = new Property(1, java.util.Date.class, "modified", false, "MODIFIED");
        public final static Property TeamNumber = new Property(2, Integer.class, "teamNumber", false, "TEAM_NUMBER");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
    };

    private DaoSession daoSession;


    public TeamDao(DaoConfig config) {
        super(config);
    }
    
    public TeamDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TEAM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MODIFIED' INTEGER," + // 1: modified
                "'TEAM_NUMBER' INTEGER," + // 2: teamNumber
                "'NICKNAME' TEXT);"); // 3: nickname
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TEAM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Team entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date modified = entity.getModified();
        if (modified != null) {
            stmt.bindLong(2, modified.getTime());
        }
 
        Integer teamNumber = entity.getTeamNumber();
        if (teamNumber != null) {
            stmt.bindLong(3, teamNumber);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
    }

    @Override
    protected void attachEntity(Team entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Team readEntity(Cursor cursor, int offset) {
        Team entity = new Team( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // modified
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // teamNumber
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // nickname
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Team entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setModified(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setTeamNumber(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Team entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Team entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}