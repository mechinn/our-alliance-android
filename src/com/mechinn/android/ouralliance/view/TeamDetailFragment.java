package com.mechinn.android.ouralliance.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamListFragment.Listener;

/**
 * A fragment representing a single Team detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public class TeamDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {
	private static final String TAG = "TeamDetailFragment";

	public static final String ARG_COMPETITIONTEAM = "competitionTeam";
	private TeamScouting scouting;
	private TeamScoutingDataSource teamScoutingData;
	private CompetitionTeam team;
	
	TextView teamNumber;
	TextView teamName;
	TextView orientation;
	TextView width;
	TextView length;
	TextView height;
	TextView notes;
	
	private Listener mCallbacks = sDummyCallbacks;
	public interface Listener {
		
	}
	private static Listener sDummyCallbacks = new Listener() {
		
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Listener)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Listener) activity;
	}
	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		teamScoutingData = new TeamScoutingDataSource(this.getActivity());
		if (getArguments().containsKey(ARG_COMPETITIONTEAM)) {
			team = (CompetitionTeam) getArguments().getSerializable(ARG_COMPETITIONTEAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);

		teamNumber = (TextView) rootView.findViewById(R.id.teamNumber);
		teamName = (TextView) rootView.findViewById(R.id.teamName);
		orientation = (TextView) rootView.findViewById(R.id.orientation);
		width = (TextView) rootView.findViewById(R.id.width);
		length = (TextView) rootView.findViewById(R.id.length);
		height = (TextView) rootView.findViewById(R.id.height);
		notes = (TextView) rootView.findViewById(R.id.notes);

		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getLoaderManager().initLoader(TeamListActivity.LOADER_TEAMSCOUTING, null, this);
	}
	
	private void setView(Cursor cursor) {
		if(cursor!=null) {
			try {
				scouting = TeamScoutingDataSource.getTeamScouting(cursor);
				teamNumber.setText(Integer.toString(scouting.getTeam().getNumber()));
				teamName.setText(scouting.getTeam().getName());
				orientation.setText(scouting.getOrientation());
				width.setText(Integer.toString(scouting.getWidth()));
				length.setText(Integer.toString(scouting.getLength()));
				height.setText(Integer.toString(scouting.getHeight()));
				notes.setText(scouting.getNotes());
				return;
			} catch (OurAllianceException e) {
			}
		}
		if(!this.isRemoving()) {
			//if we get here bail, something went very wrong
			Log.d(TAG, "removing");
			Toast.makeText(this.getActivity(), "Error loading team details", Toast.LENGTH_SHORT).show();
			this.getActivity().getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				return teamScoutingData.get(team.getCompetition().getSeason(), team.getTeam());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				setView(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				setView(null);
				break;
		}
	}
}
