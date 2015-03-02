package com.mechinn.android.ouralliance.data;

import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public abstract class OurAllianceObject extends Model {
    public final static String TAG = "OurAllianceObject";
    public final static String ID = BaseColumns._ID;
    public final static String MODIFIED = "modified";
    @Column(name = MODIFIED, notNull = true)
    private Date modified;
    public Date getModified() {
        return modified;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }
    public void saveMod() {
        setModified(new Date());
        Log.d(TAG, "saving object");
        save();
    }
    public abstract void asyncSave();
    public abstract void asyncDelete();
}
