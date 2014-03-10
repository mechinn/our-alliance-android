package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;

public abstract class AOurAllianceData extends Model implements Serializable {
    public static final String TAG = "AOurAllianceData";
	private static final long serialVersionUID = -3683063632828709652L;
    public static final String _ID = BaseColumns._ID;
//    public static final String _COUNT = BaseColumns._COUNT;
    public static final String MODIFIED = "modified";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    @AutoIncrementPrimaryKey
    @Column(_ID)
	private long _id;
    @Column(MODIFIED)
	private Date modified;
	public AOurAllianceData() {
	}
	public AOurAllianceData(long id) {
		this.setId(id);
	}
	public AOurAllianceData(long id, Date mod) {
		this.setId(id);
		this.setModified(mod);
	}
	public long getId() {
		return _id;
	}
	public void setId(long id) {
        this._id = id;
	}
	public Date getModified() {
		if(null==this.modified) {
            return new Date(0);
        }
        return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String toString() {
		return getId()+" "+getModified();
	}
	public boolean equals(AOurAllianceData data) {
		return getId()==data.getId() && getModified().equals(data.getModified());
	}
    protected void beforeSave() {
        setModified(new Date());
    }
    public abstract AOurAllianceData validate();

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        if(getId()<1) {
            AOurAllianceData item = validate();
            if(null!=item) {
                Log.d(TAG, "import mod: " + item.getModified()+" sql mod: "+this.getModified());
                if(this.getModified().after(item.getModified())) {
                    return false;
                }
                Log.d(TAG, "id: " + getId());
                this.setId(item.getId());
            }
        }
        return true;
    }
}
