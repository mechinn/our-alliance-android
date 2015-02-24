package com.mechinn.android.ouralliance.event;

/**
 * Created by mechinn on 2/23/15.
 */
public class SelectMatchEvent {
    private long id;
    public SelectMatchEvent(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
