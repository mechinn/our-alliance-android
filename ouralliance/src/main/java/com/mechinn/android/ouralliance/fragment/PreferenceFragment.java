package com.mechinn.android.ouralliance.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.mechinn.android.ouralliance.R;

/**
 * Created by mechinn on 2/19/14.
 */
public class PreferenceFragment extends PreferenceListFragment implements SharedPreferences.OnSharedPreferenceChangeListener, PreferenceListFragment.OnPreferenceAttachedListener {
    public static final String TAG = PreferenceFragment.class.getSimpleName();
    public static final String SHARED_PREFS_NAME = "settings";

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.preferences);
        preferenceManager.getSharedPreferences() .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
        if (root == null) {
            return;
        }
    }
}