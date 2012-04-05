package com.mechinn.android.ouralliance.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	private final String logTag = "Prefs";
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
		return prefs.getInt("port", 21);
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
	public String getScouter() {
		return prefs.getString("scouter", "");
	}
	 
	/**
	 * store the first run
	 */
	public void setScouter(String scouter) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("scouter", scouter);
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
