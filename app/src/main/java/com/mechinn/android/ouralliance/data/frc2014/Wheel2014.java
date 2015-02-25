package com.mechinn.android.ouralliance.data.frc2014;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.Wheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name=Wheel2014.TAG, id = Wheel2014.ID)
public class Wheel2014 extends Wheel<TeamScouting2014> {
    public final static String TAG = "Wheel2014";
    public Wheel2014() {}
    public Wheel2014(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Wheel2014.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Wheel2014.this);
            }
        });
    }
}
