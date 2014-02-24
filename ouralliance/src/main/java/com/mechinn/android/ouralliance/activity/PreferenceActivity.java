package com.mechinn.android.ouralliance.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Menu;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.fragment.PreferenceFragment;
import com.mechinn.android.ouralliance.fragment.PreferenceListFragment;

/**
 * Created by mechinn on 2/19/14.
 */
public class PreferenceActivity extends Activity implements PreferenceListFragment.OnPreferenceAttachedListener, FragmentManager.OnBackStackChangedListener {
    public static final String TAG = PreferenceActivity.class.getSimpleName();
    private Prefs prefs;
    private PreferenceFragment preferenceFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        this.getFragmentManager().addOnBackStackChangedListener(this);
        prefs = new Prefs(this);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null) {
            preferenceFrag = new PreferenceFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, preferenceFrag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
            this.setTitle(R.string.menu_settings);
        }
    }
}