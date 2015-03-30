package com.mechinn.android.ouralliance.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.gson.frc2015.MatchScouting2015Adapter;
import com.mechinn.android.ouralliance.gson.frc2015.MatchScouting2015WrapperAdapter;
import com.mechinn.android.ouralliance.gson.frc2015.TeamScouting2015Adapter;
import com.mechinn.android.ouralliance.gson.frc2015.TeamScouting2015WrapperAdapter;
import com.mechinn.android.ouralliance.gson.frc2015.Wheel2015Adapter;
import com.mechinn.android.ouralliance.rest.JsonDateAdapter;

import java.util.Date;

/**
 * Created by mechinn on 3/10/15.
 */
public class OurAllianceGson {
    public static final String TYPE = "application/json";
    public static final Gson BUILDER = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new JsonDateAdapter())
            .registerTypeAdapter(Team.class, new TeamAdapter())
            .registerTypeAdapter(Event.class, new EventAdapter())
            .registerTypeAdapter(EventTeam.class, new EventTeamAdapter())
            .registerTypeAdapter(Match.class, new MatchAdapter())
            .registerTypeAdapter(TeamScouting2015.class, new TeamScouting2015Adapter())
            .registerTypeAdapter(TeamScouting2015Wrapper.class, new TeamScouting2015WrapperAdapter())
            .registerTypeAdapter(Wheel2015.class, new Wheel2015Adapter())
            .registerTypeAdapter(MatchScouting2015.class, new MatchScouting2015Adapter())
            .registerTypeAdapter(MatchScouting2015Wrapper.class, new MatchScouting2015WrapperAdapter())
            .create();
}
