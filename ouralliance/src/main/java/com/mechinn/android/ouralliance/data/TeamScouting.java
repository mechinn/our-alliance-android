package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.*;

public abstract class TeamScouting extends AOurAllianceData implements Comparable<TeamScouting>  {
	public static final String TAG = "TeamScouting";
	private static final long serialVersionUID = 2234995463512680398L;
	public static final String TEAM = Team.TAG;
    public static final String NOTES = "notes";

    @Column(TEAM)
    @Unique(ConflictClause.IGNORE)
    @ForeignKey(TEAM+"(_id)")
    @CascadeDelete
    @Check(TEAM+" > 0")
	private Team team;
    @Column(NOTES)
	private String notes;

	public TeamScouting() {
		super();
	}
    public TeamScouting(long id) {
        super(id);
    }
	public TeamScouting(Team team) {
        this.setTeam(team);
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public String getNotes() {
		if(null==notes) {
			return "";
		}
		return notes;
	}
    public void setNotes(String notes) {
        if(null==notes) {
            this.notes = "";
        } else {
            this.notes = notes;
        }
    }
	public void setNotes(CharSequence notes) {
        if(null==notes) {
            setNotes("");
        } else {
            setNotes(notes.toString());
        }
	}
	public boolean equals(Object data) {
        if(!(data instanceof TeamScouting)) {
            return false;
        }
		return  getTeam().equals(((TeamScouting)data).getTeam()) &&
				getNotes().equals(((TeamScouting)data).getNotes());
	}
	public int compareTo(TeamScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
    public boolean empty() {
        return getNotes()=="";
    }
}
