package com.mechinn.android.ouralliance;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.greenDaoObject.frc2014.dao.DaoMaster;
import com.mechinn.android.ouralliance.greenDaoObject.frc2014.dao.DaoSession;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    public static final String TAG = "OurAlliance";
    private BluetoothReceive receiver;
    private DaoSession daoSession;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        stopBluetoothReceiver();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        startBluetoothReceiver();
                        break;
                }
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
        setupDatabase();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);
        if(null==receiver && null!=BluetoothAdapter.getDefaultAdapter() && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            startBluetoothReceiver();
        }

//        new AlarmTheBlueAlliance(this.getApplicationContext()).setAlarm();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "greendao2014", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void startBluetoothReceiver() {
        receiver = new BluetoothReceive(this,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(null!=msg.getData().getString(Import.RESULT)) {
                    Toast.makeText(OurAlliance.this,msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(null!=msg.getData().getString(BluetoothReceive.STATUS)) {
                    Toast.makeText(OurAlliance.this,"Bluetooth was disabled on device",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        }));
        receiver.start();
        Toast.makeText(OurAlliance.this,"Listening for bluetooth connections",Toast.LENGTH_SHORT).show();
    }

    public void stopBluetoothReceiver() {
        if(null!=receiver) {
            receiver.interrupt();
        }
        Toast.makeText(OurAlliance.this,"Bluetooth was disabled on device",Toast.LENGTH_SHORT).show();
    }
    public boolean isBluetoothReceiverState() {
        if(null!=receiver) {
            return receiver.isServerOn();
        }
        return false;
    }
}
