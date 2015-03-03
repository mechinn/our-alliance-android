package com.mechinn.android.ouralliance.activity;

import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.mechinn.android.ouralliance.fragment.ResetDialogFragment;
import com.mechinn.android.ouralliance.fragment.SettingsFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends ActionBarActivity {
    public static final String TAG = "SettingsActivity";
	private SettingsFragment frag;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frag = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        if(null==frag) {
            frag = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, frag, SettingsFragment.TAG).commit();
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	if(this.getSupportFragmentManager().getBackStackEntryCount()>0) {
	        		this.getSupportFragmentManager().popBackStack();
	        	} else {
	        		this.finish();
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
//	public void onBuildHeaders(List<PreferenceActivity.Header> target) {
//		this.loadHeadersFromResource(R.xml.preference_headers, target);
//	}
}
