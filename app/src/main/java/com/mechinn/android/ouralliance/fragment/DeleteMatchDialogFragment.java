package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteMatchDialogFragment extends DialogFragment {
    public static final String TAG = "DeleteMatchDialogFragment";
	public static final String MATCH_ARG = "match";

    private Match match;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		match = (Match) this.getArguments().getSerializable(MATCH_ARG);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.deleteMatch)
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Send the positive button event back to the host activity
					match.asyncDelete();
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
