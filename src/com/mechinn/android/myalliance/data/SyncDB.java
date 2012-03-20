package com.mechinn.android.myalliance.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mechinn.android.myalliance.providers.TeamInfoProvider;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

/**
 * VERY BAD WORKAROUND FOR SYNC
 * 
 * @author mechinn
 *
 */
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
		String sql = "UPDATE "+TeamInfoProvider.DBTable+" SET "+
		TeamInfoProvider.keyLastMod+" = ?, "+
		TeamInfoProvider.keyTeam+" = ?, "+
		TeamInfoProvider.keyOrientation+" = ?, "+
		TeamInfoProvider.keyNumWheels+" = ?, "+
		TeamInfoProvider.keyWheel1Type+" = ?, "+
		TeamInfoProvider.keyWheel1Diameter+" = ?, "+
		TeamInfoProvider.keyWheel2Type+" = ?, "+
		TeamInfoProvider.keyWheel2Diameter+" = ?, "+
		TeamInfoProvider.keyDeadWheelType+" = ?, "+
		TeamInfoProvider.keyTurret+" = ?, "+
		TeamInfoProvider.keyTracking+" = ?, "+
		TeamInfoProvider.keyFenderShooter+" = ?, "+
		TeamInfoProvider.keyKeyShooter+" = ?, "+
		TeamInfoProvider.keyBarrier+" = ?, "+
		TeamInfoProvider.keyClimb+" = ?, "+
		TeamInfoProvider.keyNotes+" = ?, "+
		TeamInfoProvider.keyAutonomous+" = ? "+
		"WHERE team = "+team;
		PreparedStatement prest = conn.prepareStatement(sql);
		
		teamInfo.moveToFirst();
		
		prest.setLong(1,System.currentTimeMillis()/1000);
		prest.setInt(2,team);
		prest.setString(3,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoProvider.keyOrientation)));
		prest.setInt(4,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyNumWheels)));
		prest.setString(5,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoProvider.keyWheel1Type)));
		prest.setInt(6,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyWheel1Diameter)));
		prest.setString(7,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoProvider.keyWheel2Type)));
		prest.setInt(8,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyWheel2Diameter)));
		prest.setString(9,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoProvider.keyDeadWheelType)));
		prest.setInt(10,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyTurret)));
		prest.setInt(11,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyTracking)));
		prest.setInt(12,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyFenderShooter)));
		prest.setInt(13,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyKeyShooter)));
		prest.setInt(14,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyBarrier)));
		prest.setInt(15,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyClimb)));
		prest.setString(16,teamInfo.getString(teamInfo.getColumnIndex(TeamInfoProvider.keyNotes)));
		prest.setInt(17,teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyAutonomous)));
		Log.d("update",prest.toString());
		prest.executeUpdate();
		Log.d("update","updated");
	}

	public void getTeam(int team, TeamInfoInterface teamInterface) throws SQLException {
		Cursor teamInfo = teamInterface.fetchTeam(team);
		String sql = "SELECT * FROM teams WHERE team = "+team;
//		Log.d("getTeam sql",sql);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			Log.d("getTeam","check");
			//if server has a newer last mod than ours
			if(rs.getInt(TeamInfoProvider.keyLastMod)>teamInfo.getInt(teamInfo.getColumnIndex(TeamInfoProvider.keyLastMod))) {
				Log.d("getTeam","updating");
				teamInterface.updateTeam(team, 
						rs.getInt(TeamInfoProvider.keyLastMod), 
						rs.getString(TeamInfoProvider.keyOrientation), 
						rs.getInt(TeamInfoProvider.keyNumWheels), 
						rs.getInt(TeamInfoProvider.keyWheelTypes), 
						rs.getInt(TeamInfoProvider.keyDeadWheel), 
						rs.getString(TeamInfoProvider.keyWheel1Type), 
						rs.getInt(TeamInfoProvider.keyWheel1Diameter), 
						rs.getString(TeamInfoProvider.keyWheel2Type), 
						rs.getInt(TeamInfoProvider.keyWheel2Diameter), 
						rs.getString(TeamInfoProvider.keyDeadWheelType), 
						rs.getInt(TeamInfoProvider.keyTurret), 
						rs.getInt(TeamInfoProvider.keyTracking), 
						rs.getInt(TeamInfoProvider.keyFenderShooter), 
						rs.getInt(TeamInfoProvider.keyKeyShooter), 
						rs.getInt(TeamInfoProvider.keyBarrier), 
						rs.getInt(TeamInfoProvider.keyClimb), 
						rs.getString(TeamInfoProvider.keyNotes), 
						rs.getInt(TeamInfoProvider.keyAutonomous));
			}
		}
		teamInfo.close();
		rs.close();
		st.close();
	}
}
