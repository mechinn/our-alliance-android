package com.mechinn.android.ouralliance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Graph;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public abstract class AnalysisFragment extends Fragment {
    public static final String TAG = "TeamAnalysisFragment";
    public static final String ARG_TEAM = "team";
    public static final String ARG_MATCH = "match";
    private BarChart chart;
    private Prefs prefs;
    private ArrayList<Graph> teamGraphs;
    private ArrayList<Graph> matchGraphs;
    private ArrayList<String> teams;
    private ArrayList<BarDataSet> dataSets;

    public Prefs getPrefs() {
        return prefs;
    }

    public ArrayList<Graph> getTeamGraphs() {
        return teamGraphs;
    }

    public void addTeamGraph(Graph column) {
        this.teamGraphs.add(column);
    }

    public ArrayList<Graph> getMatchGraphs() {
        return matchGraphs;
    }

    public void addMatchGraph(Graph column) {
        this.matchGraphs.add(column);
    }

    public void addTeam(String team) {
        teams.add(team);
    }

    public ArrayList<String> getTeams() {
        return teams;
    }

    public void addDataSet(BarDataSet data) {
        dataSets.add(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
        teams = new ArrayList<>();
        dataSets = new ArrayList<>();
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_TEAM)) {
            teamGraphs = (ArrayList<Graph>) savedInstanceState.getSerializable(ARG_TEAM);
        } else {
            teamGraphs = new ArrayList<>();
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_MATCH)) {
            matchGraphs = (ArrayList<Graph>) savedInstanceState.getSerializable(ARG_MATCH);
        } else {
            matchGraphs = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_analysis, container, false);
        chart = (BarChart) rootView.findViewById(R.id.chart);
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(true);
        chart.setHighlightEnabled(true);
        chart.setHighlightIndicatorEnabled(true);
        chart.animateXY(3000, 3000);
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=1;i<=4;i++) {
            xAxis.add("Q"+i);
        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(100,0));
        entries.add(new BarEntry(50,1));
        BarDataSet dataSet = new BarDataSet(entries,"entries");
        BarData barChart = new BarData(xAxis,dataSet);
        chart.setData(barChart);
        chart.invalidate();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("start");
        EventBus.getDefault().register(this);
        loadTeamList();
    }

    public void onStop() {
        Timber.d("stop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_TEAM, teamGraphs);
        outState.putSerializable(ARG_MATCH, matchGraphs);
        super.onSaveInstanceState(outState);
    }
    public void loadData() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                loadTeam();
                loadMatch();
            }
        });
    }
    public void onEventMainThread(AnalysisNavigationFragment.AnalysisNavigationSelected navigationSelected) {
        setTeamData(EventBus.getDefault().getStickyEvent(LoadTeams.class));
        setMatchData(EventBus.getDefault().getStickyEvent(LoadMatches.class));
    }
    public void loadTeamList() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<EventTeam> scouting = new Select().from(EventTeam.class).where(EventTeam.TAG + "." + EventTeam.EVENT + "=?", getPrefs().getComp()).execute();
                EventBus.getDefault().post(new LoadEventTeams(scouting));
            }
        });
    }
    public void onEventMainThread(LoadEventTeams loadTeams) {
        List<EventTeam> teamList = loadTeams.getTeams();
        Collections.sort(teamList);
        for (int count = 0; count < teamList.size(); count++) {
            EventTeam team = teamList.get(count);
            addTeam(String.valueOf(team.getTeam().getTeamNumber()));
        }
        loadData();
    }

    private class LoadEventTeams {
        List<EventTeam> teams;
        public LoadEventTeams(List<EventTeam> teams) {
            this.teams = teams;
        }
        public List<EventTeam> getTeams() {
            return teams;
        }
    }
    public abstract void loadTeam();
    public abstract void loadMatch();
    public abstract void setTeamData(LoadTeams teams);
    public abstract void setMatchData(LoadMatches matches);
    public void setChartData() {
        Timber.d("teams "+teams.size());
        for(BarDataSet set : dataSets) {
            Timber.d(set.getLabel()+" "+set.getEntryCount());
        }
        chart.setData(new BarData(teams,dataSets));
        chart.invalidate();
    }
    protected class LoadTeams {
        List<? extends TeamScouting> scouting;
        public LoadTeams(List<? extends TeamScouting> scouting) {
            this.scouting = scouting;
        }
        public List<? extends TeamScouting> getScouting() {
            return scouting;
        }
    }
    protected class LoadMatches {
        List<? extends MatchScouting> scouting;
        public LoadMatches(List<? extends MatchScouting> scouting) {
            this.scouting = scouting;
        }
        public List<? extends MatchScouting> getScouting() {
            return scouting;
        }
    }
}
