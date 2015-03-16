package com.mechinn.android.ouralliance.fragment.frc2015;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.fragment.TeamAnalysisFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public class TeamAnalysisFragment2015 extends TeamAnalysisFragment {
    public static final String TAG = "TeamAnalysisFragment2015";
    public static final int COUNT = 15;
    public static TeamAnalysisFragment2015 newInstance(int position) {
        TeamAnalysisFragment2015 fragment = new TeamAnalysisFragment2015();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public void load() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2015> teams = new Select().from(TeamScouting2015.class).join(EventTeam.class).on(EventTeam.TAG + "." + EventTeam.TEAM + "=" + TeamScouting2015.TAG + "." + TeamScouting2015.TEAM).where(EventTeam.TAG + "." + EventTeam.EVENT + "=?", getPrefs().getComp()).execute();
                EventBus.getDefault().post(new LoadTeams(teams));
            }
        });
    }
    public static String positionColumn(int position) {
        switch(position) {
            case 0:
                return TeamScouting2015.WIDTH;
            case 1:
                return TeamScouting2015.LENGTH;
            case 2:
                return TeamScouting2015.HEIGHT;
            case 3:
                return TeamScouting2015.COOP;
            case 4:
                return TeamScouting2015.DRIVER_EXPERIENCE;
            case 5:
                return TeamScouting2015.MAX_TOTE_STACK;
            case 6:
                return TeamScouting2015.MAX_CONTAINER_STACK;
            case 7:
                return TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER;
            case 8:
                return TeamScouting2015.HUMAN_PLAYER;
            case 9:
                return TeamScouting2015.NO_AUTO;
            case 10:
                return TeamScouting2015.DRIVE_AUTO;
            case 11:
                return TeamScouting2015.TOTE_AUTO;
            case 12:
                return TeamScouting2015.CONTAINER_AUTO;
            case 13:
                return TeamScouting2015.STACKED_AUTO;
            case 14:
                return TeamScouting2015.LANDFILL_AUTO;
            default:
                return "Unknown";
        }
    }
    public void onEventMainThread(LoadTeams loadTeams) {
        ArrayList<String> xAxis = new ArrayList<>();
        List<TeamScouting2015> teams = loadTeams.getScouting();
        Collections.sort(teams);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int count=0;count<teams.size();count++) {
            TeamScouting2015 team = teams.get(count);
            xAxis.add(String.valueOf(team.getTeam().getTeamNumber()));
            switch(getPosition()) {
                case 0:
                    entries.add(getBarEntry(team.getWidth(),count));
                    break;
                case 1:
                    entries.add(getBarEntry(team.getLength(),count));
                    break;
                case 2:
                    entries.add(getBarEntry(team.getHeight(),count));
                    break;
                case 3:
                    entries.add(getBarEntry(team.getCoop(),count));
                    break;
                case 4:
                    entries.add(getBarEntry(team.getDriverExperience(),count));
                    break;
                case 5:
                    entries.add(getBarEntry(team.getMaxToteStack(),count));
                    break;
                case 6:
                    entries.add(getBarEntry(team.getMaxTotesStackContainer(),count));
                    break;
                case 7:
                    entries.add(getBarEntry(team.getMaxTotesAndContainerLitter(),count));
                    break;
                case 8:
                    entries.add(getBarEntry(team.getHumanPlayer(),count));
                    break;
                case 9:
                    entries.add(getBarEntry(team.getNoAuto(),count));
                    break;
                case 10:
                    entries.add(getBarEntry(team.getDriveAuto(),count));
                    break;
                case 11:
                    entries.add(getBarEntry(team.getToteAuto(),count));
                    break;
                case 12:
                    entries.add(getBarEntry(team.getContainerAuto(),count));
                    break;
                case 13:
                    entries.add(getBarEntry(team.getStackedAuto(),count));
                    break;
                case 14:
                    entries.add(getBarEntry(team.getLandfillAuto(),count));
                    break;
            }
        }
        BarDataSet dataSet = new BarDataSet(entries,positionColumn(getPosition()));
        BarData barChart = new BarData(xAxis,dataSet);
        setChartData(barChart);
    }
    private class LoadTeams {
        List<TeamScouting2015> scouting;
        public LoadTeams(List<TeamScouting2015> scouting) {
            this.scouting = scouting;
        }
        public List<TeamScouting2015> getScouting() {
            return scouting;
        }
    }
}
