package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.activeandroid.Model;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetEventTeams extends GetHandlerThread {
    public static final String TAG = "GetEventTeams";

    public GetEventTeams(Context context) {
        super(TAG,context);
    }

    public void refreshEventTeams() {
        getThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        sendMessage("Downloading event teams...",true);
                        Event event = Model.load(Event.class,getPrefs().getComp());
                        Log.d(TAG, "year: " + getPrefs().getYear());
                        Log.d(TAG, "Setting up teams");
                        try {
                            List<Team> teams = TheBlueAlliance.getService().getEventTeams(getPrefs().getYear() + event.getEventCode());
                            Transaction.post(teams);
                            sendMessage("Finished downloading teams",false);
                            getPrefs().setEventTeamsDownloaded(true);
                        } catch (RetrofitError e) {
                            Log.e(TAG,"Error downloading event teams",e);
                            try {
                                if (e.getKind() == RetrofitError.Kind.NETWORK) {
                                    sendMessage("Unable to connect",false);
                                } else if (e.getResponse().getStatus() != 200) {
                                    sendMessage("Error " + e.getResponse().getStatus() + " connecting",false);
                                }
                            } catch (Exception ex) {
                                sendMessage(e.getMessage(),false);
                            }
                        }
                    }
                }
        );
    }

}
