package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class TeamScoutingWheel extends AOurAllianceData {
	public static final String TAG = TeamScoutingWheel.class.getSimpleName();
	private static final long serialVersionUID = -8710760990028670121L;
	public static final String SEASON = Season.TAG;
	public static final String TEAM = Team.TAG;
    public static final String TYPE = "type";
    public static final String SIZE = "size";
    public static final String COUNT = "count";

	public static final int FIELD_TYPE = 1;
	public static final int FIELD_SIZE = 3;
	public static final int FIELD_COUNT = 6;
	public static final int FIELD_DELETE = 7;

    @Column
    @UniqueCombo
    @ForeignKey("Season(_id)")
    @CascadeDelete
    @Check("season > 0")
	private Season season;
    @Column
    @UniqueCombo
    @ForeignKey("Team(_id)")
    @CascadeDelete
    @Check("team > 0")
	private Team team;
    @Column
    @UniqueCombo
    @NotNull
	private CharSequence wheelType;
    @Column
	private float wheelSize;
    @Column
	private int wheelCount;
	public TeamScoutingWheel() {
		super();
	}
    public TeamScoutingWheel(long id) {
        super(id);
    }
	public TeamScoutingWheel(Season season, Team team, CharSequence type, float size, int count) {
        this.setSeason(season);
        this.setTeam(team);
        this.setType(type);
        this.setSize(size);
        this.setCount(count);
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
	public CharSequence getType() {
		return wheelType;
	}
	public void setType(CharSequence type) {
		this.wheelType = type;
	}
	public float getSize() {
		return wheelSize;
	}
	public void setSize(float size) {
		this.wheelSize = size;
	}
	public int getCount() {
		return wheelCount;
	}
	public void setCount(int count) {
		this.wheelCount = count;
	}
	public String toString() {
		return getSeason()+" "+getTeam()+": "+getType()+" | "+getSize()+" | "+getCount();
	}
	public boolean equals(TeamScoutingWheel data) {
		return super.equals(data) &&
				getSeason().equals(data.getSeason()) &&
				getTeam().equals(data.getTeam()) &&
				getType().equals(data.getType()) &&
				getSize()==data.getSize() &&
				getCount()==data.getCount();
	}
	public int compareTo(TeamScoutingWheel another) {
		return this.getTeam().compareTo(another.getTeam());
	}
}
