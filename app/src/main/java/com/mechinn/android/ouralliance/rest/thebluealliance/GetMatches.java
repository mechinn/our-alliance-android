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
        try {
            int year = prefs.getYear();
            List<Match> matches = TheBlueAlliance.getService().getEventMatches(year + event.getEventCode());
            for(Match match : matches) {
                match.setRedScore(match.getAlliances().getRed().getScore());
                match.setBlueScore(match.getAlliances().getBlue().getScore());
                match.setEvent(event);
                List<String> teams = match.getAlliances().getRed().getTeams();
                teams.addAll(match.getAlliances().getBlue().getTeams());
                List<EventTeam> ranks = new Select().from(EventTeam.class).where(EventTeam.EVENT + "=?", event.getId()).orderBy(EventTeam.RANK + " DESC").execute();
                int nextRank = 0;
                if(!ranks.isEmpty()) {
                    nextRank = ranks.get(0).getRank()+1;
                }
                for(int position=0;position<teams.size();position++) {
                    int teamNum = Integer.parseInt(teams.get(position).substring(3));
                    Team teamObject = new Team();
                    teamObject.setTeamNumber(teamNum);
                    EventTeam eventTeam = new EventTeam();
                    eventTeam.setEvent(event);
                    eventTeam.setTeam(teamObject);
                    eventTeam.setRank(nextRank);
                    eventTeam.saveMod();
                    if(-1!=eventTeam.getId()) {
                        nextRank++;
                    }
                    switch (year) {
                        case 2014:
                            TeamScouting2014 teamScouting2014 = new TeamScouting2014();
                            teamScouting2014.setTeam(teamObject);

                            MatchScouting2014 matchScouting2014 = new MatchScouting2014();
                            matchScouting2014.setMatch(match);
                            matchScouting2014.setPosition(position);
                            matchScouting2014.setTeamScouting2014(teamScouting2014);
                            matchScouting2014.setAlliance(position > 2);
                            matchScouting2014.saveMod();
                            break;
                        case 2015:
                            TeamScouting2015 teamScouting2015 = new TeamScouting2015();
                            teamScouting2015.setTeam(teamObject);

                            MatchScouting2015 matchScouting2015 = new MatchScouting2015();
                            matchScouting2015.setMatch(match);
                            matchScouting2015.setPosition(position);
                            matchScouting2015.setTeamScouting2015(teamScouting2015);
                            matchScouting2015.setAlliance(position > 2);
                            matchScouting2015.saveMod();
                            break;
                    }
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            EventBus.getDefault().post(new Match());
            EventBus.getDefault().post(new Team());
            EventBus.getDefault().post(new EventTeam());
            switch (year) {
                case 2014:
                    EventBus.getDefault().post(new MatchScouting2014());
                    EventBus.getDefault().post(new TeamScouting2014());
                    break;
                case 2015:
                    EventBus.getDefault().post(new MatchScouting2015());
                    EventBus.getDefault().post(new TeamScouting2015());
                    break;
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
