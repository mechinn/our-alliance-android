package com.mechinn.android.ouralliance.data;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

@Table(Match.TAG)
public class Match extends AOurAllianceData implements Comparable<Match>{
    public static final String TAG = "Match";
	private static final long serialVersionUID = -6220154246271261024L;
	public static final String COMPETITION = Competition.TAG;
    public static final String NUMBER = "matchNum";
    public static final String REDSCORE = "redScore";
    public static final String BLUESCORE = "blueScore";
    public static final String MATCHTYPE = "matchType";
    public static final String MATCHSET = "matchSet";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,COMPETITION
            ,NUMBER
            ,REDSCORE
            ,BLUESCORE
            ,MATCHTYPE
            ,MATCHSET
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
    @Unique(value=ConflictClause.IGNORE,group="unique")
    @ForeignKey(COMPETITION+"(_id)")
    @CascadeDelete
    @Check(COMPETITION+" > 0")
	private Competition competition;
    @Column(NUMBER)
    @Unique(value=ConflictClause.IGNORE,group="unique")
    @SerializedName("match_number")
	private int matchNum;
    @Column(REDSCORE)
    @Check(REDSCORE+" > -2")
	private int redScore;
    @Column(BLUESCORE)
    @Check(BLUESCORE+" > -2")
	private int blueScore;
    @Column(MATCHTYPE)
    @Check(MATCHTYPE+" > -2")
	private Type matchType;
    @Column(MATCHSET)
    @SerializedName("set_number")
	private int matchSet;
    private Alliances alliances;
    @SerializedName("comp_level")
    private String compLevel;

	public Match() {
		super();
        competition = new Competition();
	}
    public Match(long id) {
        super(id);
    }
    public Match(Competition competition, int number) {
        super();
        this.setCompetition(competition);
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
	public Match(Competition competition, int number, Type type, int set) {
		super();
        this.setCompetition(competition);
        this.setMatchNum(number);
        this.setRedScore(-1);
        this.setBlueScore(-1);
        this.setMatchType(type);
        this.setMatchSet(set);
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public int getMatchNum() {
        switch(getMatchType()) {
            case FINAL:
            case SEMIFINAL:
            case QUARTERFINAL:
                if(matchNum<1000) {
                    setMatchNum(matchNum);
                }
                break;
            default:
                break;
        }
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
                case QUALIFIER:
                    this.matchNum = number;
                    break;
                default:
                    //we are importing the finals match
                    if(number>1000) {
                        this.matchNum = number;
                        break;
                    }
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
                    this.matchNum = level + number*10+ getMatchSet();
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
    public void convertCompLevelToMatchType() {
        if(null!=compLevel) {
            if(compLevel.equals("f")) {
                matchType = Type.FINAL;
            } else if(compLevel.equals("sf")) {
                matchType = Type.SEMIFINAL;
            } else if(compLevel.equals("qf")) {
                matchType = Type.QUARTERFINAL;
            } else {
                matchType = Type.QUALIFIER;
            }
        }
    }
    public void convertScores() {
        if(null!=alliances) {
            setRedScore(alliances.getRed().getScore());
            setBlueScore(alliances.getBlue().getScore());
        }
    }
	public int getMatchSet() {
		return matchSet;
	}
	public void setMatchSet(int matchSet) {
        this.matchSet = matchSet;
	}
    public Alliances getAlliances() {
        return alliances;
    }
	@Override
	public String toString() {
		Log.d(TAG,"type: "+ getMatchType());
        switch(getMatchType()) {
            case PRACTICE:
                return "Practice: "+this.getDisplayNum();
            case QUARTERFINAL:
                return "Quarterfinal: "+getMatchSet()+" Match: "+(((getMatchNum()-1000)/10));
            case SEMIFINAL:
                return "Semifinal: "+ getMatchSet()+" Match: "+(((getMatchNum()-10000)/10));
            case FINAL:
                return "Final: "+(((getMatchNum()-100000)/10));
            default:
                return "Qualifier: "+this.getDisplayNum();
        }
	}
	public boolean equals(Object data) {
        if(!(data instanceof Match)) {
            return false;
        }
		return  getCompetition().equals(((Match)data).getCompetition()) &&
				getMatchNum()==((Match)data).getMatchNum() &&
				getRedScore()==((Match)data).getRedScore() &&
				getBlueScore()==((Match)data).getBlueScore() &&
				getMatchType().equals(((Match)data).getMatchType()) &&
				getMatchSet()==((Match)data).getMatchSet();
	}
	public int compareTo(Match another) {
		return this.getDisplayNum() - another.getDisplayNum();
	}

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        Match item = Query.one(Match.class, "SELECT * FROM " + TAG + " WHERE "+COMPETITION+"=? AND " + NUMBER + "=? LIMIT 1", getCompetition().getId(), getMatchNum()).get();
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
        return getRedScore()<0
                && getBlueScore()<0
                && getMatchType().equals(Type.QUALIFIER)
                && getMatchSet()==0;
    }
}
