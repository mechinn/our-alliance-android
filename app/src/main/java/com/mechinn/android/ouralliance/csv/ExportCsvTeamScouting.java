package com.mechinn.android.ouralliance.csv;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportCsvTeamScouting extends ExportCsv {
    public ExportCsvTeamScouting(Context context) {
        super(context, TeamScouting.TAG);
    }
    public void run() throws IOException {
        Event event = Model.load(Event.class,getPrefs().getComp());
        setFileName(event.getEventCode());
        setTitle("Send team scouting csv for: "+event.getDisplayName());
        super.run();
    }
}
