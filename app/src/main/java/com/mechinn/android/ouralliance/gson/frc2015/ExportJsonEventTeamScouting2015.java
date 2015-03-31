package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.gson.ExportJson;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.io.IOException;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportJsonEventTeamScouting2015 extends ExportJson {
    public ExportJsonEventTeamScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public ExportJsonEventTeamScouting2015(Context context, BluetoothSPP bluetooth) {
        super(context, bluetooth);
    }

    public void run() throws IOException {
        ToastEvent.toast("Starting backup", false);
        List<TeamScouting2015> teams = new Select().from(TeamScouting2015.class).join(EventTeam.class).on(TeamScouting2015.TAG+"."+TeamScouting2015.TEAM+"="+EventTeam.TAG+"."+EventTeam.TEAM).where(EventTeam.TAG+"."+EventTeam.EVENT+"=?",getPrefs().getComp()).execute();
        TeamScouting2015Wrapper wrapper = new TeamScouting2015Wrapper();
        wrapper.setData(teams);
        addJson(OurAllianceGson.BUILDER.toJson(wrapper));
        super.run();
    }
}
