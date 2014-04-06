package com.mechinn.android.ouralliance.rest.thebluealliance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.mechinn.android.ouralliance.OurAllianceException;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Transaction;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/7/14.
 */
public class AlarmTheBlueAlliance extends BroadcastReceiver {
    public static final String TAG = "AlarmTheBlueAlliance";
    // Millisec * Second * Minute
//    private static final int TIME = 1000 * 60 * 10;
    private static final int TIME = 1000 * 60 * 2;
    private Context context;
    private Prefs prefs;
    private Intent intent;
    private PendingIntent pi;
    private AlarmManager am;
    private GetMatches getMatches;
    public AlarmTheBlueAlliance(Context context){
        this.context = context;
        prefs = new Prefs(context);
        intent = new Intent(context, AlarmTheBlueAlliance.class);
        pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        getMatches = new GetMatches(context);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        getMatches = new GetMatches(context);
        getMatches.refreshMatches();
    }
    public void setAlarm() {
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), TIME, pi);
    }

    public void cancelAlarm() {
        am.cancel(pi);
    }
}
