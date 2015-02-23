package com.mechinn.android.ouralliance;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.widget.Toast;

import com.activeandroid.app.Application;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.event.EventException;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.ThrowableFailureEvent;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    public static final String TAG = "OurAlliance";

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

    public void onEventMainThread(ThrowableFailureEvent event) {
        Toast.makeText(this,event.getThrowable().getMessage(),Toast.LENGTH_LONG);
        Log.e(TAG, event.getThrowable().getMessage(),event.getThrowable());
    }
}
