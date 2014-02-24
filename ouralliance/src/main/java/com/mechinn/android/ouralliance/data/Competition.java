package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Competition extends AOurAllianceData implements Comparable<Competition> {
	public static final String TAG = Competition.class.getSimpleName();
	private static final long serialVersionUID = -5179493838272851750L;
	public static final String SEASON = Season.TAG;
    public static final String NAME = "name";
    public static final String CODE = "code";

    @Column
    @UniqueCombo
    @ForeignKey("Season(_id)")
    @CascadeDelete
    @Check("season > 0")
	private Season season;
    @Column
    @UniqueCombo
    @NotNull
	private CharSequence name;
    @Column
    @NotNull
	private CharSequence code;

	public Competition() {
		super();
	}
    public Competition(long id) {
        super(id);
    }
	public Competition(Season season, CharSequence name, CharSequence code) {
		setData(season, name, code);
	}
	public Competition(long id, Date mod, Season season, CharSequence name, CharSequence code) {
		super(id, mod);
		setData(season, name, code);
	}
	private void setData(Season season, CharSequence name, CharSequence code) {
		this.setSeason(season);
		this.setName(name);
		this.setCode(code);
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public CharSequence getName() {
		return name;
	}
	public void setName(CharSequence name) {
		this.name = name;
	}
	public CharSequence getCode() {
		return code;
	}
	public void setCode(CharSequence code) {
		this.code = code;
	}
	public String toString() {
		return ""+this.name;
	}
	public boolean equals(Competition data) {
		return super.equals(data) && 
				getSeason()==data.getSeason() &&
				getName().equals(data.getName()) && 
				getCode().equals(data.getCode());
	}
	public int compareTo(Competition another) {
		return this.getName().toString().compareTo(another.getName().toString());
	}
}