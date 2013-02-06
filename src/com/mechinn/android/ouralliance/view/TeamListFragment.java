package com.mechinn.android.ouralliance.view;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

/**
 * A list fragment representing a list of Teams. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link TeamDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TeamListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private static final String TAG = "TeamListFragment";

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private int mActivatedPosition = ListView.INVALID_POSITION;
	private Prefs prefs;
	private TeamDataSource teamData;
	private CompetitionTeamDataSource competitionTeamData;
	private CompetitionTeamCursorAdapter adapter;

	private Listener mCallbacks = sDummyCallbacks;
	public interface Listener {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(CompetitionTeam team);
		public void deleteTeam(Team team);
		public void insertTeam(Team team);
	}
	private static Listener sDummyCallbacks = new Listener() {
		public void onItemSelected(CompetitionTeam team) {
		}

		public void deleteTeam(Team team) {
		}

		public void insertTeam(Team team) {
		}
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
	public TeamListFragment() {
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prefs = new Prefs(this.getActivity());
		competitionTeamData = new CompetitionTeamDataSource(this.getActivity());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMS, null, this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		this.registerForContextMenu(this.getListView());
		adapter = new CompetitionTeamCursorAdapter(getActivity(), null);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(getCompetitionTeamFromList(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	public void deleteTeam(Team team) {
		Log.d(TAG, "id: "+team);
//		try {
//			competitionTeamData.delete(competitionTeamData.get(team));
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (OurAllianceException e) {
//			e.printStackTrace();
//		}
	}
	
	public void insertTeam(Team team) {
		Log.d(TAG, "id: "+team);
//		try {
//			competitionTeamData.insert(new CompetitionTeam(thisComp,team));
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (OurAllianceException e) {
//			e.printStackTrace();
//		}
	}
	
	public void updateTeam(Team team) {
		Log.d(TAG, "id: "+team);
		try {
			teamData.update(team);
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}
	
	public CompetitionTeam getCompetitionTeamFromList(int position) {
		return adapter.get(position);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case TeamListActivity.LOADER_TEAMS:
				return competitionTeamData.getAllTeams(prefs.getComp());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMS:
				adapter.swapCursor(data);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMS:
				adapter.swapCursor(null);
				break;
		}
	}

}
