package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.JsonCompetition;
import com.mechinn.android.ouralliance.data.Season;

import se.emilsjolander.sprinkles.SqlStatement;

public class Setup extends BackgroundProgress {
    public static final String TAG = "Setup";
	public static final int VERSION = 16;
	private ObjectMapper jsonMapper;
	private AssetManager assets;
	private Prefs prefs;
	private String packageName;
	private File dbPath;
    private File sprinklesDbPath;
	private boolean reset;
	private Activity activity;
	
	public Setup(Activity activity, boolean reset) {
		super(activity, FLAG_SETUP);
		this.reset = reset;
		this.activity = activity;
		assets = activity.getAssets();
		jsonMapper = new ObjectMapper();
		prefs = new Prefs(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("ourAlliance.db");
        sprinklesDbPath = activity.getDatabasePath("sprinkles.db");
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
		Log.d(TAG,"current version: "+prefs.getVersion());
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
                //drop all the rows
                new SqlStatement("DELETE FROM season").execute();
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
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                prefs.clear();
                prefs.setVersion(15);
                setVersion(15);
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

                setStatus("Setting up 2014 competitions");
                try {
                    Season s2014 = new Season(2014, "Arial Assist");
                    Log.d(TAG,s2014.toString());
                    s2014.save();
//                    JsonCompetition[] getter = jsonMapper.readValue(assets.open("eventsDebug.json"),JsonCompetition[].class);
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
                        new Competition(s2014,competitionName,competitionKey).save();
                        this.increasePrimary();
                    }
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
                prefs.setVersion(16);
		}
		setStatus("Finished");
		return true;
	}
}
