package com.mechinn.android.ouralliance.activity;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.GraphDataSetAdapter;
import com.mechinn.android.ouralliance.adapter.TeamGraphAdapter;
import com.mechinn.android.ouralliance.data.GraphDataSet;
import com.mechinn.android.ouralliance.fragment.AnalysisFragment;
import com.mechinn.android.ouralliance.fragment.frc2015.AnalysisFragment2015;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import timber.log.Timber;

public class AnalysisActivity extends OurAllianceActivity implements FragmentManager.OnBackStackChangedListener, TabHost.OnTabChangeListener {
    private static final String GRAPHS = "Graphs";
    private static final String TEAMS = "Teams";
    private ActionBarDrawerToggle mDrawerToggle;
    private AnalysisFragment fragment;
    private DrawerLayout mDrawerLayout;
    private TabHost listTabs;
    private StickyListHeadersListView graphList;
    private StickyListHeadersListView teamList;
    private GraphDataSetAdapter graphAdapter;
    private TeamGraphAdapter teamAdapter;
    private Prefs prefs;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals(GRAPHS)) {

        }else if(tabId.equals(TEAMS)) {

        }
    }

    public class AnalysisGraphSelected {
        private int position;
        public AnalysisGraphSelected(int position) {this.position = position;}
        public int getPosition() { return position;}
    }
    public class AnalysisTeamSelected {
        private int position;
        public AnalysisTeamSelected(int position) {this.position = position;}
        public int getPosition() { return position;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this);
        this.getSupportFragmentManager().addOnBackStackChangedListener(this);
        setContentView(R.layout.activity_analysis);
        listTabs = (TabHost)findViewById(R.id.navigation_drawer);
        listTabs.setup();
        listTabs.setOnTabChangedListener(this);
        listTabs.addTab(listTabs.newTabSpec(GRAPHS).setIndicator(GRAPHS).setContent(R.id.graph_data));
        listTabs.addTab(listTabs.newTabSpec(TEAMS).setIndicator(TEAMS).setContent(R.id.graph_teams));
        graphList = (StickyListHeadersListView) listTabs.getTabContentView().getChildAt(0);
        teamList = (StickyListHeadersListView) listTabs.getTabContentView().getChildAt(1);

        graphList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GraphDataSet graph = (GraphDataSet) graphAdapter.getItem(position);
                Timber.d(graph.getLabel()+" selected");
                graphList.setItemChecked(position, !graph.isEnabled());
                graph.switchEnabled();
//                mDrawerLayout.closeDrawer(mFragmentContainerView);
                EventBus.getDefault().post(new AnalysisGraphSelected(position));
            }
        });
        graphAdapter = new GraphDataSetAdapter(this);
        graphList.setAdapter(graphAdapter);
        graphList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GraphDataSet graph = (GraphDataSet) teamAdapter.getItem(position);
                Timber.d(graph.getLabel()+" selected");
                teamList.setItemChecked(position, !graph.isEnabled());
                graph.switchEnabled();
//                mDrawerLayout.closeDrawer(mFragmentContainerView);
                EventBus.getDefault().post(new AnalysisTeamSelected(position));
            }
        });
        teamAdapter = new TeamGraphAdapter(this);
        teamList.setAdapter(teamAdapter);
        teamList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // add views to tab host
//        listTabs.addTab(listTabs.newTabSpec(TEAMS).setIndicator(TEAMS).setContent(new TabHost.TabContentFactory() {
//            public View createTabContent(String arg0) {
//                return teamList;
//            }
//        }));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                AnalysisActivity.this.supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!prefs.isAnalysisDrawerLearned()) {
                    prefs.setAnalysisDrawerLearned(true);
                }
                AnalysisActivity.this.supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        if (!prefs.isAnalysisDrawerLearned()) {
            mDrawerLayout.openDrawer(listTabs);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        fragment = new AnalysisFragment2015();
        mTitle = getTitle();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onStart() {
        super.onStart();
        Timber.d("start");
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        Timber.d("stop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEventMainThread(AnalysisFragment.DataSetLoaded dataSet) {
        ArrayList<GraphDataSet> graphDataSets = dataSet.getDataSet();
        graphAdapter.changeList(graphDataSets);
        for(int graphNum=0;graphNum<graphDataSets.size();graphNum++) {
            graphList.setItemChecked(graphNum,graphDataSets.get(graphNum).isEnabled());
        }
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
        if (!isDrawerOpen()) {
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
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(listTabs);
    }
}
