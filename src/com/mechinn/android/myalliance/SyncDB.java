package com.mechinn.android.myalliance;

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
		String sql = "UPDATE "+TeamInfoDB.DBTable+" SET "+
		TeamInfoDB.keyLastMod+" = ?, "+
		TeamInfoDB.KEY_TEAM+" = ?, "+
		TeamInfoDB.KEY_ORIENTATION+" = ?, "+
		TeamInfoDB.KEY_NUMWHEELS+" = ?, "+
		TeamInfoDB.KEY_WHEEL1TYPE+" = ?, "+
		TeamInfoDB.KEY_WHEEL1DIAMETER+" = ?, "+
		TeamInfoDB.KEY_WHEEL2TYPE+" = ?, "+
		TeamInfoDB.KEY_WHEEL2DIAMETER+" = ?, "+
		TeamInfoDB.KEY_DEADWHEELTYPE+" = ?, "+
		TeamInfoDB.KEY_TURRET+" = ?, "+
		TeamInfoDB.KEY_TRACKING+" = ?, "+
		TeamInfoDB.KEY_FENDER+" = ?, "+
		TeamInfoDB.KEY_KEY+" = ?, "+
		TeamInfoDB.KEY_BARRIER+" = ?, "+
		TeamInfoDB.KEY_CLIMB+" = ?, "+
		TeamInfoDB.KEY_NOTES+" = ?, "+
		TeamInfoDB.KEY_AUTONOMOUS+" = ? "+
		"WHERE team = "+team;
		PreparedStatement prest = conn.prepareStatement(sql);
		
		teamInfo.moveToFirst();
		
		prest.setLong(1,System.currentTimeMillis()/1000);
		prest.setInt(2,team);
		prest.setString(3,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDB.KEY_ORIENTATION)));
		prest.setInt(4,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_NUMWHEELS)));
		prest.setString(5,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDB.KEY_WHEEL1TYPE)));
		prest.setInt(6,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_WHEEL1DIAMETER)));
		prest.setString(7,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDB.KEY_WHEEL2TYPE)));
		prest.setInt(8,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_WHEEL2DIAMETER)));
		prest.setString(9,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDB.KEY_DEADWHEELTYPE)));
		prest.setInt(10,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_TURRET)));
		prest.setInt(11,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_TRACKING)));
		prest.setInt(12,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_FENDER)));
		prest.setInt(13,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_KEY)));
		prest.setInt(14,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_BARRIER)));
		prest.setInt(15,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_CLIMB)));
		prest.setString(16,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoDB.KEY_NOTES)));
		prest.setInt(17,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.KEY_AUTONOMOUS)));
		Log.d("update",prest.toString());
		prest.executeUpdate();
		Log.d("update","updated");
	}

	public void getTeam(int team, TeamInfoDB teamDB) throws SQLException {
		Cursor teamInfo = teamDB.fetchTeam(team);
		String sql = "SELECT * FROM teams WHERE team = "+team;
//		Log.d("getTeam sql",sql);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			Log.d("getTeam","check");
			//if server has a newer last mod than ours
			if(rs.getInt(TeamInfoDB.keyLastMod)>teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoDB.keyLastMod))) {
				Log.d("getTeam","updating");
				teamDB.updateTeam(team, 
						rs.getInt(TeamInfoDB.keyLastMod), 
						rs.getString(TeamInfoDB.KEY_ORIENTATION), 
						rs.getInt(TeamInfoDB.KEY_NUMWHEELS), 
						rs.getInt(TeamInfoDB.KEY_WHEELTYPES), 
						rs.getInt(TeamInfoDB.KEY_DEADWHEEL), 
						rs.getString(TeamInfoDB.KEY_WHEEL1TYPE), 
						rs.getInt(TeamInfoDB.KEY_WHEEL1DIAMETER), 
						rs.getString(TeamInfoDB.KEY_WHEEL2TYPE), 
						rs.getInt(TeamInfoDB.KEY_WHEEL2DIAMETER), 
						rs.getString(TeamInfoDB.KEY_DEADWHEELTYPE), 
						rs.getInt(TeamInfoDB.KEY_TURRET), 
						rs.getInt(TeamInfoDB.KEY_TRACKING), 
						rs.getInt(TeamInfoDB.KEY_FENDER), 
						rs.getInt(TeamInfoDB.KEY_KEY), 
						rs.getInt(TeamInfoDB.KEY_BARRIER), 
						rs.getInt(TeamInfoDB.KEY_CLIMB), 
						rs.getString(TeamInfoDB.KEY_NOTES), 
						rs.getInt(TeamInfoDB.KEY_AUTONOMOUS));
			}
		}
		teamInfo.close();
		rs.close();
		st.close();
	}
}
