package com.mechinn.android.ouralliance.fragment.frc2015;

import android.os.Bundle;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Graph;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.fragment.AnalysisFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public class AnalysisFragment2015 extends AnalysisFragment {
    public static final String TAG = "TeamAnalysisFragment2015";

    public void addTeamGraph(String column, Graph.Getter<TeamScouting2015> getter) {
        super.addTeamGraph(new Graph<TeamScouting2015>(column, getter));
    }
    public void addMatchGraph(String column, Graph.Getter<MatchScouting2015> getter) {
        super.addMatchGraph(new Graph<MatchScouting2015>(column, getter));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTeamGraph(TeamScouting2015.WIDTH, new Graph.Getter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getWidth();
            }
        });
//        addTeamGraph(TeamScouting2015.LENGTH, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getLength();
//            }
//        });
//        addTeamGraph(TeamScouting2015.HEIGHT, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getHeight();
//            }
//        });
//        addTeamGraph(TeamScouting2015.COOP, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getCoop();
//            }
//        });
//        addTeamGraph(TeamScouting2015.DRIVER_EXPERIENCE, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getDriverExperience();
//            }
//        });
//        addTeamGraph(TeamScouting2015.MAX_TOTE_STACK, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getMaxToteStack();
//            }
//        });
//        addTeamGraph(TeamScouting2015.MAX_CONTAINER_STACK, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getMaxTotesStackContainer();
//            }
//        });
//        addTeamGraph(TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getMaxTotesAndContainerLitter();
//            }
//        });
//        addTeamGraph(TeamScouting2015.HUMAN_PLAYER, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getHumanPlayer();
//            }
//        });
//        addTeamGraph(TeamScouting2015.NO_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getNoAuto();
//            }
//        });
//        addTeamGraph(TeamScouting2015.DRIVE_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getDriveAuto();
//            }
//        });
//        addTeamGraph(TeamScouting2015.TOTE_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getToteAuto();
//            }
//        });
//        addTeamGraph(TeamScouting2015.CONTAINER_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getContainerAuto();
//            }
//        });
//        addTeamGraph(TeamScouting2015.STACKED_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getStackedAuto();
//            }
//        });
//        addTeamGraph(TeamScouting2015.LANDFILL_AUTO, new Graph.Getter<TeamScouting2015>() {
//            @Override
//            public Object getter(TeamScouting2015 scouting) {
//                return scouting.getLandfillAuto();
//            }
//        });
        addMatchGraph(MatchScouting2015.AUTO_STACKED, new Graph.Getter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoStacked();
            }
        });
    }

    public void loadTeam() {
        List<TeamScouting2015> scouting = new Select().from(TeamScouting2015.class).join(EventTeam.class).on(EventTeam.TAG + "." + EventTeam.TEAM + "=" + TeamScouting2015.TAG + "." + TeamScouting2015.TEAM).where(EventTeam.TAG + "." + EventTeam.EVENT + "=?", getPrefs().getComp()).execute();
        EventBus.getDefault().postSticky(new LoadTeams(scouting));
    }
    public void loadMatch() {
        List<MatchScouting2015> scouting = new Select().from(MatchScouting2015.class).join(EventTeam.class).on(EventTeam.TAG + "." + EventTeam.TEAM + "=" + MatchScouting2015.TAG + "." + MatchScouting2015.TEAM).where(EventTeam.TAG + "." + EventTeam.EVENT + "=?", getPrefs().getComp()).execute();
        EventBus.getDefault().postSticky(new LoadMatches(scouting));
    }
    public void onEventMainThread(LoadTeams loadTeams) {
        setTeamData(loadTeams);
    }
    public void setTeamData(LoadTeams loadTeams) {
        List<TeamScouting2015> scoutingList = (List<TeamScouting2015>) loadTeams.getScouting();
        Collections.sort(scoutingList);
        for(Graph graph : getTeamGraphs()) {
            if(graph.isEnabled()) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int count = 0; count < scoutingList.size(); count++) {
                    TeamScouting2015 team = scoutingList.get(count);
                    entries.add(graph.barEntry(team, count));
                }
                addDataSet(new BarDataSet(entries, graph.getName()));
            }
        }
        setChartData();
    }
    public void onEventMainThread(LoadMatches loadMatches) {
        setMatchData(loadMatches);
    }
    public void setMatchData(LoadMatches loadMatches) {
        List<MatchScouting2015> scoutingList = (List<MatchScouting2015>) loadMatches.getScouting();
        Collections.sort(scoutingList);

        TreeMap<Integer,Float> teams = new TreeMap<>();
        for(Graph graph : getMatchGraphs()) {
            if(graph.isEnabled()) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int count = 0; count < scoutingList.size(); count++) {
                    MatchScouting2015 scouting = scoutingList.get(count);
                    int teamNumber = scouting.getTeamScouting().getTeam().getTeamNumber();
                    Float average = teams.get(teamNumber);
                    float scoutingValue = graph.getValue(scouting);
                    if(null==average) {
                        teams.put(teamNumber, scoutingValue);
                    } else {
                        teams.put(teamNumber, (average+scoutingValue)/2);
                    }
                }
                int count=0;
                for (Map.Entry<Integer, Float> entry : teams.entrySet()) {
                    Integer key = entry.getKey();
                    Timber.d("key:" + key);
                    Float value = entry.getValue();
                    entries.add(new BarEntry(value, count++));
                }
                addDataSet(new BarDataSet(entries, graph.getName()));
            }
        }
        setChartData();
    }
}
