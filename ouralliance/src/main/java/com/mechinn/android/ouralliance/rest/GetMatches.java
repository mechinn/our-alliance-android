package com.mechinn.android.ouralliance.rest;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import retrofit.RetrofitError;
import se.emilsjolander.sprinkles.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetMatches extends HandlerThread {
    public static final String TAG = "GetMatches";
    public static final String PROGRESS = "progress";
    public static final String STATUS = "status";
    private Prefs prefs;
    private Activity activity;
    private Handler threadHandler = null;
    private Handler uiHandler = null;
    private Message message;

    public GetMatches(Activity activity) {
        super(TAG);
        this.activity = activity;
        prefs = new Prefs(activity);
        message = new Message();
        uiHandler = new Handler(
            new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    GetMatches.this.activity.setProgressBarIndeterminateVisibility(msg.getData().getBoolean(PROGRESS));
                    if(null!=msg.getData().getString(STATUS)) {
                        Toast.makeText(GetMatches.this.activity,msg.getData().getString(STATUS),Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
        );
        start();
        threadHandler = new Handler(getLooper());
    }

    public void refreshMatches() {
        threadHandler.post(
            new Runnable() {
                @Override
                public void run() {
                    message = new Message();
                    message.getData().putString(STATUS, "Downloading matches...");
                    message.getData().putBoolean(PROGRESS, true);
                    uiHandler.sendMessage(message);
                    Log.d(TAG, "year: " + prefs.getYear());
                    Competition competition = Query.one(Competition.class, "SELECT * FROM " + Competition.TAG + " WHERE " + Competition._ID + "=?", prefs.getComp()).get();
                    Log.d(TAG, "getting matches "+prefs.getYear() + competition.getCode());
                    Date current = new Date();
                    try {
                        List<Match> matches = TheBlueAlliance.getService().getEventMatches(prefs.getYear() + competition.getCode());
                        Collections.sort(matches);
                        CompetitionTeam competitionTeam;
                        for (Match match : matches) {
                            Log.d(TAG, "number: " + match.getMatchNum());
                            Log.d(TAG, "set: " + match.getMatchSet());
                            Log.d(TAG, "type: " + match.getMatchType());
                            Log.d(TAG, "red score: " + match.getRedScore());
                            Log.d(TAG, "blue score: " + match.getBlueScore());
                            match.convertCompLevelToMatchType();
                            match.convertScores();
                            match.setCompetition(competition);
                            match.setModified(current);
                            if(null==match.getAlliances() || match.getAlliances().getRed().getFoundTeams().size()!=3 || match.getAlliances().getBlue().getFoundTeams().size()!=3) {
                                throw new Exception("Incomplete Data");
                            }
                            match.save();
                            switch (prefs.getYear()) {
                                case 2014:
                                    for (Team team : match.getAlliances().getRed().getFoundTeams()) {
                                        Log.d(TAG, "team: " + team);
                                        competitionTeam = new CompetitionTeam(competition, team, 0);
                                        competitionTeam.save();
                                        new MatchScouting2014(match, competitionTeam, false).save();
                                    }
                                    for (Team team : match.getAlliances().getBlue().getFoundTeams()) {
                                        Log.d(TAG, "team: " + team);
                                        competitionTeam = new CompetitionTeam(competition, team, 0);
                                        competitionTeam.save();
                                        new MatchScouting2014(match, competitionTeam, true).save();
                                    }
                                    break;
                            }
                        }
                        message = new Message();
                        message.getData().putString(STATUS, "Finished downloading matches");
                        message.getData().putBoolean(PROGRESS, false);
                        uiHandler.sendMessage(message);
                        prefs.setMatchesDownloaded(true);
                    } catch (RetrofitError e) {
                        Log.e(TAG,"Error downloading matches",e);
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
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage(),e);
                        message = new Message();
                        message.getData().putString(STATUS, e.getMessage());
                        message.getData().putBoolean(PROGRESS, false);
                        uiHandler.sendMessage(message);
                    }
                }
            }
        );
    }
}
