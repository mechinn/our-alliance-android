package com.mechinn.android.ouralliance.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.fragment.InsertTeamDialogFragment;
import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;
import com.mechinn.android.ouralliance.fragment.MultimediaContextDialogFragment;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.fragment.TeamListFragment;
import com.mechinn.android.ouralliance.fragment.frc2014.TeamDetail2014;

public class TeamScoutingActivity extends Activity implements TeamListFragment.Listener, OnBackStackChangedListener, Setup.Listener, MultimediaContextDialogFragment.Listener, InsertTeamDialogFragment.Listener {
	public static final String TAG = TeamScoutingActivity.class.getSimpleName();
	public static final String TEAM_ARG = "team";
	public static final String MATCH_ARG = "match";
	public static final String MATCHNAME_ARG = "matchname";
	private TeamListFragment teamListFrag;
	private MatchTeamListFragment matchTeamListFrag;
	private TeamDetailFragment<?> teamDetailFragment;
	private int listFrag;
	private int detailFrag;
	private long loadTeam;
	private long loadMatch;
	private Prefs prefs;
	private String matchName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Setup(this, false).execute();
		setContentView(R.layout.activity_team_scouting);
		this.getFragmentManager().addOnBackStackChangedListener(this);
		prefs = new Prefs(this);
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
            loadMatch = this.getIntent().getLongExtra(MATCH_ARG, 0);
            if(0!=loadTeam) {
            	Log.d(TAG, "listfrag: "+listFrag+" detailfrag: "+detailFrag);
            	if(listFrag==detailFrag) {
            		onTeamSelected(loadTeam);
            	} else {
                	matchTeamListFrag = new MatchTeamListFragment();
                	Bundle bundle = new Bundle();
                	bundle.putLong(MatchTeamListFragment.MATCH_ARG, loadMatch);
                	matchTeamListFrag.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(listFrag, matchTeamListFrag).commit();
            		onTeamSelected(loadTeam);
            	}
            } else {
            	teamListFrag = new TeamListFragment();
            	teamListFrag.setArguments(getIntent().getExtras());
                getFragmentManager().beginTransaction().replace(listFrag, teamListFrag).commit();
            }
        } else if(listFrag==detailFrag) {
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.ouralliance, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	if(this.getFragmentManager().getBackStackEntryCount()>0) {
	        		this.getFragmentManager().popBackStack();
	        	} else {
	        		this.finish();
	        	}
            return true;
	        case R.id.settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onTeamSelected(long team) {
        Bundle args = new Bundle();
        args.putLong(TeamDetailFragment.TEAM_ARG, team);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(prefs.getYear()) {
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
        if(listFrag==detailFrag) {
        	if(0==loadMatch) {
                transaction.addToBackStack(null);
        	}
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        transaction.commit();
	}

	@SuppressLint("NewApi")
	public void onBackStackChanged() {
		Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
        	if(null!=matchName) {
            	this.setTitle(matchName);
        	} else {
            	this.setTitle(R.string.app_name);
        		this.getActionBar().setDisplayHomeAsUpEnabled(false);
        		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            		this.getActionBar().setHomeButtonEnabled(false);
                }
        	}
        }
	}

	public void onDeletedImage() {
		teamDetailFragment.resetMultimediaAdapter();
	}

	public void cancelled(int flag) {
		switch(flag) {
			
		}
	}

	public void complete(int flag) {
		switch(flag) {
			
		}
	}

    @Override
    public void onInsertTeamDialogPositiveClick(Team team) {
        teamListFrag.saveTeam(team);
    }
}
