package com.mechinn.android.ouralliance.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Match;

import java.lang.reflect.Type;
import java.util.Date;

public class MatchAdapter implements JsonSerializer<Match>, JsonDeserializer<Match> {
    @Override
    public Match deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Match match = new Match();
        JsonObject object = json.getAsJsonObject();
        match.setModified(new Date(object.get(Match.MODIFIED).getAsLong()));
        match.setEvent(OurAllianceGson.BUILDER.fromJson(object.get(Match.EVENT), Event.class));
        match.setCompLevel(object.get(Match.COMPETITION_LEVEL).getAsString());
        match.setMatchNumber(object.get(Match.MATCH_NUMBER).getAsInt());
        match.setSetNumber(object.get(Match.SET_NUMBER).getAsInt());
        match.setTime(new Date(object.get(Match.TIME).getAsLong()));
        match.setRedScore(object.get(Match.RED_SCORE).getAsInt());
        match.setBlueScore(object.get(Match.BLUE_SCORE).getAsInt());
        return match;
    }
    @Override
    public JsonElement serialize(Match src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(Match.MODIFIED,src.getModified().getTime());
        object.add(Match.EVENT,OurAllianceGson.BUILDER.toJsonTree(src.getEvent()));
        object.addProperty(Match.COMPETITION_LEVEL, src.getCompLevel());
        object.addProperty(Match.MATCH_NUMBER,src.getMatchNumber());
        object.addProperty(Match.SET_NUMBER,src.getSetNumber());
        object.addProperty(Match.TIME,src.getTime().getTime());
        object.addProperty(Match.RED_SCORE,src.getRedScore());
        object.addProperty(Match.BLUE_SCORE,src.getBlueScore());
        return object;
    }
}
