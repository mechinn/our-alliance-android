package com.mechinn.android.ouralliance.gson;

import android.content.Context;
import android.net.Uri;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.event.ToastEvent;

import java.io.IOException;
import java.io.OutputStreamWriter;

import de.greenrobot.common.io.IoUtils;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/14/15.
 */
public abstract class ExportJson implements AsyncExecutor.RunnableEx {
    public final static String TRUE = "true";
    public final static String FALSE = "false";
    private Prefs prefs;
    private Context context;
    private Uri uri;
    private String json;
    public ExportJson(Context context, Uri uri) {
        this.context = context;
        this.prefs = new Prefs(context);
        this.uri = uri;
        json = "";
    }
    public Prefs getPrefs() {
        return prefs;
    }
    public void addJson(String json) {
        this.json+=json;
    }
    public void run() throws IOException {
        Timber.d("json: " + json);
        IoUtils.writeAllCharsAndClose(new OutputStreamWriter(context.getContentResolver().openOutputStream(uri)),json);
        ToastEvent.toast("Backup complete");
    }
}
