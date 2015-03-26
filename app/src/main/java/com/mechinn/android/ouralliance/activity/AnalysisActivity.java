package com.mechinn.android.ouralliance.activity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.fragment.AnalysisFragment;
import com.mechinn.android.ouralliance.fragment.AnalysisNavigationFragment;
import com.mechinn.android.ouralliance.fragment.frc2015.AnalysisFragment2015;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class AnalysisActivity extends OurAllianceActivity implements FragmentManager.OnBackStackChangedListener  {

    private AnalysisNavigationFragment navigation;
    private AnalysisFragment fragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportFragmentManager().addOnBackStackChangedListener(this);
        setContentView(R.layout.activity_analysis);

        navigation = (AnalysisNavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        fragment = new AnalysisFragment2015();
        mTitle = getTitle();

        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_drawer, navigation).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        navigation.setup(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
    }

    protected void onStart() {
        super.onStart();
        Timber.d("start");
        navigation.setGraphs(fragment.getTeamGraphs(),fragment.getMatchGraphs());
    }

    @Override
    public void onBackStackChanged() {
        Timber.i("back stack changed " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() < 1){
            this.getSupportActionBar().setTitle("Analysis");
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigation.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.ouralliance, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
