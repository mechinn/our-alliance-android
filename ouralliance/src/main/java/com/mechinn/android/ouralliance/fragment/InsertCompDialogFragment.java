package com.mechinn.android.ouralliance.fragment;

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

    private View dialog;
    private TextView compName;
    private TextView compCode;
    private Competition competition;
	
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
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			competition = new Competition();
			yes = R.string.create;
    		Log.d(TAG, "insert");
		}
		Season season = (Season) this.getArguments().getSerializable(SEASON_ARG);
		competition.setSeason(season);
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					competition.setName(compName.getText());
					competition.setCode(compCode.getText());
                    competition.save();
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
