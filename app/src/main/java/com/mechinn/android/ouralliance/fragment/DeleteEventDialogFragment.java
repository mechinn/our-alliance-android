package com.mechinn.android.ouralliance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;

public class DeleteEventDialogFragment extends DialogFragment {
    public static final String TAG = "DeleteTeamDialogFragment";
	public static final String EVENT_ARG = "event";
    private Event event;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
        event = ((Event) this.getArguments().getSerializable(EVENT_ARG));
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.deleteComp)
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
                    event.asyncDelete();
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
