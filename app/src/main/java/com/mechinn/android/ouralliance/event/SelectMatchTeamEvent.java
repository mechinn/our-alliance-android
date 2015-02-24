package com.mechinn.android.ouralliance.event;

/**
 * Created by mechinn on 2/23/15.
 */
public class SelectMatchTeamEvent {
    private long id;
    public SelectMatchTeamEvent(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
