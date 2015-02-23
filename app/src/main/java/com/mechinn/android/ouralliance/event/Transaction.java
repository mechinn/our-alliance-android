package com.mechinn.android.ouralliance.event;

import com.activeandroid.ActiveAndroid;
import com.mechinn.android.ouralliance.data.OurAllianceObject;

import java.util.List;

import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 2/22/15.
 */
public class Transaction implements AsyncExecutor.RunnableEx {
    private List<? extends OurAllianceObject> objects;
    private boolean delete;
    private Transaction(List<? extends OurAllianceObject> objects, boolean delete) {
        this.objects = objects;
        this.delete = delete;
    }
    public static void save(List<? extends OurAllianceObject> objects) {
        AsyncExecutor.create().execute(new Transaction(objects,false));
    }
    public static void delete(List<? extends OurAllianceObject> objects) {
        AsyncExecutor.create().execute(new Transaction(objects,true));
    }

    @Override
    public void run() throws Exception {
        ActiveAndroid.beginTransaction();
        try {
            for(OurAllianceObject object : objects) {
                if(delete) {
                    object.delete();
                } else {
                    object.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
