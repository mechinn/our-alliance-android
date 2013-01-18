package com.mechinn.android.ouralliance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefs {
	String dbSetupPref;
	String seasonPref;
	SharedPreferences prefs;
	public Prefs(Activity activity) {
		this.dbSetupPref = activity.getString(R.string.pref_resetDB);
		this.seasonPref = activity.getString(R.string.pref_season);
		prefs = activity.getPreferences(Context.MODE_PRIVATE);
	}
	public void setDbSetup(Boolean setup) {
		Editor editor = prefs.edit();
		editor.putBoolean(dbSetupPref, setup);
		editor.apply();
	}
	public boolean getDbSetup() {
		return prefs.getBoolean(dbSetupPref, false);
	}
	public void setSeason(int season) {
		Editor editor = prefs.edit();
		editor.putInt(seasonPref, season);
		editor.apply();
	}
	public int getSeason() {
		return prefs.getInt(seasonPref, 0);
	}
}
