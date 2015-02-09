package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.mechinn.android.ouralliance.OurAllianceException;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import retrofit.RetrofitError;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Transaction;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetMatches extends GetHandlerThread {
    public static final String TAG = "GetMatches";

    public GetMatches(Context context) {
        super(TAG,context);
    }

    public void refreshMatches() {
        getThreadHandler().post(
            new Runnable() {
                @Override
                public void run() {
                    sendMessage("Downloading matches...",true);
                    Log.d(TAG, "year: " + getPrefs().getYear());
                    Competition competition = Query.one(Competition.class, "SELECT * FROM " + Competition.TAG + " WHERE " + Competition._ID + "=?", getPrefs().getComp()).get();
                    Log.d(TAG, "getting matches "+getPrefs().getYear() + competition.getCode());
                    Date current = new Date();
                    Transaction t = new Transaction();
                    try {
                        List<Match> matches = TheBlueAlliance.getService().getEventMatches(getPrefs().getYear() + competition.getCode());
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
                                throw new OurAllianceException("Incomplete Data");
                            }
                            match.save(t);
                            switch (getPrefs().getYear()) {
                                case 2014:
                                    for (Team team : match.getAlliances().getRed().getFoundTeams()) {
                                        Log.d(TAG, "team: " + team);
                                        competitionTeam = new CompetitionTeam(competition, team, 0);
                                        competitionTeam.save(t);
                                        new MatchScouting2014(match, competitionTeam, false).save(t);
                                    }
                                    for (Team team : match.getAlliances().getBlue().getFoundTeams()) {
                                        Log.d(TAG, "team: " + team);
                                        competitionTeam = new CompetitionTeam(competition, team, 0);
                                        competitionTeam.save(t);
                                        new MatchScouting2014(match, competitionTeam, true).save(t);
                                    }
                                    break;
                            }
                        }
                        t.setSuccessful(true);
                        sendMessage("Finished downloading matches",false);
                        getPrefs().setMatchesDownloaded(true);
                    } catch (RetrofitError e) {
                        Log.e(TAG,"Error downloading matches",e);
                        if(e.isNetworkError()) {
                            sendMessage("Unable to connect",false);
                        } else if(e.getResponse().getStatus()!=200) {
                            sendMessage("Error "+e.getResponse().getStatus()+" connecting",false);
                        }
                    } catch (OurAllianceException e) {
                        Log.e(TAG,e.getMessage(),e);
                        sendMessage(e.getMessage(),false);
                    } finally {
                        t.finish();
                    }
                }
            }
        );
    }
}
