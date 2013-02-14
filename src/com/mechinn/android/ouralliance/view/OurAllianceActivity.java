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

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.view.frc2013.TeamDetail2013;

public class OurAllianceActivity extends Activity implements TeamListFragment.OnTeamSelectedListener, OnBackStackChangedListener {
	public static final String TAG = "OurAllianceActivity";
	private Setup setup;
	private int detailFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setup = new Setup(this);
		setup.run();
		setContentView(R.layout.activity_team_scouting);
		this.getFragmentManager().addOnBackStackChangedListener(this);
		detailFrag = R.id.detail_fragment;
        if (this.findViewById(R.id.fragment_container) != null) {
    		detailFrag = R.id.fragment_container;
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
        	if (savedInstanceState != null) {
                return;
            }
            // Create an instance of ExampleFragment
        	TeamListFragment firstFragment = new TeamListFragment();
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
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

	public void onTeamSelected(CompetitionTeam team) {
		Log.d(TAG, team.toString());
		Log.d(TAG, team.getCompetition().getSeason()+" "+team.getTeam());
		Log.d(TAG, team.getCompetition().getSeason().getId()+" "+team.getTeam().getId());
		long compId = team.getCompetition().getSeason().getId();
		int year = team.getCompetition().getSeason().getYear();
		long teamId = team.getTeam().getId();
		// The user selected the headline of an article from the HeadlinesFragment

        Bundle args = new Bundle();
        args.putLong(Season.CLASS, compId);
        args.putInt(Season.YEAR, year);
        args.putLong(Team.CLASS, teamId);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment;
		switch(year) {
			case 2013:
				fragment = new TeamDetail2013();
	            break;
	        default:
	        	Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
	            transaction.commit();
	            return;
		}
		fragment.setArguments(args);
        transaction.replace(detailFrag, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}

	public void onBackStackChanged() {
		Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
        	this.setTitle(R.string.app_name);
    		this.getActionBar().setDisplayHomeAsUpEnabled(false);
        }
	}
}
