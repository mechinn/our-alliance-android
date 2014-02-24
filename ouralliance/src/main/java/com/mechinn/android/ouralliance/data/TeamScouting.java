package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

public abstract class TeamScouting extends AOurAllianceData implements Comparable<TeamScouting>  {
	public static final String TAG = TeamScouting.class.getSimpleName();
	private static final long serialVersionUID = 2234995463512680398L;
	public static final String SEASON = Season.TAG;
	public static final String TEAM = Team.TAG;
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNSBASE = { SEASON, TEAM, NOTES };

	public static final String TITLE_NOTES = "Scouting Notes";

    @Column
    @UniqueCombo
    @ForeignKey("Team(_id)")
    @CascadeDelete
    @Check("team > 0")
	private Team team;
    @Column
    @NotNull
	private CharSequence notes;

	public TeamScouting() {
		super();
	}
    public TeamScouting(long id) {
        super(id);
    }
	public TeamScouting(Team team) {
		this.setData(team);
	}
	public TeamScouting(long id, Date mod, Team team, CharSequence notes) {
		super(id, mod);
		this.setData(team);
		this.setNotes(notes);
	}
	public void setData(Team team) {
		this.setTeam(team);
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public CharSequence getNotes() {
		if(null==notes) {
			return "";
		}
		return notes;
	}
	public void setNotes(CharSequence notes) {
		this.notes = notes;
	}
	public boolean equals(TeamScouting data) {
		return super.equals(data) &&
				getTeam().equals(data.getTeam()) &&
				getNotes().equals(data.getNotes());
	}
	public int compareTo(TeamScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
}
