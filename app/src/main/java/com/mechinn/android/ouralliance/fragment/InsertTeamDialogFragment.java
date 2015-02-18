package com.mechinn.android.ouralliance.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.greenDao.EventTeam;
import com.mechinn.android.ouralliance.greenDao.Team;

public class InsertTeamDialogFragment extends DialogFragment {
    public static final String TAG = "InsertTeamDialogFragment";
	public static final String TEAM_ARG = "team";

    private View dialog;
    private TextView teamNumber;
    private Team team;
    private Prefs prefs;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        prefs = new Prefs(activity);
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.dialog_team_insert, null);
		teamNumber = (TextView) dialog.findViewById(R.id.editTeamNumber);
		int yes;
        team = new Team();
        yes = R.string.create;
        Log.d(TAG, "insert");
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					team.setTeamNumber(Utility.getIntFromText(teamNumber.getText()));
                    new SaveTeam().run();
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}

    private class SaveTeam extends Thread {
        public void run() {
            Log.d(TAG, "saving: " + team);
            team.save();
            Log.d(TAG,"saving id: "+team.getId());
            Log.d(TAG,"competition id: "+prefs.getComp());
            EventTeam eventTeam = new EventTeam();
            eventTeam.setEventId(prefs.getComp());
            eventTeam.setTeam(team);
            eventTeam.save();
        }
    }
}
