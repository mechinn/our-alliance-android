package com.mechinn.android.ouralliance;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by mechinn on 3/7/14.
 */
public class TheBlueAlliance extends IntentService {
    public static final String TAG = "TheBlueAlliance";

    public TheBlueAlliance() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //SystemClock.sleep(30000);
    }
}
