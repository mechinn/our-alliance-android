package com.mechinn.android.ouralliance.event;

import com.activeandroid.ActiveAndroid;
import com.mechinn.android.ouralliance.data.OurAllianceObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 2/22/15.
 */
public class Transaction implements AsyncExecutor.RunnableEx {
    private Class type;
    private List<? extends OurAllianceObject> objects;
    private boolean delete;
    private Transaction(Class type, List<? extends OurAllianceObject> objects, boolean delete) {
        this.type = type;
        this.objects = objects;
        this.delete = delete;
    }
    public static void save(Class type, List<? extends OurAllianceObject> objects) {
        new Transaction(type,objects,false).run();
    }
    public static void delete(Class type, List<? extends OurAllianceObject> objects) {
        new Transaction(type,objects,true).run();
    }
    public static void asyncSave(Class type, List<? extends OurAllianceObject> objects) {
        AsyncExecutor.create().execute(new Transaction(type,objects,false));
    }
    public static void asyncDelete(Class type, List<? extends OurAllianceObject> objects) {
        AsyncExecutor.create().execute(new Transaction(type,objects,true));
    }

    @Override
    public void run() {
        ActiveAndroid.beginTransaction();
        try {
            for(OurAllianceObject object : objects) {
                if(delete) {
                    object.delete();
                } else {
                    object.saveMod();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            EventBus.getDefault().post(type.cast(objects.get(0)));
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
