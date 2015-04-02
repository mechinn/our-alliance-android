package com.mechinn.android.ouralliance.event;

import android.content.Intent;

/**
 * Created by mechinn on 3/27/15.
 */
public class ActivityResult {
    int requestCode;
    int resultCode;
    Intent data;
    public ActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
    public boolean isRequestCode(int code) {
        return code==requestCode;
    }
    public int getRequestCode() {
        return requestCode;
    }
    public boolean isResultCode(int code) {
        return code==resultCode;
    }
    public int getResultCode() {
        return resultCode;
    }
    public Intent getData() {
        return data;
    }
}
