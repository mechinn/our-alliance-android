package com.mechinn.android.ouralliance.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	private SharedPreferences prefs;
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public int getDBVersion() {
		return prefs.getInt("DBVersion", 0);
	}
	 
	/**
	 * store the first run
	 */
	public void setDBVersion(int version) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("DBVersion", version);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public String getCompetition() {
		return prefs.getString("competition", "CT");
	}
	 
	/**
	 * store the first run
	 */
	public void setCompetition(String competition) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("competition", competition);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public boolean getAllTeams() {
		return prefs.getBoolean("allTeams", false);
	}
	
	public void setAllTeams() {
		setAllTeams(!getAllTeams());
	}
	 
	/**
	 * store the first run
	 */
	public void setAllTeams(Boolean allTeams) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("allTeams", allTeams);
		edit.commit();
	}
	
	public Prefs(Context con) {
		Context mContext = con.getApplicationContext();
		prefs = mContext.getSharedPreferences("com.mechinn.android.frcscouting", 0); //0 = mode private. only this app can read these preferences
	}
}
