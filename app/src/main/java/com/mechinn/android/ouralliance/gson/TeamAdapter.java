package com.mechinn.android.ouralliance.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.Team;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by mechinn on 3/10/15.
 */
public class TeamAdapter implements JsonSerializer<Team>, JsonDeserializer<Team> {
    @Override
    public Team deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Team team = new Team();
        JsonObject object = json.getAsJsonObject();
        team.setModified(new Date(object.get(Team.MODIFIED).getAsLong()));
        team.setWebsite(object.get(Team.WEBSITE).getAsString());
        team.setName(object.get(Team.NAME).getAsString());
        team.setLocality(object.get(Team.LOCALITY).getAsString());
        team.setRegion(object.get(Team.REGION).getAsString());
        team.setCountry(object.get(Team.COUNTRY).getAsString());
        team.setTeamNumber(object.get(Team.TEAM_NUMBER).getAsInt());
        team.setNickname(object.get(Team.NICKNAME).getAsString());
        team.setRookieYear(object.get(Team.ROOKIE_YEAR).getAsInt());
        return team;
    }

    @Override
    public JsonElement serialize(Team src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(Team.MODIFIED,src.getModified().getTime());
        object.addProperty(Team.WEBSITE,src.getWebsite());
        object.addProperty(Team.NAME,src.getName());
        object.addProperty(Team.LOCALITY,src.getLocality());
        object.addProperty(Team.REGION,src.getRegion());
        object.addProperty(Team.COUNTRY,src.getCountry());
        object.addProperty(Team.TEAM_NUMBER,src.getTeamNumber());
        object.addProperty(Team.NICKNAME,src.getNickname());
        object.addProperty(Team.ROOKIE_YEAR,src.getRookieYear());
        return object;
    }
}
