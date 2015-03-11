package com.mechinn.android.ouralliance.gson.frc2015;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.lang.reflect.Type;
import java.util.Date;

public class Wheel2015Adapter implements JsonSerializer<Wheel2015>, JsonDeserializer<Wheel2015> {

    @Override
    public Wheel2015 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Wheel2015 wheel = new Wheel2015();
        JsonObject object = json.getAsJsonObject();
        wheel.setModified(new Date(object.get(Wheel2015.MODIFIED).getAsLong()));
        wheel.setTeamScouting2015(OurAllianceGson.BUILDER.fromJson(object.get(Wheel2015.TEAM_SCOUTING), TeamScouting2015.class));
        wheel.setWheelType(object.get(Wheel2015.WHEEL_TYPE).getAsString());
        wheel.setWheelSize(object.get(Wheel2015.WHEEL_SIZE).getAsDouble());
        wheel.setWheelCount(object.get(Wheel2015.WHEEL_COUNT).getAsInt());
        return wheel;
    }

    @Override
    public JsonElement serialize(Wheel2015 src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(Wheel2015.MODIFIED,src.getModified().getTime());
        object.add(Wheel2015.TEAM_SCOUTING, OurAllianceGson.BUILDER.toJsonTree(src.getTeamScouting2015()));
        object.addProperty(Wheel2015.WHEEL_TYPE, src.getWheelType());
        object.addProperty(Wheel2015.WHEEL_SIZE,src.getWheelSize());
        object.addProperty(Wheel2015.WHEEL_COUNT,src.getWheelCount());
        return object;
    }
}
