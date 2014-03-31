package com.mechinn.android.ouralliance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class Prefs {
    public static final String TAG = "Prefs";
	private SharedPreferences prefs;
	private int currentVersion;
	private String dbSetupPref;
	private String dbSetupDefault;
	private String compPref;
	private String compDefault;
	private String measurePref;
	private String measureDefault;
	private String versionPref;
	private String versionDefault;
	private String practicePref;
	private String practiceDefault;
	private String yearPref;
	private String yearDefault;
	
	public Prefs(Context context) {
		this.dbSetupPref = context.getString(R.string.pref_resetDB);
		this.dbSetupDefault = context.getString(R.string.pref_resetDB_default);
		this.compPref = context.getString(R.string.pref_comp);
		this.compDefault = context.getString(R.string.pref_comp_default);
		this.measurePref = context.getString(R.string.pref_measure);
		this.measureDefault = context.getString(R.string.pref_measure_default);
		this.versionPref = context.getString(R.string.pref_about);
		this.versionDefault = context.getString(R.string.pref_about_default);
		this.practicePref = context.getString(R.string.pref_practice);
		this.practiceDefault = context.getString(R.string.pref_practice_default);
		this.yearPref = context.getString(R.string.pref_year);
		this.yearDefault = context.getString(R.string.pref_year_default);
		try {
			this.currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			this.currentVersion = 0;
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	public int getCurrentVersion() {
		return currentVersion;
	}
	public void clear() {
		Editor editor = prefs.edit();
		editor.clear();
		editor.apply();
	}
	public void setDbSetup(Boolean setup) {
		Editor editor = prefs.edit();
		editor.putString(dbSetupPref, setup?"true":"false");
		editor.apply();
	}
	public boolean getDbSetup() {
		return Boolean.parseBoolean(prefs.getString(dbSetupPref, dbSetupDefault));
	}
	public void setSeason(String year) {
		Editor editor = prefs.edit();
		editor.putString(yearPref, year);
		editor.apply();
	}
	public int getSeason() {
		return Integer.parseInt(prefs.getString(yearPref, yearDefault));
	}
	public long getComp() {
		return Long.parseLong(prefs.getString(compPref, compDefault));
	}
	public String getMeasure() {
		return prefs.getString(measurePref, measureDefault);
	}
	public boolean isInches() {
		return getMeasure().equals(measureDefault);
	}
	public boolean getPractice() {
		return prefs.getBoolean(practicePref, Boolean.parseBoolean(practiceDefault));
	}
	public void setPractice(boolean practice) {
		Editor editor = prefs.edit();
		editor.putBoolean(practicePref, practice);
		editor.apply();
	}
	public int getVersion() {
		return Integer.parseInt(prefs.getString(versionPref, versionDefault));
	}
	public void setVersion(int version) {
		Editor editor = prefs.edit();
		editor.putString(versionPref, Integer.toString(version));
		editor.apply();
	}
	public void setChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}
	public void unsetChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
