package com.mechinn.android.ouralliance.data;

import java.util.Date;

import android.content.ContentValues;

public abstract class AOurAllianceData {
	private long id;
	private Date modified;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public AOurAllianceData() {
		
	}
	public AOurAllianceData(long id, Date mod) {
		this.setId(id);
		this.setModified(mod);
	}
	public abstract ContentValues toCV();
}
