package com.mechinn.android.myalliance.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mechinn.android.myalliance.providers.TeamScoutingProvider;


import android.content.Context;
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
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/frcscouting", user, pass);
		} else {
			throw new SQLException("Preferences not set");
		}
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void update(int team, Cursor teamInfo) throws SQLException {
		String sql = "UPDATE "+TeamScoutingProvider.DBTable+" SET "+
		TeamScoutingProvider.keyLastMod+" = ?, "+
		TeamScoutingProvider.keyTeam+" = ?, "+
		TeamScoutingProvider.keyOrientation+" = ?, "+
		TeamScoutingProvider.keyNumWheels+" = ?, "+
		TeamScoutingProvider.keyWheel1Type+" = ?, "+
		TeamScoutingProvider.keyWheel1Diameter+" = ?, "+
		TeamScoutingProvider.keyWheel2Type+" = ?, "+
		TeamScoutingProvider.keyWheel2Diameter+" = ?, "+
		TeamScoutingProvider.keyDeadWheelType+" = ?, "+
		TeamScoutingProvider.keyTurret+" = ?, "+
		TeamScoutingProvider.keyTracking+" = ?, "+
		TeamScoutingProvider.keyFenderShooter+" = ?, "+
		TeamScoutingProvider.keyKeyShooter+" = ?, "+
		TeamScoutingProvider.keyBarrier+" = ?, "+
		TeamScoutingProvider.keyClimb+" = ?, "+
		TeamScoutingProvider.keyNotes+" = ?, "+
		TeamScoutingProvider.keyAutonomous+" = ? "+
		"WHERE team = "+team;
		PreparedStatement prest = conn.prepareStatement(sql);
		
		teamInfo.moveToFirst();
		
		prest.setLong(1,System.currentTimeMillis()/1000);
		prest.setInt(2,team);
		prest.setString(3,teamInfo.getString(teamInfo.getColumnIndex(TeamScoutingProvider.keyOrientation)));
		prest.setInt(4,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyNumWheels)));
		prest.setString(5,teamInfo.getString(teamInfo.getColumnIndex(TeamScoutingProvider.keyWheel1Type)));
		prest.setInt(6,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyWheel1Diameter)));
		prest.setString(7,teamInfo.getString(teamInfo.getColumnIndex(TeamScoutingProvider.keyWheel2Type)));
		prest.setInt(8,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyWheel2Diameter)));
		prest.setString(9,teamInfo.getString(teamInfo.getColumnIndex(TeamScoutingProvider.keyDeadWheelType)));
		prest.setInt(10,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyTurret)));
		prest.setInt(11,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyTracking)));
		prest.setInt(12,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyFenderShooter)));
		prest.setInt(13,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyKeyShooter)));
		prest.setInt(14,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyBarrier)));
		prest.setInt(15,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyClimb)));
		prest.setString(16,teamInfo.getString(teamInfo.getColumnIndex(TeamScoutingProvider.keyNotes)));
		prest.setInt(17,teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyAutonomous)));
		Log.d("update",prest.toString());
		prest.executeUpdate();
		Log.d("update","updated");
	}

	public void getTeam(int team, TeamScoutingInterface teamInterface) throws SQLException {
		Cursor teamInfo = teamInterface.fetchTeam(team);
		String sql = "SELECT * FROM teams WHERE team = "+team;
//		Log.d("getTeam sql",sql);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			Log.d("getTeam","check");
			//if server has a newer last mod than ours
			if(rs.getInt(TeamScoutingProvider.keyLastMod)>teamInfo.getInt(teamInfo.getColumnIndex(TeamScoutingProvider.keyLastMod))) {
				Log.d("getTeam","updating");
				teamInterface.updateTeam(team, 
						rs.getInt(TeamScoutingProvider.keyLastMod), 
						rs.getString(TeamScoutingProvider.keyOrientation), 
						rs.getInt(TeamScoutingProvider.keyNumWheels), 
						rs.getInt(TeamScoutingProvider.keyWheelTypes), 
						rs.getInt(TeamScoutingProvider.keyDeadWheel), 
						rs.getString(TeamScoutingProvider.keyWheel1Type), 
						rs.getInt(TeamScoutingProvider.keyWheel1Diameter), 
						rs.getString(TeamScoutingProvider.keyWheel2Type), 
						rs.getInt(TeamScoutingProvider.keyWheel2Diameter), 
						rs.getString(TeamScoutingProvider.keyDeadWheelType), 
						rs.getInt(TeamScoutingProvider.keyTurret), 
						rs.getInt(TeamScoutingProvider.keyTracking), 
						rs.getInt(TeamScoutingProvider.keyFenderShooter), 
						rs.getInt(TeamScoutingProvider.keyKeyShooter), 
						rs.getInt(TeamScoutingProvider.keyBarrier), 
						rs.getInt(TeamScoutingProvider.keyClimb), 
						rs.getString(TeamScoutingProvider.keyNotes), 
						rs.getInt(TeamScoutingProvider.keyAutonomous));
			}
		}
		teamInfo.close();
		rs.close();
		st.close();
	}
}
