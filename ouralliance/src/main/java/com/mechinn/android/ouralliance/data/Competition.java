package com.mechinn.android.ouralliance.data;

import android.util.Log;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

@Table(Competition.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Competition extends AOurAllianceData implements Comparable<Competition> {
    public static final String TAG = "Competition";
	private static final long serialVersionUID = -5179493838272851750L;
	public static final String YEAR = "year";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String EVENT_CODE = "event_code";
    public static final String LOCATION = "location";
    public static final String OFFICIAL = "official";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,YEAR
            ,NAME
            ,EVENT_CODE
    };

    @Column(YEAR)
    @UniqueCombo
    @Check(YEAR+" > 0")
	private int year;
    @Column(NAME)
    @NotNull
    @Check(NAME+" != ''")
	private String name;
    @Column(EVENT_CODE)
    @UniqueCombo
    @NotNull
    @Check(EVENT_CODE+" != ''")
	private String event_code;
    @Column(CODE)
    private String code;
    @Column(LOCATION)
    private String location;
    @Column(OFFICIAL)
    private boolean official;

	public Competition() {
		super();
	}
    public Competition(long id) {
        super(id);
    }
    public Competition(int season, String name, String code) {
        this.setYear(season);
        this.setName(name);
        this.setEventCode(code);
    }
    public Competition(int season, String name, String code, String location) {
        this.setYear(season);
        this.setName(name);
        this.setEventCode(code);
        this.setLocation(location);
    }
	public Competition(int season, String name, String code, String location, boolean official) {
        this.setYear(season);
        this.setName(name);
        this.setEventCode(code);
        this.setLocation(location);
        this.setOfficial(official);
	}
	public int getYear() {
		return year;
	}
	public void setYear(int season) {
		this.year = year;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
        if(null==name) {
            this.name = "";
        } else {
            this.name = name;
        }
	}
    public void setName(CharSequence name) {
        if(null==name) {
            setName("");
        } else {
            setName(name.toString());
        }
    }
	public String getEventCode() {
		return event_code;
	}
    public void setEventCode(String code) {
        if(null==code) {
            this.event_code = "";
        } else {
            this.event_code = code;
        }
    }
    public void setEventCode(CharSequence code) {
        if(null==code) {
            setEventCode("");
        } else {
            setEventCode(code.toString());
        }
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        if(null==location) {
            this.location = "";
        } else {
            this.location = location;
        }
    }
    public void setLocation(CharSequence location) {
        if(null==location) {
            setLocation("");
        } else {
            setLocation(location.toString());
        }
    }
    public boolean isOfficial() {
        return official;
    }
    public void setOfficial(boolean official) {
        this.official = official;
    }
	public String toString() {
		return (isOfficial()?"Official":"Unofficial")+" | "+this.name;
	}
	public boolean equals(Competition data) {
		return super.equals(data) &&
                getYear()==data.getYear() &&
				getName().equals(data.getName()) && 
				getEventCode().equals(data.getEventCode());
	}
	public int compareTo(Competition another) {
		return this.getName().toString().compareTo(another.getName().toString());
	}

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        Competition item = Query.one(Competition.class, "SELECT * FROM " + TAG + " WHERE "+YEAR+"=? AND " + EVENT_CODE + "=? LIMIT 1", getYear(), getEventCode()).get();
        if(null!=item) {
            this.setId(item.getId());
            Log.d(TAG, "item: "+item+" is empty: "+item.empty()+" is equal: "+this.equals(item));
            Log.d(TAG, "import mod: " + item.getModified()+" sql mod: "+this.getModified()+" after: "+this.getModified().before(item.getModified()));
            if((this.getModified().before(item.getModified()) && !item.empty()) || this.equals(item)) {
                return false;
            }
        }
        return true;
    }
    public boolean empty() {
        return 0==getYear()
                && (getName()==null || getName()=="")
                && (getEventCode()==null || getEventCode()=="");
    }
}