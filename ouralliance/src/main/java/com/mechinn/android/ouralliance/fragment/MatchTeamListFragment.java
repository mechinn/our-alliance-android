package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MatchTeamAdapter;
import com.mechinn.android.ouralliance.data.Match;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;

public class MatchTeamListFragment extends ListFragment {
	public static final String TAG = MatchTeamListFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
	private Prefs prefs;
	private MatchTeamAdapter adapter;
    private long matchId;

	public interface Listener {
        public void onMatchTeamSelected(long match, long team);
    }

    private OneQuery.ResultHandler<Match> onMatchLoaded =
            new OneQuery.ResultHandler<Match>() {
                @Override
                public boolean handleResult(Match result) {
                    adapter.swapMatch(result);
                    if(null!=result) {
                        Log.d(TAG, "result: " + result);
                        getActivity().setTitle(result.toString());
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
        this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		prefs = new Prefs(this.getActivity());
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
                dialogArgs.putLong(MatchTeamDialogFragment.MATCH_ARG, matchId);
                dialogArgs.putLong(MatchTeamDialogFragment.TEAM_ARG, adapter.getItem(position).getTeam().getId());
                dialog.setArguments(dialogArgs);
                dialog.show(getFragmentManager(), adapter.getItem(position).getTeam().toString());
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

    @Override
    public void onResume() {
        super.onResume();
        if(0!=matchId) {
            Query.one(Match.class, "select * from Match where _id=?", matchId).getAsync(getLoaderManager(), onMatchLoaded);
        }
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onMatchTeamSelected(matchId, adapter.getItem(position).getTeam().getId());
        
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
