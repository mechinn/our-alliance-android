package com.mechinn.android.ouralliance.gson;

import android.content.Context;
import android.net.Uri;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.event.ToastEvent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.greenrobot.common.io.IoUtils;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 3/27/15.
 */
public abstract class ImportJson implements AsyncExecutor.RunnableEx {
    public final static String TRUE = "true";
    public final static String FALSE = "false";
    private Prefs prefs;
    private Context context;
    private Uri uri;
    private String json;
    public ImportJson(Context context, Uri uri) {
        this.context = context;
        this.prefs = new Prefs(context);
        this.uri = uri;
    }
    public Prefs getPrefs() {
        return prefs;
    }

    public String getJson() {
        return json;
    }

    public void run() throws IOException {
        ToastEvent.toast("Starting restore",false);
        final InputStream inputStream = context.getContentResolver().openInputStream(uri);
        json = new String(IoUtils.readAllBytesAndClose(inputStream));
    }
}
