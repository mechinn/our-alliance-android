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
        team.setTeamNumber(object.get(Team.TEAM_NUMBER).getAsInt());
        team.setModified(new Date(object.get(Team.MODIFIED).getAsLong()));
        JsonElement element = object.get(Team.WEBSITE);
        if(null!=element) {
            team.setWebsite(element.getAsString());
        }
        element = object.get(Team.NAME);
        if(null!=element) {
            team.setName(element.getAsString());
        }
        element = object.get(Team.LOCALITY);
        if(null!=element) {
            team.setLocality(element.getAsString());
        }
        element = object.get(Team.REGION);
        if(null!=element) {
            team.setRegion(element.getAsString());
        }
        element = object.get(Team.COUNTRY);
        if(null!=element) {
            team.setCountry(element.getAsString());
        }
        element = object.get(Team.NICKNAME);
        if(null!=element) {
            team.setNickname(element.getAsString());
        }
        element = object.get(Team.ROOKIE_YEAR);
        if(null!=element) {
            team.setRookieYear(element.getAsInt());
        }
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
