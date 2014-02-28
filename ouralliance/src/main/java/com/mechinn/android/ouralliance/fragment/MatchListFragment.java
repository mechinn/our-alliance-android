package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MatchAdapter;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MatchListFragment extends ListFragment {
	public static final String TAG = MatchListFragment.class.getSimpleName();
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
	private Prefs prefs;
	private MatchAdapter adapter;
	private Competition comp;
	private long compId;
    private ModelList<CompetitionTeam> teams;
	private boolean matchesLoaded;
	public Competition getComp() {
		return comp;
	}
	public void setComp(Competition comp) {
		this.comp = comp;
		setHasOptionsMenu(null!=comp);
	}

    public interface Listener {
        public void onMatchSelected(long match);
    }

    private OneQuery.ResultHandler<Competition> onCompetitionLoaded =
            new OneQuery.ResultHandler<Competition>() {
                @Override
                public boolean handleResult(Competition result) {
                    if(null!=result) {
                        Log.d(TAG, "result: " + result);
                        setComp(result);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<Match> onMatchesLoaded =
            new ManyQuery.ResultHandler<Match>() {

                @Override
                public boolean handleResult(CursorList<Match> result) {

                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<Match> matches = ModelList.from(result);
                        result.close();
                        adapter.swapList(matches);
                        Log.d(TAG, "Count: " + matches.size());
                        matchesLoaded = true;
                        if(null==getComp()) {
                            setComp(adapter.getComp());
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<CompetitionTeam> onTeamsLoaded =
            new ManyQuery.ResultHandler<CompetitionTeam>() {

                @Override
                public boolean handleResult(CursorList<CompetitionTeam> result) {
                    Log.d(TAG, "Count: " + result.size());
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        teams = ModelList.from(result);
                        result.close();
                        if(teams.size()>0) {
                            setComp(teams.get(0).getCompetition());
                        }
                        if(matchesLoaded && null==comp) {
                            Query.one(Competition.class, "select * from Competition where _id=?", compId).getAsync(getLoaderManager(), onCompetitionLoaded);
                        }
                    } else {
                        teams = null;
                    }
                    return false;
                }
            };

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
		prefs = new Prefs(this.getActivity());
		compId = this.prefs.getComp();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
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
				getListView().setItemChecked(this.getSelectedItemPosition(), false);
			} else {
				getListView().setItemChecked(position, true);
				this.setSelection(position);
			}
		}
        adapter = new MatchAdapter(getActivity(), null);
        setListAdapter(adapter);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Query.many(CompetitionTeam.class, "select * from CompetitionTeam where competition=?", compId).getAsync(getLoaderManager(),onTeamsLoaded);
        Query.many(Match.class, prefs.getPractice() ? "select * from Match where competition=? AND matchType=-1" : "select * from Match where competition=? AND matchType!=-1", compId).getAsync(getLoaderManager(),onMatchesLoaded);
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onMatchSelected(((Match)adapter.getItem(position)).getId());
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        try {
            if (this.getSelectedItemPosition() != ListView.INVALID_POSITION) {
                // Serialize and persist the activated item position.
                outState.putInt(STATE_ACTIVATED_POSITION, this.getSelectedItemPosition());
            }
        } catch (IllegalStateException e) {
            Log.d(TAG,"",e);
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
			            args.putSerializable(InsertMatchDialogFragment.TEAMS_ARG, teams);
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
		            deleteArgs.putSerializable(DeleteMatchDialogFragment.MATCH_ARG, (Match)adapter.getItem(position));
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
