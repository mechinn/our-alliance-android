package com.mechinn.android.ouralliance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.activity.AnalysisActivity;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.GraphDataSet;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.TeamGraph;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public abstract class AnalysisFragment extends Fragment {
    public static final String TAG = "TeamAnalysisFragment";
    private LineChart chart;
    private Prefs prefs;
    private ArrayList<Graph> teamGraphs;
    private ArrayList<Graph> matchGraphs;
    private ArrayList<TeamGraph> teams;
    private ArrayList<GraphDataSet> dataSets;

    public class Graph {
        private String label;
        private GraphGetter getter;
        private int color;
        public Graph(String label, int color, GraphGetter getter) {
            this.label = label;
            this.color = getResources().getColor(color);
            this.getter = getter;
        }

        public String getLabel() {
            return label;
        }

        public GraphGetter getGetter() {
            return getter;
        }

        public int getColor() {
            return color;
        }
    }
    public abstract class GraphGetter<O extends OurAllianceObject> {
        public abstract Object getter(O scouting);
        public BarEntry barEntry(O scouting, int count) {
            return new BarEntry(getValue(scouting),count);
        }
        public float getValue(O scouting) {
            return getFloat(this.getter(scouting));
        }
        private float getFloat(Object obj) {
            float value = 0;
            if(null!=obj) {
                if(obj instanceof Boolean) {
                    value = ((Boolean)obj)?1:0;
                } else if(obj instanceof Integer) {
                    value = ((Integer)obj).floatValue();
                } else if(obj instanceof Float) {
                    value = ((Float)obj).floatValue();
                } else if(obj instanceof Double) {
                    value = ((Double)obj).floatValue();
                }
            }
            return value;
        }
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public ArrayList<Graph> getTeamGraphs() {
        return teamGraphs;
    }

    public void addTeamGraph(String label, int color, GraphGetter getter) {
        this.teamGraphs.add(new Graph(label,color,getter));
    }

    public ArrayList<Graph> getMatchGraphs() {
        return matchGraphs;
    }

    public void addMatchGraph(String label, int color, GraphGetter getter) {
        this.matchGraphs.add(new Graph(label, color, getter));
    }

    public void addTeam(String team) {
        teams.add(new TeamGraph(team));
    }

    public ArrayList<TeamGraph> getTeams() {
        return teams;
    }

    public void addTeamDataSet(GraphDataSet graphDataSet) {
        dataSets.add(graphDataSet);
    }
    public void addMatchDataSet(GraphDataSet graphDataSet) {
        dataSets.add(graphDataSet);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
        teams = new ArrayList<>();
        teamGraphs = new ArrayList<>();
        matchGraphs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_analysis, container, false);
        chart = (LineChart) rootView.findViewById(R.id.chart);
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(true);
        chart.setHighlightEnabled(true);
        chart.setHighlightIndicatorEnabled(true);
        chart.animateXY(3000, 3000);
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
        dataSets = new ArrayList<>();
        loadTeamList();
    }

    public void onStop() {
        Timber.d("stop");
        EventBus.getDefault().unregister(this);
        super.onStop();
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
    public void onEventMainThread(AnalysisActivity.AnalysisNavigationSelected navigationSelected) {
        setChartData();
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
        EventBus.getDefault().post(new TeamsLoaded(getTeams()));
        loadData();
    }

    public class LoadEventTeams {
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
    public class DataSetLoaded {
        private ArrayList<GraphDataSet> dataSet;
        public DataSetLoaded(ArrayList<GraphDataSet> dataSet) {this.dataSet = dataSet;}
        public ArrayList<GraphDataSet> getDataSet() {return dataSet;}
    }
    public class TeamsLoaded {
        private ArrayList<TeamGraph> teams;
        public TeamsLoaded(ArrayList<TeamGraph> teams) {this.teams = teams;}
        public ArrayList<TeamGraph> getTeams() {return teams;}
    }
    protected void loadedChartData() {
        EventBus.getDefault().post(new DataSetLoaded(dataSets));
        setChartData();
    }
    public void setChartData() {
        Timber.d("teams "+teams.size());
        ArrayList<LineDataSet> enabledSets = new ArrayList<>();
        for(GraphDataSet set : dataSets) {
            Timber.d(set.getLabel()+" "+set.getEntryCount()+" "+(set.isEnabled()?"Enabled":"Disabled"));
            if(set.isEnabled()) {
                enabledSets.add(set);
            }
        }
        for(int teamPosition=teams.size()-1;teamPosition>=0;teamPosition--) {
            TeamGraph team = teams.get(teamPosition);
            Timber.d(team.getLabel()+" "+(team.isEnabled()?"Enabled":"Disabled"));
            if(!team.isEnabled()) {
                for(LineDataSet set : enabledSets) {
                    set.removeEntry(teamPosition);
                }
            }
        }
        ArrayList<String> enabledTeams = new ArrayList<>();
        for(int teamPosition=0;teamPosition<teams.size();teamPosition++) {
            TeamGraph team = teams.get(teamPosition);
            Timber.d(team.getLabel()+" "+(team.isEnabled()?"Enabled":"Disabled"));
            if(team.isEnabled()) {
                enabledTeams.add(team.getLabel());
            }
        }
        chart.setData(new LineData(enabledTeams,enabledSets));
        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
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
