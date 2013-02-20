package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

@JsonIgnoreProperties({"id","mod"})
public class Team extends AOurAllianceData implements Comparable<Team> {
	private static final long serialVersionUID = 6981108401294045422L;
	public static final String CLASS = "Team";
	public static final String TABLE = "team";
	public static final String NUMBER = "number";
	public static final String NAME = "name";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, NUMBER, NAME };

    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_NUMBER = TABLE+NUMBER;
    public static final String VIEW_NAME = TABLE+NAME;

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);
	
	private int number;
	private CharSequence name;
	public Team() {
		super();
	}
	public Team(int number) {
		this.setNumber(number);
	}
	public Team(int number, CharSequence name) {
		setData(number, name);
	}
	public Team(long id, Date mod, int number, CharSequence name) {
		super(id, mod);
		setData(number, name);
	}
	private void setData(int number, CharSequence name) {
		this.setNumber(number);
		this.setName(name);
	}
	public int getNumber() {
		return number;
	}
	public void setNumberString(CharSequence number) {
		try {
			setNumber(Integer.parseInt(number.toString()));
		} catch (Exception e) {
			setNumber(0);
		}
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public CharSequence getName() {
		if(null==name) {
			return "";
		}
		return name;
	}
	public void setName(CharSequence name) {
		this.name = name;
	}
	public String toString() {
		return this.getNumber()+": "+this.getName();
	}
	public int compareTo(Team another) {
		return this.getNumber() - another.getNumber();
	}
	public boolean equals(Team data) {
		return super.equals(data) &&
				getNumber()==data.getNumber() &&
				getName().equals(getName());
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Team.NUMBER, this.getNumber());
		if(TextUtils.isEmpty(this.getName())) {
			values.putNull(Team.NAME);
		} else {
			values.put(Team.NAME, this.getName().toString());
		}
		return values;
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(0==this.getNumber()) {
			error.add(NUMBER);
		}
		if(TextUtils.isEmpty(this.getName())) {
//			error += NAME;
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setNumber(cursor.getInt(cursor.getColumnIndexOrThrow(NUMBER)));
		setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
	}
	
	public static Team newFromCursor(Cursor cursor) {
		Team data = new Team();
		data.fromCursor(cursor);
		return data;
	}
	
	public static Team newFromViewCursor(Cursor cursor) {
		long teamId = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date teamMod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_NUMBER));
		String teamName = cursor.getString(cursor.getColumnIndexOrThrow(VIEW_NAME));
		return new Team(teamId, teamMod, teamNumber, teamName);
	}
}
