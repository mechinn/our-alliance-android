package com.mechinn.android.ouralliance;

import java.io.File;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class Setup {
	private static final String TAG = "Setup";
	private static final int VERSION = 1;
	private Prefs prefs;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private TeamScoutingDataSource teamScoutingData;
	private CompetitionTeamDataSource competitionTeamData;
	private String packageName;
	private File dbPath;
	
	public Setup(Activity activity) {
		prefs = new Prefs(activity);
		teamData = new TeamDataSource(activity);
		seasonData = new SeasonDataSource(activity);
		competitionData = new CompetitionDataSource(activity);
		teamScoutingData = new TeamScoutingDataSource(activity);
		competitionTeamData = new CompetitionTeamDataSource(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("ourAlliance.sqlite");
	}
	
	public void reset() {
		prefs.clear();
		run();
	}
	
	public void run() {
		int version = prefs.getVersion();
		Log.d(TAG, "version: "+version);
		switch(version+1) {
			case 1:
		        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			        File externalPath = Environment.getExternalStorageDirectory();
			        File picDir = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
			        Utility.deleteRecursive(picDir);
//			        picDir = new File(picDir.getAbsolutePath()+"/teamPic/2012/"+Integer.toString(team)+"/");
			        if(!picDir.exists()) {
			        	picDir.mkdirs();
			        }
		        }

				if(SQLiteDatabase.deleteDatabase(dbPath)) {
					Log.d(TAG,"deleted db");
				} else {
					Log.d(TAG,"did not delete db");
				}
			default:
		}
		switch(version+1) {
			case 1:
				try {
					seasonData.insert(new Season(2013, "Ultimate Ascent"));
					Season thisSeason = seasonData.get(2013);
					Team team869 = teamData.insert(new Team(869, "Power Cord"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team869));
					Team team1676 = teamData.insert(new Team(1676, "Pioneers"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team1676));
					Team team3637 = teamData.insert(new Team(3637, "The Daleks"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team3637));
					Team team25 = teamData.insert(new Team(25, "Raider Robotix"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team25));
					Team team75 = teamData.insert(new Team(75, "RoboRaiders"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team75));
					Team team11 = teamData.insert(new Team(11, "Chief Delphi"));
					teamScoutingData.insert(new TeamScouting(thisSeason, team11));
					this.addCompetitions(thisSeason);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (OurAllianceException e) {
					e.printStackTrace();
				}
			default:
		}
		competitionTeamData.close();
		teamScoutingData.close();
		competitionData.close();
		seasonData.close();
		teamData.close();
		prefs.setVersion(VERSION);
	}
	
	private void addCompetitions(Season season) throws IllegalArgumentException, OurAllianceException {
		competitionData.insert(new Competition(season, "Alamo Regional", "STX"));
		competitionData.insert(new Competition(season, "BAE Systems Granite State Regional", "NH"));
		competitionData.insert(new Competition(season, "Greater Kansas City Regional", "KC"));
		competitionData.insert(new Competition(season, "Hatboro-Horsham FIRST Robotics District Competition", "PAH"));
		competitionData.insert(new Competition(season, "Kettering University FIRST Robotics District Competition", "GG"));
		competitionData.insert(new Competition(season, "Gull Lake FIRST Robotics District Competition", "MIGL"));
		competitionData.insert(new Competition(season, "Smoky Mountains Regional", "TN"));
		competitionData.insert(new Competition(season, "San Diego Regional", "SDC"));
		competitionData.insert(new Competition(season, "Israel Regional", "IS"));
		competitionData.insert(new Competition(season, "Autodesk Oregon Regional", "OR"));
		competitionData.insert(new Competition(season, "Chesapeake Regional", "MD"));
		competitionData.insert(new Competition(season, "Chestnut Hill FIRST Robotics District Competition", "PHL"));
		competitionData.insert(new Competition(season, "Finger Lakes Regional", "ROC"));
		competitionData.insert(new Competition(season, "Greater Toronto East Regional", "ON"));
		competitionData.insert(new Competition(season, "Lake Superior Regional", "DMN"));
		competitionData.insert(new Competition(season, "Orlando Regional", "FL"));
		competitionData.insert(new Competition(season, "Pittsburgh Regional", "PIT"));
		competitionData.insert(new Competition(season, "Traverse City FIRST Robotics District Competition", "GT"));
		competitionData.insert(new Competition(season, "Waterford FIRST Robotics District Competition", "OC1"));
		competitionData.insert(new Competition(season, "WPI Regional", "WOR"));
		competitionData.insert(new Competition(season, "Rutgers University FIRST Robotics District Competition", "NJ"));
		competitionData.insert(new Competition(season, "Bayou Regional", "LA"));
		competitionData.insert(new Competition(season, "Boilermaker Regional", "IN"));
		competitionData.insert(new Competition(season, "Detroit FIRST Robotics District Competition", "DT"));
		competitionData.insert(new Competition(season, "Festival de Robotique FRC a Montreal Regional", "QC"));
		competitionData.insert(new Competition(season, "Los Angeles Regional", "CA"));
		competitionData.insert(new Competition(season, "Peachtree Regional", "GA"));
		competitionData.insert(new Competition(season, "Sacramento Regional", "SAC"));
		competitionData.insert(new Competition(season, "Utah Regional co-sponsored by NASA and Platt", "UT"));
		competitionData.insert(new Competition(season, "Virginia Regional", "VA"));
		competitionData.insert(new Competition(season, "West Michigan FIRST Robotics District Competition", "MI"));
		competitionData.insert(new Competition(season, "New York City Regional", "NY"));
		competitionData.insert(new Competition(season, "Arizona Regional", "AZ"));
		competitionData.insert(new Competition(season, "Boston Regional", "MA"));
		competitionData.insert(new Competition(season, "Hawaii Regional sponsored by BAE Systems", "HI"));
		competitionData.insert(new Competition(season, "Buckeye Regional", "OH"));
		competitionData.insert(new Competition(season, "Colorado Regional", "CO"));
		competitionData.insert(new Competition(season, "Lenape FIRST Robotics District Competition", "NJT"));
		competitionData.insert(new Competition(season, "Midwest Regional", "IL"));
		competitionData.insert(new Competition(season, "Niles FIRST Robotics District Competition", "SWM"));
		competitionData.insert(new Competition(season, "Northville FIRST Robotics District Competition", "WCA"));
		competitionData.insert(new Competition(season, "Palmetto Regional", "SC"));
		competitionData.insert(new Competition(season, "Seattle Cascade Regional", "WA2"));
		competitionData.insert(new Competition(season, "Seattle Olympic Regional", "WA"));
		competitionData.insert(new Competition(season, "St. Louis Regional", "MO"));
		competitionData.insert(new Competition(season, "Waterloo Regional", "WAT"));
		competitionData.insert(new Competition(season, "Wisconsin Regional", "WI"));
		competitionData.insert(new Competition(season, "Dallas East Regional sponsored by jcpenney", "DA"));
		competitionData.insert(new Competition(season, "Dallas West Regional sponsored by jcpenney", "DA2"));
		competitionData.insert(new Competition(season, "Greater Toronto West Regional", "ON2"));
		competitionData.insert(new Competition(season, "Livonia FIRST Robotics District Competition", "WW"));
		competitionData.insert(new Competition(season, "Minnesota 10000 Lakes Regional", "MN"));
		competitionData.insert(new Competition(season, "Minnesota North Star Regional", "MN2"));
		competitionData.insert(new Competition(season, "Mount Olive FIRST Robotics District Competition", "NJF"));
		competitionData.insert(new Competition(season, "Northeast Utilities FIRST Connecticut Regional", "CT"));
		competitionData.insert(new Competition(season, "Oklahoma Regional", "OK"));
		competitionData.insert(new Competition(season, "SBPLI Long Island Regional", "LI"));
		competitionData.insert(new Competition(season, "Silicon Valley Regional", "SJ"));
		competitionData.insert(new Competition(season, "South Florida Regional", "SFL"));
		competitionData.insert(new Competition(season, "Troy FIRST Robotics District Competition", "OC"));
		competitionData.insert(new Competition(season, "Washington DC  Regional", "DC"));
		competitionData.insert(new Competition(season, "Central Valley Regional", "CAF"));
		competitionData.insert(new Competition(season, "Las Vegas Regional", "NV"));
		competitionData.insert(new Competition(season, "Lone Star Regional", "TX"));
		competitionData.insert(new Competition(season, "North Carolina Regional", "NC"));
		competitionData.insert(new Competition(season, "Queen City Regional", "OHC"));
		competitionData.insert(new Competition(season, "Spokane Regional", "WAS"));
		competitionData.insert(new Competition(season, "Mid-Atlantic Robotics FRC Region Championship", "PA"));
		competitionData.insert(new Competition(season, "Michigan FRC State Championship", "GL"));
		competitionData.insert(new Competition(season, "FIRST Championship - Archimedes Division", "Archimedes"));
		competitionData.insert(new Competition(season, "FIRST Championship - Curie Division", "Curie"));
		competitionData.insert(new Competition(season, "FIRST Championship - Galileo Division", "Galileo"));
		competitionData.insert(new Competition(season, "FIRST Championship - Newton Division", "Newton"));
		Competition temp = competitionData.insert(new Competition(season, "FIRST Championship", "CMP"));

		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(869)));
		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(1676)));
		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(3637)));
		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(25)));
		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(75)));
		competitionTeamData.insert(new CompetitionTeam(temp, teamData.getNum(11)));
	}
}
