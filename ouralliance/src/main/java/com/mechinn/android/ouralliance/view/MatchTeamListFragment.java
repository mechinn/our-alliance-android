package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.frc2014.Match2014DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MatchTeamListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	public static final String TAG = MatchTeamListFragment.class.getSimpleName();
	public static final int LOADER_MATCH = 0;
	public static final String MATCH_ARG = "match";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
	private Prefs prefs;
	private Match2014DataSource match2014Data;
	private MatchTeamCursorAdapter adapter;
	private long matchId;

	public interface Listener {
        public void onTeamSelected(long team);
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
		match2014Data = new Match2014DataSource(this.getActivity());
		adapter = new MatchTeamCursorAdapter(getActivity(), null);
		this.setListAdapter(adapter);
		matchId = this.getArguments().getLong(MATCH_ARG);
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
        if(0!=matchId) {
        	this.getLoaderManager().restartLoader(LOADER_MATCH, null, this);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onTeamSelected(((Team) adapter.getItem(position)).getId());
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.getSelectedItemPosition() != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, this.getSelectedItemPosition());
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case LOADER_MATCH:
				switch(prefs.getYear()) {
					case 2014:
						return match2014Data.get(matchId);
				}
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch(loader.getId()) {
			case LOADER_MATCH:
				Match match = null;
				try {
					switch(prefs.getYear()) {
						case 2013:
							match = Match2014DataSource.getSingle(data);
					}
					if(null!=match) {
						adapter.swapMatch(match);
					}
				} catch (OurAllianceException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					data.close();
				}
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
		
		}
	}
}
