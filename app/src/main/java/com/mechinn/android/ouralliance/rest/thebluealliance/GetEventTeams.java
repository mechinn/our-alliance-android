package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.Model;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;

import java.util.List;

public class GetEventTeams implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetEventTeams";
    private Context context;
    private Prefs prefs;

    public GetEventTeams(Context context) {
        this.context = context;
    }

    @Override
    public void run() throws Exception {
        ToastEvent.toast("Downloading event teams...",true);
        Event event = Model.load(Event.class,prefs.getComp());
        Log.d(TAG, "year: " + prefs.getYear());
        Log.d(TAG, "Setting up teams");
        try {
            List<Team> teams = TheBlueAlliance.getService().getEventTeams(prefs.getYear() + event.getEventCode());
            Transaction.save(teams);
            ToastEvent.toast("Finished downloading teams",false);
            prefs.setEventTeamsDownloaded(true);
        } catch (RetrofitError e) {
            Log.e(TAG,"Error downloading event teams",e);
            if (e.getKind() == RetrofitError.Kind.NETWORK) {
                ToastEvent.toast("Unable to connect");
            } else if (e.getResponse().getStatus() != 200) {
                ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting");
            }
        }
    }
}
