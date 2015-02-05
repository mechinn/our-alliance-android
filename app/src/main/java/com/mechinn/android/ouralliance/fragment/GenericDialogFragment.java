package com.mechinn.android.ouralliance.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mechinn.android.ouralliance.R;

public class GenericDialogFragment extends DialogFragment {
    public static final String TAG = "GenericDialogFragment";
	public static final String FLAG = "flag";
	public static final String MESSAGE = "message";
	public static final String POSITIVE = "positive";
	public static final String NEGATIVE = "negative";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onGenericDialogPositiveClick(int flag, DialogInterface dialog, int id);
        public void onGenericDialogNegativeClick(int flag, DialogInterface dialog, int id);
    }
    
    private Listener listener;
    private int flag;
    
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
		flag = this.getArguments().getInt(FLAG, -1);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(this.getArguments().getInt(MESSAGE))
			.setPositiveButton(this.getArguments().getInt(POSITIVE,R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Send the positive button event back to the host activity
					listener.onGenericDialogPositiveClick(flag, dialog,id);
				}
			}).setNegativeButton(this.getArguments().getInt(NEGATIVE,R.string.no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					listener.onGenericDialogNegativeClick(flag, dialog,id);
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
