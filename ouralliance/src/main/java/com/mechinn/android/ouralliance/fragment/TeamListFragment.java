package com.mechinn.android.ouralliance.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.data.Import;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.CompetitionTeamDragSortListAdapter;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.ExportTeamScouting2014;
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
    public static final String TAG = "TeamListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private DragSortListView dslv;
    private int selectedPosition;
	private Prefs prefs;
	private CompetitionTeamDragSortListAdapter adapter;
	private Competition comp;
    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOn;

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

	public void setComp(Competition comp) {
		this.comp = comp;
        this.getActivity().invalidateOptionsMenu();
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
        menu.findItem(R.id.matchList).setVisible(null!=comp);
        menu.findItem(R.id.insertTeamScouting).setVisible(null!=comp);
        menu.findItem(R.id.importTeamScouting).setVisible(null!=comp);
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
