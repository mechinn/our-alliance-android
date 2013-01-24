package com.mechinn.android.ouralliance.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
//	final static String ACTION_PREFS_ONE = "com.example.prefs.PREFS_ONE";
	public static final int LOADER_SEASON = 0;
	public static final int LOADER_COMPETITION = 1;
	public static final int LOADER_SEASON_SUMMARY = 2;
	public static final int LOADER_COMPETITION_SUMMARY = 3;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction() .replace(android.R.id.content, new SettingsFragment()).commit();
        
//        String action = getIntent().getAction();
//        if (action != null && action.equals(ACTION_PREFS_ONE)) {
//            addPreferencesFromResource(R.xml.preferences);
//        }
        
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            // Load the legacy preferences headers
//            addPreferencesFromResource(R.xml.preference_headers_legacy);
//        }
    }
	
//	public void onBuildHeaders(List<PreferenceActivity.Header> target) {
//		this.loadHeadersFromResource(R.xml.preference_headers, target);
//	}
}
