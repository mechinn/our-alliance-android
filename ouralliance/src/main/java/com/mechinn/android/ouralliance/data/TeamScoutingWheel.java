package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table(TeamScoutingWheel.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class TeamScoutingWheel extends AOurAllianceData {
	public static final String TAG = "TeamScoutingWheel";
	private static final long serialVersionUID = -8710760990028670121L;
	public static final String SEASON = Season.TAG;
	public static final String TEAM = Team.TAG;
    public static final String TYPE = "wheelType";
    public static final String SIZE = "wheelSize";
    public static final String COUNT = "wheelCount";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,SEASON
            ,TEAM
            ,TYPE
            ,SIZE
            ,COUNT
    };

	public static final int FIELD_TYPE = 1;
	public static final int FIELD_SIZE = 3;
	public static final int FIELD_COUNT = 6;
	public static final int FIELD_DELETE = 7;

    @Column(SEASON)
    @UniqueCombo
    @ForeignKey(SEASON+"(_id)")
    @CascadeDelete
    @Check(SEASON+" > 0")
	private Season season;
    @Column(TEAM)
    @UniqueCombo
    @ForeignKey(TEAM+"(_id)")
    @CascadeDelete
    @Check(TEAM+" > 0")
	private Team team;
    @Column(TYPE)
    @UniqueCombo
	private String wheelType;
    @Column(SIZE)
	private double wheelSize;
    @Column(COUNT)
	private int wheelCount;
	public TeamScoutingWheel() {
		super();
        season = new Season();
        team = new Team();
	}
    public TeamScoutingWheel(long id) {
        super(id);
    }
	public TeamScoutingWheel(Season season, Team team, String type, float size, int count) {
        this.setSeason(season);
        this.setTeam(team);
        this.setWheelType(type);
        this.setWheelSize(size);
        this.setWheelCount(count);
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public String getWheelType() {
		if(null==wheelType) {
            return "";
        }
        return wheelType;
	}
    public void setWheelType(String type) {
        if(null==type) {
            this.wheelType = "";
        } else {
            this.wheelType = type;
        }
    }
    public void setWheelType(CharSequence type) {
        if(null==type) {
            setWheelType("");
        } else {
            setWheelType(type.toString());
        }
    }
	public double getWheelSize() {
		return wheelSize;
	}
	public void setWheelSize(double size) {
		this.wheelSize = size;
	}
	public int getWheelCount() {
		return wheelCount;
	}
	public void setWheelCount(int count) {
		this.wheelCount = count;
	}
	public String toString() {
		return getSeason()+" "+getTeam()+": "+ getWheelType()+" | "+ getWheelSize()+" | "+ getWheelCount();
	}
	public boolean equals(TeamScoutingWheel data) {
		return super.equals(data) &&
				getSeason().equals(data.getSeason()) &&
				getTeam().equals(data.getTeam()) &&
				getWheelType().equals(data.getWheelType()) &&
				getWheelSize()==data.getWheelSize() &&
				getWheelCount()==data.getWheelCount();
	}
	public int compareTo(TeamScoutingWheel another) {
		return this.getTeam().compareTo(another.getTeam());
	}

    public AOurAllianceData validate() {
        return Query.one(Match.class, "SELECT * FROM " + TAG + " WHERE " + SEASON + "=? AND " + TEAM + "=? AND "+TYPE+"='?' LIMIT 1", getSeason().getId(),getTeam().getId(),getWheelType()).get();
    }
    public boolean empty() {
        return (null==getSeason() || getSeason().empty())
                && (null==getTeam() || getTeam().empty())
                && (getWheelType()==null || getWheelType()=="")
                && getWheelSize()==0
                && getWheelCount()==0;
    }
}
