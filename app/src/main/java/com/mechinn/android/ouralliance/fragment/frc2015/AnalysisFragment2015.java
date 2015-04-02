package com.mechinn.android.ouralliance.fragment.frc2015;

import android.os.Bundle;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.GraphDataSet;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.fragment.AnalysisFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public class AnalysisFragment2015 extends AnalysisFragment {
    public static final String TAG = "TeamAnalysisFragment2015";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTeamGraph(TeamScouting2015.WIDTH, android.R.color.holo_blue_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getWidth();
            }
        });
        addTeamGraph(TeamScouting2015.LENGTH, android.R.color.holo_green_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getLength();
            }
        });
        addTeamGraph(TeamScouting2015.HEIGHT, android.R.color.holo_orange_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getHeight();
            }
        });
        addTeamGraph(TeamScouting2015.COOP, android.R.color.holo_purple, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getCoop();
            }
        });
        addTeamGraph(TeamScouting2015.DRIVER_EXPERIENCE, android.R.color.holo_red_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getDriverExperience();
            }
        });
        addTeamGraph(TeamScouting2015.MAX_TOTE_STACK, android.R.color.holo_blue_bright, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getMaxToteStack();
            }
        });
        addTeamGraph(TeamScouting2015.MAX_CONTAINER_STACK, android.R.color.holo_green_light, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getMaxTotesStackContainer();
            }
        });
        addTeamGraph(TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER, android.R.color.holo_orange_light, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getMaxTotesAndContainerLitter();
            }
        });
        addTeamGraph(TeamScouting2015.HUMAN_PLAYER, android.R.color.holo_red_light, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getHumanPlayer();
            }
        });
        addTeamGraph(TeamScouting2015.NO_AUTO, android.R.color.holo_blue_light, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getNoAuto();
            }
        });
        addTeamGraph(TeamScouting2015.DRIVE_AUTO, android.R.color.black, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getDriveAuto();
            }
        });
        addTeamGraph(TeamScouting2015.TOTE_AUTO, android.R.color.holo_blue_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getToteAuto();
            }
        });
        addTeamGraph(TeamScouting2015.CONTAINER_AUTO, android.R.color.holo_green_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getContainerAuto();
            }
        });
        addTeamGraph(TeamScouting2015.STACKED_AUTO, android.R.color.holo_orange_dark, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getStackedAuto();
            }
        });
        addTeamGraph(TeamScouting2015.LANDFILL_AUTO, android.R.color.holo_purple, new GraphGetter<TeamScouting2015>() {
            @Override
            public Object getter(TeamScouting2015 scouting) {
                return scouting.getLandfillAuto();
            }
        });
        addMatchGraph(MatchScouting2015.AUTO_STACKED, android.R.color.holo_red_dark, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoStacked();
            }
        });
        addMatchGraph(MatchScouting2015.AUTO_TOTES, android.R.color.holo_blue_bright, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoTotes();
            }
        });
        addMatchGraph(MatchScouting2015.AUTO_CONTAINERS, android.R.color.holo_green_light, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoContainers();
            }
        });
        addMatchGraph(MatchScouting2015.AUTO_LANDFILL, android.R.color.holo_orange_light, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoLandfill();
            }
        });
        addMatchGraph(MatchScouting2015.AUTO_MOVE, android.R.color.holo_red_light, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getAutoMove();
            }
        });
        addMatchGraph(MatchScouting2015.COOP, android.R.color.holo_blue_light, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getCoop();
            }
        });
        addMatchGraph(MatchScouting2015.TOTES, android.R.color.black, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getTotes();
            }
        });
        addMatchGraph(MatchScouting2015.CONTAINERS, android.R.color.holo_blue_dark, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getContainers();
            }
        });
        addMatchGraph(MatchScouting2015.LITTER, android.R.color.holo_green_dark, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getLitter();
            }
        });
        addMatchGraph(MatchScouting2015.FOULS, android.R.color.holo_orange_dark, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getFouls();
            }
        });
        addMatchGraph(MatchScouting2015.HUMAN_ATTEMPT, android.R.color.holo_purple, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getHumanAttempt();
            }
        });
        addMatchGraph(MatchScouting2015.HUMAN_SUCCESS, android.R.color.holo_red_dark, new GraphGetter<MatchScouting2015>() {
            @Override
            public Object getter(MatchScouting2015 scouting) {
                return scouting.getHumanSuccess();
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
        for (Graph teamGraph : getTeamGraphs()) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int count = 0; count < scoutingList.size(); count++) {
                TeamScouting2015 team = scoutingList.get(count);
                entries.add(teamGraph.getGetter().barEntry(team, count));
            }
            GraphDataSet graphDataSet = new GraphDataSet(entries,teamGraph.getLabel(), GraphDataSet.Type.TEAM);
            graphDataSet.setColor(teamGraph.getColor());
            addTeamDataSet(graphDataSet);
        }
        loadedChartData();
    }
    public void onEventMainThread(LoadMatches loadMatches) {
        setMatchData(loadMatches);
    }
    public void setMatchData(LoadMatches loadMatches) {
        List<MatchScouting2015> scoutingList = (List<MatchScouting2015>) loadMatches.getScouting();
        Collections.sort(scoutingList);
        TreeMap<Integer,Float> teams = new TreeMap<>();
        for (Graph matchGraph : getMatchGraphs()) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int count = 0; count < scoutingList.size(); count++) {
                MatchScouting2015 scouting = scoutingList.get(count);
                int teamNumber = scouting.getTeamScouting().getTeam().getTeamNumber();
                Float average = teams.get(teamNumber);
                float scoutingValue = matchGraph.getGetter().getValue(scouting);
                if(null==average) {
                    teams.put(teamNumber, scoutingValue);
                } else {
                    teams.put(teamNumber, (average+scoutingValue)/2);
                }
            }
            int count=0;
            for (Map.Entry<Integer, Float> team : teams.entrySet()) {
                Integer key = team.getKey();
                Timber.d("key:" + key);
                Float value = team.getValue();
                entries.add(new BarEntry(value, count++));
            }
            GraphDataSet graphDataSet = new GraphDataSet(entries,matchGraph.getLabel(), GraphDataSet.Type.MATCH);
            graphDataSet.setColor(matchGraph.getColor());
            addMatchDataSet(graphDataSet);
        }
        loadedChartData();
    }
}
