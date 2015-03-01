package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.EventTeam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class MatchDetailFragment<MatchScoutingYear extends MatchScouting> extends Fragment {
    public static final String TAG = "MatchDetailFragment";
    public static final String SCOUTING_ARG = "team";

    private Prefs prefs;
    private long scoutingId;

	private View rootView;
    private TextView notes;
    private LinearLayout season;

	private MatchScoutingYear match;

    public long getScoutingId() {
        return scoutingId;
    }
    public void setScoutingId(long scoutingId) {
        this.scoutingId = scoutingId;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	setRetainInstance(true);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            scoutingId = savedInstanceState.getLong(EventTeam.TAG, 0);
            Log.d(TAG, "team: "+ scoutingId);
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
        notes = (TextView) rootView.findViewById(R.id.matchNotes);
        season = (LinearLayout) rootView.findViewById(R.id.season);
		return rootView;
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
            scoutingId = getArguments().getLong(SCOUTING_ARG, 0);
            Log.d(TAG, "team: " + scoutingId);
        }
    }
	
	@Override
	public void onPause() {
        if(null!=match) {
            updateMatch();
            this.getMatch().asyncSave();
        }
        super.onPause();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SCOUTING_ARG, scoutingId);
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

    public abstract void loadMatchScouting();
    public void onEventMainThread(MatchScoutingYear scoutingChanged) {
        loadMatchScouting();
    }
    public void onEventMainThread(LoadMatcheScouting scouting) {
        MatchScoutingYear result = scouting.getScouting();
        Log.d(TAG, "result: " + result);
        setMatch(result);
        setView();
        rootView.setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }
    protected class LoadMatcheScouting {
        MatchScoutingYear scouting;
        public LoadMatcheScouting(MatchScoutingYear scouting) {
            this.scouting = scouting;
        }
        public MatchScoutingYear getScouting() {
            return scouting;
        }
    }
}
