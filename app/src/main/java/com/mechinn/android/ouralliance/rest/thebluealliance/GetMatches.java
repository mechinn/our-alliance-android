package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.Model;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;

import java.util.List;

public class GetMatches implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetMatches";
    private Context context;
    private Prefs prefs;

    public GetMatches(Context context) {
        this.context = context;
        this.prefs = new Prefs(context);
    }

    @Override
    public void run() throws Exception {
        ToastEvent.toast("Downloading matches...",true);
        Event event = Model.load(Event.class, prefs.getComp());
        Log.d(TAG, "year: " + prefs.getYear());
        Log.d(TAG, "getting matches "+prefs.getYear() + event.getEventCode());
        try {
            List<Match> matches = TheBlueAlliance.getService().getEventMatches(prefs.getYear() + event.getEventCode());
            Transaction.save(Match.class,matches);
            ToastEvent.toast("Finished downloading matches", false);
            prefs.setMatchesDownloaded(true);
        } catch (RetrofitError e) {
            Log.e(TAG,"Error downloading matches",e);
            if (e.getKind() == RetrofitError.Kind.NETWORK) {
                ToastEvent.toast("Unable to connect",false);
            } else if (e.getResponse().getStatus() != 200) {
                ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting",false);
            }
        }
    }
}
