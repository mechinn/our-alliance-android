package com.mechinn.android.ouralliance.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Season;

public class InsertCompDialogFragment extends DialogFragment {
	public static final String TAG = InsertCompDialogFragment.class.getSimpleName();
	public static final String SEASON_ARG = "season";
	public static final String COMP_ARG = "compeition";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onInsertCompDialogPositiveClick(boolean update, Competition competition);
    }
    
    Listener listener;
    private View dialog;
    private TextView compName;
    private TextView compCode;
    private Competition competition;
    private boolean update;
    
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
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.dialog_comp_insert, null);
		compName = (TextView) dialog.findViewById(R.id.editCompName);
		compCode = (TextView) dialog.findViewById(R.id.editCompCode);
		int yes;
		try {
			competition = (Competition) this.getArguments().getSerializable(COMP_ARG);
			compName.setText(competition.getName());
			compCode.setText(competition.getCode());
    		yes = R.string.update;
    		update = true;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			competition = new Competition();
			yes = R.string.create;
			update = false;
    		Log.d(TAG, "insert");
		}
		Season season = (Season) this.getArguments().getSerializable(SEASON_ARG);
		competition.setSeason(season);
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					competition.setName(compName.getText());
					competition.setCode(compCode.getText());
					listener.onInsertCompDialogPositiveClick(update, competition);
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
