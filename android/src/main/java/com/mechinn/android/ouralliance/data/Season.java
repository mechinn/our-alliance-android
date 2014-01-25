package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class Season extends AOurAllianceData implements Comparable<Season> {
	public static final String TAG = Season.class.getSimpleName();
	private static final long serialVersionUID = -7570760453585739510L;
	public static final String TABLE = "season";
	public static final String YEAR = "year";
	public static final String TITLE = "title";
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, YEAR, TITLE };

    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_YEAR = TABLE+YEAR;
    public static final String VIEW_TITLE = TABLE+TITLE;

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+TAG;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);

	public static final String TITLE_YEAR = "Season Year";
	public static final String TITLE_TITLE = "Season Title";
	
	private int year;
	private CharSequence title;
	public Season() {
		super();
	}
	public Season(int year) {
		this.setYear(year);
	}
	public Season(int year, CharSequence title) {
		setData(year, title);
	}
	public Season(long id, Date mod, int year, CharSequence title) {
		super(id, mod);
		setData(year, title);
	}
	private void setData(int year, CharSequence title) {
		this.setYear(year);
		this.setTitle(title);
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public CharSequence getTitle() {
		if(null==title) {
			return "";
		}
		return title;
	}
	public void setTitle(CharSequence title) {
		this.title = title;
	}
	public String toString() {
		return getYear()+": "+getTitle();
	}
	public int compareTo(Season another) {
		return this.getYear() - another.getYear();
	}
	public boolean equals(Season data) {
		return super.equals(data) &&
				getYear()==data.getYear() &&
				getTitle().equals(data.getTitle());
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(Season.YEAR, this.getYear());
		if(TextUtils.isEmpty(this.getTitle())) {
			values.putNull(Season.TITLE);
		} else {
			values.put(Season.TITLE, this.getTitle().toString());
		}
		return values;
	}
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(0==this.getYear()) {
			error.add(YEAR);
		}
		if(TextUtils.isEmpty(this.getTitle())) {
//			error += TITLE;
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setYear(cursor.getInt(cursor.getColumnIndexOrThrow(YEAR)));
		setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
	}

	@Override
	public String[] toStringArray() {
		return new String[] {Integer.toString(getYear()),
				getTitle().toString()};
	}
	
	public static Season newFromCursor(Cursor cursor) {
		Season data = new Season();
		data.fromCursor(cursor);
		return data;
	}
	
	public static Season newFromViewCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		int year = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_YEAR));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(VIEW_TITLE));
		return new Season(id, mod, year, title);
	}
}
