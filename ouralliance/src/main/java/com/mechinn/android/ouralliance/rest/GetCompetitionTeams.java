package com.mechinn.android.ouralliance.rest;

import android.app.Activity;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import retrofit.RetrofitError;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.SqlStatement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetCompetitionTeams extends HandlerThread {
    public static final String TAG = "GetCompetitionTeams";
    public static final String PROGRESS = "progress";
    public static final String STATUS = "status";
    private Prefs prefs;
    private Handler threadHandler = null;
    private Handler uiHandler = null;
    private Message message;
    private Activity activity;

    public GetCompetitionTeams(Activity activity) {
        super(TAG);
        this.activity = activity;
        prefs = new Prefs(activity);
        message = new Message();
        uiHandler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        GetCompetitionTeams.this.activity.setProgressBarIndeterminateVisibility(msg.getData().getBoolean(PROGRESS));
                        if(null!=msg.getData().getString(STATUS)) {
                            Toast.makeText(GetCompetitionTeams.this.activity,msg.getData().getString(STATUS),Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }
        );
        start();
        threadHandler = new Handler(getLooper());
    }

    public void refreshCompetitionTeams() {
        threadHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        message = new Message();
                        message.getData().putString(STATUS, "Downloading competition teams...");
                        message.getData().putBoolean(PROGRESS, true);
                        uiHandler.sendMessage(message);
                        Log.d(TAG, "year: " + prefs.getYear());
                        CursorList<CompetitionTeam> competitionTeamCursorList = Query.many(CompetitionTeam.class, "SELECT * FROM "+CompetitionTeam.TAG+" WHERE "+CompetitionTeam.COMPETITION+"=?",prefs.getComp()).get();
                        ModelList<CompetitionTeam> competitionTeamList = null;
                        if(null!=competitionTeamCursorList && null!=competitionTeamCursorList.getCursor() && !competitionTeamCursorList.getCursor().isClosed()) {
                            competitionTeamList = ModelList.from(competitionTeamCursorList);
                            competitionTeamCursorList.close();
                        }
                        Competition competition = Query.one(Competition.class, "SELECT * FROM " + Competition.TAG + " WHERE " + Competition._ID + "=?", prefs.getComp()).get();
                        Log.d(TAG, "Setting up teams");
                        try {
                            List<Team> teams = TheBlueAlliance.getService().getEventTeams(prefs.getYear() + competition.getCode());
                            Collections.sort(teams);
                            Date current = new Date();
                            for (int i = 0; i < teams.size(); ++i) {
                                Log.d(TAG, "name: " + teams.get(i).getNickName());
                                Log.d(TAG, "number: " + teams.get(i).getTeamNumber());
                                teams.get(i).setModified(current);
                                teams.get(i).save();
                                new CompetitionTeam(competition, teams.get(i), i).save();
                                switch (prefs.getYear()) {
                                    case 2014:
                                        new TeamScouting2014(teams.get(i)).save();
                                        break;
                                }
                            }
                            if(null!=competitionTeamList) {
                                for(CompetitionTeam team : competitionTeamList) {
                                    if(!teams.contains(team.getTeam())) {
                                        Log.d(TAG,"deleting "+team);
                                        team.delete();
                                    }
                                }
                            }
                            message = new Message();
                            message.getData().putString(STATUS, "Finished downloading matches");
                            message.getData().putBoolean(PROGRESS, false);
                            uiHandler.sendMessage(message);
                            prefs.setCompetitionTeamsDownloaded(true);
                        } catch (RetrofitError e) {
                            Log.e(TAG,"Error downloading competition teams",e);
                            if(e.isNetworkError()) {
                                message = new Message();
                                message.getData().putString(STATUS, "Unable to connect");
                                message.getData().putBoolean(PROGRESS, false);
                                uiHandler.sendMessage(message);
                            } else if(e.getResponse().getStatus()!=200) {
                                message = new Message();
                                message.getData().putString(STATUS, "Error "+e.getResponse().getStatus()+" connecting");
                                message.getData().putBoolean(PROGRESS, false);
                                uiHandler.sendMessage(message);
                            }
                        }
                    }
                }
        );
    }

}
