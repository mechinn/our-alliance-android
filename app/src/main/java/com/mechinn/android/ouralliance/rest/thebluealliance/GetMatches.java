package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;

import java.util.ArrayList;
import java.util.List;

public class GetMatches implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetMatches";
    private Context context;
    private Prefs prefs;

    public GetMatches(Context context) {
        this.context = context;
        this.prefs = new Prefs(context);
    }

    @Override
    public void run() throws Exception {
        ToastEvent.toast("Downloading matches...",true);
        Event event = Model.load(Event.class, prefs.getComp());
        Log.d(TAG, "year: " + prefs.getYear());
        Log.d(TAG, "getting matches "+prefs.getYear() + event.getEventCode());
        ActiveAndroid.beginTransaction();
        boolean teamsChanged = false;
        boolean teamScoutingChanged = false;
        boolean matchScoutingChanged = false;
        boolean matchesChanged = false;
        try {
            int year = prefs.getYear();
            List<Match> matches = TheBlueAlliance.getService().getEventMatches(year + event.getEventCode());
            for(Match match : matches) {
                match.setRedScore(match.getAlliances().getRed().getScore());
                match.setBlueScore(match.getAlliances().getBlue().getScore());
                match.setEvent(event);
                match.saveMod();
                matchesChanged = true;
            }
            switch(year) {
                case 2014:
                    for(Match match : matches) {
                        List<String> teams = match.getAlliances().getRed().getTeams();
                        teams.addAll(match.getAlliances().getBlue().getTeams());
                        for(int position=0;position<teams.size();position++) {
                            MatchScouting2014 matchScouting = new MatchScouting2014();
                            matchScouting.setMatch(match);
                            matchScouting.setPosition(position);
                            int teamNum = Integer.parseInt(teams.get(position).substring(3));
                            TeamScouting2014 teamScouting = new Select().from(TeamScouting2014.class).join(Team.class).on(TeamScouting2014.TAG+"."+TeamScouting2014.TEAM+"="+Team.TAG+"."+Team.ID).where(Team.TAG+"."+Team.TEAM_NUMBER+"=?", teamNum).executeSingle();
                            if(null== teamScouting) {
                                teamScouting = new TeamScouting2014();
                                Team teamObject = new Select().from(Team.class).where(Team.TEAM_NUMBER+"=?", teamNum).executeSingle();
                                if(null==teamObject) {
                                    teamObject = new Team();
                                    teamObject.setTeamNumber(teamNum);
                                    teamObject.saveMod();
                                    teamsChanged = true;
                                }
                                teamScouting.setTeam(teamObject);
                                teamScouting.saveMod();
                                teamScoutingChanged = true;
                            }
                            matchScouting.setTeamScouting2014(teamScouting);
                            matchScouting.setAlliance(position>2);
                            matchScouting.saveMod();
                            matchScoutingChanged = true;
                        }
                    }
                    break;
            }
            ActiveAndroid.setTransactionSuccessful();
            if(matchesChanged) {
                EventBus.getDefault().post(new Match());
            }
            if(teamsChanged) {
                EventBus.getDefault().post(new Team());
            }
            if(teamScoutingChanged) {
                EventBus.getDefault().post(new TeamScouting2014());
            }
            if(matchScoutingChanged) {
                EventBus.getDefault().post(new MatchScouting2014());
            }
            ToastEvent.toast("Finished downloading matches", false);
            prefs.setMatchesDownloaded(true);
        } catch (RetrofitError e) {
            Log.e(TAG,"Error downloading matches",e);
            if (e.getKind() == RetrofitError.Kind.NETWORK) {
                ToastEvent.toast("Unable to connect",false);
            } else if (e.getResponse().getStatus() != 200) {
                ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting",false);
            }
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
