package com.mechinn.android.ouralliance.rest;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;
import com.mechinn.android.ouralliance.Prefs;

/**
 * Created by mechinn on 4/6/14.
 */
public class GetHandlerThread extends HandlerThread {
    public static final String TAG = "GetCompetitionTeams";
    public static final String PROGRESS = "progress";
    public static final String STATUS = "status";
    private Prefs prefs;
    private Handler threadHandler = null;
    private Handler uiHandler = null;
    private Context context;

    public GetHandlerThread(String tag, Context context) {
        super(tag);
        this.context = context;
        prefs = new Prefs(context);
        uiHandler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(GetHandlerThread.this.getContext() instanceof Activity) {
                            ((Activity) GetHandlerThread.this.getContext()).setProgressBarIndeterminateVisibility(msg.getData().getBoolean(PROGRESS));
                        }
                        if(null!=msg.getData().getString(STATUS)) {
                            Toast.makeText(GetHandlerThread.this.getContext(), msg.getData().getString(STATUS), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }
        );
        start();
        threadHandler = new Handler(getLooper());
    }

    public Prefs getPrefs() {
        return prefs;
    }
    public Handler getThreadHandler() {
        return threadHandler;
    }
    public Handler getUiHandler() {
        return uiHandler;
    }
    public void sendMessage(String string, boolean progress) {
        Message message = new Message();
        message.getData().putString(STATUS, string);
        message.getData().putBoolean(PROGRESS, progress);
        getUiHandler().sendMessage(message);
    }
    public Context getContext() {
        return context;
    }
}
