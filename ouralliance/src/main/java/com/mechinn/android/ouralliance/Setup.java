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
	public static final int VERSION = 3;
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
                prefs.setVersion(1);
			case 2:
				increaseVersion();
				if(this.isCancelled()) {
					return false;
				}
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
                        competitionName = competition.getName().replaceAll(
                                String.format("%s|%s|%s",
                                        "(?<=[A-Z])(?=[A-Z][a-z])",
                                        "(?<=[^A-Z])(?=[A-Z])",
                                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                                ),
                                " "
                        );
                        competitionKey = competition.getKey().substring(4);
                        if(this.isCancelled()) {
                            return false;
                        }
                        Log.d(TAG, "name: "+competitionName);
                        Log.d(TAG, "key: "+competitionKey);
                        try {
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
                            return false;
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            return false;
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
                    return false;
                } catch (JsonParseException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                prefs.setVersion(3);
		}
		setStatus("Finished");
		return true;
	}
}
