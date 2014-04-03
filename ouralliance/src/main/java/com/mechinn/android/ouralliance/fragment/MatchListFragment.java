package com.mechinn.android.ouralliance.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.data.Export;
import com.mechinn.android.ouralliance.data.Import;
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

import com.mechinn.android.ouralliance.data.frc2014.ExportMatchScouting2014;
import com.mechinn.android.ouralliance.rest.GetCompetitionTeams;
import com.mechinn.android.ouralliance.rest.GetMatches;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;

public class MatchListFragment extends ListFragment {
    public static final String TAG = "MatchListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Listener mCallback;
	private Prefs prefs;
	private MatchAdapter adapter;
    private ModelList<CompetitionTeam> teams;
    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOn;
    private GetMatches downloadMatches;

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
        public void onMatchSelected(long match);
    }

    private ManyQuery.ResultHandler<Match> onMatchesLoaded =
            new ManyQuery.ResultHandler<Match>() {

                @Override
                public boolean handleResult(CursorList<Match> result) {

                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<Match> matches = ModelList.from(result);
                        result.close();
                        adapter.swapList(matches);
                        Log.d(TAG, "Count: " + matches.size());
                        getActivity().invalidateOptionsMenu();
                        return true;
                    } else {
                        getActivity().invalidateOptionsMenu();
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
                        if(teams.size()<6) {
                            getActivity().finish();
                        }
                        result.close();
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
		prefs = new Prefs(this.getActivity());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        downloadMatches = new GetMatches(this.getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
        if(!prefs.getMatchesDownloaded()) {
            downloadMatches.refreshMatches();
        }
        Query.many(CompetitionTeam.class, "select * from "+CompetitionTeam.TAG+" where "+CompetitionTeam.COMPETITION+"=?", prefs.getComp()).getAsync(getLoaderManager(),onTeamsLoaded);
        Query.many(Match.class, prefs.getPractice() ? "select * from "+Match.TAG+" where "+Match.COMPETITION+"=? AND "+Match.MATCHTYPE+"=-1" : "select * from "+Match.TAG+" where "+Match.COMPETITION+"=? AND "+Match.MATCHTYPE+"!=-1", prefs.getComp()).getAsync(getLoaderManager(),onMatchesLoaded);
    }

    @Override
    public void onDestroy() {
        downloadMatches.quit();
        super.onDestroy();
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onMatchSelected(((Match)adapter.getItem(position)).getId());
        getActivity().setTitle(((Match)adapter.getItem(position)).toString());
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
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
	}

    private void bluetoothTransfer() {
        DialogFragment newFragment = new TransferBluetoothDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransferBluetoothDialogFragment.TYPE, Import.Type.MATCHSCOUTING2014);
        newFragment.setArguments(bundle);
        newFragment.show(this.getFragmentManager(), "Bluetooth Transfer Match Scouting");
    }
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    menu.findItem(R.id.practice).setChecked(prefs.getPractice());
        menu.findItem(R.id.insert).setVisible(prefs.getComp()>0 && null != teams && teams.size() > 5);
        menu.findItem(R.id.bluetoothMatchScouting).setVisible(null!=adapter && adapter.getCount()>0 && bluetoothAdapter!=null);
        menu.findItem(R.id.importMatchScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.exportMatchScouting).setVisible(null!=adapter && adapter.getCount()>0);
        if(bluetoothOn) {
            menu.findItem(R.id.bluetoothMatchScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
        } else {
            menu.findItem(R.id.bluetoothMatchScouting).setIcon(R.drawable.ic_action_bluetooth);
        }
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
                DialogFragment newFragment = new InsertMatchDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(InsertMatchDialogFragment.TEAMS_ARG, teams);
                newFragment.setArguments(args);
                newFragment.show(this.getFragmentManager(), "Add Match");
	            return true;
            case R.id.exportMatchScouting:
                new ExportMatchScouting2014(this.getActivity()).execute();
                return true;
            case R.id.importMatchScouting:
                new Import(this.getActivity(),new Handler(new Handler.Callback(){
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(null!=msg.getData().getString(Import.RESULT)) {
                            Toast.makeText(getActivity(),msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                }),Import.Type.MATCHSCOUTING2014).start();
                return true;
            case R.id.bluetoothMatchScouting:
                if(!bluetoothAdapter.isEnabled()) {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, R.id.action_request_enable_bluetooth);
                } else {
                    bluetoothTransfer();
                }
                return true;
            case R.id.refreshMatches:
                downloadMatches.refreshMatches();
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
                dialog = new DeleteMatchDialogFragment();
                Bundle deleteArgs = new Bundle();
                deleteArgs.putSerializable(DeleteMatchDialogFragment.MATCH_ARG, (Match)adapter.getItem(position));
                dialog.setArguments(deleteArgs);
                dialog.show(this.getFragmentManager(), "Delete Team");
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
}
