package com.mechinn.android.ouralliance.rest;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.data.Competition;
import se.emilsjolander.sprinkles.Transaction;

import java.util.List;

/**
 * Created by mechinn on 3/31/14.
 */
public class GetCompetitions extends BackgroundProgress {
    public static final String TAG = "GetCompetitions";
    public GetCompetitions(Activity activity) {
        super(activity, FLAG_COMPETITION_LIST);
        setTitle("Downloading");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(!getPrefs().getCompetitionsDownloaded()) {
            Transaction t = new Transaction();
            Log.d(TAG, "year: " + this.getPrefs().getYear());
            try {
                switch (this.getPrefs().getYear()) {
                    case 2014:
                        setStatus("Setting up 2014 competitions");
                        List<String> events = TheBlueAlliance.getService().getEventList(2014);
                        Competition competition;
                        this.setTotal(events.size());
                        for (String eventKey : events) {
                            if (this.isCancelled()) {
                                return false;
                            }
                            competition = TheBlueAlliance.getService().getEvent(eventKey);
                            Log.d(TAG, "name: " + competition.getName());
                            Log.d(TAG, "code: " + competition.getEventCode());
                            Log.d(TAG, "location: " + competition.getLocation());
                            Log.d(TAG, "season: " + competition.getYear());
                            Log.d(TAG, "official: " + competition.isOfficial());
                            competition.save(t);
                            this.increasePrimary();
                        }
                        break;
                }
                t.setSuccessful(true);
            } finally {
                t.finish();
                getPrefs().setCompetitionsDownloaded(true);
            }
        }
        return true;
    }
}
