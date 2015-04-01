package com.mechinn.android.ouralliance.gson;

import android.content.Context;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventMatchScouting2015;
import com.mechinn.android.ouralliance.gson.frc2015.ImportJsonEventTeamScouting2015;

import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 4/1/15.
 */
public class BluetoothImportJson implements AsyncExecutor.RunnableEx {
    private Context context;
    private Prefs prefs;
    private String json;
    public BluetoothImportJson(Context context, String json) {
        this.context = context;
        this.prefs = new Prefs(context);
        this.json = json;
    }
    @Override
    public void run() throws Exception {
        switch(prefs.getYear()) {
            case 2015:
                JsonWrapper jsonTeams = OurAllianceGson.BUILDER.fromJson(json, TeamScouting2015Wrapper.class);
                if(jsonTeams.getSize()>0) {
                    new ImportJsonEventTeamScouting2015(context, jsonTeams).run();
                } else {
                    jsonTeams = OurAllianceGson.BUILDER.fromJson(json, MatchScouting2015Wrapper.class);
                    if(jsonTeams.getSize()>0) {
                        new ImportJsonEventMatchScouting2015(context, jsonTeams).run();
                    }
                }
        }
    }
}
