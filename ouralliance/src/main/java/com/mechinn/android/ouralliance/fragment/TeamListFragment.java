package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Export;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.CompetitionTeamDragSortListAdapter;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.activity.MatchScoutingActivity;
import com.mobeta.android.dslv.DragSortListView;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
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

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;

public class TeamListFragment extends Fragment {
	public static final String TAG = TeamListFragment.class.getSimpleName();
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private DragSortListView dslv;
    private int selectedPosition;
	private Prefs prefs;
	private CompetitionTeamDragSortListAdapter adapter;
	private Competition comp;
    private Team saving;
	public void setComp(Competition comp) {
		this.comp = comp;
		setHasOptionsMenu(null!=comp);
	}

    public interface Listener {
        public void onTeamSelected(long team);
    }

    private ManyQuery.ResultHandler<CompetitionTeam> onCompetitionTeamsLoaded =
            new ManyQuery.ResultHandler<CompetitionTeam>() {

                @Override
                public boolean handleResult(CursorList<CompetitionTeam> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        Log.d(TAG,"cursor size: "+result.size());
                        ModelList<CompetitionTeam> teams = ModelList.from(result);
                        result.close();
                        adapter.swapList(teams);
                        if(teams.size()<1) {
                            Query.one(Competition.class, "select * from Competition where _id=?",prefs.getComp()).getAsync(getLoaderManager(),onCompetitionLoaded);
                        } else {
                            setComp(teams.get(0).getCompetition());
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private OneQuery.ResultHandler<Team> onTeamLoaded =
            new OneQuery.ResultHandler<Team>() {
                @Override
                public boolean handleResult(Team result) {
                    if(null!=result) {
                        saving = result;
                        saveCompetitionTeam();
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private OneQuery.ResultHandler<Competition> onCompetitionLoaded =
            new OneQuery.ResultHandler<Competition>() {
                @Override
                public boolean handleResult(Competition result) {
                    if(null!=result) {
                        setComp(result);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private OneQuery.ResultHandler<TeamScouting2014> onTeamScouting2014Loaded =
            new OneQuery.ResultHandler<TeamScouting2014>() {
                @Override
                public boolean handleResult(TeamScouting2014 result) {
                    if(null==result) {
                        new TeamScouting2014(saving).save();
                    }
                    return false;
                }
            };

    private OneQuery.ResultHandler<CompetitionTeam> onCompetitionTeamLoaded =
            new OneQuery.ResultHandler<CompetitionTeam>() {
                @Override
                public boolean handleResult(CompetitionTeam result) {
                    if(null==result) {
                        new CompetitionTeam(new Competition(prefs.getComp()),saving).save();
                    }
                    return false;
                }
            };

    public void saveTeam(Team team) {
        saving = team;
        saving.save();
        Log.d(TAG,"saving id: "+saving.getId());
        if(saving.getId()==0) {
            Query.one(Team.class, "select * from Team where teamNumber=? LIMIT 1",saving.getNumber()).getAsync(this.getLoaderManager(),onTeamLoaded);
        } else {
            saveCompetitionTeam();
        }
    }

    public void saveCompetitionTeam() {
        switch(comp.getSeason().getYear()) {
            case 2014:
                Query.one(TeamScouting2014.class, "select * from TeamScouting2014 where team=? LIMIT 1",saving.getId()).getAsync(this.getLoaderManager(), onTeamScouting2014Loaded);
                break;

        }
        Query.one(CompetitionTeam.class, "select * from CompetitionTeam where competition=? AND team=? LIMIT 1",prefs.getComp(),saving.getId()).getAsync(this.getLoaderManager(),onCompetitionTeamLoaded);
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
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
        dslv = (DragSortListView) rootView.findViewById(android.R.id.list);
    	return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	setRetainInstance(true);
		registerForContextMenu(dslv);
		dslv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	selectItem(position);
			}
		});
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			int position = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
			if (position == ListView.INVALID_POSITION) {
				dslv.setItemChecked(selectedPosition, false);
			} else {
				dslv.setItemChecked(position, true);
			}
			selectedPosition = position;
		}
        adapter = new CompetitionTeamDragSortListAdapter(getActivity(), null);
        dslv.setAdapter(adapter);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"start");
//		this.getActivity().registerForContextMenu(this.getListView());
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list_fragment) != null) {
        	dslv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        if(null!=adapter) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"resume");
        Log.d(TAG,"CompID: "+prefs.getComp());
        if(prefs.getComp()>0) {
            setHasOptionsMenu(true);
            Query.many(CompetitionTeam.class, "select * from CompetitionTeam where competition=? ORDER BY rank", prefs.getComp()).getAsync(this.getLoaderManager(),onCompetitionTeamsLoaded);
        } else {
            setHasOptionsMenu(false);
        }
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onTeamSelected(adapter.getItem(position).getTeam().getId());
        
        // Set the item as checked to be highlighted when in two-pane layout
        dslv.setItemChecked(position, true);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        try {
            if (selectedPosition != ListView.INVALID_POSITION) {
                // Serialize and persist the activated item position.
                outState.putInt(STATE_ACTIVATED_POSITION, selectedPosition);
            }
        } catch (IllegalStateException e) {
            Log.d(TAG,"",e);
        }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.team_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.matchList:
	        	if(null!=comp) {
		        	Intent intent = new Intent(this.getActivity(), MatchScoutingActivity.class);
		            startActivity(intent);
	        	} else {
	        		noCompetition();
	        	}
	            return true;
	        case R.id.insert:
	        	if(null!=comp) {
		            DialogFragment newFragment = new InsertTeamDialogFragment();
		    		newFragment.show(this.getFragmentManager(), "Add Team");
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
		        	dialog = new DeleteTeamDialogFragment();
		            Bundle deleteArgs = new Bundle();
		            deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, adapter.getItem(position));
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
	
	private void noCompetition() {
		String message = "Select a competition in settings first";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
