package com.mechinn.android.ouralliance.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.R.id;
import com.mechinn.android.ouralliance.R.layout;
import com.mechinn.android.ouralliance.data.Team;

/**
 * A fragment representing a single Team detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public class TeamDetailFragment extends Fragment {
	private static final String tag = "TeamDetailFragment";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	private Team team;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			team = (Team) getArguments().getSerializable(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_team_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (team != null) {
			((TextView) rootView.findViewById(R.id.team_detail)).setText(team.toString());
		}

		return rootView;
	}
}
