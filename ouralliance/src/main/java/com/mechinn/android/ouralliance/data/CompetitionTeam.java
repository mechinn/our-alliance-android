package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class CompetitionTeam extends AOurAllianceData implements Comparable<CompetitionTeam> {
	public static final String TAG = CompetitionTeam.class.getSimpleName();
	private static final long serialVersionUID = 1458046534212642950L;

    @Column
    @UniqueCombo
    @ForeignKey("Competition(_id)")
    @CascadeDelete
    @Check("competition > 0")
	private Competition competition;
    @Column
    @UniqueCombo
    @ForeignKey("Team(_id)")
    @CascadeDelete
    @Check("team > 0")
	private Team team;
    @Column
	private int rank;
    @Column
	private boolean scouted;
	public CompetitionTeam() {
		super();
	}
    public CompetitionTeam(long id) {
        super(id);
    }
	public CompetitionTeam(Competition competition, Team team) {
        this.setCompetition(competition);
        this.setTeam(team);
        this.setRank(999);
        this.setScouted(false);
	}
	public CompetitionTeam(Competition competition, Team team, int rank, boolean scouted) {
        this.setCompetition(competition);
        this.setTeam(team);
        this.setRank(rank);
        this.setScouted(scouted);
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public boolean isScouted() {
		return scouted;
	}
	public void setScouted(boolean scouted) {
		this.scouted = scouted;
	}
	public String toString() {
		return this.competition+" # "+this.rank+" "+this.team;
	}
	public boolean equals(CompetitionTeam data) {
		return super.equals(data) &&
				getCompetition()==data.getCompetition() &&
				getTeam()==data.getTeam() &&
				getRank() == data.getRank() &&
				isScouted() == data.isScouted();
	}
	public int compareTo(CompetitionTeam another) {
        return this.getRank() - another.getRank();
	}
}