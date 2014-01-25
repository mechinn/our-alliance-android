package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public abstract class AOurAllianceData implements Serializable {
	private static final long serialVersionUID = -3683063632828709652L;

	public static final String TAG = AOurAllianceData.class.getSimpleName();
	
	public static final String TITLE_ID = "ID";
	public static final String TITLE_MODIFIED = "Last Modified";
	
	private long id;
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
	public boolean insert() throws OurAllianceException, SQLException {
		if(getId()<0) {
			throw new SQLException("error");
		}
		if(getId()>0) {
			return false;
		}
		throwNulls();
		return true;
	}
	public void update() throws OurAllianceException, SQLException {
		if(getId()>0) {
			throwNulls();
			return;
		}
		throw new SQLException(BaseColumns._ID);
	}
	public long getId() {
		return id;
	}
	public void setIdUri(Uri uri) {
		String id = uri.getLastPathSegment();
//		Log.d(TAG, id);
		long num = Long.parseLong(id);
//		Log.d(TAG, "ID: "+num);
		setId(num);
	}
	public void setId(long id) {
//		Log.d(TAG, "ID: "+id);
		this.id = id;
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
	public void fromCursor(Cursor cursor) {
		setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
		setModified(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Database.MODIFIED))));
	}
	public String[] getStringArrayHeaders() {
		return new String[] {BaseColumns._ID,
				Database.MODIFIED};
	}
	public String[] toStringArray() {
		return new String[] {Long.toString(getId()),
				getModified().toString()};
	}
	public void throwNulls() throws OurAllianceException {
		List<String> error = checkNotNulls();
		if(!error.isEmpty()) {
			String throwing = "";
			int i;
			for(i=0;i<error.size()-1;++i) {
				throwing += error.get(i)+", ";
			}
			throw new OurAllianceException(throwing+error.get(i));
		}
	}
	public abstract ContentValues toCV();
	public abstract List<String> checkNotNulls() ;
}