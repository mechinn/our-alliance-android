package com.mechinn.android.ouralliance.activity;

import com.mechinn.android.ouralliance.*;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.fragment.GenericDialogFragment;
import com.mechinn.android.ouralliance.fragment.SettingsFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.mechinn.android.ouralliance.rest.GetCompetitions;

public class SettingsActivity extends PreferenceActivity implements BackgroundProgress.Listener, GenericDialogFragment.Listener {
    public static final String TAG = "SettingsActivity";
	private SettingsFragment frag;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
        // Display the fragment as the main content.
        frag = (SettingsFragment) getFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        if(null==frag) {
            frag = new SettingsFragment();
            getFragmentManager().beginTransaction().replace(android.R.id.content, frag, SettingsFragment.TAG).commit();
        }
    }

	public void cancelled(int flag) {
		switch(flag) {
			case Setup.FLAG_SETUP:
				Log.wtf(TAG,"reset cancelled");
				Toast.makeText(this, "Reset was cancelled please contact the developer", Toast.LENGTH_LONG).show();
		}
	}

	public void complete(int flag) {
		Log.d(TAG, "flag: " + flag);
		switch(flag) {
			case Setup.FLAG_SETUP:
				Utility.restartApp(this);
		}
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onGenericDialogPositiveClick(int flag, DialogInterface dialog, int id) {
		frag.onGenericDialogPositiveClick(flag, dialog, id);
	}

	public void onGenericDialogNegativeClick(int flag, DialogInterface dialog, int id) {
		frag.onGenericDialogNegativeClick(flag, dialog, id);
	}
	
//	public void onBuildHeaders(List<PreferenceActivity.Header> target) {
//		this.loadHeadersFromResource(R.xml.preference_headers, target);
//	}
}
