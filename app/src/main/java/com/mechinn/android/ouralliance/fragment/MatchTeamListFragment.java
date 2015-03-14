package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MatchTeamAdapter;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.event.SelectMatchTeamEvent;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public abstract class MatchTeamListFragment extends ListFragment {
    public static final String TAG = "MatchTeamListFragment";
	public static final String MATCH_ARG = "match";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Prefs prefs;
    private MatchTeamAdapter<? extends MatchScouting> adapter;
    private long matchId;

    public MatchTeamAdapter<? extends MatchScouting> getAdapter() {
        return adapter;
    }
    public void setAdapter(MatchTeamAdapter<? extends MatchScouting> adapter) {
        this.adapter = adapter;
    }
    public long getMatchId() {
        return matchId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		prefs = new Prefs(this.getActivity());
		matchId = this.getArguments().getLong(MATCH_ARG);
        Timber.d("match: " + matchId);
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
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment dialog = new MatchTeamDialogFragment();
                Bundle dialogArgs = new Bundle();
                dialogArgs.putLong(MatchTeamDialogFragment.MATCHSCOUTING_ARG, adapter.getItem(position).getId());
                dialogArgs.putLong(MatchTeamDialogFragment.TEAM_ARG, adapter.getItem(position).getTeamScouting().getTeam().getId());
                dialog.setArguments(dialogArgs);
                dialog.show(getFragmentManager(), adapter.getItem(position).getTeamScouting().toString());
                return true;
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
        adapter = new MatchTeamAdapter(getActivity(), null);
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

    @Override
    public void onResume() {
        super.onResume();
        if(0!=getMatchId()) {
            load();
        }
    }

    public abstract void load();

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        EventBus.getDefault().post(new SelectMatchTeamEvent(adapter.getItem(position).getId()));
        
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
}
