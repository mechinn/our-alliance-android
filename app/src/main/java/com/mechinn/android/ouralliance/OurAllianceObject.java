package com.mechinn.android.ouralliance;

/**
 * Created by mechinn on 2/7/2015.
 */
public abstract class OurAllianceObject {
    public abstract void update();
    public abstract void setModified(java.util.Date modified);
    public void save() {
        setModified(new java.util.Date());
        update();
    }
}
