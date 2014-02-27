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

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;

public class MatchTeamDialogFragment extends DialogFragment {
	public static final String TAG = MatchTeamDialogFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
	public static final String TEAM_ARG = "team";
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onMatchTeamDialogPositiveClick(long team);
        public void onMatchTeamDialogNegativeClick(long match, long team);
    }

    Listener listener;
    private View dialog;
    private long matchId;
    private long teamId;

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
        matchId = this.getArguments().getLong(MATCH_ARG);
        teamId = this.getArguments().getLong(TEAM_ARG);
		builder.setView(dialog)
			.setPositiveButton("Team Scouting", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
                    listener.onMatchTeamDialogPositiveClick(teamId);
				}
			}).setNegativeButton("Match Scouting", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
                    listener.onMatchTeamDialogNegativeClick(matchId, teamId);
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
