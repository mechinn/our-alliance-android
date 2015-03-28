package com.mechinn.android.ouralliance.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mechinn.android.ouralliance.*;
import com.mechinn.android.ouralliance.event.MultimediaDeletedEvent;
import com.mechinn.android.ouralliance.event.SelectTeamEvent;
import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;
import com.mechinn.android.ouralliance.fragment.MultimediaContextDialogFragment;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.fragment.TeamListFragment;
import com.mechinn.android.ouralliance.fragment.frc2014.MatchTeamList2014Fragment;
import com.mechinn.android.ouralliance.fragment.frc2014.TeamDetail2014;
import com.mechinn.android.ouralliance.fragment.frc2015.MatchTeamList2015Fragment;
import com.mechinn.android.ouralliance.fragment.frc2015.TeamDetail2015;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class TeamScoutingActivity extends OurAllianceActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "TeamScoutingActivity";
	public static final String TEAM_ARG = "team";
	public static final String MATCH_ARG = "match";
	public static final String MATCHNAME_ARG = "matchname";
    public static final int GOOGLE_PLAY_ERROR = 0;
	private TeamListFragment teamListFrag;
	private MatchTeamListFragment matchTeamListFrag;
	private TeamDetailFragment teamDetailFragment;
	private int listFrag;
	private int detailFrag;
	private long loadTeam;
	private String matchName;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle(R.string.matches);
		setContentView(R.layout.activity_team_scouting);

		this.getSupportFragmentManager().addOnBackStackChangedListener(this);
        // Add the fragment to the 'fragment_container' FrameLayout
        if (this.findViewById(R.id.fragment_container) != null) {
        	listFrag = R.id.fragment_container;
    		detailFrag = R.id.fragment_container;
		} else {
        	listFrag = R.id.list_fragment;
			detailFrag = R.id.detail_fragment;
		}
        if(savedInstanceState == null) {
            matchName = this.getIntent().getStringExtra(MATCHNAME_ARG);
            loadTeam = this.getIntent().getLongExtra(TEAM_ARG, 0);
            if(0!=loadTeam) {
            	Timber.d( "listfrag: "+listFrag+" detailfrag: "+detailFrag);
            	if(listFrag==detailFrag) {
                    selectTeam(loadTeam);
            	} else {
                    switch(getPrefs().getYear()) {
                        case 2014:
                            matchTeamListFrag = new MatchTeamList2014Fragment();
                            break;
                        case 2015:
                            matchTeamListFrag = new MatchTeamList2015Fragment();
                            break;
                        default:
                            Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
                            return;

                    }
                	Bundle bundle = new Bundle();
                	matchTeamListFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(listFrag, matchTeamListFrag).commit();
                    selectTeam(loadTeam);
            	}
            } else {
            	teamListFrag = new TeamListFragment();
            	teamListFrag.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(listFrag, teamListFrag).commit();
            }
        } else if(listFrag==detailFrag) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

	public void onEventMainThread(SelectTeamEvent team) {
        selectTeam(team.getId());
	}

    public void selectTeam(long team) {
        Timber.d( "team: "+team);
        Bundle args = new Bundle();
        args.putLong(TeamDetailFragment.TEAM_ARG, team);

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
        if(listFrag==detailFrag) {
            transaction.addToBackStack(null);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        transaction.commit();
    }

	public void onBackStackChanged() {
		Timber.i("back stack changed " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() < 1){
        	if(null!=matchName) {
                this.getSupportActionBar().setTitle(matchName);
        	} else {
                this.getSupportActionBar().setTitle(R.string.app_name);
        		this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                this.getSupportActionBar().setHomeButtonEnabled(false);
        	}
        }
	}

}
