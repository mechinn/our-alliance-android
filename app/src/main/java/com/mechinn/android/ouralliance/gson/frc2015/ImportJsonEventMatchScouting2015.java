package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.google.gson.reflect.TypeToken;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Match;
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
public class ImportJsonEventMatchScouting2015 extends ImportJson {

    public ImportJsonEventMatchScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public void run() throws IOException {
        super.run();
        Type listType = new TypeToken<ArrayList<MatchScouting2015>>() {}.getType();
        List<MatchScouting2015> jsonTeams = OurAllianceGson.BUILDER.fromJson(getJson(), listType);
        List<MatchScouting2015> dbTeams = new Select().from(MatchScouting2015.class).execute();
        for(MatchScouting2015 jsonTeam : jsonTeams) {
            boolean found = false;
            if(null!=dbTeams) {
                for (MatchScouting2015 dbTeam : dbTeams) {
                    Timber.d("checking if "+jsonTeam+" equal to "+dbTeam);
                    if (jsonTeam.getTeamScouting2015().equals(dbTeam.getTeamScouting2015()) && jsonTeam.getMatch().equals(dbTeam.getMatch())) {
                        Timber.d(jsonTeam.getModified()+" after "+dbTeam.getModified());
                        if (jsonTeam.getModified().after(dbTeam.getModified())) {
                            dbTeam.copy(jsonTeam);
                            Timber.d("saving "+dbTeam);
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
                TeamScouting2015 findTeam2015 = new Select().from(TeamScouting2015.class).join(Team.class).on(TeamScouting2015.TAG + "." + TeamScouting2015.TEAM + "=" + Team.TAG + "." + Team.ID)
                        .where(Team.TAG + "." + Team.TEAM_NUMBER + "=?", jsonTeam.getTeamScouting2015().getTeam().getTeamNumber()).executeSingle();
                if(null==findTeam2015) {
                    Team findTeam = new Select().from(Team.class).where(Team.TEAM_NUMBER + "=?", jsonTeam.getTeamScouting2015().getTeam().getTeamNumber()).executeSingle();
                    if(null==findTeam) {
                        Timber.d("saving "+jsonTeam.getTeamScouting2015().getTeam());
                        jsonTeam.getTeamScouting2015().getTeam().saveMod();
                    } else {
                        Timber.d("loaded "+findTeam);
                        jsonTeam.getTeamScouting2015().setTeam(findTeam);
                    }
                    Timber.d("saving " + jsonTeam.getTeamScouting2015());
                    jsonTeam.getTeamScouting2015().saveMod();
                } else {
                    Timber.d("loaded "+findTeam2015);
                    jsonTeam.setTeamScouting2015(findTeam2015);
                }
                Match findMatch = new Select().from(Match.class).join(Event.class).on(Match.TAG + "." + Match.EVENT + "=" + Event.TAG + "." + Event.ID)
                        .where(Event.TAG + "." + Event.EVENT_CODE + "=?", jsonTeam.getMatch().getEvent().getEventCode())
                        .and(Event.TAG + "." + Event.YEAR + "=?", jsonTeam.getMatch().getEvent().getYear())
                        .and(Match.TAG + "." + Match.COMPETITION_LEVEL + "=?", jsonTeam.getMatch().getCompLevel())
                        .and(Match.TAG + "." + Match.SET_NUMBER + "=?", jsonTeam.getMatch().getSetNumber())
                        .and(Match.TAG+"."+Match.MATCH_NUMBER + "=?", jsonTeam.getMatch().getMatchNumber())
                        .executeSingle();
                if(null==findMatch) {
                    Event findEvent = new Select().from(Event.class)
                            .where(Event.EVENT_CODE + "=?", jsonTeam.getMatch().getEvent().getEventCode())
                            .and(Event.YEAR + "=?", jsonTeam.getMatch().getEvent().getYear())
                            .executeSingle();
                    if(null==findEvent) {
                        Timber.d("saving "+jsonTeam.getTeamScouting2015().getTeam());
                        jsonTeam.getMatch().getEvent().saveMod();
                    } else {
                        Timber.d("loaded "+findEvent);
                        jsonTeam.getMatch().setEvent(findEvent);
                    }
                    Timber.d("saving "+jsonTeam.getTeamScouting2015());
                    jsonTeam.getMatch().saveMod();
                } else {
                    Timber.d("loaded "+findMatch);
                    jsonTeam.setMatch(findMatch);
                }
                Timber.d("saving "+jsonTeam);
                jsonTeam.saveMod();
                EventTeam findEventTeam = new Select().from(EventTeam.class)
                        .join(Event.class).on(EventTeam.TAG + "." + EventTeam.EVENT + "=" + Event.TAG + "." + Event.ID)
                        .join(Event.class).on(EventTeam.TAG + "." + EventTeam.TEAM + "=" + Team.TAG + "." + Team.ID)
                        .where(Event.TAG + "." + Event.EVENT_CODE + "=?", jsonTeam.getMatch().getEvent().getEventCode())
                        .and(Event.TAG + "." + Event.YEAR + "=?", jsonTeam.getMatch().getEvent().getYear())
                        .and(Team.TAG + "." + Team.TEAM_NUMBER + "=?", jsonTeam.getTeamScouting2015().getTeam().getTeamNumber())
                        .executeSingle();
                if(null==findEventTeam) {
                    findEventTeam = new EventTeam();
                    findEventTeam.setEvent(jsonTeam.getMatch().getEvent());
                    findEventTeam.setTeam(jsonTeam.getTeamScouting2015().getTeam());
                    findEventTeam.saveMod();
                } else {
                    Timber.d("loaded " + findEventTeam);
                }
            }
        }
        ToastEvent.toast("Restore complete");
    }
}
