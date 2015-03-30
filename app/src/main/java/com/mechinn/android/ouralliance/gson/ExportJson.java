package com.mechinn.android.ouralliance.gson;

import android.content.Context;
import android.net.Uri;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.event.ToastEvent;

import java.io.IOException;
import java.io.OutputStreamWriter;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import de.greenrobot.common.io.IoUtils;
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
    private BluetoothSPP bluetooth;
    public ExportJson(Context context, Uri uri) {
        this.context = context;
        this.prefs = new Prefs(context);
        this.uri = uri;
        json = "";
    }
    public ExportJson(Context context, BluetoothSPP bluetooth) {
        this.context = context;
        this.prefs = new Prefs(context);
        this.bluetooth = bluetooth;
        json = "";
    }
    public Prefs getPrefs() {
        return prefs;
    }
    public void addJson(String json) {
        this.json+=json;
    }
    public void addJsonHeader(String json) {
        this.json+=json;
    }
    public void run() throws IOException {
        Timber.d("json: " + json);
        if(null!=bluetooth) {
            bluetooth.send(json, true);
        } else {
            IoUtils.writeAllCharsAndClose(new OutputStreamWriter(context.getContentResolver().openOutputStream(uri)), json);
        }
        ToastEvent.toast("Backup complete");
    }
}
