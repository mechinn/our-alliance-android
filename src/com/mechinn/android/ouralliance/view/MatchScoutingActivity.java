package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.view.frc2013.MatchDetail2013;
import com.mechinn.android.ouralliance.view.frc2013.MatchList2013;

import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MatchScoutingActivity extends Activity implements OnBackStackChangedListener, MatchListFragment.Listener, InsertMatchDialogFragment.Listener, DeleteMatchDialogFragment.Listener, OnSharedPreferenceChangeListener {
	public static final String TAG = MatchScoutingActivity.class.getSimpleName();
	private Prefs prefs;
	private MatchListFragment<?, ?> matchListFrag;
	private MatchDetailFragment<?, ?> matchDetailFragment;
	private int listFrag;
	private int detailFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_scouting);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getFragmentManager().addOnBackStackChangedListener(this);
		prefs = new Prefs(this);
		setListFrag();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.ouralliance, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    prefs.setChangeListener(this);
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
	
	private void setListFrag() {
        // Create an instance of ExampleFragment
		switch(prefs.getYear()) {
			case 2013:
				matchListFrag = new MatchList2013();
				break;
			default:
				throw new ClassCastException("Must give year!");
		}
        // Add the fragment to the 'fragment_container' FrameLayout
        if (this.findViewById(R.id.fragment_container) != null) {
        	listFrag = R.id.fragment_container;
    		detailFrag = R.id.fragment_container;
		} else {
        	listFrag = R.id.list_fragment;
			detailFrag = R.id.detail_fragment;
		}
        getFragmentManager().beginTransaction().replace(listFrag, matchListFrag).commitAllowingStateLoss();
	}

	public void onMatchSelected(long match) {
//		if(null!=teamDetailFragment) {
//			teamDetailFragment.updateScouting();
//			teamDetailFragment.commitUpdatedScouting();
//		}
		Log.d(TAG, "match: "+match);
		// The user selected the headline of an article from the HeadlinesFragment

        Bundle args = new Bundle();
        args.putLong(MatchDetailFragment.MATCH_ARG, match);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(prefs.getYear()) {
			case 2013:
				matchDetailFragment = new MatchDetail2013();
	            break;
	        default:
	        	Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
	            transaction.commit();
	            return;
		}
		matchDetailFragment.setArguments(args);
        transaction.replace(detailFrag, matchDetailFragment);
        if(listFrag==detailFrag) {
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
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, key);
		if(key.equals(this.getString(R.string.pref_practice))) {
			this.recreate();
		}
	}

	public void onInsertMatchDialogPositiveClick(boolean update, Match match) {
		if(update) {
	    	matchListFrag.updateMatch(match);
		} else {
			matchListFrag.insertMatch(match);
		}
	}

	@Override
	public void onDeleteDialogPositiveClick(long match) {
		matchListFrag.deleteMatch(match);
	}
}
