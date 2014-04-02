package com.mechinn.android.ouralliance.rest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Transaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetCompetitionTeams extends Thread {
    public static final String TAG = "GetCompetitionTeams";
    private Prefs prefs;

    public GetCompetitionTeams(Context context) {
        prefs = new Prefs(context);
    }

    @Override
    public void run() {
        if(prefs.getComp()>0 && !prefs.getCompetitionTeamsDownloaded()) {
            Log.d(TAG, "year: " + prefs.getYear());
            Competition competition = Query.one(Competition.class, "SELECT * FROM " + Competition.TAG + " WHERE " + Competition._ID + "=?", prefs.getComp()).get();
            Log.d(TAG, "Setting up teams");
            List<Team> teams = TheBlueAlliance.getService().getEventTeams(prefs.getYear() + competition.getEventCode());
            Collections.sort(teams);
            for (int i = 0; i < teams.size(); ++i) {
                Log.d(TAG, "name: " + teams.get(i).getNickName());
                Log.d(TAG, "number: " + teams.get(i).getTeamNumber());
                teams.get(i).save();
                new CompetitionTeam(competition, teams.get(i), i).save();
                switch (prefs.getYear()) {
                    case 2014:
                        new TeamScouting2014(teams.get(i)).save();
                        break;
                }
            }
            prefs.setCompetitionTeamsDownloaded(true);
        }
    }
}
