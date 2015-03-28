package com.mechinn.android.ouralliance.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MatchAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.mechinn.android.ouralliance.csv.frc2015.ExportCsvMatchScouting2015;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.event.SelectMatchEvent;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;
import com.mechinn.android.ouralliance.gson.frc2015.ExportJsonEventMatchScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ExportJsonEventTeamScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventMatchScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventTeamScouting2015;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetMatches;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

public class MatchListFragment extends ListFragment {
    public static final String TAG = "MatchListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    public static final int OPEN_DOCUMENT_REQUEST_CODE = 4002;
    public static final int CREATE_DOCUMENT_CSV_REQUEST_CODE = 4003;
    public static final int CREATE_DOCUMENT_JSON_REQUEST_CODE = 4004;
	private Prefs prefs;
	private MatchAdapter adapter;
    private GetMatches downloadMatches;
    private boolean enoughTeams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
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
        EventBus.getDefault().register(this);
//		this.getActivity().registerForContextMenu(this.getListView());
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list_fragment) != null) {
        	getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
        if(!prefs.isMatchesDownloaded()) {
            AsyncExecutor.create().execute(downloadMatches);
        }
        loadMatches();
        loadEventTeams();
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        EventBus.getDefault().post(new SelectMatchEvent(((Match)adapter.getItem(position)).getId()));

        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(((Match)adapter.getItem(position)).toString());
        
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
            Timber.d(e,e.getMessage());
        }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.match_list, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
        BluetoothEvent bluetoothState = EventBus.getDefault().getStickyEvent(BluetoothEvent.class);
	    menu.findItem(R.id.practice).setChecked(prefs.isPractice());
        menu.findItem(R.id.insert).setVisible(prefs.getComp() > 0 && enoughTeams);
        menu.findItem(R.id.sendMatchScoutingCsv).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.restoreMatchListScouting).setVisible(prefs.getComp() > 0);
        menu.findItem(R.id.backupMatchListScouting).setVisible(null!=adapter && adapter.getCount()>0);
//        menu.findItem(R.id.bluetoothMatchScouting).setVisible(null!=adapter && adapter.getCount()>0 && !bluetoothState.isDisabled());
//        if(bluetoothState.isOn()) {
//            menu.findItem(R.id.bluetoothMatchScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
//        } else {
//            menu.findItem(R.id.bluetoothMatchScouting).setIcon(R.drawable.ic_action_bluetooth);
//        }
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
                new InsertMatchDialogFragment().show(this.getFragmentManager(), "Add Match");
	            return true;
            case R.id.refreshMatches:
                AsyncExecutor.create().execute(downloadMatches);
                return true;
            case R.id.sendMatchScoutingCsv:
                switch(prefs.getYear()) {
                    case 2015:
                        AsyncExecutor.create().execute(new ExportCsvMatchScouting2015(this.getActivity()));
                        break;
                }
                return true;
            case R.id.backupMatchListScouting:
                switch(prefs.getYear()) {
                    case 2015:
                        final Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType(OurAllianceGson.TYPE);
                        intent.putExtra(Intent.EXTRA_TITLE, "matchList.json");
                        startActivityForResult(intent, CREATE_DOCUMENT_JSON_REQUEST_CODE);
                        break;
                }
                return true;
            case R.id.restoreMatchListScouting:
                switch(prefs.getYear()) {
                    case 2015:
                        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType(OurAllianceGson.TYPE);
                        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE);
                        break;
                }
                return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(OPEN_DOCUMENT_REQUEST_CODE == requestCode &&
                Activity.RESULT_OK == resultCode &&
                null!=data) {
            final Uri uri = data.getData();
            Timber.d("Uri: " + uri.toString());
            AsyncExecutor.create().execute(new ImportJsonEventMatchScouting2015(this.getActivity(), uri));
        } else if(CREATE_DOCUMENT_JSON_REQUEST_CODE == requestCode &&
                Activity.RESULT_OK == resultCode &&
                null!=data) {
            final Uri uri = data.getData();
            Timber.d("Uri: " + uri.toString());
            AsyncExecutor.create().execute(new ExportJsonEventMatchScouting2015(this.getActivity(), uri));
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public void loadEventTeams() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                int count = new Select().from(EventTeam.class).where(EventTeam.EVENT+"=?",prefs.getComp()).count();
                Timber.d("event teams =" + count);
                EventBus.getDefault().post(new LoadEventTeams(count));
            }
        });
    }
    public void onEventMainThread(EventTeam eventTeamsChanged) {
        loadEventTeams();
    }
    public void onEventMainThread(LoadEventTeams teams) {
        enoughTeams = teams.enoughTeams();
        Timber.d("enough teams ="+enoughTeams);
        getActivity().invalidateOptionsMenu();
    }
    private class LoadEventTeams {
        int count;
        public LoadEventTeams(int count) {
            this.count = count;
        }
        public boolean enoughTeams() {
            return count>5;
        }
    }

    public void loadMatches() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                From matchBuilder = new Select().from(Match.class).where(Match.EVENT+"=?",prefs.getComp());
                if(prefs.isPractice()) {
                    matchBuilder = matchBuilder.and(Match.COMPETITION_LEVEL+"=?",Match.PRACTICE);
                } else {
                    matchBuilder = matchBuilder.and(Match.COMPETITION_LEVEL+"<>?",Match.PRACTICE);
                }
                List<Match> matches = matchBuilder.execute();
                EventBus.getDefault().post(new LoadMatches(matches));
            }
        });
    }
    public void onEventMainThread(Match scoutingChanged) {
        loadMatches();
    }
    public void onEventMainThread(LoadMatches matches) {
        List<Match> result = matches.getMatches();
        Timber.d( "Count: " + result.size());
        Collections.sort(result);
        adapter.swapList(result);
        getActivity().invalidateOptionsMenu();
    }
    private class LoadMatches {
        List<Match> matches;
        public LoadMatches(List<Match> matches) {
            this.matches = matches;
        }
        public List<Match> getMatches() {
            return matches;
        }
    }
}
