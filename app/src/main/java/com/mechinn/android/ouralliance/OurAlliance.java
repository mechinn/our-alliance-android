package com.mechinn.android.ouralliance;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.app.Application;
import com.activeandroid.query.Delete;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.event.EventException;
import com.mechinn.android.ouralliance.event.ResetEvent;
import com.mechinn.android.ouralliance.event.ToastEvent;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import de.greenrobot.event.util.ThrowableFailureEvent;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "OurAlliance";
    private Prefs prefs;
    private String packageName;
    public static final int VERSION = 18;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                try {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    EventBus.getDefault().post(new BluetoothEvent(state));
                } catch (EventException e) {
                    Toast.makeText(OurAlliance.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
        EventBus.getDefault().register(this);
        prefs = new Prefs(this);
        prefs.setChangeListener(this);
        packageName = this.getPackageName();

        Log.d(TAG, "current version: " + prefs.getVersion());
        Log.d(TAG,"new version: "+VERSION);
        if(prefs.getVersion() < VERSION) {
            setup();
        }

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);
//        new AlarmTheBlueAlliance(this.getApplicationContext()).setAlarm();
    }

    public void onEventMainThread(BluetoothEvent event) {
        switch (event.getState()) {
            case STATE_OFF:
            case STATE_TURNING_OFF:
                break;
            case STATE_ON:
            case STATE_TURNING_ON:
                break;
        }
    }

    @Override
    public void onTerminate() {
        prefs.unsetChangeListener(this);
        EventBus.getDefault().unregister(this);
        super.onTerminate();
    }

    public void setup() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "version: " + prefs.getVersion());
                switch (prefs.getVersion() + 1) {
                    //reset
                    case 0:
                        prefs.clear();
                        prefs.setVersion(0);
                        Log.i(TAG, "Resetting database");
                        new Delete().from(Event.class).execute();
                        new Delete().from(Team.class).execute();
                    case 1:
                    case 2:
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
                    case 17:
                        prefs.setVersion(17);
                    case 18:
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Log.i(TAG, "Deleting old file directory");
                            File externalPath = Environment.getExternalStorageDirectory();
                            File fileDir = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
                            Utility.deleteRecursive(fileDir);
                        }
                        Log.i(TAG, "Resetting database");
                        if (deleteDatabase(getDatabasePath("sprinkles.db").getAbsolutePath())) {
                            Log.d(TAG, "deleted db");
                        } else {
                            Log.d(TAG, "did not delete db");
                        }
                        prefs.increaseVersion();
                }
                Log.i(TAG, "Finished setup");
            }
        });
    }

    public void onEventMainThread(ToastEvent event) {
        if(event.isLong()) {
            Toast.makeText(this,event.getMessage(),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,event.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void onEventMainThread(ThrowableFailureEvent event) {
        Toast.makeText(this,event.getThrowable().getMessage(),Toast.LENGTH_LONG);
        Log.e(TAG, event.getThrowable().getMessage(),event.getThrowable());
    }

    public void onEventMainThread(ResetEvent event) {
        String pack = this.getPackageName();
        Log.d(TAG, pack);
        Intent i = this.getPackageManager().getLaunchIntentForPackage(pack);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        prefs.clear();
        prefs.setVersion(-1);
        setup();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Prefs.changed(this,key);
    }
}
