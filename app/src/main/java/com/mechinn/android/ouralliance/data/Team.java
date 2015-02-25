package com.mechinn.android.ouralliance.data;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = Team.TAG, id = Team.ID)
public class Team extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Team>, java.io.Serializable {
    public final static String TAG = "Team";
    public final static String WEBSITE = "website";
    public final static String NAME = "name";
    public final static String LOCALITY = "locality";
    public final static String REGION = "region";
    public final static String COUNTRY = "country";
    public final static String TEAM_NUMBER = "teamNumber";
    public final static String NICKNAME = "nickname";
    public final static String ROOKIE_YEAR = "rookieYear";
    @Column(name=WEBSITE)
    private String website;
    @Column(name=NAME)
    private String name;
    @Column(name=LOCALITY)
    private String locality;
    @Column(name=REGION)
    private String region;
    @Column(name=COUNTRY)
    private String country;
    @Column(name=TEAM_NUMBER, notNull = true, onNullConflict = Column.ConflictAction.FAIL, unique = true, onUniqueConflict = Column.ConflictAction.FAIL)
    private int teamNumber;
    @Column(name=NICKNAME)
    private String nickname;
    @Column(name=ROOKIE_YEAR)
    private Integer rookieYear;
    public Team() {}
    public Team(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public int getTeamNumber() {
        return teamNumber;
    }
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public int getRookieYear() {
        return rookieYear;
    }
    public void setRookieYear(int rookieYear) {
        this.rookieYear = rookieYear;
    }
    public String toString() {
        return this.getTeamNumber()+": "+this.getNickname();
    }
    public int compareTo(Team another) {
        return this.getTeamNumber() - another.getTeamNumber();
    }
    public boolean equals(Object data) {
        if(!(data instanceof Team)) {
            return false;
        }
        return  getTeamNumber()==((Team) data).getTeamNumber()
                && getNickname().equals(((Team)data).getNickname());
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(Team.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(Team.this);
            }
        });
    }
}
