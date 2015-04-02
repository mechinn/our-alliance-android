package com.mechinn.android.ouralliance.activity;

import android.support.v4.app.FragmentManager;
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
import com.mechinn.android.ouralliance.fragment.frc2015.MatchDetail2015;
import com.mechinn.android.ouralliance.fragment.frc2015.MatchTeamList2015Fragment;
import com.mechinn.android.ouralliance.fragment.frc2015.TeamDetail2015;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class MatchScoutingActivity extends OurAllianceActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "MatchScoutingActivity";
	private MatchListFragment matchListFrag;
    private MatchTeamListFragment matchTeamListFrag;
	private MatchDetailFragment matchDetailFragment;
    private TeamDetailFragment teamDetailFragment;
	private int matchFrag;
    private int teamFrag;
	private int detailFrag;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_scouting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.getSupportFragmentManager().addOnBackStackChangedListener(this);
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
    protected void onResume() {
	    super.onResume();
        if(getPrefs().getComp()<1) {
            this.finish();
        }
	}

	public void onEventMainThread(SelectMatchEvent match) {
        long matchId = match.getId();
		Timber.d( "match: "+matchId);

        Bundle bundle = new Bundle();
        bundle.putLong(MatchTeamListFragment.MATCH_ARG, matchId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                matchTeamListFrag = new MatchTeamList2014Fragment();
                break;
            case 2015:
                matchTeamListFrag = new MatchTeamList2015Fragment();
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

    public void onEventMainThread(SelectMatchTeamEvent scouting) {
        long scoutingId = scouting.getId();
        Timber.d( "team: "+scoutingId);
        Bundle args = new Bundle();
        args.putLong(MatchDetailFragment.SCOUTING_ARG, scoutingId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                matchDetailFragment = new MatchDetail2014();
                break;
            case 2015:
                matchDetailFragment = new MatchDetail2015();
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

    public void onEventMainThread(SelectTeamEvent team) {
        long teamId = team.getId();
        Timber.d( "team: "+teamId);
        Bundle args = new Bundle();
        args.putLong(TeamDetailFragment.TEAM_ARG, teamId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(getPrefs().getYear()) {
            case 2014:
                teamDetailFragment = new TeamDetail2014();
                break;
            case 2015:
                teamDetailFragment = new TeamDetail2015();
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
		Timber.i("back stack changed " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() < 1){
            this.getSupportActionBar().setTitle(R.string.matches);
        }
	}
	
	public void onEventMainThread(Prefs prefsChanged) {
        String key = prefsChanged.getKeyChanged();
		Timber.d( key);
		if(key.equals(this.getString(R.string.pref_practice))) {
			this.recreate();
		}
	}
}
