package com.mechinn.android.ouralliance.data;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = Event.TAG, id = Event.ID)
public class Event extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Event>, java.io.Serializable {
    public final static String TAG = "Event";
    public final static String NAME = "name";
    public final static String SHORT_NAME = "shortName";
    public final static String EVENT_CODE = "eventCode";
    public final static String EVENT_TYPE = "eventType";
    public final static String EVENT_DISTRICT = "eventDistrict";
    public final static String YEAR = "year";
    public final static String VENUE_ADDRESS = "venueAddress";
    public final static String WEBSITE = "website";
    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";
    public final static String OFFICIAL = "official";
    @Column(name=NAME)
    private String name;
    @Column(name=SHORT_NAME)
    private String shortName;
    @Column(name=EVENT_CODE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private String eventCode;
    @Column(name=EVENT_TYPE, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private int eventType;
    @Column(name=EVENT_DISTRICT, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private int eventDistrict;
    @Column(name=YEAR, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int year;
    @Column(name=VENUE_ADDRESS)
    private String venueAddress;
    @Column(name=WEBSITE)
    private String website;
    @Column(name=START_DATE)
    private Date startDate;
    @Column(name=END_DATE)
    private Date endDate;
    @Column(name=OFFICIAL, notNull = true, onNullConflict = Column.ConflictAction.FAIL)
    private boolean official;
    public Event() {}
    public Event(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if(!name.equals(this.name)) {
            this.name = name;
            changedData();
        }
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        if(!shortName.equals(this.shortName)) {
            this.shortName = shortName;
            changedData();
        }
    }
    public String getDisplayName() {
        if(null!=getShortName()) {
            return getShortName();
        } else if(null!=getName()) {
            return getName();
        } else {
            return getEventCode();
        }
    }
    public String getEventCode() {
        return eventCode;
    }
    public void setEventCode(String eventCode) {
        if(!eventCode.equals(this.eventCode)) {
            this.eventCode = eventCode;
            changedData();
        }
    }
    public int getEventType() {
        return eventType;
    }
    public void setEventType(int eventType) {
        if(eventType!=this.eventType) {
            this.eventType = eventType;
            changedData();
        }
    }
    public int getEventDistrict() {
        return eventDistrict;
    }
    public void setEventDistrict(int eventDistrict) {
        if(eventDistrict!=this.eventDistrict) {
            this.eventDistrict = eventDistrict;
            changedData();
        }
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        if(year!=this.year) {
            this.year = year;
            changedData();
        }
    }
    public String getVenueAddress() {
        return venueAddress;
    }
    public void setVenueAddress(String venueAddress) {
        if(!venueAddress.equals(this.venueAddress)) {
            this.venueAddress = venueAddress;
            changedData();
        }
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        if(!website.equals(this.website)) {
            this.website = website;
            changedData();
        }
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        if(!startDate.equals(this.startDate)) {
            this.startDate = startDate;
            changedData();
        }
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        if(!endDate.equals(this.endDate)) {
            this.endDate = endDate;
            changedData();
        }
    }
    public boolean isOfficial() {
        return official;
    }
    public void setOfficial(boolean official) {
        if(official!=this.official) {
            this.official = official;
            changedData();
        }
    }
    public String toString() {
        return (isOfficial()?"Official":"Unofficial")+" | "+this.getShortName();
    }
    public static Event load(String eventCode, int year) {
        return new Select().from(Event.class).where(Event.EVENT_CODE+"=?",eventCode).and(Event.YEAR+"=?",year).executeSingle();
    }
    public boolean copy(Event data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setName(data.getName());
            this.setShortName(data.getShortName());
            this.setEventCode(data.getEventCode());
            this.setEventDistrict(data.getEventDistrict());
            this.setVenueAddress(data.getVenueAddress());
            this.setWebsite(data.getWebsite());
            this.setStartDate(data.getStartDate());
            this.setEndDate(data.getEndDate());
            this.setOfficial(data.isOfficial());
            return true;
        }
        return false;
    }
    public boolean equals(Event data) {
        try {
            return getYear() == data.getYear() &&
                    getEventCode().equals(data.getEventCode());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public int compareTo(Event another) {
        int compare = (this.isOfficial()?0:1)-(another.isOfficial()?0:1);
        if(0==compare) {
            compare = this.getDisplayName().compareTo(another.getDisplayName());
        }
        return compare;
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Event.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Event.this);
            }
        });
    }
}
