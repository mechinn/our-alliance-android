package com.mechinn.android.ouralliance;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;

import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.event.EventException;
import com.mechinn.android.ouralliance.greenDao.dao.DaoMaster;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.event.EventBus;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    public static final String TAG = "OurAlliance";
    private DaoSession daoSession;
    private AsyncSession asyncSession;

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
        setupDatabase();
        EventBus.getDefault().register(this);

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
        EventBus.getDefault().unregister(this);
        super.onTerminate();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "greendao", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
    public AsyncSession getAsyncSession() {
        return asyncSession;
    }
}
