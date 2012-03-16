package com.mechinn.android.myalliance.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	private SharedPreferences prefs;
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public boolean getFirstRun() {
		return prefs.getBoolean("firstRun", true);
	}
	 
	/**
	 * store the first run
	 */
	public void setRunned() {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public String getHost() {
		return prefs.getString("host", "");
	}
	 
	/**
	 * store the first run
	 */
	public void setHost(String host) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("host", host);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public int getPort() {
		return prefs.getInt("port", 3306);
	}
	 
	/**
	 * store the first run
	 */
	public void setPort(int port) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("port", port);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public String getUser() {
		return prefs.getString("user", "");
	}
	 
	/**
	 * store the first run
	 */
	public void setUser(String user) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("user", user);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public String getPass() {
		return prefs.getString("pass", "");
	}
	 
	/**
	 * store the first run
	 */
	public void setPass(String pass) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("pass", pass);
		edit.commit();
	}
	
	/**
	 * get if this is the first run
	 *
	 * @return returns true, if this is the first run
	 */
	public String getCompetition() {
		return prefs.getString("competition", "");
	}
	 
	/**
	 * store the first run
	 */
	public void setCompetition(String competition) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("competition", competition);
		edit.commit();
	}
	
	public Prefs(Context con) {
		Context mContext = con.getApplicationContext();
		prefs = mContext.getSharedPreferences("com.mechinn.android.frcscouting", 0); //0 = mode private. only this app can read these preferences
	}
}
