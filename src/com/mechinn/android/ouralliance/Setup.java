package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.GetTeams;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.view.LoadingDialogFragment;
import com.mechinn.android.ouralliance.view.TeamListFragment.Listener;

public class Setup extends AsyncTask<Void, Object, Boolean> {
	public static final int VERSION = 1;
	public static final String TAG = "Setup";
	private static final int INDETERMINATE = -1;
	private static final int NORMAL = 0;
	private ObjectMapper jsonMapper;
	private Prefs prefs;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private TeamScouting2013DataSource teamScouting2013Data;
	private CompetitionTeamDataSource competitionTeamData;
	private String packageName;
	private File dbPath;
	private Integer flag;
	private Integer primary;
    private Integer progressTotal;
	private Integer version;
	private CharSequence status;
	private Map<String, String> comps;
	private LoadingDialogFragment dialog;
	private ContentResolver data;
	private AssetManager assets;
	private boolean reset;
	private FragmentManager fragmentManager;
    private Listener listener;

    public interface Listener {
        public void setupComplete();
    }
	
	public Setup(Activity activity, boolean reset) {
        try {
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
		this.reset = reset;
		this.fragmentManager = activity.getFragmentManager();
		assets = activity.getAssets();
		this.data = activity.getContentResolver();
		jsonMapper = new ObjectMapper();
		prefs = new Prefs(activity);
		teamData = new TeamDataSource(activity);
		seasonData = new SeasonDataSource(activity);
		competitionData = new CompetitionDataSource(activity);
		teamScouting2013Data = new TeamScouting2013DataSource(activity);
		competitionTeamData = new CompetitionTeamDataSource(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("ourAlliance.sqlite");
	}
	
	@Override
	protected void onPreExecute() {
		CharSequence title = "Setup data";
		if(reset) {
			prefs.clear();
			prefs.setVersion(-1);
			title = "Reset data";
		}
		Log.d(TAG,"curent version: "+prefs.getVersion());
		Log.d(TAG,"new version: "+VERSION);
		if(prefs.getVersion() < VERSION) {
			dialog = new LoadingDialogFragment();
			Bundle dialogArgs = new Bundle();
			dialogArgs.putCharSequence(LoadingDialogFragment.TITLE, title);
			dialog.setArguments(dialogArgs);
            dialog.show(fragmentManager, "Setup Our Alliance");
		} else {
			this.cancel(true);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		version = prefs.getVersion();
		Log.d(TAG, "version: "+version);
		flag = 0;
		primary = 0;
		progressTotal = 100;
		status = "Loading...";
		switch(version+1) {
			case 0:
				increaseVersion();
//		        if(SQLiteDatabase.deleteDatabase(dbPath)) {
//					Log.d(TAG,"deleted db");
//				} else {
//					Log.d(TAG,"did not delete db");
//				}
				setStatus("Resetting database");
		        data.call(Uri.parse(DataProvider.BASE_URI_STRING+TAG), DataProvider.RESET, null, null);
			case 1:
				increaseVersion();
	        	setFlag(INDETERMINATE);
		        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		        	setFlag(INDETERMINATE);
					setStatus("Deleting old picture directory");
			        File externalPath = Environment.getExternalStorageDirectory();
			        File picDir = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
			        Utility.deleteRecursive(picDir);
		        }
				setStatus("Setting up 2013 competitions");
				try {
					Season s2013 = new Season(2013, "Ultimate Ascent");
					Log.d(TAG,s2013.toString());
					s2013 = seasonData.insert(s2013);
					if(!addCompetitions(s2013)) {
						return false;
					}
				} catch (OurAllianceException e) {
					e.printStackTrace();
					return false;
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				setStatus("Adding 2013 teams");
				try {
					GetTeams getter = jsonMapper.readValue(assets.open("teams.json"),GetTeams.class);
					Log.d(TAG, "teams: "+getter.getData().size());
					this.setTotal(getter.getData().size());
					for(Team team : getter.getData()) {
						Log.d(TAG, team.toString());
						try {
							teamData.insert(team);
					    	this.increasePrimary();
							continue;
						} catch (OurAllianceException e) {
							//just go to the update part then
						} catch (SQLException e) {
							//just go to the update part then
						}
						Cursor cusor = teamData.query(team.getNumber());
						try {
							Team fromDb = TeamDataSource.getSingle(cusor);
							fromDb.setName(team.getName());
							fromDb.setNumber(team.getNumber());
							teamData.update(fromDb);
						} catch (OurAllianceException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
				    	this.increasePrimary();
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
//			case 2:
//				increaseVersion();
		}
		setStatus("Finished");
		prefs.setVersion(version);
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Object... progress) {
		int flag = (Integer)progress[0];
		int primary = (Integer)progress[1];
		int total = (Integer)progress[2];
		dialog.setProgressStatus((String)progress[3]);
		switch(flag) {
			case INDETERMINATE:
				dialog.setProgressIndeterminate();
			case NORMAL:
			default:
				dialog.setProgressPercent(primary,total);
		}
    }

	@Override
    protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		listener.setupComplete();
    }
	
	private boolean addCompetitions(Season season) {
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
	
	private void setFlag(int flag) {
		this.flag = flag;
        publishProgress(new Object[]{flag, primary, progressTotal, version+": "+status});
	}
	
	private void increasePrimary() {
    	setFlag(NORMAL);
        publishProgress(new Object[]{flag, ++primary, progressTotal, version+": "+status});
	}
	
	private void increaseVersion() {
        publishProgress(new Object[]{flag, primary, progressTotal, (++version)+": "+status});
	}
	
	private void setTotal(int total) {
    	setFlag(NORMAL);
		this.primary = 0;
		this.progressTotal = total;
        publishProgress(new Object[]{flag, primary, progressTotal, version+": "+status});
	}
	
	private void setStatus(CharSequence status) {
		this.status = status;
		Log.d(TAG, status.toString());
        publishProgress(new Object[]{flag, primary, progressTotal, version+": "+status});
	}
}
