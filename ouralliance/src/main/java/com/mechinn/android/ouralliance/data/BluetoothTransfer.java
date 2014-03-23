package com.mechinn.android.ouralliance.data;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.frc2014.ExportMatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.ExportTeamScouting2014;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by mechinn on 3/21/14.
 */
public class BluetoothTransfer extends BackgroundProgress {
    public static final String TAG = "BluetoothTransfer";
    private Prefs prefs;
    private final UUID uuid;
    private final BluetoothDevice device;
    private final BluetoothSocket socket;
    private Import.Type type;

    public BluetoothTransfer(Activity activity, BluetoothDevice device, Import.Type type) {
        super(activity, FLAG_EXPORT);
        prefs = new Prefs(activity);
        uuid = UUID.fromString(activity.getString(R.string.uuid));
        this.device = device;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "unable to create socket record", e);
        }
        socket = tmp;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        this.setTitle("Transfer to "+device.getName());
        super.onPreExecute();
        this.setProgressFlag(INDETERMINATE);
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        //BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            socket.connect();
            OutputStream outputStream = socket.getOutputStream();
            String status = null;
            switch(type) {
                default:
                    status = "Unknown type to transfer";
                    break;
                case TEAMSCOUTING2014:
                    status = new ExportTeamScouting2014(getActivity(), outputStream).run();
                    break;
                case MATCHSCOUTING2014:
                    status = new ExportMatchScouting2014(getActivity(), outputStream).run();
                    break;
            }
            if(null!=status) {
                this.setStatus(status);
                return false;
            }
//            byte[] buffer = new byte[1024];
//            int length = socket.getInputStream().read(buffer);
//            String message = new String(buffer,0,length);
//            if(length>0) {
//                this.setStatus(message);
//                return false;
//            }
        } catch (IOException e) {
            this.setStatus("Unable to connect to bluetooth device");
            Log.e(TAG,"unable to connect socket",e);
            try {
                socket.close();
            } catch (IOException e1) {
                Log.e(TAG,"unable to close socket",e1);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        try {
            socket.close();
            if(result) {
                Toast.makeText(getActivity(), "Completed sending scouting to "+device.getName(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(),getDialog().getProgressStatus(),Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e1) {
            Toast.makeText(getActivity(),"Unable to close bluetooth connection",Toast.LENGTH_SHORT).show();
            Log.e(TAG,"unable to close socket",e1);
        }
        getDialog().dismiss();
    }
}
