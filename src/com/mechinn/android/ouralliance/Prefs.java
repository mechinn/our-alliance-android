package com.mechinn.android.ouralliance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class Prefs {
	private SharedPreferences prefs;
	private int currentVersion;
	private String dbSetupPref;
	private String dbSetupDefault;
	private String seasonPref;
	private String seasonDefault;
	private String compPref;
	private String compDefault;
	private String versionPref;
	private String versionDefault;
	
	public Prefs(Activity activity) {
		this.dbSetupPref = activity.getString(R.string.pref_resetDB);
		this.dbSetupDefault = activity.getString(R.string.pref_resetDB_default);
		this.seasonPref = activity.getString(R.string.pref_season);
		this.seasonDefault = activity.getString(R.string.pref_season_default);
		this.compPref = activity.getString(R.string.pref_comp);
		this.compDefault = activity.getString(R.string.pref_comp_default);
		this.versionPref = activity.getString(R.string.pref_about);
		this.versionDefault = activity.getString(R.string.pref_about_default);
		try {
			this.currentVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			this.currentVersion = 0;
		};
		prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
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
	public long getSeason() {
		return Long.parseLong(prefs.getString(seasonPref, seasonDefault));
	}
	public long getComp() {
		return Long.parseLong(prefs.getString(compPref, compDefault));
	}
	public int getVersion() {
		return Integer.parseInt(prefs.getString(versionPref, versionDefault));
	}
	public void setVersion(int version) {
		Editor editor = prefs.edit();
		editor.putString(versionPref, Integer.toString(version));
		editor.apply();
	}
}
