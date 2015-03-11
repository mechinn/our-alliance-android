package com.mechinn.android.ouralliance.gson.frc2015;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by mechinn on 3/10/15.
 */
public class MatchScouting2015Adapter implements JsonSerializer<MatchScouting2015>, JsonDeserializer<MatchScouting2015> {
    @Override
    public MatchScouting2015 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MatchScouting2015 scouting = new MatchScouting2015();
        JsonObject object = json.getAsJsonObject();
        scouting.setModified(new Date(object.get(MatchScouting2015.MODIFIED).getAsLong()));
        scouting.setMatch(OurAllianceGson.BUILDER.fromJson(object.get(MatchScouting2015.MATCH), Match.class));
        scouting.setAlliance(object.get(MatchScouting2015.ALLIANCE).getAsBoolean());
        scouting.setPosition(object.get(MatchScouting2015.POSITION).getAsInt());
        scouting.setNotes(object.get(MatchScouting2015.NOTES).getAsString());
        scouting.setTeamScouting2015(OurAllianceGson.BUILDER.fromJson(object.get(MatchScouting2015.TEAM), TeamScouting2015.class));
        scouting.setAutoStacked(object.get(MatchScouting2015.AUTO_STACKED).getAsBoolean());
        scouting.setAutoTotes(object.get(MatchScouting2015.AUTO_TOTES).getAsInt());
        scouting.setAutoContainers(object.get(MatchScouting2015.AUTO_CONTAINERS).getAsInt());
        scouting.setAutoLandfill(object.get(MatchScouting2015.AUTO_LANDFILL).getAsInt());
        scouting.setAutoMove(object.get(MatchScouting2015.AUTO_MOVE).getAsFloat());
        scouting.setCoop(object.get(MatchScouting2015.COOP).getAsBoolean());
        scouting.setTotes(object.get(MatchScouting2015.TOTES).getAsInt());
        scouting.setContainers(object.get(MatchScouting2015.CONTAINERS).getAsInt());
        scouting.setLitter(object.get(MatchScouting2015.LITTER).getAsInt());
        scouting.setFowls(object.get(MatchScouting2015.FOWLS).getAsInt());
        scouting.setHumanAttempt(object.get(MatchScouting2015.HUMAN_ATTEMPT).getAsInt());
        scouting.setHumanSuccess(object.get(MatchScouting2015.HUMAN_SUCCESS).getAsInt());
        return scouting;
    }
    @Override
    public JsonElement serialize(MatchScouting2015 src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MatchScouting2015.MODIFIED,src.getModified().getTime());
        object.add(MatchScouting2015.MATCH, OurAllianceGson.BUILDER.toJsonTree(src.getMatch()));
        object.addProperty(MatchScouting2015.ALLIANCE, src.isAlliance());
        object.addProperty(MatchScouting2015.POSITION, src.getPosition());
        object.addProperty(MatchScouting2015.NOTES, src.getNotes());
        object.add(MatchScouting2015.TEAM, OurAllianceGson.BUILDER.toJsonTree(src.getTeamScouting2015()));
        object.addProperty(MatchScouting2015.AUTO_STACKED, src.getAutoStacked());
        object.addProperty(MatchScouting2015.AUTO_TOTES, src.getAutoTotes());
        object.addProperty(MatchScouting2015.AUTO_CONTAINERS, src.getAutoContainers());
        object.addProperty(MatchScouting2015.AUTO_LANDFILL, src.getAutoLandfill());
        object.addProperty(MatchScouting2015.AUTO_MOVE, src.getAutoMove());
        object.addProperty(MatchScouting2015.COOP, src.getCoop());
        object.addProperty(MatchScouting2015.TOTES, src.getTotes());
        object.addProperty(MatchScouting2015.CONTAINERS, src.getContainers());
        object.addProperty(MatchScouting2015.LITTER, src.getLitter());
        object.addProperty(MatchScouting2015.FOWLS, src.getFowls());
        object.addProperty(MatchScouting2015.HUMAN_ATTEMPT, src.getHumanAttempt());
        object.addProperty(MatchScouting2015.HUMAN_SUCCESS, src.getHumanSuccess());
        return object;
    }
}
