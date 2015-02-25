package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.content.Context;
import android.util.Log;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.event.ToastEvent;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

import de.greenrobot.event.util.AsyncExecutor;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetEvents implements AsyncExecutor.RunnableEx {
    public static final String TAG = "GetEvents";
    private Context context;
    private Prefs prefs;

    public GetEvents(Context context) {
        this.context = context;
        this.prefs = new Prefs(context);
    }

    @Override
    public void run() throws Exception {
        if(this.prefs.getYear()>0) {
            Log.d(TAG, "year: " + this.prefs.getYear());
            try {
                int year = this.prefs.getYear();
                ToastEvent.toast("Setting up " + year + " events");
                List<Event> events = TheBlueAlliance.getService().getEventList(year);
                for (Event event : events) {
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
                }
                Transaction.save(Event.class,events);
                prefs.setEventsDownloaded(true);
            } catch (RetrofitError e) {
                Log.e(TAG,"Error downloading events",e);
                if (e.getKind() == RetrofitError.Kind.NETWORK) {
                    ToastEvent.toast("Unable to connect");
                } else if (e.getResponse().getStatus() != 200) {
                    ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting");
                }
            }
        } else {
            Log.i(TAG, "No season selected");
        }
    }
}
