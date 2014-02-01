package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.GetTeams;
import com.mechinn.android.ouralliance.data.JsonCompetition;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.DataProvider;

public class Setup extends BackgroundProgress {
	public static final String TAG = Setup.class.getSimpleName();
	public static final int VERSION = 2;
	private ObjectMapper jsonMapper;
	private AssetManager assets;
	private Prefs prefs;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private String packageName;
	private File dbPath;
	private Map<String, String> comps;
	private ContentResolver data;
	private boolean reset;
	private Activity activity;
	
	public Setup(Activity activity, boolean reset) {
		super(activity, FLAG_SETUP);
		this.reset = reset;
		this.activity = activity;
		this.data = activity.getContentResolver();
		assets = activity.getAssets();
		jsonMapper = new ObjectMapper();
		prefs = new Prefs(activity);
		teamData = new TeamDataSource(activity);
		seasonData = new SeasonDataSource(activity);
		competitionData = new CompetitionDataSource(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("ourAlliance.sqlite");
	}
	
	@Override
	protected void onPreExecute() {
		if(reset) {
			prefs.clear();
			prefs.setVersion(-1);
			setTitle("Reset data");
		} else {
			setTitle("Setup data");
		}
		Log.d(TAG,"curent version: "+prefs.getVersion());
		Log.d(TAG,"new version: "+VERSION);
		if(prefs.getVersion() < VERSION) {
			super.onPreExecute();
		} else {
			this.cancel(true);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		setVersion(prefs.getVersion());
		Log.d(TAG, "version: "+getVersion());
		switch(getVersion()+1) {
            //reset
			case 0:
				increaseVersion();
				if(this.isCancelled()) {
					return false;
				}
				setStatus("Resetting database");
		        if(activity.deleteDatabase(dbPath.getAbsolutePath())) {
					Log.d(TAG,"deleted db");
				} else {
					Log.d(TAG,"did not delete db");
				}
				if(this.isCancelled()) {
					return false;
				}
		        data.call(Uri.parse(DataProvider.BASE_URI_STRING+TAG), DataProvider.RESET, null, null);
                prefs.setVersion(0);
			case 1:
				increaseVersion();
				if(this.isCancelled()) {
					return false;
				}
	        	setProgressFlag(INDETERMINATE);
		        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		        	setProgressFlag(INDETERMINATE);
					setStatus("Deleting old picture directory");
			        File externalPath = Environment.getExternalStorageDirectory();
			        File picDir = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
			        Utility.deleteRecursive(picDir);
		        }
				if(this.isCancelled()) {
					return false;
				}
				setStatus("Setting up 2013 competitions");
				try {
					Season s2013 = new Season(2014, "Ultimate Ascent");
					Log.d(TAG,s2013.toString());
					s2013 = seasonData.insert(s2013);
					if(!add2013Competitions(s2013)) {
						return false;
					}
				} catch (OurAllianceException e) {
					e.printStackTrace();
					return false;
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				if(this.isCancelled()) {
					return false;
				}
                prefs.setVersion(1);
			case 2:
				increaseVersion();
				if(this.isCancelled()) {
					return false;
				}
                //not doing this anymore, planning on a more user friendly way of doing this (that doesnt lock up the system for like 5 min)
//				setStatus("Updating 2013 teams");
//				try {
//					GetTeams getter = jsonMapper.readValue(assets.open("teams2013.json"),GetTeams.class);
//					Log.d(TAG, "teams: "+getter.getData().size());
//					this.setTotal(getter.getData().size());
//					if(this.isCancelled()) {
//						return false;
//					}
//					for(Team team : getter.getData()) {
//						Log.d(TAG, team.toString());
//						try {
//							if(this.isCancelled()) {
//								return false;
//							}
//							teamData.insert(team);
//					    	this.increasePrimary();
//							continue;
//						} catch (OurAllianceException e) {
//							//just go to the update part then
//						} catch (SQLException e) {
//							//just go to the update part then
//						}
//						Cursor cusor = teamData.query(team.getNumber());
//						try {
//							if(this.isCancelled()) {
//								return false;
//							}
//							Team fromDb = TeamDataSource.getSingle(cusor);
//							//if something is different lets update the team
//							if(!fromDb.getName().equals(team.getName()) || fromDb.getNumber()!=team.getNumber()) {
//								fromDb.setName(team.getName());
//								fromDb.setNumber(team.getNumber());
//								teamData.update(fromDb);
//							}
//						} catch (OurAllianceException e1) {
//							e1.printStackTrace();
//						} catch (SQLException e1) {
//							e1.printStackTrace();
//						}
//				    	this.increasePrimary();
//					}
//				} catch (JsonParseException e) {
//					e.printStackTrace();
//				} catch (JsonMappingException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if(this.isCancelled()) {
//					return false;
//				}
                prefs.setVersion(2);
            case 3:
                increaseVersion();
                if(this.isCancelled()) {
                    return false;
                }
                setStatus("Setting up 2014 competitions");
                try {
                    Season s2014 = new Season(2014, "Arial Assist");
                    Log.d(TAG,s2014.toString());
                    s2014 = seasonData.insert(s2014);
                    JsonCompetition[] getter = jsonMapper.readValue(assets.open("events2014.json"),JsonCompetition[].class);
                    Log.d(TAG, "competitions: "+getter.length);
                    this.setTotal(getter.length);
                    if(this.isCancelled()) {
                        return false;
                    }
                    String competitionName;
                    String competitionKey;
                    for(JsonCompetition competition : getter) {
                        Log.d(TAG, competition.toString());
                        competitionName = "";
                        for (String w : competition.getName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                            competitionName.concat(w);
                        }
                        competitionKey = competition.getKey().substring(4);
                        try {
                            if(this.isCancelled()) {
                                return false;
                            }
                            competitionData.insert(new Competition(s2014,competitionName,competitionKey));
                            this.increasePrimary();
                            continue;
                        } catch (OurAllianceException e) {
                            //just go to the update part then
                        } catch (SQLException e) {
                            //just go to the update part then
                        }
                        Cursor cusor = competitionData.query(competitionKey);
                        try {
                            if(this.isCancelled()) {
                                return false;
                            }
                            Competition fromDb = CompetitionDataSource.getSingle(cusor);
                            //if something is different lets update the team
                            if(!fromDb.getSeason().equals(s2014) || !fromDb.getName().equals(competitionName) || !fromDb.getCode().equals(competitionKey)) {
                                fromDb.setSeason(s2014);
                                fromDb.setName(competitionName);
                                fromDb.setCode(competitionKey);
                                competitionData.update(fromDb);
                            }
                        } catch (OurAllianceException e1) {
                            e1.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        this.increasePrimary();
                    }
                } catch (OurAllianceException e) {
                    e.printStackTrace();
                    return false;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(this.isCancelled()) {
                    return false;
                }
                prefs.setVersion(3);
		}
		setStatus("Finished");
		return true;
	}

	private boolean add2013Competitions(Season season) {
		comps = new HashMap<String,String>();
    	increasePrimary();
		putComp("txsa", "Alamo Regional sponsored by Rackspace Hosting");
		putComp("nhma", "BAE Systems Granite State Regional");
		putComp("txlu", "Hub City Regional");
		putComp("azch", "Phoenix Regional");
		putComp("njewn", "TCNJ FIRST Robotics District Competition");
		putComp("misjo", "St Joseph FIRST Robotics District Competition");
		putComp("mokc", "Greater Kansas City Regional");
		putComp("pahat", "Hatboro-Horsham FIRST Robotics District Competition");
		putComp("miket", "Kettering University FIRST Robotics District Competition");
		putComp("migul", "Gull Lake FIRST Robotics District Competition");
		putComp("tnkn", "Smoky Mountains Regional");
		putComp("casd", "San Diego Regional");
		putComp("ista", "Israel Regional");
		putComp("orpo", "Autodesk Oregon Regional");
		putComp("mdba", "Chesapeake Regional");
		putComp("paphi", "Springside - Chestnut Hill FIRST Robotics District Competition");
		putComp("nyro", "Finger Lakes Regional");
		putComp("onto", "Greater Toronto East Regional");
		putComp("onto2", "Greater Toronto West Regional");
		putComp("mndu", "Lake Superior Regional");
		putComp("mndu2", "Northern Lights Regional");
		putComp("flor", "Orlando Regional");
		putComp("papi", "Pittsburgh Regional");
		putComp("mitvc", "Traverse City FIRST Robotics District Competition");
		putComp("miwfd", "Waterford FIRST Robotics District Competition");
		putComp("mawo", "WPI Regional");
		putComp("lake", "Bayou Regional");
		putComp("inwl", "Boilermaker Regional");
		putComp("midet", "Detroit FIRST Robotics District Competition");
		putComp("qcmo", "Festival de Robotique FRC a Montreal Regional");
		putComp("calb", "Los Angeles Regional");
		putComp("gadu", "Peachtree Regional");
		putComp("casa", "Sacramento Regional");
		putComp("utwv", "Utah Regional sponsored by NASA");
		putComp("vari", "Virginia Regional");
		putComp("miwmi", "West Michigan FIRST Robotics District Competition");
		putComp("nyny", "New York City Regional");
		putComp("mabo", "Boston Regional");
		putComp("hiho", "Hawaii Regional sponsored by BAE Systems");
		putComp("ohcl", "Buckeye Regional");
		putComp("code", "Colorado Regional");
		putComp("mele", "Pine Tree Regional");
		putComp("inth", "Crossroads Regional");
		putComp("njlen", "Lenape Seneca FIRST Robotics District Competition");
		putComp("casb", "Inland Empire Regional");
		putComp("ilch", "Midwest Regional");
		putComp("scmb", "Palmetto Regional");
		putComp("wase", "Seattle Regional");
		putComp("wase2", "Central Washington Regional");
		putComp("mosl", "St. Louis Regional");
		putComp("onwa", "Waterloo Regional");
		putComp("wimi", "Wisconsin Regional");
		putComp("txda", "Dallas Regional");
		putComp("miliv", "Livonia FIRST Robotics District Competition");
		putComp("arfa", "Razorback Regional");
		putComp("mnmi", "Minnesota 10000 Lakes Regional");
		putComp("mnmi2", "Minnesota North Star Regional");
		putComp("njfla", "Mount Olive FIRST Robotics District Competition");
		putComp("abca", "Western Canadian FRC Regional");
		putComp("ctha", "Connecticut Regional sponsored by UTC");
		putComp("okok", "Oklahoma Regional");
		putComp("nyli", "SBPLI Long Island Regional");
		putComp("casj", "Silicon Valley Regional");
		putComp("flbr", "South Florida Regional");
		putComp("mitry", "Troy FIRST Robotics District Competition");
		putComp("dcwa", "Washington DC  Regional");
		putComp("cama", "Central Valley Regional");
		putComp("nvlv", "Las Vegas Regional");
		putComp("txho", "Lone Star Regional");
		putComp("ncre", "North Carolina Regional");
		putComp("ohic", "Queen City Regional");
		putComp("mibed", "Bedford FIRST Robotics District Competition");
		putComp("migbl", "Grand Blanc FIRST Robotics District Competition");
		putComp("wach", "Spokane Regional");
		putComp("njbrg", "Bridgewater-Raritan FIRST Robotics District Competition");
		putComp("mrcmp", "Mid-Atlantic Robotics FRC Region Championship");
		putComp("micmp", "Michigan FRC State Championship");
		putComp("archimedes", "FIRST Championship - Archimedes Division");
		putComp("curie", "FIRST Championship - Curie Division");
		putComp("galileo", "FIRST Championship - Galileo Division");
		putComp("newton", "FIRST Championship - Newton Division");
		putComp("cmp", "FIRST Championship");
		Competition thisComp;
		this.setTotal(comps.size());
		for (Map.Entry<String, String> each : comps.entrySet()) {
			try {
				thisComp = new Competition(season, each.getValue(), each.getKey());
				Log.d(TAG,thisComp.toString());
				competitionData.insert(thisComp);
			} catch (OurAllianceException e) {
				e.printStackTrace();
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	    	increasePrimary();
		}
		return true;
	}

	public void putComp(String code, String name) {
		comps.put(code, name);
    	increasePrimary();
	}
}
