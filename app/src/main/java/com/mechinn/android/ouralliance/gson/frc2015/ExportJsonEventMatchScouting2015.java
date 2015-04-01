package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.gson.ExportJson;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportJsonEventMatchScouting2015 extends ExportJson {
    public ExportJsonEventMatchScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public ExportJsonEventMatchScouting2015(Context context, BluetoothSPP bluetooth) {
        super(context, bluetooth);
    }
    public void run() throws IOException, InterruptedException {
        ToastEvent.toast("Starting backup", false);
        List<MatchScouting2015> teams = new Select().from(MatchScouting2015.class).join(Match.class).on(MatchScouting2015.TAG+"."+MatchScouting2015.MATCH+"="+Match.TAG+"."+Match.ID).where(Match.TAG+"."+Match.EVENT+"=?",getPrefs().getComp()).execute();
        MatchScouting2015Wrapper wrapper = new MatchScouting2015Wrapper();
        wrapper.setData(teams);
        addJson(OurAllianceGson.BUILDER.toJson(wrapper));
        super.run();
    }
}
