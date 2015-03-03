package com.mechinn.android.ouralliance.data;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
        this.name = name;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
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
        this.eventCode = eventCode;
    }
    public int getEventType() {
        return eventType;
    }
    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
    public int getEventDistrict() {
        return eventDistrict;
    }
    public void setEventDistrict(int eventDistrict) {
        this.eventDistrict = eventDistrict;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getVenueAddress() {
        return venueAddress;
    }
    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public boolean isOfficial() {
        return official;
    }
    public void setOfficial(boolean official) {
        this.official = official;
    }
    public String toString() {
        return (isOfficial()?"Official":"Unofficial")+" | "+this.getShortName();
    }
    public boolean equals(Event data) {
        return  getYear()==data.getYear() &&
                getShortName().equals(data.getShortName()) &&
                getEventCode().equals(data.getEventCode());
    }
    public int compareTo(Event another) {
        int compare = (this.isOfficial()?1:0)-(another.isOfficial()?1:0);
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
