package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.*;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements Setup.Listener, GenericDialogFragment.Listener {
	private SettingsFragment frag;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		frag = new SettingsFragment();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();
        
//        String action = getIntent().getAction();
//        if (action != null && action.equals(ACTION_PREFS_ONE)) {
//            addPreferencesFromResource(R.xml.preferences);
//        }
        
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            // Load the legacy preferences headers
//            addPreferencesFromResource(R.xml.preference_headers_legacy);
//        }
    }

	public void setupComplete() {
		frag.onResume();
	}
	
	public void onGenericDialogPositiveClick(DialogInterface dialog, int id) {
		frag.resetData();
	}

	public void onGenericDialogNegativeClick(DialogInterface dialog, int id) {
		dialog.dismiss();
	}
	
//	public void onBuildHeaders(List<PreferenceActivity.Header> target) {
//		this.loadHeadersFromResource(R.xml.preference_headers, target);
//	}
}
