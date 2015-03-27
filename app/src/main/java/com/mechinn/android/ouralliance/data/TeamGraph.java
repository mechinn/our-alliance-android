package com.mechinn.android.ouralliance.data;

/**
 * Created by mechinn on 3/27/15.
 */
public class TeamGraph {
    public boolean enabled;
    public String label;
    public TeamGraph(String label) {
        this.label = label;
        enabled = true;
    }

    public void switchEnabled() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getLabel() {
        return label;
    }
}
