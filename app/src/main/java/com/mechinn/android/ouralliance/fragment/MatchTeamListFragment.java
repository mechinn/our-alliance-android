package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MatchTeamAdapter;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;

public abstract class MatchTeamListFragment<MatchScoutingYear extends MatchScouting> extends ListFragment implements AsyncOperationListener {
    public static final String TAG = "MatchTeamListFragment";
	public static final String MATCH_ARG = "match";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
	private Prefs prefs;
	private MatchTeamAdapter<MatchScoutingYear> adapter;
    private long matchId;
    private DaoSession daoSession;
    private AsyncSession async;
    private AsyncOperation onMatchLoaded;

    public AsyncSession getAsync() {
        return async;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

	public interface Listener {
        public void onMatchTeamSelected(long team);
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if(onMatchLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                ArrayList<MatchScoutingYear> result = (ArrayList<MatchScoutingYear>) operation.getResult();
                Log.d(TAG, "Count: " + result.size());
                adapter.swapMatch(result);
            } else {

            }
        }
    }

    public AsyncOperation getOnMatchLoaded() {
        return onMatchLoaded;
    }
    public void setOnMatchLoaded(AsyncOperation onMatchLoaded) {
        this.onMatchLoaded = onMatchLoaded;
    }
    public long getMatchId() {
        return matchId;
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
        this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		prefs = new Prefs(this.getActivity());
        daoSession = ((OurAlliance) this.getActivity().getApplication()).getDaoSession();
        async = ((OurAlliance) this.getActivity().getApplication()).getAsyncSession();
        async.setListener(this);
		matchId = this.getArguments().getLong(MATCH_ARG);
        Log.d(TAG, "match: "+matchId);
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
//		this.getActivity().registerForContextMenu(this.getListView());
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list_fragment) != null) {
        	getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onMatchTeamSelected(adapter.getItem(position).getId());
        
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
}
