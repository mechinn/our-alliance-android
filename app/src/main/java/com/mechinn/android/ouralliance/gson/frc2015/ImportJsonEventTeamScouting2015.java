package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.google.gson.reflect.TypeToken;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
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
public class ImportJsonEventTeamScouting2015 extends ImportJson {

    public ImportJsonEventTeamScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public void run() throws IOException {
        super.run();
        Type listType = new TypeToken<ArrayList<TeamScouting2015>>() {}.getType();
        List<TeamScouting2015> jsonTeams = OurAllianceGson.BUILDER.fromJson(getJson(), listType);
        List<TeamScouting2015> dbTeams = new Select().from(TeamScouting2015.class).execute();
        for(TeamScouting2015 jsonTeam : jsonTeams) {
            boolean found = false;
            if(null!=dbTeams) {
                for (TeamScouting2015 dbTeam : dbTeams) {
                    Timber.d("checking if "+jsonTeam+" equal to "+dbTeam);
                    if (jsonTeam.getTeam().equals(dbTeam.getTeam())) {
                        Timber.d(jsonTeam.getModified()+" after "+dbTeam.getModified());
                        if (jsonTeam.getModified().after(dbTeam.getModified())) {
                            dbTeam.copy(jsonTeam);
                            Timber.d("saving " + dbTeam);
                            dbTeam.saveMod();
                        } else {
                            Timber.d("ignore "+jsonTeam);
                        }
                        dbTeams.remove(dbTeam);
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                jsonTeam.saveMod();
            }
        }
        ToastEvent.toast("Restore complete");
    }
}
