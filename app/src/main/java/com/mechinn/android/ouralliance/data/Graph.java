package com.mechinn.android.ouralliance.data;

import com.github.mikephil.charting.data.BarEntry;

/**
 * Created by mechinn on 3/23/15.
 */
public class Graph<O extends OurAllianceObject> {
    private Getter<O> getter;
    private String column;
    private boolean enabled;
    public Graph(String column, Getter getter) {
        this.column = column;
        this.getter = getter;
        enabled = true;
    }
    public final boolean equals(Graph graph) {
        return this.column.equals(graph.column);
    }
    public void switchEnabled() {
        enabled = !enabled;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public String getName() {
        return column;
    }
    public BarEntry barEntry(O scouting, int count) {
        return new BarEntry(getValue(scouting),count);
    }
    public float getValue(O scouting) {
        return getFloat(getter.getter(scouting));
    }
    private float getFloat(Object obj) {
        float value = 0;
        if(null!=obj) {
            if(obj instanceof Boolean) {
                value = ((Boolean)obj)?1:0;
            } else if(obj instanceof Integer) {
                value = ((Integer)obj).floatValue();
            } else if(obj instanceof Float) {
                value = ((Float)obj).floatValue();
            } else if(obj instanceof Double) {
                value = ((Double)obj).floatValue();
            }
        }
        return value;
    }

    public interface Getter<O extends OurAllianceObject> {
        public Object getter(O scouting);
    }
}