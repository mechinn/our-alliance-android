package com.mechinn.android.ouralliance.data;

/**
 * Created by mechinn on 2/7/2015.
 */
public abstract class OurAllianceObject {
    public abstract Long getId();
    public abstract void setId(Long id);
    public abstract java.util.Date getModified();
    public abstract void setModified(java.util.Date modified);
    public abstract void delete();
    public abstract void update();
    public abstract void refresh();
    public void save() {
        setModified(new java.util.Date());
        update();
    }
}
