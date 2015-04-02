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
        event.setEventCode(object.get(Event.EVENT_CODE).getAsString());
        event.setEventType(object.get(Event.EVENT_TYPE).getAsInt());
        event.setEventDistrict(object.get(Event.EVENT_DISTRICT).getAsInt());
        event.setYear(object.get(Event.YEAR).getAsInt());
        event.setOfficial(object.get(Event.OFFICIAL).getAsBoolean());
        JsonElement element = object.get(Event.NAME);
        if(null!=element) {
            event.setName(element.getAsString());
        }
        element = object.get(Event.SHORT_NAME);
        if(null!=element) {
            event.setShortName(element.getAsString());
        }
        element = object.get(Event.VENUE_ADDRESS);
        if(null!=element) {
            event.setVenueAddress(element.getAsString());
        }
        element = object.get(Event.WEBSITE);
        if(null!=element) {
            event.setWebsite(element.getAsString());
        }
        element = object.get(Event.START_DATE);
        if(null!=element) {
            event.setStartDate(new Date(element.getAsLong()));
        }
        element = object.get(Event.END_DATE);
        if(null!=element) {
            event.setEndDate(new Date(element.getAsLong()));
        }
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
