package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;
import com.mechinn.android.ouralliance.gson.frc2015.ExportJsonEventMatchScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ExportJsonEventTeamScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventMatchScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventTeamScouting2015;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

public abstract class MatchDetailFragment extends Fragment {
    public static final String TAG = "MatchDetailFragment";
    public static final String SCOUTING_ARG = "team";
    public static final int OPEN_DOCUMENT_REQUEST_CODE = 5002;
    public static final int CREATE_DOCUMENT_CSV_REQUEST_CODE = 5003;
    public static final int CREATE_DOCUMENT_JSON_REQUEST_CODE = 5004;

    private Prefs prefs;
    private long scoutingId;

    private View rootView;
    private TextView notes;
    private LinearLayout season;

	private MatchScouting match;

    public View getRootView() {
        return rootView;
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
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
	public MatchScouting getMatch() {
		return match;
	}
	public void setMatch(MatchScouting match) {
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
            Timber.d( "team: "+ scoutingId);
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
            Timber.d( "team: " + scoutingId);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d( "team: " + getScoutingId());
        loadMatchScouting();
    }

    @Override
    public void onPause() {
        Timber.d("pausing");
        if(null!=getMatch()) {
            Timber.d("saving "+getMatch());
            updateMatch();
            this.getMatch().asyncSave();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
            case R.id.backupMatchListScouting:
                switch(prefs.getYear()) {
                    case 2015:
                        final Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType(OurAllianceGson.TYPE);
                        intent.putExtra(Intent.EXTRA_TITLE, "match.json");
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
	
	public void setView() {
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(match.getTeamScouting().getTeam().toString());
        notes.setText(match.getNotes());
	}
	
	public void updateMatch() {
        match.setNotes(notes.getText().toString());
	}

    public abstract void loadMatchScouting();
}
