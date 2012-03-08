package com.mechinn.android.frcscouting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SyncDB {
	Connection conn;
	Context context;
	Prefs prefs;
	String host;
	int port;
	String user;
	String pass;
	
	public SyncDB(Context c) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		context = c;
		prefs = new Prefs(context);
		host = prefs.getHost();
		port = prefs.getPort();
		user = prefs.getUser();
		pass = prefs.getPass();
		if(!host.equals("") && !user.equals("") && !pass.equals("")) {
			String url = "jdbc:mysql://"+host+":"+port+"/frcscouting";
			conn = DriverManager.getConnection("jdbc:mysql://192.168.1.141/frcscouting", "frc", "869");
		} else {
			throw new SQLException("Preferences not set");
		}
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void query() throws SQLException {
		String sql = "SELECT *"+/*TeamInfoDb.KEY_ROWID+", "+TeamInfoDb.KEY_LASTMOD+", "+TeamInfoDb.KEY_TEAM+", "+TeamInfoDb.KEY_ORIENTATION+", "+TeamInfoDb.KEY_NUMWHEELS+", "+TeamInfoDb.KEY_WHEEL1TYPE+", "+
        		TeamInfoDb.KEY_WHEEL1DIAMETER+", "+TeamInfoDb.KEY_WHEEL2TYPE+", "+TeamInfoDb.KEY_WHEEL2DIAMETER+", "+TeamInfoDb.KEY_DEADWHEELTYPE+", "+
        		TeamInfoDb.KEY_TURRET+", "+TeamInfoDb.KEY_TRACKING+", "+TeamInfoDb.KEY_FENDER+", "+TeamInfoDb.KEY_KEY+", "+TeamInfoDb.KEY_BARRIER+", "+
        		TeamInfoDb.KEY_CLIMB+", "+TeamInfoDb.KEY_NOTES+", "+TeamInfoDb.KEY_AUTONOMOUS+*/" FROM teams WHERE team = '869';";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_ROWID)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_LASTMOD)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_TEAM)));
			Log.d("mySQL",rs.getString(TeamInfoDb.KEY_ORIENTATION));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_NUMWHEELS)));
			Log.d("mySQL",rs.getString(TeamInfoDb.KEY_WHEEL1TYPE));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_WHEEL1DIAMETER)));
			Log.d("mySQL",rs.getString(TeamInfoDb.KEY_WHEEL2TYPE));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_WHEEL2DIAMETER)));
			Log.d("mySQL",rs.getString(TeamInfoDb.KEY_DEADWHEELTYPE));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_TURRET)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_TRACKING)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_FENDER)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_KEY)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_BARRIER)));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_CLIMB)));
			Log.d("mySQL",rs.getString(TeamInfoDb.KEY_NOTES));
			Log.d("mySQL",Integer.toString(rs.getInt(TeamInfoDb.KEY_AUTONOMOUS)));
		}
		rs.close();
		st.close();
	}
}
