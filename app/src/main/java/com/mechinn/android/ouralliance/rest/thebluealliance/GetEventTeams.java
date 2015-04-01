package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;


import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;
import timber.log.Timber;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GetEventTeams implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetEventTeams";
    private Prefs prefs;

    public GetEventTeams(Context context) {
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
            List<EventTeam> ranks = new Select().from(EventTeam.class).where(EventTeam.EVENT + "=?", event.getId()).orderBy(EventTeam.RANK + " DESC").execute();
            int nextRank = 0;
            if(!ranks.isEmpty()) {
                nextRank = ranks.get(0).getRank()+1;
            }
            Collections.sort(teams);
            for(int teamRank=0;teamRank<teams.size();teamRank++) {
                EventTeam eventTeam = new EventTeam();
                eventTeam.setEvent(event);
                eventTeam.setTeam(teams.get(teamRank));
                eventTeam.setRank(nextRank);
                eventTeam.saveMod();
                if(-1!=eventTeam.getId()) {
                    nextRank++;
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            if(!teams.isEmpty()) {
                EventBus.getDefault().post(new Team());
                EventBus.getDefault().post(new EventTeam());
            }
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
