package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;

public abstract class MatchScouting extends AOurAllianceData implements Comparable<MatchScouting>  {
    public static final String TAG = "MatchScouting";
	private static final long serialVersionUID = 2234995463512680398L;
    public static final String MATCH = Match.TAG;
	public static final String TEAM = Team.TAG;
    public static final String ALLIANCE = "alliance";
    public static final String NOTES = "notes";

    @Column(MATCH)
    @UniqueCombo
    @ForeignKey("Match(_id)")
    @CascadeDelete
    @Check("match > 0")
	private Match match;
    @Column(TEAM)
    @UniqueCombo
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("team > 0")
    private CompetitionTeam team;
    @Column(ALLIANCE)
    private boolean alliance;
    @Column(NOTES)
	private String notes;

	public MatchScouting() {
		super();
	}
    public MatchScouting(long id) {
        super(id);
    }
	public MatchScouting(Match match, CompetitionTeam team, boolean blue) {
        this.setMatch(match);
        this.setTeam(team);
        this.setAlliance(blue);
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
    public boolean isAlliance() {
        return alliance;
    }
    public String getAlliance() {
        if(alliance) {
            return "Blue";
        } else {
            return "Red";
        }
    }
    public void setAlliance(boolean blue) {
        this.alliance = blue;
    }
	public String getNotes() {
		if(null==notes) {
			return "";
		}
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
    public void setNotes(CharSequence notes) {
        if(null==notes) {
            setNotes("");
        } else {
            setNotes(notes.toString());
        }
    }
	public String toString() {
		return this.match+" - "+this.getAlliance()+": "+this.team.getTeam();
	}
	public boolean equals(MatchScouting data) {
		return super.equals(data) &&
				getMatch().equals(data.getMatch()) &&
				getTeam().equals(data.getTeam()) &&
                isAlliance()==data.isAlliance() &&
				getNotes().equals(data.getNotes());
	}
	public int compareTo(MatchScouting another) {
		return this.getTeam().compareTo(another.getTeam());
	}
}
