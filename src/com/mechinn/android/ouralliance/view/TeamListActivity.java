package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.view.frc2013.TeamDetail2013;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * An activity representing a list of Teams. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link TeamDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TeamListFragment} and the item details (if present) is a
 * {@link TeamDetailFragment}.
 * <p>
 * This activity also implements the required {@link TeamListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class TeamListActivity extends Activity implements TeamListFragment.Listener, DeleteTeamDialogFragment.Listener, InsertTeamDialogFragment.Listener {
	public static final String TAG = "TeamListActivity";
	public static final int LOADER_COMPETITIONTEAMS = 0;
	public static final int LOADER_TEAMSCOUTING = 1;
	public static final int LOADER_TEAMWHEEL = 2;
	public static final int LOADER_TEAMS = 3;
	public static final int LOADER_SEASON = 4;
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private Setup setup;
	private TeamDetailFragment<?,?> fragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setup = new Setup(this);
		setup.run();

		setContentView(R.layout.activity_team_list);

        if (this.findViewById(R.id.team_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((TeamListFragment) getFragmentManager().findFragmentById(R.id.team_list)).setActivateOnItemClick(true);
		}
	}
	
	/**
	 * Callback method from {@link TeamListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onItemSelected(CompetitionTeam team) {
		Log.d(TAG, team.toString());
		Log.d(TAG, team.getCompetition().getSeason()+" "+team.getTeam());
		Log.d(TAG, team.getCompetition().getSeason().getId()+" "+team.getTeam().getId());
		Log.d(TAG, "two pane: "+mTwoPane);
		long seasonId = team.getCompetition().getSeason().getId();
		int year = team.getCompetition().getSeason().getYear();
		long teamId = team.getTeam().getId();
		if (mTwoPane) {
			//update the frag before making a new one
			if(null!=fragment) {
				fragment.finish();
			}
			Bundle arguments = new Bundle();
			arguments.putBoolean(TeamDetailFragment.ARG_TWOPANE, true);
			arguments.putLong(TeamDetailFragment.ARG_SEASON, seasonId);
			arguments.putInt(TeamDetailFragment.ARG_YEAR, year);
			arguments.putLong(TeamDetailFragment.ARG_TEAM, teamId);
			switch(year) {
				case 2013:
					fragment = new TeamDetail2013();
					fragment.setArguments(arguments);
					this.getFragmentManager().beginTransaction().replace(R.id.team_detail_container, fragment, team.toString()).commit();
			}
		} else {
			Intent detailIntent = new Intent(this, TeamDetailActivity.class);
			detailIntent.putExtra(TeamDetailFragment.ARG_SEASON, seasonId);
			detailIntent.putExtra(TeamDetailFragment.ARG_YEAR, year);
			detailIntent.putExtra(TeamDetailFragment.ARG_TEAM, teamId);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.team_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.insert:
	            DialogFragment newFragment = new InsertTeamDialogFragment();
	    		newFragment.show(this.getFragmentManager(), "Add Team");
	            return true;
	        case R.id.settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
//	            EditText editText = (EditText) findViewById(R.id.edit_message);
//	            CharSequence message = editText.getText();
//	            intent.putExtra(EXTRA_MESSAGE, message);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.team_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TeamListFragment teamListFrag = (TeamListFragment) getFragmentManager().findFragmentById(R.id.team_list);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    DialogFragment dialog;
	    switch (item.getItemId()) {
	        case R.id.edit:
	        	dialog = new InsertTeamDialogFragment();
	            Bundle updateArgs = new Bundle();
	            updateArgs.putSerializable(InsertTeamDialogFragment.TEAM_ARG, teamListFrag.getCompetitionTeamFromList(info.position).getTeam());
	            dialog.setArguments(updateArgs);
	        	dialog.show(this.getFragmentManager(), "Edit Team");
	            return true;
	        case R.id.delete:
	        	dialog = new DeleteTeamDialogFragment();
	            Bundle deleteArgs = new Bundle();
	            deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, teamListFrag.getCompetitionTeamFromList(info.position));
	            dialog.setArguments(deleteArgs);
	            dialog.show(this.getFragmentManager(), "Delete Team");
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void insertTeam(Team team) {
		TeamListFragment teamListFrag = (TeamListFragment) getFragmentManager().findFragmentById(R.id.team_list);
        if (teamListFrag != null) {
        	teamListFrag.insertTeam(team);
        } else {
        	//shouldn't be possible
        }
    }
	
	public void updateTeam(Team team) {
		TeamListFragment teamListFrag = (TeamListFragment) getFragmentManager().findFragmentById(R.id.team_list);

        if (teamListFrag != null) {
        	teamListFrag.updateTeam(team);
        } else {
        	//shouldn't be possible
        }
    }
	
	public void deleteTeam(CompetitionTeam team) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article

		TeamListFragment teamListFrag = (TeamListFragment) getFragmentManager().findFragmentById(R.id.team_list);

        if (teamListFrag != null) {
        	teamListFrag.deleteTeam(team);
        } else {
        	//shouldn't be possible
        }
    }

	public void onInsertDialogPositiveClick(boolean update, Team team) {
		if(update) {
			this.updateTeam(team);
		} else {
			this.insertTeam(team);
		}
	}

	public void onInsertDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
	}

	public void onDeleteDialogPositiveClick(CompetitionTeam team) {
		this.deleteTeam(team);
	}

	public void onDeleteDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
		
	}
}
