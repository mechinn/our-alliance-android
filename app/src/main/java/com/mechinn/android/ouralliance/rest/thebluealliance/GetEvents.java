package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetEvents extends BackgroundProgress {
    public static final String TAG = "GetEvents";
    public GetEvents(FragmentActivity activity) {
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
                    Log.d(TAG, "name: " + event.getName());
                    Log.d(TAG, "shortName: " + event.getShortName());
                    Log.d(TAG, "eventCode: " + event.getEventCode());
                    Log.d(TAG, "eventType: " + event.getEventType());
                    Log.d(TAG, "eventDistrict: " + event.getEventDistrict());
                    Log.d(TAG, "season: " + event.getYear());
                    Log.d(TAG, "venueAddress: " + event.getVenueAddress());
                    Log.d(TAG, "website: " + event.getWebsite());
                    Log.d(TAG, "startDate: " + event.getStartDate());
                    Log.d(TAG, "endDate: " + event.getEndDate());
                    Log.d(TAG, "official: " + event.isOfficial());
                    this.increasePrimary();
                }
                EventBus.getDefault().post(new Transaction(events));
                getPrefs().setEventsDownloaded(true);
            } catch (RetrofitError e) {
                Log.e(TAG,"Error downloading events",e);
                try {
                    if (e.isNetworkError()) {
                        setStatus("Unable to connect");
                        return false;
                    } else if (e.getResponse().getStatus() != 200) {
                        setStatus("Error " + e.getResponse().getStatus() + " connecting");
                        return false;
                    }
                } catch (Exception ex) {
                    setStatus(e.getMessage());
                }
            }
        }
        setStatus("No season selected");
        return true;
    }
}
