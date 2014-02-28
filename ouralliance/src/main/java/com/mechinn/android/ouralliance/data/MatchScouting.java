package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;

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
