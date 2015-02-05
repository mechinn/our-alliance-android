package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.*;

import java.util.Date;

public abstract class MatchScouting extends AOurAllianceData implements Comparable<MatchScouting>  {
    public static final String TAG = "MatchScouting";
	private static final long serialVersionUID = 2234995463512680398L;
    public static final String MATCH = Match.TAG;
	public static final String TEAM = Team.TAG;
    public static final String COMPETITIONTEAM = CompetitionTeam.TAG;
    public static final String ALLIANCE = "alliance";
    public static final String NOTES = "notes";

    @Column(MATCH)
    @Unique(value= ConflictClause.IGNORE,group="unique")
    @ForeignKey(MATCH+"(_id)")
    @CascadeDelete
    @Check(MATCH+" > 0")
	private Match match;
    @Column(TEAM)
    private long oldTeam;
    @Column(COMPETITIONTEAM)
    @Unique(value=ConflictClause.IGNORE,group="unique")
    @ForeignKey(COMPETITIONTEAM+"(_id)")
    @CascadeDelete
    @Check(COMPETITIONTEAM+" > 0")
    private CompetitionTeam team;
    @Column(ALLIANCE)
    private boolean alliance;
    @Column(NOTES)
	private String notes;

	public MatchScouting() {
		super();
        match = new Match();
        team = new CompetitionTeam();
	}
    public MatchScouting(long id) {
        super(id);
    }
	public MatchScouting(Match match, CompetitionTeam team, boolean blue) {
        this.setMatch(match);
        this.setCompetitionTeam(team);
        this.setAlliance(blue);
	}
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
    public Date getMatchmodified() {
        return match.getModified();
    }
    public void setMatchmodified(Date modified) {
        this.match.setModified(modified);
    }
    public int getMatchNum() {
        return match.getMatchNum();
    }
    public void setMatchNum(int number) {
        match.setMatchNum(number);
    }
    public int getMatchType() {
        return match.getMatchType().getValue();
    }
    public void setMatchType(int type) {
        match.setMatchType(Match.getType(type));
    }
    public int getMatchSet() {
        return match.getMatchSet();
    }
    public void setMatchSet(int matchSet) {
        match.setMatchSet(matchSet);
    }
	public CompetitionTeam getCompetitionTeam() {
        return team;
	}
	public void setCompetitionTeam(CompetitionTeam team) {
		this.team = team;
	}
    public Team getTeam() {
        return team.getTeam();
    }
    public void setTeam(Team team) {
        this.team.setTeam(team);
    }
    public Competition getCompetition() {
        return team.getCompetition();
    }
    public void setCompetition(Competition competition) {
        this.team.setCompetition(competition);
        this.match.setCompetition(competition);
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
	public String toString() {
		return this.match+" - "+this.getAlliance()+": "+this.team.getTeam();
	}
	public boolean equals(Object data) {
        if(!(data instanceof MatchScouting)) {
            return false;
        }
		return  getMatch().equals(((MatchScouting)data).getMatch()) &&
				getCompetitionTeam().equals(((MatchScouting)data).getCompetitionTeam()) &&
                isAlliance()==((MatchScouting)data).isAlliance() &&
				getNotes().equals(((MatchScouting)data).getNotes());
	}
	public int compareTo(MatchScouting another) {
        int compare = (this.isAlliance()?1:0) - (another.isAlliance()?1:0);
        if(0==compare) {
            compare = this.getCompetitionTeam().getTeam().compareTo(another.getCompetitionTeam().getTeam());
        }
		return compare;
	}
    public boolean empty() {
        return isAlliance()==false
                && getNotes()=="";
    }
}
