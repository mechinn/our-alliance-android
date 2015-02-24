package com.mechinn.android.ouralliance.event;

/**
 * Created by mechinn on 2/23/15.
 */
public class SelectTeamEvent {
    private long id;
    public SelectTeamEvent(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
