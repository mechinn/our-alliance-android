package com.mechinn.android.ouralliance.data;

import android.util.Log;

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
public class Match extends AOurAllianceData implements Comparable<Match>{
	public static final String TAG = Match.class.getSimpleName();
	private static final long serialVersionUID = -6220154246271261024L;
	public static final String COMPETITION = Competition.TAG;
    public static final String NUMBER = "number";
    public static final String RED1 = "red1";
    public static final String RED2 = "red2";
    public static final String RED3 = "red3";
    public static final String BLUE1 = "blue1";
    public static final String BLUE2 = "blue2";
    public static final String BLUE3 = "blue3";
    public static final String REDSCORE = "redScore";
    public static final String BLUESCORE = "blueScore";
    public static final String TYPE = "matchType";
    public static final String SET = "matchSet";
    public static final String OF = "matchOf";
	public static final String[] ALLCOLUMNSBASE = { COMPETITION, NUMBER, RED1, RED2, RED3, BLUE1, BLUE2, BLUE3, REDSCORE, BLUESCORE, TYPE, SET, OF };
	
	public static final int PRACTICE = -1;
	public static final int QUALIFIER = 0;
	public static final int QUARTERFINAL = 1;
	public static final int SEMIFINAL = 2;
	public static final int FINAL = 3;
	
	public static final String SELECTPRACTICE = TYPE+" = "+PRACTICE;
	public static final String SELECTNOTPRACTICE = TYPE+" != "+PRACTICE;

    @Column
    @UniqueCombo
    @ForeignKey("Competition(_id)")
    @CascadeDelete
    @Check("competition > 0")
	private Competition competition;
    @Column
    @UniqueCombo
	private int matchNum;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("red1 > 0")
	private CompetitionTeam red1;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("red2 > 0")
	private CompetitionTeam red2;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("red3 > 0")
	private CompetitionTeam red3;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("blue1 > 0")
	private CompetitionTeam blue1;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("blue2 > 0")
	private CompetitionTeam blue2;
    @Column
    @ForeignKey("CompetitionTeam(_id)")
    @CascadeDelete
    @Check("blue3 > 0")
	private CompetitionTeam blue3;
    @Column
	private int redScore;
    @Column
	private int blueScore;
    @Column
	private int matchType;
    @Column
	private int matchSet;
    @Column
	private int outOf;

	public Match() {
		super();
	}
    public Match(long id) {
        super(id);
    }
	public Match(Competition competition, int number, CompetitionTeam red1, CompetitionTeam red2, CompetitionTeam red3, CompetitionTeam blue1, CompetitionTeam blue2, CompetitionTeam blue3, int type) {
		super();
        this.setCompetition(competition);
        this.setNumber(number);
        this.setRed1(red1);
        this.setRed2(red2);
        this.setRed3(red3);
        this.setBlue1(blue1);
        this.setBlue2(blue2);
        this.setBlue3(blue3);
        this.setRedScore(-1);
        this.setBlueScore(-1);
        this.setType(type);
        this.setSet(0);
        this.setOf(0);
	}
	public Match(Competition competition, int number, CompetitionTeam red1, CompetitionTeam red2, CompetitionTeam red3, CompetitionTeam blue1, CompetitionTeam blue2, CompetitionTeam blue3, int type, int set, int of) {
		super();
        this.setCompetition(competition);
        this.setNumber(number);
        this.setRed1(red1);
        this.setRed2(red2);
        this.setRed3(red3);
        this.setBlue1(blue1);
        this.setBlue2(blue2);
        this.setBlue3(blue3);
        this.setRedScore(-1);
        this.setBlueScore(-1);
        this.setType(type);
        this.setSet(set);
        this.setOf(of);
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public int getSQLNumber() {
		return matchNum;
	}
	public int getNumber() {
		return Math.abs(matchNum);
	}
	public void setNumber(int number) {
		//negative all our practices so we dont get conflicts
		if(matchType==PRACTICE) {
			this.matchNum = -number;
		} else {
			this.matchNum = number;
		}
	}
	public CompetitionTeam getRed1() {
		return red1;
	}
	public void setRed1(CompetitionTeam red1) {
		this.red1 = red1;
	}
	public void setRed1(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.red1 = thisCompetitionTeam;
	}
	public CompetitionTeam getRed2() {
		return red2;
	}
	public void setRed2(CompetitionTeam red2) {
		this.red2 = red2;
	}
	public void setRed2(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.red2 = thisCompetitionTeam;
	}
	public CompetitionTeam getRed3() {
		return red3;
	}
	public void setRed3(CompetitionTeam red3) {
		this.red3 = red3;
	}
	public void setRed3(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.red3 = thisCompetitionTeam;
	}
	public CompetitionTeam getBlue1() {
		return blue1;
	}
	public void setBlue1(CompetitionTeam blue1) {
		this.blue1 = blue1;
	}
	public void setBlue1(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.blue1 = thisCompetitionTeam;
	}
	public CompetitionTeam getBlue2() {
		return blue2;
	}
	public void setBlue2(CompetitionTeam blue2) {
		this.blue2 = blue2;
	}
	public void setBlue2(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.blue2 = thisCompetitionTeam;
	}
	public CompetitionTeam getBlue3() {
		return blue3;
	}
	public void setBlue3(CompetitionTeam blue3) {
		this.blue3 = blue3;
	}
	public void setBlue3(long CompetitionTeam) {
		CompetitionTeam thisCompetitionTeam = new CompetitionTeam();
		thisCompetitionTeam.setId(CompetitionTeam);
		this.blue3 = thisCompetitionTeam;
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
	public int getType() {
		return matchType;
	}
	public void setType(int type) {
		this.matchType = type;
	}
	public int getSet() {
		return matchSet;
	}
	public void setSet(int matchSet) {
		this.matchSet = matchSet;
	}
	public int getOf() {
		return outOf;
	}
	public void setOf(int outof) {
		this.outOf = outof;

		//set the match number stupid high
		int level = 0;
		if(getType()==1) { //quarter
			level = 1000;
		} else if(getType()==2) { //semi
			level = 10000;
		} else if(getType()==3) { //Finals
			level = 100000;
		}
		this.matchNum = level+getOf()*10+getSet();
	}
	public CompetitionTeam selectTeam(int position) {
		switch(position) {
			case 0:
				return getRed1();
			case 1:
				return getRed2();
			case 2:
				return getRed3();
			case 3:
				return getBlue1();
			case 4:
				return getBlue2();
			case 5:
				return getBlue3();
		}
		return null;
	}
	public String getTeams() {
		return getRed1().getTeam().getNumber()+" "+
				getRed2().getTeam().getNumber()+" "+
				getRed3().getTeam().getNumber()+" | "+
				getBlue1().getTeam().getNumber()+" "+
				getBlue2().getTeam().getNumber()+" "+
				getBlue3().getTeam().getNumber();
	}
	@Override
	public String toString() {
		Log.d(TAG,"type: "+getType());
		if(PRACTICE==getType()) {
			return "Practice: "+this.getNumber();
		} else if(QUARTERFINAL==getType()) {
			return "Quarterfinal: "+getSet()+"-"+getOf();
		} else if(SEMIFINAL==getType()) {
			return "Semifinal: "+getSet()+"-"+getOf();
		} else if(FINAL==getType()) {
			return "Final: "+getOf();
		} else {
			return "Qualifier: "+this.getNumber();
		}
	}
	public boolean equals(Match data) {
		return super.equals(data) &&
				getCompetition().equals(data.getCompetition()) &&
				getSQLNumber()==data.getSQLNumber() &&
				getRed1().equals(data.getRed1()) &&
				getRed2().equals(data.getRed2()) &&
				getRed3().equals(data.getRed3()) &&
				getBlue1().equals(data.getBlue1()) &&
				getBlue2().equals(data.getBlue2()) &&
				getBlue3().equals(data.getBlue3()) &&
				getRedScore()==data.getRedScore() &&
				getBlueScore()==data.getBlueScore() &&
				getType()==data.getType() &&
				getSet()==data.getSet() &&
				getOf()==data.getOf();
	}
	public int compareTo(Match another) {
		return this.getNumber() - another.getNumber();
	}
}
