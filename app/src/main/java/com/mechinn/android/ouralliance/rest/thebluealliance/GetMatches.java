package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;
import timber.log.Timber;

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
        Timber.d("year: " + prefs.getYear());
        Timber.d( "getting matches "+prefs.getYear() + event.getEventCode());
        ActiveAndroid.beginTransaction();
        boolean teamsChanged = false;
        boolean teamScoutingChanged = false;
        boolean matchScoutingChanged = false;
        boolean matchesChanged = false;
        boolean eventTeamsChanged = false;
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
            for(Match match : matches) {
                List<String> teams = match.getAlliances().getRed().getTeams();
                teams.addAll(match.getAlliances().getBlue().getTeams());
                for(int position=0;position<teams.size();position++) {
                    int teamNum = Integer.parseInt(teams.get(position).substring(3));
                    Team teamObject = new Select().from(Team.class).where(Team.TEAM_NUMBER + "=?", teamNum).executeSingle();
                    if (null == teamObject) {
                        teamObject = new Team();
                        teamObject.setTeamNumber(teamNum);
                        teamObject.saveMod();
                        teamsChanged = true;
                    }
                    EventTeam eventTeam = new Select().from(EventTeam.class).where(EventTeam.TEAM + "=?", teamObject.getId()).and(EventTeam.EVENT + "=?", event.getId()).executeSingle();
                    if (null == eventTeam) {
                        List<EventTeam> ranks = new Select().from(EventTeam.class).where(EventTeam.EVENT + "=?", event.getId()).orderBy(EventTeam.RANK + " DESC").execute();
                        eventTeam = new EventTeam();
                        eventTeam.setEvent(event);
                        eventTeam.setTeam(teamObject);
                        eventTeam.setRank(ranks.get(0).getRank() + 1);
                        eventTeam.saveMod();
                        eventTeamsChanged = true;
                    }
                    switch (year) {
                        case 2014:
                            MatchScouting2014 matchScouting2014 = new MatchScouting2014();
                            matchScouting2014.setMatch(match);
                            matchScouting2014.setPosition(position);
                            TeamScouting2014 teamScouting2014 = new Select().from(TeamScouting2014.class).join(Team.class).on(TeamScouting2014.TAG + "." + TeamScouting2014.TEAM + "=" + Team.TAG + "." + Team.ID).where(Team.TAG + "." + Team.TEAM_NUMBER + "=?", teamNum).executeSingle();
                            if (null == teamScouting2014) {
                                teamScouting2014 = new TeamScouting2014();
                                teamScouting2014.setTeam(teamObject);
                                teamScouting2014.saveMod();
                                teamScoutingChanged = true;
                            }
                            matchScouting2014.setTeamScouting2014(teamScouting2014);
                            matchScouting2014.setAlliance(position > 2);
                            matchScouting2014.saveMod();
                            matchScoutingChanged = true;
                            break;
                        case 2015:
                            MatchScouting2015 matchScouting2015 = new MatchScouting2015();
                            matchScouting2015.setMatch(match);
                            matchScouting2015.setPosition(position);
                            TeamScouting2015 teamScouting2015 = new Select().from(TeamScouting2015.class).join(Team.class).on(TeamScouting2015.TAG + "." + TeamScouting2015.TEAM + "=" + Team.TAG + "." + Team.ID).where(Team.TAG + "." + Team.TEAM_NUMBER + "=?", teamNum).executeSingle();
                            if (null == teamScouting2015) {
                                teamScouting2015 = new TeamScouting2015();
                                teamScouting2015.setTeam(teamObject);
                                teamScouting2015.saveMod();
                                teamScoutingChanged = true;
                            }
                            matchScouting2015.setTeamScouting2015(teamScouting2015);
                            matchScouting2015.setAlliance(position > 2);
                            matchScouting2015.saveMod();
                            matchScoutingChanged = true;
                            break;
                    }
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            if(matchesChanged) {
                EventBus.getDefault().post(new Match());
            }
            if(teamsChanged) {
                EventBus.getDefault().post(new Team());
            }
            if(teamScoutingChanged) {
                switch (year) {
                    case 2014:
                        EventBus.getDefault().post(new TeamScouting2014());
                        break;
                    case 2015:
                        EventBus.getDefault().post(new TeamScouting2015());
                        break;
                }
            }
            if(matchScoutingChanged) {
                switch (year) {
                    case 2014:
                        EventBus.getDefault().post(new MatchScouting2014());
                        break;
                    case 2015:
                        EventBus.getDefault().post(new MatchScouting2015());
                        break;
                }
            }
            if(eventTeamsChanged) {
                EventBus.getDefault().post(new EventTeam());
            }
            ToastEvent.toast("Finished downloading matches", false);
            prefs.setMatchesDownloaded(true);
        } catch (RetrofitError e) {
            Timber.e(e,"Error downloading matches");
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
