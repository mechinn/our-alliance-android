package com.mechinn.android.ouralliance.data;

import com.google.gson.JsonElement;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 3/29/15.
 */
public class JsonWrapper<Wrapped extends OurAllianceObject> {
    public final static String TYPE = "type";
    public final static String COUNT = "count";
    public final static String DATA = "data";
    private List<Wrapped> data;
    private String type;

    public JsonWrapper(String type) {
        this.type = type;
        this.data = new ArrayList<>();
    }

    public void add(Wrapped u){
        data.add(u);
    }

    public int getSize() { return data.size(); }

    public List<Wrapped> getData() {
        return data;
    }

    public void setData(List<Wrapped> data) {
        this.data = data;
    }

    public boolean isType(String type) {
        return this.type.equals(type);
    }

    public String getTypeName() {
        return type;
    }
}
