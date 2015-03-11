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

import java.lang.reflect.Type;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class EventAdapter implements JsonSerializer<Event>, JsonDeserializer<Event> {
    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Event event = new Event();
        JsonObject object = json.getAsJsonObject();
        event.setModified(new Date(object.get(Event.MODIFIED).getAsLong()));
        event.setName(object.get(Event.NAME).getAsString());
        event.setShortName(object.get(Event.SHORT_NAME).getAsString());
        event.setEventCode(object.get(Event.EVENT_CODE).getAsString());
        event.setEventType(object.get(Event.EVENT_TYPE).getAsInt());
        event.setEventDistrict(object.get(Event.EVENT_DISTRICT).getAsInt());
        event.setYear(object.get(Event.YEAR).getAsInt());
        event.setVenueAddress(object.get(Event.VENUE_ADDRESS).getAsString());
        event.setWebsite(object.get(Event.WEBSITE).getAsString());
        event.setStartDate(new Date(object.get(Event.START_DATE).getAsLong()));
        event.setEndDate(new Date(object.get(Event.END_DATE).getAsLong()));
        event.setOfficial(object.get(Event.OFFICIAL).getAsBoolean());
        return event;
    }
    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(Event.MODIFIED,src.getModified().getTime());
        object.addProperty(Event.NAME,src.getName());
        object.addProperty(Event.SHORT_NAME,src.getShortName());
        object.addProperty(Event.EVENT_CODE,src.getEventCode());
        object.addProperty(Event.EVENT_TYPE,src.getEventType());
        object.addProperty(Event.EVENT_DISTRICT,src.getEventDistrict());
        object.addProperty(Event.YEAR,src.getYear());
        object.addProperty(Event.VENUE_ADDRESS,src.getVenueAddress());
        object.addProperty(Event.WEBSITE,src.getWebsite());
        object.addProperty(Event.START_DATE,src.getStartDate().getTime());
        object.addProperty(Event.END_DATE,src.getEndDate().getTime());
        object.addProperty(Event.OFFICIAL,src.isOfficial());
        return object;
    }
}
