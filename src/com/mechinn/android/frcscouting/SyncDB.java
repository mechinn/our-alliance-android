package com.mechinn.android.frcscouting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.content.SharedPreferences;

public class SyncDB {
	Connection conn;
	Context context;
	String host;
	String user;
	String pass;
	public SyncDB(Context c) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.driver");
		context = c;
		getPreferences(context);
		if(!host.equals("") && !user.equals("") && !pass.equals("")) {
			String url = "jdbc:postgresql://"+host+":5432/pgdatabase?sslfactory=org.postgresql.ssl.NonValidatingFactory&ssl=true";
			conn = DriverManager.getConnection(url, user, pass);
		} else {
			throw new SQLException("Preferences not set");
		}
	}
	
	/**
	 * setting up preferences storage
	 */
	public void getPreferences(Context context) {
		Context mContext = context.getApplicationContext();
		SharedPreferences prefs = mContext.getSharedPreferences("com.mechinn.android.frcscouting", 0); //0 = mode private. only this app can read these preferences
		host = prefs.getString("host", "");
		user = prefs.getString("user", "");
		pass = prefs.getString("pass", "");
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void query() throws SQLException {
		String sql = "SELECT relname FROM pgclass WHERE relkind = 'r';";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			String realname = rs.getString("relname");
		}
		rs.close();
		st.close();
	}
	
	
}
