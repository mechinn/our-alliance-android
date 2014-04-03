package com.mechinn.android.ouralliance.data;

import android.util.Log;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

import java.util.Date;

@Table(CompetitionTeam.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class CompetitionTeam extends AOurAllianceData implements Comparable<CompetitionTeam> {
    public static final String TAG = "CompetitionTeam";
	private static final long serialVersionUID = 1458046534212642950L;

    public static final String COMPETITION = Competition.TAG;
    public static final String TEAM = Team.TAG;
    public static final String RANK = "rank";
    public static final String SCOUTED = "scouted";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,COMPETITION+"."+Competition.CODE
            ,TEAM+"."+Team.NUMBER
            ,RANK
            ,SCOUTED
    };

    @Column(COMPETITION)
    @UniqueCombo
    @ForeignKey(COMPETITION+"(_id)")
    @CascadeDelete
    @Check(COMPETITION+" > 0")
	private Competition competition;
    @Column(TEAM)
    @UniqueCombo
    @ForeignKey(TEAM+"(_id)")
    @CascadeDelete
    @Check(TEAM+" > 0")
	private Team team;
    @Column(RANK)
    @Check(RANK+" > -1")
	private int rank;
    @Column(SCOUTED)
	private boolean scouted;
	public CompetitionTeam() {
		super();
        this.setCompetition(new Competition());
        this.setTeam(new Team());
        this.setRank(999);
	}
    public CompetitionTeam(long id) {
        super(id);
    }
	public CompetitionTeam(Competition competition, Team team, int rank) {
        this.setCompetition(competition);
        this.setTeam(team);
        this.setRank(rank);
        this.setScouted(false);
	}
	public CompetitionTeam(Competition competition, Team team, int rank, boolean scouted) {
        this.setCompetition(competition);
        this.setTeam(team);
        this.setRank(rank);
        this.setScouted(scouted);
	}
    public CompetitionTeam(Date modified, Competition competition, Team team, int rank, boolean scouted) {
        this.setModified(modified);
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
	public boolean equals(Object data) {
        if(!(data instanceof CompetitionTeam)) {
            return false;
        }
		return  getCompetition().equals(((CompetitionTeam)data).getCompetition()) &&
				getTeam().equals(((CompetitionTeam)data).getTeam()) &&
				getRank() == ((CompetitionTeam)data).getRank() &&
				isScouted() == ((CompetitionTeam)data).isScouted();
	}
	public int compareTo(CompetitionTeam another) {
        return this.getTeam().compareTo(another.getTeam());
	}

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        CompetitionTeam item = Query.one(CompetitionTeam.class, "SELECT * FROM " + TAG + " WHERE "+COMPETITION+"=? AND " + TEAM + "=? LIMIT 1",getCompetition().getId(), getTeam().getId()).get();
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
        return getRank()==999
                && isScouted()==false;
    }
}