package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.app.Activity;
import android.util.Log;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetEvents extends BackgroundProgress {
    public static final String TAG = "GetEvents";
    public GetEvents(Activity activity) {
        super(activity, FLAG_COMPETITION_LIST);
        setTitle("Downloading");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(this.getPrefs().getYear()>0) {
            Log.d(TAG, "year: " + this.getPrefs().getYear());
            try {
                int year = this.getPrefs().getYear();
                setStatus("Setting up "+year+" events");
                List<Event> events = TheBlueAlliance.getService().getEventList(year);
                for (Event event : events) {
                    if (this.isCancelled()) {
                        return false;
                    }
                    Log.d(TAG, "name: " + event.getShortName());
                    Log.d(TAG, "code: " + event.getEventCode());
                    Log.d(TAG, "location: " + event.getVenueAddress());
                    Log.d(TAG, "season: " + event.getYear());
                    Log.d(TAG, "official: " + event.getOfficial());
                    this.increasePrimary();
                }
                ((OurAlliance)this.getActivity().getApplication()).getDaoSession().getEventDao().insertInTx(events);
                getPrefs().setEventsDownloaded(true);
            } catch (RetrofitError e) {
                Log.e(TAG,"Error downloading events",e);
                if(e.isNetworkError()) {
                    setStatus("Unable to connect");
                    return false;
                } else if(e.getResponse().getStatus()!=200) {
                    setStatus("Error "+e.getResponse().getStatus()+" connecting");
                    return false;
                }
            }
        }
        setStatus("No season selected");
        return true;
    }
}
