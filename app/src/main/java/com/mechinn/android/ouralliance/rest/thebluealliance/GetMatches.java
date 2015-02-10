package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.greenDao.Match;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import com.mechinn.android.ouralliance.rest.GetHandlerThread;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetMatches extends GetHandlerThread {
    public static final String TAG = "GetMatches";

    public GetMatches(Activity activity) {
        super(TAG,activity);
    }

    public void refreshMatches() {
        getThreadHandler().post(
            new Runnable() {
                @Override
                public void run() {
                    sendMessage("Downloading matches...",true);
                    DaoSession dao = ((OurAlliance) GetMatches.this.getActivity().getApplication()).getDaoSession();
                    Event event = dao.getEventDao().load(getPrefs().getComp());
                    Log.d(TAG, "year: " + getPrefs().getYear());
                    Log.d(TAG, "getting matches "+getPrefs().getYear() + event.getEventCode());
                    try {
                        List<Match> matches = TheBlueAlliance.getService().getEventMatches(getPrefs().getYear() + event.getEventCode());
                        dao.getMatchDao().insertInTx(matches);
                        sendMessage("Finished downloading matches",false);
                        getPrefs().setMatchesDownloaded(true);
                    } catch (RetrofitError e) {
                        Log.e(TAG,"Error downloading matches",e);
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
