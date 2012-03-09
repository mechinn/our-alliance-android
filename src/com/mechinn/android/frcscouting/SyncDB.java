package com.mechinn.android.frcscouting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/frcscouting", user, pass);
		} else {
			throw new SQLException("Preferences not set");
		}
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void update(int team, Cursor teamInfo) throws SQLException {
		String sql = "UPDATE "+TeamInfoDb.DATABASE_TABLE+" SET "+
		TeamInfoDb.KEY_LASTMOD+" = ?, "+
		TeamInfoDb.KEY_TEAM+" = ?, "+
		TeamInfoDb.KEY_ORIENTATION+" = ?, "+
		TeamInfoDb.KEY_NUMWHEELS+" = ?, "+
		TeamInfoDb.KEY_WHEEL1TYPE+" = ?, "+
		TeamInfoDb.KEY_WHEEL1DIAMETER+" = ?, "+
		TeamInfoDb.KEY_WHEEL2TYPE+" = ?, "+
		TeamInfoDb.KEY_WHEEL2DIAMETER+" = ?, "+
		TeamInfoDb.KEY_DEADWHEELTYPE+" = ?, "+
		TeamInfoDb.KEY_TURRET+" = ?, "+
		TeamInfoDb.KEY_TRACKING+" = ?, "+
		TeamInfoDb.KEY_FENDER+" = ?, "+
		TeamInfoDb.KEY_KEY+" = ?, "+
		TeamInfoDb.KEY_BARRIER+" = ?, "+
		TeamInfoDb.KEY_CLIMB+" = ?, "+
		TeamInfoDb.KEY_NOTES+" = ?, "+
		TeamInfoDb.KEY_AUTONOMOUS+" = ? "+
		"WHERE team = "+team;
		PreparedStatement prest = conn.prepareStatement(sql);
		
		teamInfo.moveToFirst();
		
		prest.setLong(1,System.currentTimeMillis()/1000);
		prest.setInt(2,team);
		prest.setString(3,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDb.KEY_ORIENTATION)));
		prest.setInt(4,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_NUMWHEELS)));
		prest.setString(5,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDb.KEY_WHEEL1TYPE)));
		prest.setInt(6,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_WHEEL1DIAMETER)));
		prest.setString(7,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDb.KEY_WHEEL2TYPE)));
		prest.setInt(8,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_WHEEL2DIAMETER)));
		prest.setString(9,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDb.KEY_DEADWHEELTYPE)));
		prest.setInt(10,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_TURRET)));
		prest.setInt(11,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_TRACKING)));
		prest.setInt(12,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_FENDER)));
		prest.setInt(13,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_KEY)));
		prest.setInt(14,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_BARRIER)));
		prest.setInt(15,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_CLIMB)));
		prest.setString(16,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDb.KEY_NOTES)));
		prest.setInt(17,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_AUTONOMOUS)));
		Log.d("update",prest.toString());
		prest.executeUpdate();
		Log.d("update","updated");
	}

	public void getTeam(int team, TeamInfoDb teamDB) throws SQLException {
		Cursor teamInfo = teamDB.fetchTeam(team);
		String sql = "SELECT * FROM teams WHERE team = "+team;
//		Log.d("getTeam sql",sql);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			Log.d("getTeam","check");
			//if server has a newer last mod than ours
			if(rs.getInt(TeamInfoDb.KEY_LASTMOD)>teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDb.KEY_LASTMOD))) {
				Log.d("getTeam","updating");
				teamDB.updateTeam(team, 
						rs.getInt(TeamInfoDb.KEY_LASTMOD), 
						rs.getString(TeamInfoDb.KEY_ORIENTATION), 
						rs.getInt(TeamInfoDb.KEY_NUMWHEELS), 
						rs.getInt(TeamInfoDb.KEY_WHEELTYPES), 
						rs.getInt(TeamInfoDb.KEY_DEADWHEEL), 
						rs.getString(TeamInfoDb.KEY_WHEEL1TYPE), 
						rs.getInt(TeamInfoDb.KEY_WHEEL1DIAMETER), 
						rs.getString(TeamInfoDb.KEY_WHEEL2TYPE), 
						rs.getInt(TeamInfoDb.KEY_WHEEL2DIAMETER), 
						rs.getString(TeamInfoDb.KEY_DEADWHEELTYPE), 
						rs.getInt(TeamInfoDb.KEY_TURRET), 
						rs.getInt(TeamInfoDb.KEY_TRACKING), 
						rs.getInt(TeamInfoDb.KEY_FENDER), 
						rs.getInt(TeamInfoDb.KEY_KEY), 
						rs.getInt(TeamInfoDb.KEY_BARRIER), 
						rs.getInt(TeamInfoDb.KEY_CLIMB), 
						rs.getString(TeamInfoDb.KEY_NOTES), 
						rs.getInt(TeamInfoDb.KEY_AUTONOMOUS));
			}
		}
		teamInfo.close();
		rs.close();
		st.close();
	}
}
