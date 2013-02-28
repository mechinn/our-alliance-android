package com.mechinn.android.ouralliance.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.view.frc2013.TeamDetail2013;

public class TeamScoutingActivity extends Activity implements TeamListFragment.Listener, InsertTeamDialogFragment.Listener, DeleteTeamDialogFragment.Listener, OnBackStackChangedListener, Setup.Listener, MultimediaContextDialog.Listener {
	public static final String TAG = TeamScoutingActivity.class.getSimpleName();
	public static final String TEAM_ARG = "team";
	public static final String MATCH_ARG = "match";
	private TeamListFragment teamListFrag;
	private TeamDetailFragment<?, ?> teamDetailFragment;
	private int listFrag;
	private int detailFrag;
	private long loadTeam;
	private Prefs prefs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Setup(this, false).execute();
		setContentView(R.layout.activity_team_scouting);
		this.getFragmentManager().addOnBackStackChangedListener(this);
		prefs = new Prefs(this);
        // Create an instance of ExampleFragment
    	teamListFrag = new TeamListFragment();
        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
    	teamListFrag.setArguments(getIntent().getExtras());
        // Add the fragment to the 'fragment_container' FrameLayout
        if (this.findViewById(R.id.fragment_container) != null) {
        	listFrag = R.id.fragment_container;
    		detailFrag = R.id.fragment_container;
		} else {
        	listFrag = R.id.list_fragment;
			detailFrag = R.id.detail_fragment;
		}
        loadTeam = this.getIntent().getLongExtra(TEAM_ARG, 0);
        if(0!=loadTeam) {
        	if(listFrag==detailFrag) {
        		onTeamSelected(loadTeam);
        	} else {
                getFragmentManager().beginTransaction().replace(listFrag, teamListFrag).commit();
        		onTeamSelected(loadTeam);
        	}
        } else {
            getFragmentManager().beginTransaction().replace(listFrag, teamListFrag).commit();
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
        args.putLong(Team.TAG, team);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(prefs.getYear()) {
			case 2013:
				teamDetailFragment = new TeamDetail2013();
	            break;
	        default:
	        	Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
	            transaction.commit();
	            return;
		}
		teamDetailFragment.setArguments(args);
        transaction.replace(detailFrag, teamDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}

	public void onBackStackChanged() {
		Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
        	this.setTitle(R.string.app_name);
    		this.getActionBar().setDisplayHomeAsUpEnabled(false);
    		this.getActionBar().setHomeButtonEnabled(false);
        }
	}

	public void onInsertTeamDialogPositiveClick(boolean update, Team team) {
		if(update) {
	    	teamListFrag.updateTeam(team);
		} else {
	    	teamListFrag.insertTeam(team);
		}
	}
	
	public void onDeleteDialogPositiveClick(long team) {
    	teamListFrag.deleteTeam(team);
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
}
