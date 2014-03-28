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
    public static final String STATUS = "Status";

    private Handler uiHandler;
    private Application application;
    private String serviceName;
    private UUID uuid;
    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter adapter;
    private Prefs prefs;
    private BluetoothSocket socket;
    private boolean serverOn;

    public BluetoothReceive(Application application,Handler handler) {
        super(TAG);
        this.application = application;
        this.uiHandler = handler;
        serviceName = application.getString(R.string.app_name);
        uuid = UUID.fromString(application.getString(R.string.uuid));
        adapter = BluetoothAdapter.getDefaultAdapter();
        serverSocket = null;
        this.prefs = new Prefs(application);
    }

    public boolean isServerOn() {
        return serverOn;
    }

    @Override
    public synchronized void start() {
        super.start();
        Log.d(TAG,"bluetooth server started");
    }

    @Override
    public void run () {
        if(adapter != null) {
            while(!Thread.currentThread().isInterrupted()) {
                if (adapter.isEnabled()) {
                    Log.d(TAG, "bluetooth server enabled");
                    try {
                        serverSocket = adapter.listenUsingRfcommWithServiceRecord(serviceName, uuid);
                        socket = serverSocket.accept();
                        if(prefs.getComp()>0) {
                            InputStream input = socket.getInputStream();
                            new Import(application, uiHandler, socket.getRemoteDevice().getName(), input).execute();
                        }
                    } catch (IOException e) {
                        Log.w(TAG, "unable to find connection", e);
                    } finally {
                        try {
                            socket.close();
                            serverSocket.close();
                        } catch (Exception e) {
                            Log.w(TAG, "Bluetooth was disabled on device", e);
                        }
                        Log.d(TAG, "bluetooth server closed");
                    }
                }
                if (Thread.interrupted()) {
                    Log.d(TAG, "bluetooth server shutting down");
                    serverOn = false;
                    return;
                } else {
                    Thread.yield();
                }
            }
        } else {
            Log.d(TAG,"bluetooth is not available on this device");
        }
    }
}
