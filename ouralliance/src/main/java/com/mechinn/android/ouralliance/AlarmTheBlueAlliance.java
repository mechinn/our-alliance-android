package com.mechinn.android.ouralliance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;

/**
 * Created by mechinn on 3/7/14.
 */
public class AlarmTheBlueAlliance extends BroadcastReceiver {
    public static final String TAG = "AlarmTheBlueAlliance";
    // Millisec * Second * Minute
    private static final int TIME = 1000 * 60 * 10;
    private Context context;
    private Intent intent;
    private PendingIntent pi;
    private AlarmManager am;
    public void  AlarmTheBlueAlliance(Context context){
        this.context = context;
        intent = new Intent(context, AlarmTheBlueAlliance.class);
        pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            Intent msgIntent = new Intent(context, TheBlueAlliance.class);
            context.startService(msgIntent);
        } else {
            //dont do anything, we arent online
        }
    }
    public void setAlarm() {
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), TIME, pi);
    }

    public void cancelAlarm() {
        am.cancel(pi);
    }
}
