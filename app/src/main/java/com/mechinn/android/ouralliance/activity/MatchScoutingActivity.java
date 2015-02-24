package com.mechinn.android.ouralliance.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.event.SelectMatchEvent;
import com.mechinn.android.ouralliance.event.SelectMatchTeamEvent;
import com.mechinn.android.ouralliance.event.SelectTeamEvent;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;
import com.mechinn.android.ouralliance.fragment.MatchListFragment;
import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.fragment.frc2014.MatchDetail2014;
import com.mechinn.android.ouralliance.fragment.frc2014.MatchTeamList2014Fragment;
import com.mechinn.android.ouralliance.fragment.frc2014.TeamDetail2014;

import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class MatchScoutingActivity extends OurAllianceActivity implements OnBackStackChangedListener {
    public static final String TAG = "MatchScoutingActivity";
	private MatchListFragment matchListFrag;
    private MatchTeamListFragment<?> matchTeamListFrag;
	private MatchDetailFragment<?> matchDetailFragment;
    private TeamDetailFragment<?> teamDetailFragment;
	private int matchFrag;
    private int teamFrag;
	private int detailFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_scouting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.getFragmentManager().addOnBackStackChangedListener(this);
        // Add the fragment to the 'fragment_container' FrameLayout
        if (this.findViewById(R.id.fragment_container) != null) {
            matchFrag = R.id.fragment_container;
            teamFrag = R.id.fragment_container;
    		detailFrag = R.id.fragment_container;
		} else {
        	matchFrag = R.id.list_fragment;
            teamFrag = R.id.team_fragment;
			detailFrag = R.id.detail_fragment;
		}
        if(savedInstanceState == null) {
            matchListFrag = new MatchListFragment();
            getSupportFragmentManager().beginTransaction().replace(matchFrag, matchListFrag).commitAllowingStateLoss();
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.ouralliance, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
	
	@Override
	public void onResume() {
	    super.onResume();
        if(getPrefs().getComp()<1) {
            this.finish();
        }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	if(this.getFragmentManager().getBackStackEntryCount()>0) {
	        		this.getFragmentManager().popBackStack();
	        	} else {
	        		this.finish();
	        	}
	        	return true;
	        case R.id.settings:
	        	intent = new Intent(this, SettingsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onEvent(SelectMatchEvent match) {
        long matchId = match.getId();
		Log.d(TAG, "match: "+matchId);

        Bundle bundle = new Bundle();
        bundle.putLong(MatchTeamListFragment.MATCH_ARG, matchId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                matchTeamListFrag = new MatchTeamList2014Fragment();
                break;
            default:
                Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
                transaction.commit();
                return;
        }
        matchTeamListFrag.setArguments(bundle);
        transaction.replace(teamFrag, matchTeamListFrag);
        if(matchFrag==teamFrag) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
	}

    public void onEvent(SelectMatchTeamEvent team) {
        long teamId = team.getId();
        Log.d(TAG, "team: "+teamId);
        Bundle args = new Bundle();
        args.putLong(MatchDetailFragment.TEAM_ARG, teamId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                matchDetailFragment = new MatchDetail2014();
                break;
            default:
                Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
                transaction.commit();
                return;
        }
        matchDetailFragment.setArguments(args);
        transaction.replace(detailFrag, matchDetailFragment);
        if(teamFrag==detailFrag) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void onEvent(SelectTeamEvent team) {
        long teamId = team.getId();
        Log.d(TAG, "team: "+teamId);
        Bundle args = new Bundle();
        args.putLong(TeamDetailFragment.TEAM_ARG, teamId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                teamDetailFragment = new TeamDetail2014();
                break;
            default:
                Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
                transaction.commit();
                return;
        }
        teamDetailFragment.setArguments(args);
        transaction.replace(detailFrag, teamDetailFragment);
        if(teamFrag==detailFrag) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

	public void onBackStackChanged() {
		Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
        	this.setTitle(R.string.matches);
        }
	}
	
	public void onEvent(Prefs prefsChanged) {
        String key = prefsChanged.getKeyChanged();
		Log.d(TAG, key);
		if(key.equals(this.getString(R.string.pref_practice))) {
			this.recreate();
		}
	}
}
