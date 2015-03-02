package com.mechinn.android.ouralliance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Event;

public class InsertCompDialogFragment extends DialogFragment {
    public static final String TAG = "InsertCompDialog";
	public static final String SEASON_ARG = "season";
	public static final String COMP_ARG = "compeition";

    private View dialog;
    private TextView compName;
    private TextView compCode;
    private Event event;
	
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
			event = (Event) this.getArguments().getSerializable(COMP_ARG);
			compName.setText(event.getShortName());
			compCode.setText(event.getEventCode());
    		yes = R.string.update;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			event = new Event();
			yes = R.string.create;
    		Log.d(TAG, "insert");
		}
		int season = this.getArguments().getInt(SEASON_ARG);
		event.setYear(season);
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					event.setShortName(compName.getText().toString());
					event.setEventCode(compCode.getText().toString());
                    event.asyncSave();
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
