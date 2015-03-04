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
import timber.log.Timber;

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
            Timber.d("year: " + this.prefs.getYear());
            try {
                int year = this.prefs.getYear();
                ToastEvent.toast("Setting up " + year + " events");
                List<Event> events = TheBlueAlliance.getService().getEventList(year);
                for (Event event : events) {
                    Timber.d( "name: " + event.getName());
                    Timber.d( "shortName: " + event.getShortName());
                    Timber.d( "eventCode: " + event.getEventCode());
                    Timber.d( "eventType: " + event.getEventType());
                    Timber.d( "eventDistrict: " + event.getEventDistrict());
                    Timber.d( "season: " + event.getYear());
                    Timber.d( "venueAddress: " + event.getVenueAddress());
                    Timber.d( "website: " + event.getWebsite());
                    Timber.d( "startDate: " + event.getStartDate());
                    Timber.d( "endDate: " + event.getEndDate());
                    Timber.d( "official: " + event.isOfficial());
                }
                Transaction.save(Event.class, events);
                prefs.setEventsDownloaded(true);
            } catch (RetrofitError e) {
                Timber.e("Error downloading events",e);
                if (e.getKind() == RetrofitError.Kind.NETWORK) {
                    ToastEvent.toast("Unable to connect");
                } else if (e.getResponse().getStatus() != 200) {
                    ToastEvent.toast("Error " + e.getResponse().getStatus() + " connecting");
                }
            }
        } else {
            Timber.i("No season selected");
        }
    }
}
