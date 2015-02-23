package com.mechinn.android.ouralliance.data;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

import de.greenrobot.event.util.AsyncExecutor;

public abstract class OurAllianceObject extends Model {
    public final static String TAG = "OurAllianceObject";
    public final static Column.ConflictAction UNIQUE = Column.ConflictAction.FAIL;
    public final static String ID = BaseColumns._ID;
    public final static String MODIFIED = "modified";
    @Column(name = MODIFIED, notNull = true)
    private Date modified;
    public Date getModified() {
        return new Date();
    }
    public Date lastModified() {
        return modified;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                save();
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
            }
        });
    }
}
