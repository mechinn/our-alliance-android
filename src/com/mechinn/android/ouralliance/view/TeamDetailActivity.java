package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.view.frc2013.TeamDetail2013;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

/**
 * An activity representing a single Team detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link TeamListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link TeamDetailFragment}.
 */
public class TeamDetailActivity extends Activity {
	private static final String TAG = "TeamDetailActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//load view
		setContentView(R.layout.activity_team_detail);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			long team = getIntent().getLongExtra(TeamDetailFragment.ARG_SEASON, 0);
			int year = getIntent().getIntExtra(TeamDetailFragment.ARG_YEAR, 0);
			long season = getIntent().getLongExtra(TeamDetailFragment.ARG_TEAM, 0);
			Log.d(TAG, "team id: "+team);
			arguments.putLong(TeamDetailFragment.ARG_SEASON, team);
			arguments.putInt(TeamDetailFragment.ARG_YEAR, year);
			arguments.putLong(TeamDetailFragment.ARG_TEAM, season);
			switch(year) {
				case 2013:
					TeamDetailFragment<?,?> fragment = new TeamDetail2013();
					fragment.setArguments(arguments);
					this.getFragmentManager().beginTransaction().add(R.id.team_detail_container, fragment).commit();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.navigateUpToFromChild(this, new Intent(this, TeamListActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
