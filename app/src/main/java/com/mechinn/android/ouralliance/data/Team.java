package com.mechinn.android.ouralliance.data;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

@Table(name = Team.TAG, id = Team.ID)
public class Team extends OurAllianceObject implements Comparable<Team>, java.io.Serializable {
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
        if(teamNumber!=this.teamNumber) {
            this.teamNumber = teamNumber;
            changedData();
        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if(null==name && null!=this.name || null!=name && !name.equals(this.name)) {
            this.name = name;
            changedData();
        }
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        if(null==nickname && null!=this.nickname || null!=nickname && !nickname.equals(this.nickname)) {
            this.nickname = nickname;
            changedData();
        }
    }
    public String getDisplayName() {
        if(null!=getNickname() && ""!=getNickname()) {
            return getNickname();
        } else if(null!=getName() && ""!=getName()) {
            return getName();
        } else {
            return "";
        }
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        if(null==website && null!=this.website || null!=website && !website.equals(this.website)) {
            this.website = website;
            changedData();
        }
    }
    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        if(null==locality && null!=this.locality || null!=locality && !locality.equals(this.locality)) {
            this.locality = locality;
            changedData();
        }
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        if(null==region && null!=this.region || null!=region && !region.equals(this.region)) {
            this.region = region;
            changedData();
        }
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        if(null==country && null!=this.country || null!=country && !country.equals(this.country)) {
            this.country = country;
            changedData();
        }
    }
    public Integer getRookieYear() {
        return rookieYear;
    }
    public void setRookieYear(Integer rookieYear) {
        if(null==rookieYear && null!=this.rookieYear || null!=rookieYear && !rookieYear.equals(this.rookieYear)) {
            this.rookieYear = rookieYear;
            changedData();
        }
    }
    public String toString() {
        return this.getTeamNumber()+": "+this.getDisplayName();
    }
    public int compareTo(Team another) {
        return this.getTeamNumber() - another.getTeamNumber();
    }
    public boolean copy(Team data) {
        if(this.equals(data)) {
            super.copy(data);
            this.setName(data.getName());
            this.setNickname(data.getNickname());
            this.setWebsite(data.getWebsite());
            this.setLocality(data.getLocality());
            this.setRegion(data.getRegion());
            this.setCountry(data.getCountry());
            this.setRookieYear(data.getRookieYear());
            return true;
        }
        return false;
    }
    public boolean equals(Object data) {
        if(!(data instanceof Team)) {
            return false;
        }
        try {
            return  getTeamNumber()==((Team) data).getTeamNumber();
        } catch (NullPointerException e) {
            return false;
        }
    }
    public static Team load(int teamNumber) {
        return new Select().from(Team.class).where(Team.TEAM_NUMBER+"=?",teamNumber).executeSingle();
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
