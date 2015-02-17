package com.mechinn.android.ouralliance.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.BluetoothDeviceAdapter;
import com.mechinn.android.ouralliance.data.BluetoothTransfer;
import com.mechinn.android.ouralliance.data.Import;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class TransferBluetoothDialogFragment extends DialogFragment {
    public static final String TAG = "TransferBluetoothDialogFragment";
    public static final String TYPE = "Type";

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;
    private Prefs prefs;
    private Activity activity;
    private Import.Type type;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        prefs = new Prefs(activity);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            pairedDevices = bluetoothAdapter.getBondedDevices();
            bluetoothDeviceAdapter = new BluetoothDeviceAdapter(this.getActivity(),pairedDevices);
        } else {
            this.dismiss();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(bluetoothDeviceAdapter.getCount()>0) {
            type = (Import.Type) this.getArguments().getSerializable(TYPE);
            builder.setAdapter(bluetoothDeviceAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new BluetoothTransfer(activity,bluetoothDeviceAdapter.getItem(which),type).execute();
                }
            });
        } else {
            Toast.makeText(activity,"No paired bluetooth devices",Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
        return builder.create();
	}
}
