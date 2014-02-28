package com.mechinn.android.ouralliance.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@JsonIgnoreProperties({"id","mod"})
@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Team extends AOurAllianceData implements Comparable<Team> {
	public static final String TAG = Team.class.getSimpleName();
	private static final long serialVersionUID = 6981108401294045422L;
	public static final String NUMBER = "number";
	public static final String NAME = "name";

    @Column
    @UniqueCombo()
    @Check("teamNumber > 0")
	private int teamNumber;
    @Column
    @NotNull
	private CharSequence name;

	public Team() {
		super();
	}
    public Team(long id) {
        super(id);
    }
	public Team(int number) {
		this.setNumber(number);
	}
	public Team(int number, CharSequence name) {
        this.setNumber(number);
        this.setName(name);
	}
	public int getNumber() {
		return teamNumber;
	}
	public void setNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	public CharSequence getName() {
		if(null==name) {
			return "";
		}
		return name;
	}
	public void setName(CharSequence name) {
		this.name = name;
	}
	public String toString() {
		return this.getNumber()+": "+this.getName();
	}
	public int compareTo(Team another) {
		return this.getNumber() - another.getNumber();
	}
	public boolean equals(Team data) {
		return super.equals(data) &&
				getNumber()==data.getNumber() &&
				getName().equals(getName());
	}
}
