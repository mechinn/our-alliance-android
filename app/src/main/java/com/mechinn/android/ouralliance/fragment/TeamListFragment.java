package com.mechinn.android.ouralliance.fragment;

import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.EventTeamDragSortListAdapter;
import com.mechinn.android.ouralliance.data.frc2014.Sort2014;
import com.mechinn.android.ouralliance.activity.MatchScoutingActivity;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.event.SelectTeamEvent;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetEventTeams;
import com.mobeta.android.dslv.DragSortListView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class TeamListFragment extends Fragment {
    public static final String TAG = "TeamListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    protected DragSortListView dslv;
    private int selectedPosition;
	private Prefs prefs;
	private EventTeamDragSortListAdapter adapter;
    private Sort2014 sort2014;
    private Spinner sortTeams;
    private GetEventTeams downloadTeams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
        sort2014 = Sort2014.RANK;
        downloadTeams = new GetEventTeams(this.getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
        dslv = (DragSortListView) rootView.findViewById(android.R.id.list);
        sortTeams = (Spinner) rootView.findViewById(R.id.sortTeams);
        sortTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(prefs.getYear()) {
                    case 2014:
                        sort2014 = Sort.sort2014List.get(position);
                        break;
                }
                load();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    	return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
        adapter = new EventTeamDragSortListAdapter(getActivity(), null);
        dslv.setAdapter(adapter);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start");
        EventBus.getDefault().register(this);
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

    public void onEventMainThread(BluetoothEvent event) {
        getActivity().invalidateOptionsMenu();
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        Log.d(TAG,"CompID: "+prefs.getComp());
        if(prefs.getComp()>0) {
            if(prefs.getComp()>0 && !prefs.isEventTeamsDownloaded()) {
                AsyncExecutor.create().execute(downloadTeams);
            }
            switch(prefs.getYear()) {
                case 2014:
                    ArrayAdapter<Sort2014> sort2014Adapter = new ArrayAdapter<Sort2014>(getActivity(),android.R.layout.simple_list_item_1, Sort.sort2014List);
                    sortTeams.setAdapter(sort2014Adapter);
                    break;
            }
            sortTeams.setSelection(0);
        } else {
            emptyTeams();
        }
    }

    private void emptyTeams() {

    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        EventBus.getDefault().post(new SelectTeamEvent(adapter.getTeam(position).getTeam().getId()));
        
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
    public void onPrepareOptionsMenu(Menu menu) {
        BluetoothEvent bluetooth = EventBus.getDefault().getStickyEvent(BluetoothEvent.class);
        menu.findItem(R.id.matchList).setVisible(prefs.getComp()>0 && null!=adapter && adapter.getCount() > 5);
        menu.findItem(R.id.insertTeamScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.importTeamScouting).setVisible(prefs.getComp() > 0);
        menu.findItem(R.id.exportTeamScouting).setVisible(null != adapter && adapter.getCount() > 0);
        menu.findItem(R.id.bluetoothTeamScouting).setVisible(null != adapter && adapter.getCount() > 0 && bluetooth.isEnabled());
        if(bluetooth.isOn()) {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
        } else {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth);
        }
        menu.findItem(R.id.refreshCompetitionTeams).setVisible(prefs.getComp() > 0);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.matchList:
                Intent intent = new Intent(this.getActivity(), MatchScoutingActivity.class);
                startActivity(intent);
	            return true;
	        case R.id.insertTeamScouting:
                DialogFragment newFragment = new InsertTeamDialogFragment();
                Bundle updateArgs = new Bundle();
                updateArgs.putSerializable(InsertTeamDialogFragment.RANK_ARG, adapter.getCount());
                newFragment.setArguments(updateArgs);
                newFragment.show(this.getFragmentManager(), "Add Team");
	            return true;
            case R.id.refreshCompetitionTeams:
                AsyncExecutor.create().execute(downloadTeams);
                return true;
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
//		        	dialog = new InsertTeamDialogFragment();
//		            Bundle updateArgs = new Bundle();
//		            updateArgs.putSerializable(InsertTeamDialogFragment.TEAM_ARG, adapter.get(position).getTeam());
//		            dialog.setArguments(updateArgs);
//		        	dialog.show(this.getFragmentManager(), "Edit Team");
//	            return true;
	        case R.id.delete:
                dialog = new DeleteTeamDialogFragment();
                Bundle deleteArgs = new Bundle();
                deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, adapter.getTeam(position));
                dialog.setArguments(deleteArgs);
                dialog.show(this.getFragmentManager(), "Delete Team");
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

    public void load() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                From query = new Select().from(EventTeam.class).join(Team.class).on(EventTeam.TAG+"."+EventTeam.TEAM+"="+Team.TAG+"."+Team.ID);
                String orderBy = EventTeam.TAG+"."+EventTeam.RANK+" ASC";
                switch(prefs.getYear()) {
                    case 2014:
                        query = query.join(TeamScouting2014.class).on(Team.TAG+"."+Team.ID+"="+TeamScouting2014.TAG+"."+TeamScouting2014.TEAM);
                        Log.d(TAG,"sort: "+sort2014);
                        switch(sort2014) {
                            case NUMBER:
                                orderBy = Team.TAG+"."+Team.TEAM_NUMBER+" ASC";
                                break;
                            case ORIENTATION:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.ORIENTATION+" ASC";
                                break;
                            case DRIVETRAIN:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.DRIVE_TRAIN+" ASC";
                                break;
                            case WIDTH:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.WIDTH+" DESC";
                                break;
                            case LENGTH:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.LENGTH+" DESC";
                                break;
                            case HEIGHTSHOOTER:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHT_SHOOTER+" DESC";
                                break;
                            case HEIGHTMAX:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHT_MAX+" DESC";
                                break;
                            case SHOOTERTYPE:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTER_TYPE+" DESC";
                                break;
                            case SHOOTGOAL:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.HIGH_GOAL+" DESC, "+TeamScouting2014.TAG+"."+TeamScouting2014.LOW_GOAL+" DESC";
                                break;
                            case SHOOTINGDISTANCE:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTING_DISTANCE+" DESC";
                                break;
                            case PASS:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.PASS_TRUSS+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.PASS_AIR+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.PASS_GROUND+" DESC";
                                break;
                            case PICKUP:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.PICKUP_CATCH+" DESC, "+TeamScouting2014.TAG+"."+TeamScouting2014.PICKUP_GROUND+" DESC";
                                break;
                            case PUSHER:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.PUSHER+" ASC";
                                break;
                            case BLOCKER:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.BLOCKER+" ASC";
                                break;
                            case HUMANPLAYER:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.HUMAN_PLAYER+" DESC";
                                break;
                            case AUTONOMOUS:
                                orderBy = TeamScouting2014.TAG+"."+TeamScouting2014.HOT_AUTO+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.HIGH_AUTO+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.LOW_AUTO+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.DRIVE_AUTO+" DESC, "+
                                        TeamScouting2014.TAG+"."+TeamScouting2014.NO_AUTO+" DESC";
                                break;
                        }
                        break;
                }

                List<EventTeam> teams = query.where(EventTeam.TAG+"."+EventTeam.EVENT+"=?",prefs.getComp()).orderBy(orderBy).execute();

                EventBus.getDefault().post(new LoadTeams(teams));
            }
        });
    }

    public void onEventMainThread(EventTeam eventTeamsChanged) {
        load();
    }

    public void onEventMainThread(TeamScouting2014 teamScouting2014Changed) {
        load();
    }
    public void onEventMainThread(Team teamsChanged) {
        load();
    }

    public void onEventMainThread(LoadTeams teams) {
        switch (prefs.getYear()) {
            case 2014:
                adapter.showDrag(sort2014.equals(Sort2014.RANK));
                break;
        }
        adapter.swapList(teams.getTeams());
        getActivity().invalidateOptionsMenu();
    }

    private class LoadTeams {
        List<EventTeam> teams;
        public LoadTeams(List<EventTeam> teams) {
            this.teams = teams;
        }
        public List<EventTeam> getTeams() {
            return teams;
        }
    }
}
