package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteMatchDialogFragment extends DialogFragment {
	public static final String TAG = DeleteMatchDialogFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onDeleteDialogPositiveClick(long match);
    }
    
    Listener listener;
    private long match;
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		match = this.getArguments().getLong(MATCH_ARG);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.deleteMatch)
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Send the positive button event back to the host activity
					listener.onDeleteDialogPositiveClick(match);
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
