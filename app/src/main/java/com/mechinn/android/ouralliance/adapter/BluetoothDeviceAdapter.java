package com.mechinn.android.ouralliance.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import se.emilsjolander.sprinkles.ModelList;

import java.util.Set;

public class BluetoothDeviceAdapter extends BaseAdapter {
    public static final String TAG = "CompetitionTeamAdapter";

    private Context context;
    private BluetoothDevice[] devices;
    private LayoutInflater inflater;
    private int iterator;

    public BluetoothDeviceAdapter(Context context, Set<BluetoothDevice> devices) {
        this.context = context;
        inflater = LayoutInflater.from(getContext());
        setDevices(devices);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public BluetoothDevice[] getDevices() {
        return devices;
    }

    public void setDevices(Set<BluetoothDevice> devices) {
        if(null==devices) {
            this.devices = new BluetoothDevice[0];
        } else {
            this.devices = devices.toArray(new BluetoothDevice[devices.size()]);
        }
        this.notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isEmpty() {
        return getDevices().length<1;
    }

    @Override
    public int getCount() {
        return getDevices().length;
    }

    @Override
    public BluetoothDevice getItem(int position) {
        if(position>getDevices().length) {
            return null;
        }
        return getDevices()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CheckedTextView container = (CheckedTextView) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (CheckedTextView) inflater.inflate(android.R.layout.select_dialog_singlechoice, parent, false);
            }
            container.setText(getDevices()[position].getName()+"\n"+getDevices()[position].getAddress());
        }
        return container;
    }
}
