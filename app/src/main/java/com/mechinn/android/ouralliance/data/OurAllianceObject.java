package com.mechinn.android.ouralliance.data;

import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

public abstract class OurAllianceObject extends Model {
    public final static String TAG = "OurAllianceObject";
    public final static String ID = BaseColumns._ID;
    public final static String MODIFIED = "modified";
    @Column(name = MODIFIED, notNull = true)
    private Date modified;
    private boolean changed;
    public OurAllianceObject() {
        super();
        changed = false;
    }
    public Date getModified() {
        return modified;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }
    public boolean copy(OurAllianceObject data) {
        if(this.equals(data)) {
            this.setModified(data.getModified());
            return true;
        }
        return false;
    }
    protected void changedData() {
        changed = true;
    }
    public void saveMod() {
        if(changed || null==getModified()) {
            if(null==this.getId()) {
                setModified(new Date(0));
            } else {
                setModified(new Date());
            }
        }
        Timber.d("saving object");
        save();
    }
    public void saveEvent() {
        EventBus.getDefault().post(this);
    }
    public abstract void asyncSave();
    public abstract void asyncDelete();
}
