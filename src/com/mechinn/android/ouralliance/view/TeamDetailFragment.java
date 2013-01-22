package com.mechinn.android.ouralliance.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

/**
 * A fragment representing a single Team detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public class TeamDetailFragment extends Fragment {
	private static final String TAG = "TeamDetailFragment";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	private TeamScouting scouting;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Prefs prefs = new Prefs(this.getActivity());
		int year = 2013;//prefs.getSeason();
		Log.d(TAG, "year: "+year);
		SeasonDataSource seasonData = new SeasonDataSource(this.getActivity());
		try {
			Season season = seasonData.get(year);
			TeamScoutingDataSource teamScoutingData = new TeamScoutingDataSource(this.getActivity());
			if (getArguments().containsKey(ARG_ITEM_ID)) {
				Team team = (Team) getArguments().getSerializable(ARG_ITEM_ID);
				scouting = teamScoutingData.get(season, team);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);

		if (scouting != null) {
			((TextView) rootView.findViewById(R.id.teamNumber)).setText(Integer.toString(scouting.getTeam().getNumber()));
			((TextView) rootView.findViewById(R.id.teamName)).setText(scouting.getTeam().getName());
			((TextView) rootView.findViewById(R.id.orientation)).setText(scouting.getOrientation());
			((TextView) rootView.findViewById(R.id.width)).setText(Integer.toString(scouting.getWidth()));
			((TextView) rootView.findViewById(R.id.length)).setText(Integer.toString(scouting.getLength()));
			((TextView) rootView.findViewById(R.id.height)).setText(Integer.toString(scouting.getHeight()));
			((TextView) rootView.findViewById(R.id.notes)).setText(scouting.getNotes());
		}

		return rootView;
	}
}
