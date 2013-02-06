package com.mechinn.android.ouralliance;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;

public class Setup {
	private static final String TAG = "Setup";
	private static final int VERSION = 1;
	private Prefs prefs;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private TeamScouting2013DataSource teamScouting2013Data;
	private CompetitionTeamDataSource competitionTeamData;
	private String packageName;
	private File dbPath;
	
	public Setup(Activity activity) {
		prefs = new Prefs(activity);
		teamData = new TeamDataSource(activity);
		seasonData = new SeasonDataSource(activity);
		competitionData = new CompetitionDataSource(activity);
		teamScouting2013Data = new TeamScouting2013DataSource(activity);
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

//				if(SQLiteDatabase.deleteDatabase(dbPath)) {
//					Log.d(TAG,"deleted db");
//				} else {
//					Log.d(TAG,"did not delete db");
//				}
			default:
		}
		switch(version+1) {
			case 1:
				Season s2013 = new Season(2013, "Ultimate Ascent");
				Uri s2013Uri = seasonData.insert(s2013);
				long s2013Id = Long.parseLong(s2013Uri.getLastPathSegment());
				s2013.setId(s2013Id);
				this.addCompetitions(s2013);
			default:
		}
		prefs.setVersion(VERSION);
	}
	
	private void addCompetitions(Season season) {
		Competition comp = new Competition(season, "Alamo Regional", "STX");
		Uri temp = competitionData.insert(comp);
		long champsId = Long.parseLong(temp.getLastPathSegment());
		comp.setId(champsId);
		addTestData(season, comp);
//		competitionData.insert(new Competition(season, "Alamo Regional", "STX"));
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
		competitionData.insert(new Competition(season, "FIRST Championship", "CMP"));
	}
	
	private void addTestData(Season season, Competition comp) {
		SparseArray<String> teams = new SparseArray<String>();
		teams.append(869, "Power Cord");
		teams.append(1676, "Pioneers");
		teams.append(3637, "The Daleks");
		teams.append(25, "Raider Robotix");
		teams.append(75, "RoboRaiders");
		teams.append(11, "Chief Delphi");
		for(int i=0;i<teams.size();++i) {
			Team team = new Team(teams.keyAt(i), teams.valueAt(i));
			Uri teamUri = teamData.insert(team);
			long teamId = Long.parseLong(teamUri.getLastPathSegment());
			team.setId(teamId);
			teamScouting2013Data.insert(new TeamScouting2013(season, team));
			competitionTeamData.insert(new CompetitionTeam(comp, team));
		}
	}
}
