package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.greenDao.EventTeam;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;

public abstract class MatchDetailFragment<MatchScoutingYear extends MatchScouting> extends Fragment implements AsyncOperationListener {
    public static final String TAG = "MatchDetailFragment";
    public static final String TEAM_ARG = "team";

    private Prefs prefs;
    private long teamId;

	private View rootView;
    @InjectView(R.id.matchNotes) protected TextView notes;
    @InjectView(R.id.season) protected LinearLayout season;

	private MatchScoutingYear match;
    private DaoSession daoSession;
    private AsyncSession async;
    private AsyncOperation onMatchLoaded;

    public long getTeamId() {
        return teamId;
    }
    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }
    public LinearLayout getSeason() {
    return season;
}
    public void setSeason(LinearLayout season) {
        this.season = season;
    }
	public MatchScoutingYear getMatch() {
		return match;
	}
	public void setMatch(MatchScoutingYear match) {
		this.match = match;
	}

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public AsyncSession getAsync() {
        return async;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if(onMatchLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                MatchScoutingYear result = (MatchScoutingYear) operation.getResult();
                Log.d(TAG, "result: " + result);
                setMatch(result);
                setView();
                rootView.setVisibility(View.VISIBLE);
            } else {
                rootView.setVisibility(View.GONE);
            }
            getActivity().invalidateOptionsMenu();
        }
    }

    public AsyncOperation getOnMatchLoaded() {
        return onMatchLoaded;
    }

    public void setOnMatchLoaded(AsyncOperation onMatchLoaded) {
        this.onMatchLoaded = onMatchLoaded;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
        daoSession = ((OurAlliance) this.getActivity().getApplication()).getDaoSession();
        async = ((OurAlliance) this.getActivity().getApplication()).getAsyncSession();
        async.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	setRetainInstance(true);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            teamId = savedInstanceState.getLong(EventTeam.TAG, 0);
            Log.d(TAG, "team: "+teamId);
        }
    	
//    	OnClickListener teamButton = new OnClickListener() {
//			public void onClick(View v) {
//				Team team = (Team) v.getTag();
//	        	Intent intent = new Intent(MatchDetailFragment.this.getActivity(), TeamScoutingActivity.class);
//	        	Bundle args = new Bundle();
//	        	args.putString(TeamScoutingActivity.MATCHNAME_ARG, match.toString());
//	        	args.putLong(TeamScoutingActivity.MATCH_ARG,match.getId());
//                args.putLong(TeamScoutingActivity.TEAM_ARG,team.getId());
//	        	intent.putExtras(args);
//	            startActivity(intent);
//		    }
//		};
        
        rootView = inflater.inflate(R.layout.fragment_match_detail, container, false);
        ButterKnife.inject(this, rootView);
		return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            teamId = getArguments().getLong(TEAM_ARG, 0);
            Log.d(TAG, "team: " + teamId);
        }
    }
	
	@Override
	public void onPause() {
        if(null!=match) {
            updateMatch();
            commitUpdatedMatch();
        }
        super.onPause();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TEAM_ARG, teamId);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setView() {
		this.getActivity().setTitle(match.toString());
        notes.setText(match.getNotes());
	}
	
	public void updateMatch() {
        match.setNotes(notes.getText().toString());
	}

    public void commitUpdatedMatch() {
        async.update(this.getMatch());
    }
}
