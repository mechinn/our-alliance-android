package com.mechinn.android.ouralliance.view;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.Export;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.Match2013;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public abstract class MatchListFragment<A extends Match, B extends AOurAllianceDataSource<A>> extends ListFragment implements LoaderCallbacks<Cursor> {
	public static final String TAG = MatchListFragment.class.getSimpleName();
	public static final int LOADER_MATCHES = 0;
	public static final int LOADER_COMPETITION = 1;
	public static final int LOADER_TEAMS = 2;
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private int selectedPosition;
	private Prefs prefs;
	private CompetitionTeamDataSource competitionTeamData;
	private CompetitionDataSource compeititonData;
	private B scouting;
	private MatchCursorAdapter adapter;
	private Competition comp;
	private long compId;
	private ArrayList<Integer> teams;
	private long[] teamIds;
	private boolean matchesLoaded;
	private boolean teamsLoaded;
	public Competition getComp() {
		return comp;
	}
	public void setComp(Competition comp) {
		this.comp = comp;
		setHasOptionsMenu(null!=comp);
	}
	public abstract B createDataSouce();
	public abstract void insertMatch(Match match);
	public abstract void updateMatch(Match match);
	
	public void deleteMatch(long match) {
		Log.d(TAG, "id: "+match);
		try {
			this.getScouting().delete(match);
		} catch (OurAllianceException e) {
			Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public B getScouting() {
		return scouting;
	}
	public void setScouting(B scouting) {
		this.scouting = scouting;
	}
	public Prefs getPrefs() {
		return prefs;
	}
	public void setPrefs(Prefs prefs) {
		this.prefs = prefs;
	}
	public MatchCursorAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(MatchCursorAdapter adapter) {
		this.adapter = adapter;
	}
	public long getCompId() {
		return compId;
	}
	public void setCompId(long compId) {
		this.compId = compId;
	}
	public CompetitionDataSource getCompeititonData() {
		return compeititonData;
	}
	public void setCompeititonData(CompetitionDataSource compeititonData) {
		this.compeititonData = compeititonData;
	}

    public interface Listener {
        public void onMatchSelected(Match team);
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
        matchesLoaded = false;
        teamsLoaded = false;
		prefs = new Prefs(this.getActivity());
		compId = this.getPrefs().getComp();
		competitionTeamData = new CompetitionTeamDataSource(this.getActivity());
		compeititonData = new CompetitionDataSource(this.getActivity());
		scouting = createDataSouce();
		adapter = new MatchCursorAdapter(getActivity(), null);
		this.setListAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
    	return rootView;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
    	setRetainInstance(true);
		registerForContextMenu(getListView());
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	selectItem(position);
			}
		});
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
		this.getLoaderManager().restartLoader(LOADER_TEAMS, null, this);
		this.getLoaderManager().restartLoader(LOADER_MATCHES, null, this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
		setHasOptionsMenu(null!=comp);
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onMatchSelected(adapter.get(position));
        
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
		inflater.inflate(R.menu.match_list, menu);
	    menu.findItem(R.id.practice).setChecked(prefs.getPractice());
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    menu.findItem(R.id.practice).setChecked(prefs.getPractice());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.practice:
	            if (item.isChecked()) {
	            	item.setChecked(false);
	            } else {
	            	item.setChecked(true);
	            }
            	prefs.setPractice(item.isChecked());
	            return true;
	        case R.id.insert:
	        	if(null!=comp) {
	        		if(null!=teams && teams.size()>5) {
			            DialogFragment newFragment = new InsertMatchDialogFragment();
			            Bundle args = new Bundle();
			            args.putLongArray(InsertMatchDialogFragment.TEAMIDS_ARG, teamIds);
			            args.putIntegerArrayList(InsertMatchDialogFragment.TEAMS_ARG, teams);
			            newFragment.setArguments(args);
			    		newFragment.show(this.getFragmentManager(), "Add Match");
	        		} else {
	        			notEnoughTeams();
	        		}
	        	} else {
	        		noCompetition();
	        	}
	            return true;
//	        case R.id.export:
//	        	if(null!=comp) {
//	        		new Export(this.getActivity()).execute();
//	        	} else {
//	        		noCompetition();
//	        	}
//	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.team_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
	    DialogFragment dialog;
	    switch (item.getItemId()) {
	        case R.id.open:
	        	selectItem(position);
	            return true;
//	        case R.id.edit:
//	        	if(null!=comp) {
//		        	dialog = new InsertTeamDialogFragment();
//		            Bundle updateArgs = new Bundle();
//		            updateArgs.putSerializable(InsertTeamDialogFragment.TEAM_ARG, adapter.get(position).getTeam());
//		            dialog.setArguments(updateArgs);
//		        	dialog.show(this.getFragmentManager(), "Edit Team");
//		    	} else {
//	        		noCompetition();
//		    	}
//	            return true;
	        case R.id.delete:
	        	if(null!=comp) {
		        	dialog = new DeleteMatchDialogFragment();
		            Bundle deleteArgs = new Bundle();
		            deleteArgs.putLong(DeleteMatchDialogFragment.MATCH_ARG, adapter.get(position).getId());
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
	
	public void getComp(Cursor cursor) {
		try {
			setComp(CompetitionDataSource.getSingle(cursor));
		} catch (OurAllianceException e) {
    		noCompetition();
		} catch (SQLException e) {
    		noCompetition();
		}
		cursor.close();
		//stop loading!
		this.getLoaderManager().destroyLoader(LOADER_COMPETITION);
	}
	
	public void getTeams(Cursor cursor) {
		try {
			List<CompetitionTeam> teamList = CompetitionTeamDataSource.getList(cursor);
			if(!teamList.isEmpty()) {
				ArrayList<Long> teamIdArray = new ArrayList<Long>();
				teams = new ArrayList<Integer>();
				for(CompetitionTeam team : teamList) {
					teamIdArray.add(team.getTeam().getId());
					teams.add(team.getTeam().getNumber());
				}
				teamIds = new long[teamIdArray.size()];
				for(int i=0;i<teamIdArray.size();++i) {
					if(null!=teamIdArray.get(i)) {
						teamIds[i] = teamIdArray.get(i);
					}
				}
				setComp(teamList.get(0).getCompetition());
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cursor.close();
		//stop loading!
		this.getLoaderManager().destroyLoader(LOADER_TEAMS);
        teamsLoaded = true;
        if(matchesLoaded && null==comp) {
			this.getLoaderManager().initLoader(LOADER_COMPETITION, null, this);
		}
	}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case LOADER_TEAMS:
				return competitionTeamData.getAllTeams(getCompId());
			case LOADER_COMPETITION:
				return getCompeititonData().get(getCompId());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch(loader.getId()) {
			case LOADER_TEAMS:
				getTeams(data);
				break;
			case LOADER_MATCHES:
				this.getAdapter().swapCursor(data);
				matchesLoaded = true;
				if(null==comp) {
					try {
						setComp(this.getAdapter().getComp());
					} catch (OurAllianceException e) {
						//we need that competition!
						if(teamsLoaded) {
							this.getLoaderManager().initLoader(LOADER_COMPETITION, null, this);
						}
					}
				}
				break;
			case LOADER_COMPETITION:
				getComp(data);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case LOADER_MATCHES:
				this.getAdapter().swapCursor(null);
				break;
		}
	}
	
	private void notEnoughTeams() {
		String message = "Please add at least 6 teams to the competition first.";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	
	private void noCompetition() {
		String message = "Select a competition in settings first";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
