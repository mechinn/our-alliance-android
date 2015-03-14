package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetEventTeams implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetEventTeams";
    private Context context;
    private Prefs prefs;

    public GetEventTeams(Context context) {
        this.context = context;
        this.prefs = new Prefs(context);
    }

    @Override
    public void run() throws Exception {
        ToastEvent.toast("Downloading event teams...",true);
        Event event = Model.load(Event.class,prefs.getComp());
        Timber.d("year: " + prefs.getYear());
        Timber.d( "Setting up teams");
        ActiveAndroid.beginTransaction();
        try {
            int year = prefs.getYear();
            List<Team> teams = TheBlueAlliance.getService().getEventTeams(year + event.getEventCode());
            for(Team team : teams) {
                team.saveMod();
                switch(year) {
                    case 2014:
                        TeamScouting2014 scouting2014 = new TeamScouting2014();
                        scouting2014.setTeam(team);
                        scouting2014.saveMod();
                        break;
                    case 2015:
                        TeamScouting2015 scouting2015 = new TeamScouting2015();
                        scouting2015.setTeam(team);
                        scouting2015.saveMod();
                        break;
                }
            }
            Collections.sort(teams);
            List<EventTeam> eventTeams = new ArrayList<>();
            for(int teamRank=0;teamRank<teams.size();teamRank++) {
                EventTeam eventTeam = new EventTeam();
                eventTeam.setEvent(event);
                eventTeam.setTeam(teams.get(teamRank));
                eventTeam.setRank(teamRank);
                eventTeams.add(eventTeam);
                eventTeam.saveMod();
            }
            ActiveAndroid.setTransactionSuccessful();
            EventBus.getDefault().post(teams.get(0));
            EventBus.getDefault().post(eventTeams.get(0));
            ToastEvent.toast("Finished downloading teams",false);
            prefs.setEventTeamsDownloaded(true);
        } catch (RetrofitError e) {
            Timber.e(e,"Error downloading event teams");
            if (e.getKind() == RetrofitError.Kind.NETWORK) {
                ToastEvent.toast("Unable to connect");
            } else if (e.getResponse().getStatus() != 200) {
                ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting");
            }
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
