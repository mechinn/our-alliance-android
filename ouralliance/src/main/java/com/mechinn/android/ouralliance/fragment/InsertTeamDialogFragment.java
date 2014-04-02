package com.mechinn.android.ouralliance.fragment;

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

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

public class InsertTeamDialogFragment extends DialogFragment {
    public static final String TAG = "InsertTeamDialogFragment";
	public static final String TEAM_ARG = "team";
    public static final String NEXT = "next";

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
		try {
			team = (Team) this.getArguments().getSerializable(TEAM_ARG);
    		teamNumber.setText(Integer.toString(team.getTeamNumber()));
    		yes = R.string.update;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			team = new Team();
			yes = R.string.create;
    		Log.d(TAG, "insert");
		}
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
            Log.d(TAG,"saving: "+team);
            team.save();
            Log.d(TAG,"saving id: "+team.getId());
            switch(prefs.getYear()) {
                case 2014:
                    new TeamScouting2014(team).save();
                    break;
            }
            Log.d(TAG,"competition id: "+prefs.getComp());
            new CompetitionTeam(new Competition(prefs.getComp()),team,getArguments().getInt(NEXT,999)).save();
        }
    }
}
