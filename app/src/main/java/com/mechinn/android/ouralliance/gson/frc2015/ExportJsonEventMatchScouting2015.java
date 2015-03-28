package com.mechinn.android.ouralliance.gson.frc2015;

import android.content.Context;
import android.net.Uri;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.gson.ExportJson;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportJsonEventMatchScouting2015 extends ExportJson {
    public ExportJsonEventMatchScouting2015(Context context, Uri uri) {
        super(context, uri);
    }
    public void run() throws IOException {
        ToastEvent.toast("Starting backup", false);
        List<MatchScouting2015> teams = new Select().from(MatchScouting2015.class).join(Match.class).on(MatchScouting2015.TAG+"."+MatchScouting2015.MATCH+"="+Match.TAG+"."+Match.ID).where(Match.TAG+"."+Match.EVENT+"=?",getPrefs().getComp()).execute();
        addJson(OurAllianceGson.BUILDER.toJson(teams));
        super.run();
    }
}
