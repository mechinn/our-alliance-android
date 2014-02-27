package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;

public abstract class AOurAllianceData extends Model implements Serializable {
    public static final String TAG = AOurAllianceData.class.getSimpleName();
	private static final long serialVersionUID = -3683063632828709652L;
    public static final String _ID = BaseColumns._ID;
//    public static final String _COUNT = BaseColumns._COUNT;
    public static final String MODIFIED = "modified";

    @AutoIncrementPrimaryKey
    @Column("_id")
	private long _id;
    @Column("modified")
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
}
