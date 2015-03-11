package com.mechinn.android.ouralliance.gson;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Team;

import java.lang.reflect.Type;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class EventTeamAdapter implements JsonSerializer<EventTeam>, JsonDeserializer<EventTeam> {
    @Override
    public EventTeam deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        EventTeam eventTeam = new EventTeam();
        JsonObject object = json.getAsJsonObject();
        eventTeam.setModified(new Date(object.get(EventTeam.MODIFIED).getAsLong()));
        eventTeam.setEvent(OurAllianceGson.BUILDER.fromJson(object.get(EventTeam.EVENT), Event.class));
        eventTeam.setTeam(OurAllianceGson.BUILDER.fromJson(object.get(EventTeam.TEAM), Team.class));
        eventTeam.setRank(object.get(EventTeam.RANK).getAsInt());
        eventTeam.setScouted(object.get(EventTeam.SCOUTED).getAsBoolean());
        return eventTeam;
    }
    @Override
    public JsonElement serialize(EventTeam src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(EventTeam.MODIFIED,src.getModified().getTime());
        object.add(EventTeam.EVENT, OurAllianceGson.BUILDER.toJsonTree(src.getEvent()));
        object.add(EventTeam.TEAM, OurAllianceGson.BUILDER.toJsonTree(src.getTeam()));
        object.addProperty(EventTeam.RANK,src.getRank());
        object.addProperty(EventTeam.SCOUTED,src.isScouted());
        return object;
    }
}
