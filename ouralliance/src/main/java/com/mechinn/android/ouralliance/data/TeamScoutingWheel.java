package com.mechinn.android.ouralliance.data;

import android.util.Log;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

@Table(TeamScoutingWheel.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class TeamScoutingWheel extends AOurAllianceData {
	public static final String TAG = "TeamScoutingWheel";
	private static final long serialVersionUID = -8710760990028670121L;
	public static final String YEAR = "year";
	public static final String TEAM = Team.TAG;
    public static final String TYPE = "wheelType";
    public static final String SIZE = "wheelSize";
    public static final String COUNT = "wheelCount";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,YEAR
            ,TEAM
            ,TYPE
            ,SIZE
            ,COUNT
    };

	public static final int FIELD_TYPE = 1;
	public static final int FIELD_SIZE = 3;
	public static final int FIELD_COUNT = 6;
	public static final int FIELD_DELETE = 7;

    @Column(YEAR)
    @UniqueCombo
    @Check(YEAR+" > 0")
	private int year;
    @Column(TEAM)
    @UniqueCombo
    @ForeignKey(TEAM+"(_id)")
    @CascadeDelete
    @Check(TEAM+" > 0")
	private Team team;
    @Column(TYPE)
    @UniqueCombo
    @NotNull
    @Check(TYPE+" != ''")
	private String wheelType;
    @Column(SIZE)
	private double wheelSize;
    @Column(COUNT)
	private int wheelCount;
	public TeamScoutingWheel() {
		super();
        team = new Team();
	}
    public TeamScoutingWheel(long id) {
        super(id);
    }
	public TeamScoutingWheel(int season, Team team, String type, float size, int count) {
        this.setYear(season);
        this.setTeam(team);
        this.setWheelType(type);
        this.setWheelSize(size);
        this.setWheelCount(count);
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
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
		return getYear()+" "+getTeam()+": "+ getWheelType()+" | "+ getWheelSize()+" | "+ getWheelCount();
	}
	public boolean equals(Object data) {
        if(!(data instanceof TeamScoutingWheel)) {
            return false;
        }
		return  getYear()==((TeamScoutingWheel)data).getYear() &&
				getTeam().equals(((TeamScoutingWheel)data).getTeam()) &&
				getWheelType().equals(((TeamScoutingWheel)data).getWheelType()) &&
				getWheelSize()==((TeamScoutingWheel)data).getWheelSize() &&
				getWheelCount()==((TeamScoutingWheel)data).getWheelCount();
	}
	public int compareTo(TeamScoutingWheel another) {
		return this.getTeam().compareTo(another.getTeam());
	}

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        TeamScoutingWheel item = Query.one(TeamScoutingWheel.class, "SELECT * FROM " + TAG + " WHERE " + YEAR + "=? AND " + TEAM + "=? AND "+TYPE+"=? LIMIT 1", getYear(),getTeam().getId(),getWheelType()).get();
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
        return getWheelSize()==0
                && getWheelCount()==0;
    }
}
