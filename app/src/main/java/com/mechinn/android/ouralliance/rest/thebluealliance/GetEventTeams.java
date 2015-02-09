package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import retrofit.RetrofitError;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Transaction;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetEventTeams extends GetHandlerThread {
    public static final String TAG = "GetCompetitionTeams";

    public GetEventTeams(Context context) {
        super(TAG,context);
    }

    public void refreshCompetitionTeams() {
        getThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        sendMessage("Downloading competition teams...",true);
                        Log.d(TAG, "year: " + getPrefs().getYear());
                        CursorList<CompetitionTeam> competitionTeamCursorList = Query.many(CompetitionTeam.class, "SELECT * FROM "+CompetitionTeam.TAG+" WHERE "+CompetitionTeam.COMPETITION+"=?",getPrefs().getComp()).get();
                        ModelList<CompetitionTeam> competitionTeamList = null;
                        if(null!=competitionTeamCursorList && null!=competitionTeamCursorList.getCursor() && !competitionTeamCursorList.getCursor().isClosed()) {
                            competitionTeamList = ModelList.from(competitionTeamCursorList);
                            competitionTeamCursorList.close();
                        }
                        Competition competition = Query.one(Competition.class, "SELECT * FROM " + Competition.TAG + " WHERE " + Competition._ID + "=?", getPrefs().getComp()).get();
                        Log.d(TAG, "Setting up teams");
                        Transaction t = new Transaction();
                        try {
                            List<Team> teams = TheBlueAlliance.getService().getEventTeams(getPrefs().getYear() + competition.getCode());
                            Collections.sort(teams);
                            Date current = new Date();
                            for (int i = 0; i < teams.size(); ++i) {
                                Log.d(TAG, "name: " + teams.get(i).getNickName());
                                Log.d(TAG, "number: " + teams.get(i).getTeamNumber());
                                teams.get(i).setModified(current);
                                teams.get(i).save(t);
                                new CompetitionTeam(competition, teams.get(i), i).save(t);
                                switch (getPrefs().getYear()) {
                                    case 2014:
                                        new TeamScouting2014(teams.get(i)).save(t);
                                        break;
                                }
                            }
                            if(null!=competitionTeamList) {
                                SparseArray<Team> teamMap = new SparseArray<Team>(teams.size());
                                for (Team team : teams) {
                                    teamMap.put(team.getTeamNumber(),team);
                                }
                                for(CompetitionTeam team : competitionTeamList) {
                                    if(null==teamMap.get(team.getTeam().getTeamNumber())) {
                                        Log.d(TAG,"deleting "+team);
                                        team.delete(t);
                                    }
                                }
                            }
                            t.setSuccessful(true);
                            sendMessage("Finished downloading teams",false);
                            getPrefs().setCompetitionTeamsDownloaded(true);
                        } catch (RetrofitError e) {
                            Log.e(TAG,"Error downloading competition teams",e);
                            if(e.isNetworkError()) {
                                sendMessage("Unable to connect",false);
                            } else if(e.getResponse().getStatus()!=200) {
                                sendMessage("Error "+e.getResponse().getStatus()+" connecting",false);
                            }
                        } finally {
                            t.finish();
                        }
                    }
                }
        );
    }

}
