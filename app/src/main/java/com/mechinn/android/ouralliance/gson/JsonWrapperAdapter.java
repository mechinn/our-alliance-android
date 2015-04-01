package com.mechinn.android.ouralliance.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public abstract class JsonWrapperAdapter<Wrapped extends OurAllianceObject> implements JsonSerializer<JsonWrapper<Wrapped>>, JsonDeserializer<JsonWrapper<Wrapped>> {
    public abstract JsonWrapper<Wrapped> newAdapter();
    public abstract Wrapped elementToObject(JsonElement element);
    @Override
    public JsonWrapper<Wrapped> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonWrapper<Wrapped> wrapper = newAdapter();
        JsonObject object = json.getAsJsonObject();
        if(wrapper.isType(object.get(JsonWrapper.TYPE).getAsString())) {
            JsonArray dataList = object.getAsJsonArray(JsonWrapper.DATA);
            if(dataList.size() == object.get(JsonWrapper.COUNT).getAsInt()) {
                for(int i=0;i<dataList.size();i++) {
                    Wrapped newWrapped = elementToObject(dataList.get(i));
                    Timber.d("imported: "+newWrapped);
                    wrapper.add(newWrapped);
                }
            }
        }
        return wrapper;
    }
    @Override
    public JsonElement serialize(JsonWrapper<Wrapped> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(JsonWrapper.TYPE,src.getTypeName());
        object.addProperty(JsonWrapper.COUNT, src.getData().size());
        object.add(JsonWrapper.DATA, OurAllianceGson.BUILDER.toJsonTree(src.getData()));
        return object;
    }
}
