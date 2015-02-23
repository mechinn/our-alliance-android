package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteTeamDialogFragment extends DialogFragment {
    public static final String TAG = "DeleteTeamDialogFragment";
	public static final String TEAM_ARG = "team";
    private EventTeam team;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
        team = ((EventTeam) this.getArguments().getSerializable(TEAM_ARG));
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.deleteTeam)
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
                    team.delete();
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
