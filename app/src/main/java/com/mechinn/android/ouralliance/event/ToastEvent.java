package com.mechinn.android.ouralliance.event;

import android.content.Context;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Created by mechinn on 2/23/15.
 */
public class ToastEvent {
    private String message;
    private boolean lengthLong;
    private ToastEvent(String message) {
        this.message = message;
        this.lengthLong = true;
    }
    private ToastEvent(String message, boolean lengthLong) {
        this.message = message;
        this.lengthLong = lengthLong;
    }
    public static void toast(String message) {
        EventBus.getDefault().post(new ToastEvent(message));
    }
    public static void toast(String message,boolean lengthLong) {
        EventBus.getDefault().post(new ToastEvent(message,lengthLong));
    }
    public String getMessage() {
        return message;
    }
    public boolean isLong() {
        return lengthLong;
    }
}
