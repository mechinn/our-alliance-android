package com.mechinn.android.ouralliance.data;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.*;
import android.os.Process;
import android.util.Log;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by mechinn on 3/22/14.
 */
public class BluetoothReceive extends Thread {
    public static final String TAG = "BluetoothReceive";
    public static final String STATUS = "status";

    private Handler uiHandler;
    private Application application;
    private String serviceName;
    private UUID uuid;
    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter adapter;
    private Prefs prefs;

    public BluetoothReceive(Application application,Handler handler) {
        super(TAG);
        this.application = application;
        this.uiHandler = handler;
        serviceName = application.getString(R.string.app_name);
        uuid = UUID.fromString(application.getString(R.string.uuid));
        adapter = BluetoothAdapter.getDefaultAdapter();
        serverSocket = null;
        this.prefs = new Prefs(application);
        setDaemon(true);
    }

    @Override
    public void run () {
        if(adapter != null) {
            while(!Thread.currentThread().isInterrupted()) {
                if (adapter.isEnabled()) {
                    Log.d(TAG, "bluetooth server enabled");
                    try {
                        serverSocket = adapter.listenUsingRfcommWithServiceRecord(serviceName, uuid);
                        BluetoothSocket socket = serverSocket.accept();
                        if(prefs.getComp()>0) {
                            InputStream input = socket.getInputStream();
                            new Import(application, uiHandler, socket.getRemoteDevice().getName(), input).start();
//                        } else {
//                            socket.getOutputStream().write("Receiver must select competition first".getBytes());
//                            while(socket.isConnected()) {
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    Log.d(TAG,"sleep interrupted",e);
//                                }
//                            }
                        }
                        socket.close();
                    } catch (IOException e) {
                        Log.w(TAG, "unable to find connection", e);
                    }
                } else {
                    Log.d(TAG, "bluetooth server disabled");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "interrupted");
                    }
                }
                Thread.yield();
            }
        } else {
            Log.d(TAG,"bluetooth is not available on this device");
        }
    }
}
