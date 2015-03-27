package com.mechinn.android.ouralliance.data;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by mechinn on 3/23/15.
 */
public class GraphDataSet extends LineDataSet {
    private boolean enabled;
    private Type type;

    public GraphDataSet(ArrayList<Entry> yVals, String label, Type type) {
        super(yVals, label);
        this.type = type;
        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void switchEnabled() {
        enabled = !enabled;
    }
    public enum Type {
        TEAM,MATCH
    }
    public Type getType() {
        return type;
    }
}