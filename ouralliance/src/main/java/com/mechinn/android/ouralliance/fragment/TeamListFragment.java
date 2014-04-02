package com.mechinn.android.ouralliance.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.*;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.CompetitionTeamDragSortListAdapter;
import com.mechinn.android.ouralliance.data.frc2014.ExportTeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.Sort2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.activity.MatchScoutingActivity;
import com.mechinn.android.ouralliance.rest.GetCompetitionTeams;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;

public class TeamListFragment extends Fragment {
    public static final String TAG = "TeamListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private DragSortListView dslv;
    private int selectedPosition;
	private Prefs prefs;
	private CompetitionTeamDragSortListAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOn;
    private int competitionLoader;
    private Sort2014 sort;
    private Spinner sortTeams;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        bluetoothOn = false;
                        break;
                    case BluetoothAdapter.STATE_ON:
                    case BluetoothAdapter.STATE_TURNING_ON:
                        bluetoothOn = true;
                        break;
                }
                getActivity().invalidateOptionsMenu();
            }
        }
    };

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
                        switch(prefs.getYear()) {
                            case 2014:
                                adapter.showDrag(sort.equals(Sort2014.RANK));
                                break;
                        }
                        adapter.swapList(teams);
                        getActivity().invalidateOptionsMenu();
                        return true;
                    } else {
                        getActivity().invalidateOptionsMenu();
                        return false;
                    }
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
		prefs = new Prefs(this.getActivity());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sort = Sort2014.RANK;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
        sortTeams = (Spinner) rootView.findViewById(R.id.sortTeams);
        sortTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(prefs.getYear()) {
                    case 2014:
                        sort = Sort.sort2014List.get(position);
                        break;
                }
                reloadTeams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dslv = (DragSortListView) rootView.findViewById(android.R.id.list);
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
        bluetoothOn = bluetoothAdapter.isEnabled();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.getActivity().registerReceiver(broadcastReceiver, filter);
    }

    public void onStop() {
        super.onStop();
        // Unregister broadcast listeners
        this.getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"resume");
        Log.d(TAG,"CompID: "+prefs.getComp());
        if(prefs.getComp()>0) {
            new GetCompetitionTeams(this.getActivity()).start();
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
        if(competitionLoader>0) {
            getLoaderManager().destroyLoader(competitionLoader);
            competitionLoader = 0;
        }
    }

    private void reloadTeams() {
        switch(prefs.getYear()) {
            case 2014:
                competitionLoader = Query.many(CompetitionTeam.class,
                        "SELECT " + CompetitionTeam.TAG + ".*" +
                                " FROM " + CompetitionTeam.TAG +
                                " INNER JOIN " + TeamScouting2014.TAG +
                                " ON " + TeamScouting2014.TAG + "." + TeamScouting2014.TEAM + "=" + CompetitionTeam.TAG + "." + CompetitionTeam.TEAM +
                                " AND " + CompetitionTeam.COMPETITION + "=?" +
                                " ORDER BY "+sort.getValue(), prefs.getComp()).getAsync(getLoaderManager(), onCompetitionTeamsLoaded);
                break;
        }
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onTeamSelected(adapter.getItem(position).getTeam().getId());
        
        // Set the item as checked to be highlighted when in two-pane layout
        dslv.setItemChecked(position, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.action_request_enable_bluetooth) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothTransfer();
            }
        }
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
        menu.findItem(R.id.matchList).setVisible(prefs.getComp()>0 && null!=adapter && adapter.getCount() > 5);
        menu.findItem(R.id.insertTeamScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.importTeamScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.exportTeamScouting).setVisible(null!=adapter && adapter.getCount()>0);
        menu.findItem(R.id.bluetoothTeamScouting).setVisible(null != adapter && adapter.getCount() > 0 && bluetoothAdapter != null);
        if(bluetoothOn) {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
        } else {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth);
        }
    }

    private void bluetoothTransfer() {
        DialogFragment newFragment = new TransferBluetoothDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransferBluetoothDialogFragment.TYPE, Import.Type.TEAMSCOUTING2014);
        newFragment.setArguments(bundle);
        newFragment.show(this.getFragmentManager(), "Bluetooth Transfer Team Scouting");
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
                Bundle bundle = new Bundle();
                bundle.putInt(InsertTeamDialogFragment.NEXT,adapter.getCount());
                newFragment.setArguments(bundle);
                newFragment.show(this.getFragmentManager(), "Add Team");
	            return true;
	        case R.id.exportTeamScouting:
                new ExportTeamScouting2014(this.getActivity()).execute();
	            return true;
            case R.id.importTeamScouting:
                new Import(this.getActivity(),new Handler(new Handler.Callback(){
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(null!=msg.getData().getString(Import.RESULT)) {
                            Toast.makeText(getActivity(),msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                }),Import.Type.TEAMSCOUTING2014).start();
                return true;
            case R.id.bluetoothTeamScouting:
                if(!bluetoothAdapter.isEnabled()) {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, R.id.action_request_enable_bluetooth);
                } else {
                    bluetoothTransfer();
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
                deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, adapter.getItem(position));
                dialog.setArguments(deleteArgs);
                dialog.show(this.getFragmentManager(), "Delete Team");
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
}
