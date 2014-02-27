package com.mechinn.android.ouralliance.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

public abstract class MatchScouting extends AOurAllianceData implements Comparable<MatchScouting>  {
	public static final String TAG = MatchScouting.class.getSimpleName();
	private static final long serialVersionUID = 2234995463512680398L;
    public static final String MATCH = Match.TAG;
	public static final String TEAM = Team.TAG;
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNSBASE = { TEAM, NOTES };

    @Column
    @UniqueCombo
    @ForeignKey("Match(_id)")
    @CascadeDelete
    @Check("match > 0")
	private Match match;
    @Column
    @UniqueCombo
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("team > 0")
    private CompetitionTeam team;
    @Column
    @NotNull
	private CharSequence notes;

	public MatchScouting() {
		super();
	}
    public MatchScouting(long id) {
        super(id);
    }
	public MatchScouting(Match match, CompetitionTeam team) {
		this.setData(match, team);
	}
	public MatchScouting(long id, Date mod, Match match, CompetitionTeam team, CharSequence notes) {
		super(id, mod);
		this.setData(match, team);
		this.setNotes(notes);
	}
	public void setData(Match match, CompetitionTeam team) {
		this.setMatch(match);
		this.setTeam(team);
	}
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public CompetitionTeam getTeam() {
		return team;
	}
	public void setTeam(CompetitionTeam team) {
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
	public String toString() {
		return this.match+" - "+this.team.getTeam();
	}
	public boolean equals(MatchScouting data) {
		return super.equals(data) &&
				getMatch().equals(data.getMatch()) &&
				getTeam().equals(data.getTeam()) &&
				getNotes().equals(data.getNotes());
	}
	public int compareTo(MatchScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
}
