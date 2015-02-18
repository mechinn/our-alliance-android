package com.mechinn.android.ouralliance;

import java.io.File;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Setup extends BackgroundProgress {
    public static final String TAG = "Setup";
	public static final int VERSION = 18;
	private Prefs prefs;
	private String packageName;
	private File dbPath;
	private boolean reset;
	private final Activity activity;
	
	public Setup(final FragmentActivity activity, boolean reset) {
		super(activity, FLAG_SETUP);
		this.reset = reset;
		this.activity = activity;
		prefs = new Prefs(activity);
		packageName = activity.getPackageName();
		dbPath = activity.getDatabasePath("sprinkles.db");
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
        try {
            switch (getVersion() + 1) {
                //reset
                case 0:
                    setVersion(0);
                    prefs.clear();
                    prefs.setVersion(0);
                case 1:
                    increaseVersion();
                    prefs.increaseVersion();
                case 2:
                    increaseVersion();
                    prefs.increaseVersion();
                case 3:
                    increaseVersion();
                    prefs.increaseVersion();
                case 4:
                    increaseVersion();
                    prefs.increaseVersion();
                case 5:
                    increaseVersion();
                    prefs.increaseVersion();
                case 6:
                    increaseVersion();
                    prefs.increaseVersion();
                case 7:
                    increaseVersion();
                    prefs.increaseVersion();
                case 8:
                    increaseVersion();
                    prefs.increaseVersion();
                case 9:
                    increaseVersion();
                    prefs.increaseVersion();
                case 10:
                    increaseVersion();
                    prefs.increaseVersion();
                case 11:
                    increaseVersion();
                    prefs.increaseVersion();
                case 12:
                    increaseVersion();
                    prefs.increaseVersion();
                case 13:
                    increaseVersion();
                    prefs.increaseVersion();
                case 14:
                    increaseVersion();
                    prefs.increaseVersion();
                case 15:
                    increaseVersion();
                    prefs.increaseVersion();
                case 16:
                    increaseVersion();
                    prefs.increaseVersion();
                case 17:
                    increaseVersion();
                    prefs.increaseVersion();
                case 18:
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

                    if (this.isCancelled()) {
                        return false;
                    }
                    prefs.increaseVersion();
            }
        } finally {

        }
		setStatus("Finished");
		return true;
	}
}
