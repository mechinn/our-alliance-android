package com.mechinn.android.ouralliance.data;

import android.util.Log;

import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.CascadeDelete;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.ForeignKey;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table(Match.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Match extends AOurAllianceData implements Comparable<Match>{
    public static final String TAG = "Match";
	private static final long serialVersionUID = -6220154246271261024L;
	public static final String COMPETITION = Competition.TAG;
    public static final String NUMBER = "matchNum";
    public static final String REDSCORE = "redScore";
    public static final String BLUESCORE = "blueScore";
    public static final String MATCHTYPE = "matchType";
    public static final String MATCHSET = "matchSet";
    public static final String MATCHOF = "matchOf";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,COMPETITION
            ,NUMBER
            ,REDSCORE
            ,BLUESCORE
            ,MATCHTYPE
            ,MATCHSET
            ,MATCHOF
    };

    public enum Type {
        PRACTICE(-1),QUALIFIER(0),QUARTERFINAL(1),SEMIFINAL(2),FINAL(3);
        private int value;
        private Type(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public static final Type getType(int value) {
        switch(value) {
            case -1:
                return Type.PRACTICE;
            case 1:
                return Type.QUARTERFINAL;
            case 2:
                return Type.SEMIFINAL;
            case 3:
                return Type.FINAL;
            default:
                return Type.QUALIFIER;
        }
    }
	
	public static final String SELECTPRACTICE = MATCHTYPE +" = "+Type.PRACTICE;
	public static final String SELECTNOTPRACTICE = MATCHTYPE +" != "+Type.PRACTICE;

    @Column(COMPETITION)
    @UniqueCombo
    @ForeignKey("Competition(_id)")
    @CascadeDelete
    @Check("competition > 0")
	private Competition competition;
    @Column(NUMBER)
    @UniqueCombo
	private int matchNum;
    @Column(REDSCORE)
	private int redScore;
    @Column(BLUESCORE)
	private int blueScore;
    @Column(MATCHTYPE)
	private Type matchType;
    @Column(MATCHSET)
	private int matchSet;
    @Column(MATCHOF)
	private int outOf;

	public Match() {
		super();
        competition = new Competition();
	}
    public Match(long id) {
        super(id);
    }
    public Match(int number) {
        super();
        this.setMatchNum(number);
        this.setRedScore(-1);
        this.setBlueScore(-1);
    }
	public Match(Competition competition, int number, Type type) {
		super();
        this.setCompetition(competition);
        this.setMatchNum(number);
        this.setRedScore(-1);
        this.setBlueScore(-1);
        this.setMatchType(type);
	}
	public Match(Competition competition, int number, Type type, int set, int of) {
		super();
        this.setCompetition(competition);
        this.setMatchNum(number);
        this.setRedScore(-1);
        this.setBlueScore(-1);
        this.setMatchType(type);
        this.setMatchSet(set);
        this.setMatchOf(of);
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public int getMatchNum() {
		return matchNum;
	}
	public int getDisplayNum() {
		return Math.abs(matchNum);
	}
	public void setMatchNum(int number) {
        if(number<0) {
            this.matchNum = number;
        } else {
            //negative all our practices so we dont get conflicts
            switch(getMatchType()) {
                case PRACTICE:
                    this.matchNum = -number;
                    break;
                default:
                    this.matchNum = number;
                    break;
            }
        }
	}
	public int getRedScore() {
		return redScore;
	}
	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}
	public int getBlueScore() {
		return blueScore;
	}
	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}
	public Type getMatchType() {
        if(null==matchType) {
            return Type.QUALIFIER;
        }
        return matchType;
	}
	public void setMatchType(Type type) {
		this.matchType = type;
	}
    public void setMatchType(int type) {
        this.matchType = getType(type);
    }
	public int getMatchSet() {
		return matchSet;
	}
	public void setMatchSet(int matchSet) {
		this.matchSet = matchSet;
	}
	public int getMatchOf() {
		return outOf;
	}
	public void setMatchOf(int outof) {
		this.outOf = outof;

		//set the match number stupid high
		int level = 0;
        switch(getMatchType()) {
            case QUARTERFINAL:
                level = 1000;
                break;
            case SEMIFINAL:
                level = 10000;
                break;
            case FINAL:
                level = 100000;
                break;
        }
		this.matchNum = level+ getMatchOf()*10+ getMatchSet();
	}
	@Override
	public String toString() {
		Log.d(TAG,"type: "+ getMatchType());
        switch(getMatchType()) {
            case PRACTICE:
                return "Practice: "+this.getDisplayNum();
            case QUARTERFINAL:
                return "Quarterfinal: "+ getMatchSet()+"-"+ getMatchOf();
            case SEMIFINAL:
                return "Semifinal: "+ getMatchSet()+"-"+ getMatchOf();
            case FINAL:
                return "Final: "+ getMatchOf();
            default:
                return "Qualifier: "+this.getDisplayNum();
        }
	}
	public boolean equals(Match data) {
		return super.equals(data) &&
				getCompetition().equals(data.getCompetition()) &&
				getMatchNum()==data.getMatchNum() &&
				getRedScore()==data.getRedScore() &&
				getBlueScore()==data.getBlueScore() &&
				getMatchType()==data.getMatchType() &&
				getMatchSet()==data.getMatchSet() &&
				getMatchOf()==data.getMatchOf();
	}
	public int compareTo(Match another) {
		return this.getDisplayNum() - another.getDisplayNum();
	}

    public AOurAllianceData validate() {
        return Query.one(Match.class, "SELECT * FROM " + TAG + " WHERE "+COMPETITION+"=? AND " + NUMBER + "=? LIMIT 1", getCompetition().getId(), getMatchNum()).get();
    }
    public boolean empty() {
        return (null==getCompetition() || getCompetition().empty())
                && getMatchNum()==0
                && getRedScore()==-1
                && getBlueScore()==-1
                && getMatchType().equals(Type.QUALIFIER)
                && getMatchSet()==0
                && getMatchOf()==0;
    }
}
