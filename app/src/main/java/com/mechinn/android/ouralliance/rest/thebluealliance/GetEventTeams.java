package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.greenDao.Team;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
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
                        DaoSession dao = ((OurAlliance) GetEventTeams.this.getContext().getApplicationContext()).getDaoSession();
                        Event event = dao.getEventDao().load(getPrefs().getComp());
                        Log.d(TAG, "year: " + getPrefs().getYear());
                        Log.d(TAG, "Setting up teams");
                        try {
                            List<Team> teams = TheBlueAlliance.getService().getEventTeams(getPrefs().getYear() + event.getEventCode());
                            dao.getTeamDao().insertInTx(teams);
                            sendMessage("Finished downloading teams",false);
                            getPrefs().setEventTeamsDownloaded(true);
                        } catch (RetrofitError e) {
                            Log.e(TAG,"Error downloading event teams",e);
                            if(e.isNetworkError()) {
                                sendMessage("Unable to connect",false);
                            } else if(e.getResponse().getStatus()!=200) {
                                sendMessage("Error "+e.getResponse().getStatus()+" connecting",false);
                            }
                        }
                    }
                }
        );
    }

}
