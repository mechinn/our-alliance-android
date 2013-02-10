package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
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
	private SeasonDataSource seasonData;
	private CompetitionTeamDataSource competitionTeamData;
	private TeamScouting2013DataSource scouting2013;
	private CompetitionTeamCursorAdapter adapter;
	private Competition comp;
	private Season season;
	private Cursor teamCursor;
	private Cursor seasonCursor;
	private Team addTeam;

	private Listener mCallbacks = sDummyCallbacks;
	public interface Listener {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(CompetitionTeam team);
		public void deleteTeam(CompetitionTeam team);
		public void insertTeam(Team team);
		public void updateTeam(Team team);
	}
	private static Listener sDummyCallbacks = new Listener() {
		public void onItemSelected(CompetitionTeam team) {}
		public void deleteTeam(CompetitionTeam team) {}
		public void insertTeam(Team team) {}
		public void updateTeam(Team team) {}
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
		comp = new Competition();
		season = new Season();
		teamData = new TeamDataSource(this.getActivity());
		seasonData = new SeasonDataSource(this.getActivity());
		competitionTeamData = new CompetitionTeamDataSource(this.getActivity());
		scouting2013 = new TeamScouting2013DataSource(this.getActivity());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		comp.setId(prefs.getComp());
		season.setId(prefs.getSeason());
		this.getLoaderManager().restartLoader(TeamListActivity.LOADER_SEASON, null, this);
		this.getLoaderManager().restartLoader(TeamListActivity.LOADER_COMPETITIONTEAMS, null, this);
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
	
	public void deleteTeam(CompetitionTeam team) {
		Log.d(TAG, "id: "+team);
		try {
			competitionTeamData.delete(team);
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}
	
	public void compTeamExists(Team team) {
	}
	
	public void insertTeam(Team team) {
		Log.d(TAG, "id: "+team);
		//try inserting the team first in case it doesnt exist
		try {
			addTeam = teamData.insert(team);
			attemptInsertTeamScouting();
			insertCompetitionTeam(new CompetitionTeam(comp, addTeam));
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			//if we errored out then the team must already exist, we need to get it.
			addTeam = team;
			this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMS, null, this);
		}
	}
	
	public void attemptInsertTeamScouting() {
		if(seasonCursor==null) {
			this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMS, null, this);
		} else {
			insertTeamScouting();
		}
	}
	
	public void insertTeamScouting() {
		if(addTeam!=null) {
			try {
				if(2013==season.getYear()) {
					scouting2013.insert(new TeamScouting2013(season,addTeam));
				} else {
					Log.w(TAG, "unknown season");
				}
			} catch (OurAllianceException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void insertCompetitionTeam(CompetitionTeam team) {
		//insert the team into the 
		try {
			Log.d(TAG, "id: "+comp.getId());
			competitionTeamData.insert(team);
		} catch (OurAllianceException e) {
			Toast.makeText(this.getActivity(), "Cannot create team without "+e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {
			Toast.makeText(this.getActivity(), "Team already in this competition", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void updateTeam(Team team) {
		Log.d(TAG, "id: "+team);
		try {
			teamData.update(team);
		} catch (OurAllianceException e) {
			Toast.makeText(this.getActivity(), "Cannot update team without "+e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {
			Toast.makeText(this.getActivity(), "Team does not exist, creating it and adding to competition", Toast.LENGTH_SHORT).show();
			insertTeam(team);
		}
	}
	
	public CompetitionTeam getCompetitionTeamFromList(int position) {
		return adapter.get(position);
	}
	
	public void getTeam(Cursor cursor) {
		if(teamCursor!=null) {
			teamCursor.close();
		}
		teamCursor = cursor;
		try {
			Team team = TeamDataSource.getSingle(teamCursor);
			team.setName(addTeam.getName());
			teamData.update(team);
			insertCompetitionTeam(new CompetitionTeam(comp, team));
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//stop loading!
		this.getLoaderManager().destroyLoader(TeamListActivity.LOADER_TEAMS);
	}
	
	public void getSeason(Cursor cursor) {
		if(seasonCursor!=null) {
			seasonCursor.close();
		}
		seasonCursor = cursor;
		try {
			season = SeasonDataSource.getSingle(seasonCursor);
			insertTeamScouting();
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//stop loading!
		this.getLoaderManager().destroyLoader(TeamListActivity.LOADER_SEASON);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case TeamListActivity.LOADER_COMPETITIONTEAMS:
				return competitionTeamData.getAllTeams(prefs.getComp());
			case TeamListActivity.LOADER_TEAMS:
				return teamData.get(addTeam.getNumber());
			case TeamListActivity.LOADER_SEASON:
				return seasonData.get(season.getId());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_COMPETITIONTEAMS:
				adapter.swapCursor(data);
				break;
			case TeamListActivity.LOADER_TEAMS:
				getTeam(data);
				break;
			case TeamListActivity.LOADER_SEASON:
				getSeason(data);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_COMPETITIONTEAMS:
				adapter.swapCursor(null);
				break;
			case TeamListActivity.LOADER_TEAMS:
				getTeam(null);
				break;
		}
	}

}
