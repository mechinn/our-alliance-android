package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TeamListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	private static final String TAG = "TeamListFragment";
	public static final int LOADER_COMPETITIONTEAMS = 0;
	public static final int LOADER_COMPETITION = 1;
	public static final int LOADER_TEAMS = 2;
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private int selectedPosition;
	private Prefs prefs;
	private TeamDataSource teamData;
	private CompetitionDataSource competitionData;
	private CompetitionTeamDataSource competitionTeamData;
	private TeamScouting2013DataSource scouting2013;
	private CompetitionTeamCursorAdapter adapter;
	private Competition comp;
	private Team addTeam;

    public interface Listener {
        public void onTeamSelected(CompetitionTeam team);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
		teamData = new TeamDataSource(this.getActivity());
		competitionData = new CompetitionDataSource(this.getActivity());
		competitionTeamData = new CompetitionTeamDataSource(this.getActivity());
		scouting2013 = new TeamScouting2013DataSource(this.getActivity());
		// Restore the previously serialized activated item position.
		adapter = new CompetitionTeamCursorAdapter(getActivity(), null);
		setListAdapter(adapter);
		setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = super.onCreateView(inflater, container, savedInstanceState);
    	return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
		registerForContextMenu(getListView());
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			int position = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
			if (position == ListView.INVALID_POSITION) {
				getListView().setItemChecked(selectedPosition, false);
			} else {
				getListView().setItemChecked(position, true);
			}
			selectedPosition = position;
		}
    }
    
    @Override
    public void onStart() {
        super.onStart();
//		this.getActivity().registerForContextMenu(this.getListView());
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
		this.getLoaderManager().restartLoader(LOADER_COMPETITIONTEAMS, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	selectItem(position);
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onTeamSelected(adapter.get(position));
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (selectedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, selectedPosition);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.team_list_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.insert:
	        	if(null!=comp) {
		            DialogFragment newFragment = new InsertTeamDialogFragment();
		    		newFragment.show(this.getFragmentManager(), "Add Team");
	        	} else {
	        		noCompetition();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.team_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
	    DialogFragment dialog;
	    switch (item.getItemId()) {
	        case R.id.open:
	        	selectItem(position);
	            return true;
	        case R.id.edit:
	        	if(null!=comp) {
		        	dialog = new InsertTeamDialogFragment();
		            Bundle updateArgs = new Bundle();
		            updateArgs.putSerializable(InsertTeamDialogFragment.TEAM_ARG, adapter.get(position).getTeam());
		            dialog.setArguments(updateArgs);
		        	dialog.show(this.getFragmentManager(), "Edit Team");
		    	} else {
	        		noCompetition();
		    	}
	            return true;
	        case R.id.delete:
	        	if(null!=comp) {
		        	dialog = new DeleteTeamDialogFragment();
		            Bundle deleteArgs = new Bundle();
		            deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, adapter.get(position));
		            dialog.setArguments(deleteArgs);
		            dialog.show(this.getFragmentManager(), "Delete Team");
				} else {
	        		noCompetition();
				}
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void deleteTeam(CompetitionTeam team) {
		Log.d(TAG, "id: "+team);
		try {
			competitionTeamData.delete(team);
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}
	
	public void insertTeam(Team team) {
		Log.d(TAG, "id: "+team);
		//try inserting the team first in case it doesnt exist
		try {
			addTeam = teamData.insert(team);
			insertTeamScouting();
			insertCompetitionTeam(new CompetitionTeam(comp, addTeam));
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			//if we errored out then the team must already exist, we need to get it.
			addTeam = team;
			this.getLoaderManager().initLoader(LOADER_TEAMS, null, this);
		}
	}
	
	public void insertTeamScouting() {
		if(addTeam!=null) {
			try {
				if(2013==comp.getSeason().getYear()) {
					scouting2013.insert(new TeamScouting2013(comp.getSeason(),addTeam));
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
	
	public void getComp(Cursor cursor) {
		try {
			comp = CompetitionDataSource.getSingle(cursor);
		} catch (OurAllianceException e) {
    		noCompetition();
		} catch (SQLException e) {
    		noCompetition();
		}
		cursor.close();
		//stop loading!
		this.getLoaderManager().destroyLoader(LOADER_COMPETITION);
	}
	
	public void getTeam(Cursor cursor) {
		try {
			Team team = TeamDataSource.getSingle(cursor);
			Log.d(TAG, "team len: "+addTeam.getName().length());
			if(addTeam.getName().length()>0) {
				team.setName(addTeam.getName());
			}
			teamData.update(team);
			insertTeamScouting();
			insertCompetitionTeam(new CompetitionTeam(comp, team));
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cursor.close();
		//stop loading!
		this.getLoaderManager().destroyLoader(LOADER_TEAMS);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case LOADER_COMPETITIONTEAMS:
				return competitionTeamData.getAllTeams(prefs.getComp());
			case LOADER_COMPETITION:
				return competitionData.get(prefs.getComp());
			case LOADER_TEAMS:
				return teamData.get(addTeam.getNumber());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch(loader.getId()) {
			case LOADER_COMPETITIONTEAMS:
				adapter.swapCursor(data);
				try {
					comp = adapter.getComp();
				} catch (OurAllianceException e) {
					//we need that competition!
					this.getLoaderManager().initLoader(LOADER_COMPETITION, null, this);
				}
				break;
			case LOADER_COMPETITION:
				getComp(data);
				break;
			case LOADER_TEAMS:
				getTeam(data);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case LOADER_COMPETITIONTEAMS:
				adapter.swapCursor(null);
				break;
		}
	}
	
	private void noCompetition() {
		String message = "Select a competition in settings first";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
