package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.JsonCompetition;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import se.emilsjolander.sprinkles.SqlStatement;
import se.emilsjolander.sprinkles.Transaction;

public class Setup extends BackgroundProgress {
    public static final String TAG = "Setup";
	public static final int VERSION = 16;
	private Prefs prefs;
	private String packageName;
	private File dbPath;
	private boolean reset;
	private final Activity activity;
	
	public Setup(final Activity activity, boolean reset) {
		super(activity, FLAG_SETUP);
		this.reset = reset;
		this.activity = activity;
		prefs = new Prefs(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("ourAlliance.db");
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
        getDialog().setCancelable(false);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		setVersion(prefs.getVersion());
		Log.d(TAG, "version: "+getVersion());
        Transaction t = new Transaction();
        try {
            switch (getVersion() + 1) {
                //reset
                case 0:
                    increaseVersion();
                    //drop all the rows
                    new SqlStatement("DELETE FROM "+ Team.TAG).execute();
                    new SqlStatement("DELETE FROM "+ Competition.TAG).execute();
                    prefs.setVersion(0);
                case 1:
                    increaseVersion();
                    if (this.isCancelled()) {
                        return false;
                    }
                    setProgressFlag(INDETERMINATE);
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        setProgressFlag(INDETERMINATE);
                        setStatus("Deleting old file directory");
                        File externalPath = Environment.getExternalStorageDirectory();
                        File fileDir = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
                        Utility.deleteRecursive(fileDir);
                    }
                    prefs.setVersion(1);
                case 2:
                    increaseVersion();
                    if (this.isCancelled()) {
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
                    if (this.isCancelled()) {
                        return false;
                    }
                    setStatus("Resetting database");
                    if (activity.deleteDatabase(dbPath.getAbsolutePath())) {
                        Log.d(TAG, "deleted db");
                    } else {
                        Log.d(TAG, "did not delete db");
                    }
                    if (this.isCancelled()) {
                        return false;
                    }
                    prefs.setVersion(16);
            }
            t.setSuccessful(true);
        } finally {
            t.finish();
        }
		setStatus("Finished");
		return true;
	}
}
