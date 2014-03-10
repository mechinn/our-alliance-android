package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

@Table(Competition.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Competition extends AOurAllianceData implements Comparable<Competition> {
    public static final String TAG = "Competition";
	private static final long serialVersionUID = -5179493838272851750L;
	public static final String SEASON = Season.TAG;
    public static final String NAME = "name";
    public static final String CODE = "code";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,SEASON+"."+Season.YEAR
            ,NAME
            ,CODE
    };

    @Column(SEASON)
    @UniqueCombo
    @ForeignKey(SEASON+"("+_ID+")")
    @CascadeDelete
    @Check(SEASON+" > 0")
	private Season season;
    @Column(NAME)
    @NotNull
    @Check(NAME+" != ''")
	private String name;
    @Column(CODE)
    @UniqueCombo
    @NotNull
    @Check(CODE+" != ''")
	private String code;

	public Competition() {
		super();
	}
    public Competition(long id) {
        super(id);
    }
	public Competition(Season season, String name, String code) {
        this.setSeason(season);
        this.setName(name);
        this.setCode(code);
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public void setName(CharSequence name) {
        if(null==name) {
            setName("");
        } else {
            setName(name.toString());
        }
    }
	public String getCode() {
		return code;
	}
    public void setCode(String code) {
        this.code = code;
    }
    public void setCode(CharSequence code) {
        if(null==code) {
            setCode("");
        } else {
            setCode(code.toString());
        }
    }
	public String toString() {
		return ""+this.name;
	}
	public boolean equals(Competition data) {
		return super.equals(data) && 
				getSeason()==data.getSeason() &&
				getName().equals(data.getName()) && 
				getCode().equals(data.getCode());
	}
	public int compareTo(Competition another) {
		return this.getName().toString().compareTo(another.getName().toString());
	}

    public AOurAllianceData validate() {
        return Query.one(Competition.class, "SELECT * FROM " + TAG + " WHERE "+SEASON+"=? AND " + CODE + "=? LIMIT 1", getSeason(), getCode()).get();
    }
}