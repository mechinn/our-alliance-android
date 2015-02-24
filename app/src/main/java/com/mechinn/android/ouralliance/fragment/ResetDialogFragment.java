package com.mechinn.android.ouralliance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.event.ResetEvent;

import de.greenrobot.event.EventBus;

public class ResetDialogFragment extends DialogFragment {
    public static final String TAG = "GenericDialogFragment";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.confirmReset)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Send the positive button event back to the host activity
                    EventBus.getDefault().post(new ResetEvent());
                }
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
