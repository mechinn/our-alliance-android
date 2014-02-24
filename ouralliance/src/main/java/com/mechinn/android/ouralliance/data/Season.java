package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Season extends AOurAllianceData implements Comparable<Season> {
	public static final String TAG = Season.class.getSimpleName();
	private static final long serialVersionUID = -7570760453585739510L;
	public static final String YEAR = "year";
	public static final String TITLE = "title";

    @Column
    @UniqueCombo
    @Check("year > 0")
	private int year;
    @Column
    @NotNull
    private CharSequence title;

	public Season() {
		super();
	}
    public Season(long id) {
        super(id);
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
		int yearCompare = this.getYear() - another.getYear();
        if(0==yearCompare) {
            return this.getTitle().toString().compareToIgnoreCase(another.getTitle().toString());
        } else {
            return yearCompare;
        }
	}
	public boolean equals(Season data) {
		return super.equals(data) &&
				getYear()==data.getYear() &&
				getTitle().equals(data.getTitle());
	}
}
