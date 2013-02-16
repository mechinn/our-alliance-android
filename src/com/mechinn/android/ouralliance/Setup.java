package com.mechinn.android.ouralliance;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
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
	public static final int PRIMARY = 148;
	public static final int VERSION = 1;
	public static final String TAG = "Setup";
	private static final int INDETERMINATE = -1;
	private static final int NORMAL = 0;
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
	private Integer version;
	private CharSequence status;
	private Map<String, String> comps;
	private LoadingDialogFragment dialog;
	private ContentResolver data;
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
		this.data = activity.getContentResolver();
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
			dialogArgs.putInt(LoadingDialogFragment.MAX, PRIMARY);
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
	        	setFlag(NORMAL);
			case 1:
				increaseVersion();
	        	setFlag(INDETERMINATE);
		        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		        	setFlag(INDETERMINATE);
					setStatus("Deleting old picture directory");
			        File externalPath = Environment.getExternalStorageDirectory();
			        File picDir = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
			        Utility.deleteRecursive(picDir);
//			        picDir = new File(picDir.getAbsolutePath()+"/teamPic/2012/"+Integer.toString(team)+"/");
			        if(!picDir.exists()) {
			        	picDir.mkdirs();
			        }
		        }
				setStatus("Adding 2013 data");
				try {
					Season s2013 = new Season(2013, "Ultimate Ascent");
					Log.d(TAG,s2013.toString());
					s2013 = seasonData.insert(s2013);
		        	increasePrimary();
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
//			case 2:
//
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
		dialog.setProgressStatus((String)progress[2]);
		switch(flag) {
			case INDETERMINATE:
				dialog.setProgressIndeterminate();
			case NORMAL:
			default:
				dialog.setProgressPercent(primary);
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
		putComp("STX", "Alamo Regional");
		putComp("NH", "BAE Systems Granite State Regional");
		putComp("KC", "Greater Kansas City Regional");
		putComp("PAH", "Hatboro-Horsham FIRST Robotics District Competition");
		putComp("GG", "Kettering University FIRST Robotics District Competition");
		putComp("MIGL", "Gull Lake FIRST Robotics District Competition");
		putComp("TN", "Smoky Mountains Regional");
		putComp("SDC", "San Diego Regional");
		putComp("IS", "Israel Regional");
		putComp("OR", "Autodesk Oregon Regional");
		putComp("MD", "Chesapeake Regional");
		putComp("PHL", "Chestnut Hill FIRST Robotics District Competition");
		putComp("ROC", "Finger Lakes Regional");
		putComp("ON", "Greater Toronto East Regional");
		putComp("DMN", "Lake Superior Regional");
		putComp("FL", "Orlando Regional");
		putComp("PIT", "Pittsburgh Regional");
		putComp("GT", "Traverse City FIRST Robotics District Competition");
		putComp("OC1", "Waterford FIRST Robotics District Competition");
		putComp("WOR", "WPI Regional");
		putComp("NJ", "Rutgers University FIRST Robotics District Competition");
		putComp("LA", "Bayou Regional");
		putComp("IN", "Boilermaker Regional");
		putComp("DT", "Detroit FIRST Robotics District Competition");
		putComp("QC", "Festival de Robotique FRC a Montreal Regional");
		putComp("CA", "Los Angeles Regional");
		putComp("GA", "Peachtree Regional");
		putComp("SAC", "Sacramento Regional");
		putComp("UT", "Utah Regional co-sponsored by NASA and Platt");
		putComp("VA", "Virginia Regional");
		putComp("MI", "West Michigan FIRST Robotics District Competition");
		putComp("NY", "New York City Regional");
		putComp("AZ", "Arizona Regional");
		putComp("MA", "Boston Regional");
		putComp("HI", "Hawaii Regional sponsored by BAE Systems");
		putComp("OH", "Buckeye Regional");
		putComp("CO", "Colorado Regional");
		putComp("NJT", "Lenape FIRST Robotics District Competition");
		putComp("IL", "Midwest Regional");
		putComp("SWM", "Niles FIRST Robotics District Competition");
		putComp("WCA", "Northville FIRST Robotics District Competition");
		putComp("SC", "Palmetto Regional");
		putComp("WA2", "Seattle Cascade Regional");
		putComp("WA", "Seattle Olympic Regional");
		putComp("MO", "St. Louis Regional");
		putComp("WAT", "Waterloo Regional");
		putComp("WI", "Wisconsin Regional");
		putComp("DA", "Dallas East Regional sponsored by jcpenney");
		putComp("DA2", "Dallas West Regional sponsored by jcpenney");
		putComp("ON2", "Greater Toronto West Regional");
		putComp("WW", "Livonia FIRST Robotics District Competition");
		putComp("MN", "Minnesota 10000 Lakes Regional");
		putComp("MN2", "Minnesota North Star Regional");
		putComp("NJF", "Mount Olive FIRST Robotics District Competition");
		putComp("CT", "Northeast Utilities FIRST Connecticut Regional");
		putComp("OK", "Oklahoma Regional");
		putComp("LI", "SBPLI Long Island Regional");
		putComp("SJ", "Silicon Valley Regional");
		putComp("SFL", "South Florida Regional");
		putComp("OC", "Troy FIRST Robotics District Competition");
		putComp("DC", "Washington DC  Regional");
		putComp("CAF", "Central Valley Regional");
		putComp("NV", "Las Vegas Regional");
		putComp("TX", "Lone Star Regional");
		putComp("NC", "North Carolina Regional");
		putComp("OHC", "Queen City Regional");
		putComp("WAS", "Spokane Regional");
		putComp("PA", "Mid-Atlantic Robotics FRC Region Championship");
		putComp("GL", "Michigan FRC State Championship");
		putComp("Archimedes", "FIRST Championship - Archimedes Division");
		putComp("Curie", "FIRST Championship - Curie Division");
		putComp("Galileo", "FIRST Championship - Galileo Division");
		putComp("Newton", "FIRST Championship - Newton Division");
		putComp("CMP", "FIRST Championship");
		Competition thisComp;
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
        publishProgress(new Object[]{flag, primary, version+": "+status});
	}
	
	private void increasePrimary() {
        publishProgress(new Object[]{flag, ++primary, version+": "+status});
	}
	
	private void increaseVersion() {
        publishProgress(new Object[]{flag, primary, (++version)+": "+status});
	}
	
	private void setStatus(CharSequence status) {
		this.status = status;
		Log.d(TAG, status.toString());
        publishProgress(new Object[]{flag, primary, version+": "+status});
	}
}
