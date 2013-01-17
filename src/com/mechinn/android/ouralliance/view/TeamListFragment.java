package com.mechinn.android.ouralliance.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.SeasonDataSource;
import com.mechinn.android.ouralliance.TeamDataSource;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
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
public class TeamListFragment extends ListFragment {
	
	private static final String tag = "TeamListFragment";

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Listener mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private SimpleCursorAdapter adapter;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private List<Team> teams;
	private Prefs prefs;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Listener {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Team team);
		public void deleteTeam(Team team);
		public void insertTeam(Team team);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Listener sDummyCallbacks = new Listener() {
		@Override
		public void onItemSelected(Team team) {
		}

		@Override
		public void deleteTeam(Team team) {
		}

		@Override
		public void insertTeam(Team team) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamListFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		teamData = new TeamDataSource(this.getActivity());
		teamData.open();
		seasonData = new SeasonDataSource(this.getActivity());
		seasonData.open();
		
//		prefs = new Prefs(this.getActivity());
//		if(!prefs.getDbSetup()) {
//			datasource.createTeam(869, "Power Cord");
//			prefs.setDbSetup(true);
//		} else {
			new Database(this.getActivity()).reset();
			try {
				teamData.editTeam(new Team(869, "Power Cord"));
				seasonData.editSeason(new Season(2013, "Test Comp"));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (OurAllianceException e) {
				e.printStackTrace();
			}
//		}
			
		refreshView();
	}
	
	public void refreshView() {
		try {
			teams = teamData.getAllTeams();
		} catch (Exception e) {
			teams = new ArrayList<Team>();
		}
		setListAdapter(
			new TeamListAdapter(
				getActivity(),
				R.layout.fragment_team_list,
				R.id.team_list_item,
				teams
			)
		);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		this.registerForContextMenu(this.getListView());
	}

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

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(getTeamFromList(position));
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
		Log.d(tag, "id: "+team);
		teamData.deleteTeam(team);
		refreshView();
	}
	
	public void insertTeam(Team team) {
		Log.d(tag, "id: "+team);
		try {
			teamData.editTeam(team);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
		refreshView();
	}
	
	public Team getTeamFromList(int position) {
		return (Team) this.getListAdapter().getItem(position);
	}
}
