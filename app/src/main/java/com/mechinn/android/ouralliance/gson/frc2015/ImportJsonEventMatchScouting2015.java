package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.google.gson.reflect.TypeToken;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.gson.ImportJson;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by mechinn on 3/28/15.
 */
public class ImportJsonEventMatchScouting2015 extends ImportJson {

    public ImportJsonEventMatchScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public ImportJsonEventMatchScouting2015(Context context, JsonWrapper jsonWrapper) {
        super(context,jsonWrapper);
    }
    public void run() throws IOException {
        super.run();
        MatchScouting2015Wrapper jsonTeams = (MatchScouting2015Wrapper) getJsonWrapper();
        if(null==getJsonWrapper()) {
            jsonTeams = OurAllianceGson.BUILDER.fromJson(getJson(), MatchScouting2015Wrapper.class);
        }
        List<MatchScouting2015> dbTeams = new Select().from(MatchScouting2015.class).execute();
        ActiveAndroid.beginTransaction();
        try {
            for(MatchScouting2015 jsonTeam : jsonTeams.getData()) {
                boolean found = false;
                if(null!=dbTeams) {
                    for (MatchScouting2015 dbTeam : dbTeams) {
//                        Timber.d("checking if "+jsonTeam+" equal to "+dbTeam);
                        if (jsonTeam.getTeamScouting2015().equals(dbTeam.getTeamScouting2015()) && jsonTeam.getMatch().equals(dbTeam.getMatch())) {
                            Timber.d(jsonTeam.getModified()+" after "+dbTeam.getModified());
                            if (jsonTeam.getModified().after(dbTeam.getModified())) {
                                dbTeam.copy(jsonTeam);
                                Timber.d("saving "+dbTeam);
                                dbTeam.saveMod();
                            } else {
//                                Timber.d("ignore "+jsonTeam);
                            }
                            dbTeams.remove(dbTeam);
                            found = true;
                            break;
                        }
                    }
                }
                if(!found) {
                    jsonTeam.saveMod();
                    EventTeam newEventTeam = new EventTeam();
                    newEventTeam.setEvent(jsonTeam.getMatch().getEvent());
                    newEventTeam.setTeam(jsonTeam.getTeamScouting2015().getTeam());
                    newEventTeam.saveMod();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            ToastEvent.toast("Restore complete");
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
