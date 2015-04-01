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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.activeandroid.query.Delete;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.event.EventException;
import com.mechinn.android.ouralliance.event.ResetEvent;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.gson.BluetoothImportJson;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventTeamScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.TeamScouting2015WrapperAdapter;

import java.io.File;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import de.greenrobot.event.util.ThrowableFailureEvent;
import timber.log.Timber;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "OurAlliance";
    private Prefs prefs;
    private String packageName;
    public static final int VERSION = 18;
    private BluetoothSPP bt;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                EventBus.getDefault().postSticky(new BluetoothEvent(state));
//                EventBus.getDefault().postSticky(new BluetoothEvent(BluetoothAdapter.ERROR));
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Crashlytics.start(this);
        EventBus.getDefault().register(this);
        prefs = new Prefs(this);
        prefs.setChangeListener(this);
        packageName = this.getPackageName();

        Timber.d("current version: " + prefs.getVersion());
        Timber.d("new version: "+VERSION);
        if(prefs.getVersion() < VERSION) {
            setup();
        }

        bt = new BluetoothSPP(this);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(null==bluetoothAdapter) {
            EventBus.getDefault().postSticky(new BluetoothEvent(BluetoothAdapter.STATE_OFF));
        } else {
            EventBus.getDefault().postSticky(new BluetoothEvent(bluetoothAdapter.getState()));
        }
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                ToastEvent.toast("Recieved data");
                Timber.d("Recieved data");
                Timber.d(message);
                AsyncExecutor.create().execute(new BluetoothImportJson(OurAlliance.this,message));
            }
        });
        EventBus.getDefault().postSticky(bt);
//        new AlarmTheBlueAlliance(this.getApplicationContext()).setAlarm();
    }

    public void onEventMainThread(BluetoothEvent event) {
        switch (event.getState()) {
            case OFF:
            case TURNING_OFF:
                bt.stopService();
                break;
            case ON:
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                break;
        }
    }

    @Override
    public void onTerminate() {
        bt.stopService();
        prefs.unsetChangeListener(this);
        EventBus.getDefault().unregister(this);
        super.onTerminate();
    }

    public void setup() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                Timber.d( "version: " + prefs.getVersion());
                switch (prefs.getVersion() + 1) {
                    //reset
                    case 0:
                        prefs.clear();
                        prefs.setVersion(0);
                        Timber.i("Resetting database");
                        ActiveAndroid.beginTransaction();
                        try {
                            new Delete().from(MatchScouting2015.class).execute();
                            new Delete().from(Wheel2015.class).execute();
                            new Delete().from(TeamScouting2015.class).execute();

                            new Delete().from(MatchScouting2014.class).execute();
                            new Delete().from(Wheel2014.class).execute();
                            new Delete().from(TeamScouting2014.class).execute();

                            new Delete().from(Match.class).execute();
                            new Delete().from(EventTeam.class).execute();
                            new Delete().from(Event.class).execute();
                            new Delete().from(Team.class).execute();
                            ActiveAndroid.setTransactionSuccessful();
                        } finally {
                            ActiveAndroid.endTransaction();
                        }
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
                            Timber.i( "Deleting old file directory");
                            File externalPath = Environment.getExternalStorageDirectory();
                            File fileDir = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
                            Utility.deleteRecursive(fileDir);
                        }
                        Timber.i( "Resetting database");
                        if (deleteDatabase(getDatabasePath("sprinkles.db").getAbsolutePath())) {
                            Timber.d( "deleted db");
                        } else {
                            Timber.d( "did not delete db");
                        }
                        prefs.increaseVersion();
                }
                Timber.i( "Finished setup");
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
        Timber.e(event.getThrowable(),event.getThrowable().getMessage());
    }

    public void onEventMainThread(ResetEvent event) {
        String pack = this.getPackageName();
        Timber.d( pack);
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
