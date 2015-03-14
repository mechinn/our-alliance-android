package com.mechinn.android.ouralliance.csv;

import android.content.Context;

import com.activeandroid.Model;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.io.IOException;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportCsvMatchScouting extends ExportCsv {
    public ExportCsvMatchScouting(Context context) {
        super(context, MatchScouting.TAG);
    }
    public void run() throws IOException {
        Event event = Model.load(Event.class, getPrefs().getComp());
        setFileName(event.getEventCode());
        setTitle("Send match scouting csv for: "+event.getDisplayName());
        super.run();
    }
}
