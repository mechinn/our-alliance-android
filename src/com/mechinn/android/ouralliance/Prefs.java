package com.mechinn.android.ouralliance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefs {
	private static final String DBSETUP = "dbSetup";
	SharedPreferences prefs;
	public Prefs(Activity activity) {
		prefs = activity.getPreferences(Context.MODE_PRIVATE);
	}
	public void setDbSetup(Boolean setup) {
		Editor editor = prefs.edit();
		editor.putBoolean(DBSETUP, setup);
		editor.apply();
	}
	public boolean getDbSetup() {
		return prefs.getBoolean(DBSETUP, false);
	}
}
