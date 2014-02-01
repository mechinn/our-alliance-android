package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.mechinn.android.ouralliance.provider.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Match extends AOurAllianceData implements Comparable<Match>{
	public static final String TAG = Match.class.getSimpleName();
	private static final long serialVersionUID = -6220154246271261024L;
	public static final String TABLE = "match";
	public static final String COMPETITION = Competition.TABLE;
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
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, ALLCOLUMNSBASE);

	public static final String VIEW = TABLE+"view";
	public static final String[] VIEWCOLUMNSBASE = ArrayUtils.addAll(
			ALLCOLUMNSBASE, 
			new String[] { Competition.VIEW_ID, Competition.VIEW_MODIFIED, Competition.VIEW_SEASON, Competition.VIEW_NAME, Competition.VIEW_CODE,
					Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
					RED1+Team.VIEW_ID, RED1+Team.VIEW_MODIFIED, RED1+Team.VIEW_NUMBER, RED1+Team.VIEW_NAME,
					RED2+Team.VIEW_ID, RED2+Team.VIEW_MODIFIED, RED2+Team.VIEW_NUMBER, RED2+Team.VIEW_NAME,
					RED3+Team.VIEW_ID, RED3+Team.VIEW_MODIFIED, RED3+Team.VIEW_NUMBER, RED3+Team.VIEW_NAME,
					BLUE1+Team.VIEW_ID, BLUE1+Team.VIEW_MODIFIED, BLUE1+Team.VIEW_NUMBER, BLUE1+Team.VIEW_NAME,
					BLUE2+Team.VIEW_ID, BLUE2+Team.VIEW_MODIFIED, BLUE2+Team.VIEW_NUMBER, BLUE2+Team.VIEW_NAME,
					BLUE3+Team.VIEW_ID, BLUE3+Team.VIEW_MODIFIED, BLUE3+Team.VIEW_NUMBER, BLUE3+Team.VIEW_NAME}
		);
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(Database.COLUMNSBASE, VIEWCOLUMNSBASE);
	
	public static final int PRACTICE = -1;
	public static final int QUALIFIER = 0;
	public static final int QUARTERFINAL = 1;
	public static final int SEMIFINAL = 2;
	public static final int FINAL = 3;
	
	public static final String SELECTPRACTICE = TYPE+" = "+PRACTICE;
	public static final String SELECTNOTPRACTICE = TYPE+" != "+PRACTICE;
	
	private Competition competition;
	private int number;
	private Team red1;
	private Team red2;
	private Team red3;
	private Team blue1;
	private Team blue2;
	private Team blue3;
	private int redScore;
	private int blueScore;
	private int type;
	private int set;
	private int of;
	public Match() {
		super();
	}
	public Match(Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int type) {
		super();
		setData(competition, number, red1, red2, red3, blue1, blue2, blue3, -1, -1, type, 0, 0);
	}
	public Match(Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int type, int set, int of) {
		super();
		setData(competition, number, red1, red2, red3, blue1, blue2, blue3, -1, -1, type, set, of);
	}
	public Match(Competition competition, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int redScore, int blueScore, int type, int set, int of) {
		super();
		setData(competition, -1, red1, red2, red3, blue1, blue2, blue3, redScore, blueScore, type, set, of);
	}
	public Match(long id, Date mod, Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int redScore, int blueScore, int type, int set, int of) {
		super(id, mod);
		setData(competition, number, red1, red2, red3, blue1, blue2, blue3, redScore, blueScore, type, set, of);
	}
	private void setData(Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int redScore, int blueScore, int type, int set, int of) {
		this.setCompetition(competition);
		this.setRed1(red1);
		this.setRed2(red2);
		this.setRed3(red3);
		this.setBlue1(blue1);
		this.setBlue2(blue2);
		this.setBlue3(blue3);
		this.setRedScore(redScore);
		this.setBlueScore(blueScore);
		this.setType(type);
		this.setSet(set);
		this.setOf(of);
		this.setNumber(number);
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public int getSQLNumber() {
		return number;
	}
	public int getNumber() {
		return Math.abs(number);
	}
	public void setNumber(int number) {

		//negative all our practices so we dont get conflicts
		if(type==PRACTICE) {
			this.number = -number;
		} else {
			this.number = number;
		}
	}
	public Team getRed1() {
		return red1;
	}
	public void setRed1(Team red1) {
		this.red1 = red1;
	}
	public void setRed1(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.red1 = thisTeam;
	}
	public Team getRed2() {
		return red2;
	}
	public void setRed2(Team red2) {
		this.red2 = red2;
	}
	public void setRed2(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.red2 = thisTeam;
	}
	public Team getRed3() {
		return red3;
	}
	public void setRed3(Team red3) {
		this.red3 = red3;
	}
	public void setRed3(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.red3 = thisTeam;
	}
	public Team getBlue1() {
		return blue1;
	}
	public void setBlue1(Team blue1) {
		this.blue1 = blue1;
	}
	public void setBlue1(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.blue1 = thisTeam;
	}
	public Team getBlue2() {
		return blue2;
	}
	public void setBlue2(Team blue2) {
		this.blue2 = blue2;
	}
	public void setBlue2(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.blue2 = thisTeam;
	}
	public Team getBlue3() {
		return blue3;
	}
	public void setBlue3(Team blue3) {
		this.blue3 = blue3;
	}
	public void setBlue3(long team) {
		Team thisTeam = new Team();
		thisTeam.setId(team);
		this.blue3 = thisTeam;
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
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSet() {
		return set;
	}
	public void setSet(int set) {
		this.set = set;
	}
	public int getOf() {
		return of;
	}
	public void setOf(int of) {
		this.of = of;

		//set the match number stupid high
		int level = 0;
		if(type==1) {
			level = 1000;
		} else if(type==2) {
			level = 10000;
		} else if(type==3) { //Finals
			level = 100000;
		}
		this.number = level+of*10+set;
	}
	public Team selectTeam(int position) {
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
		return getRed1().getNumber()+" "+
				getRed2().getNumber()+" "+
				getRed3().getNumber()+" | "+
				getBlue1().getNumber()+" "+
				getBlue2().getNumber()+" "+
				getBlue3().getNumber();
	}
	@Override
	public String toString() {
		Log.d(TAG,"type: "+type);
		if(PRACTICE==type) {
			return "Practice: "+this.getNumber();
		} else if(QUARTERFINAL==type) {
			return "Quarterfinal: "+getSet()+"-"+getOf();
		} else if(SEMIFINAL==type) {
			return "Semifinal: "+getSet()+"-"+getOf();
		} else if(FINAL==type) {
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

	@Override
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(COMPETITION, this.getCompetition().getId());
		values.put(NUMBER, this.getSQLNumber());
		values.put(RED1, this.getRed1().getId());
		values.put(RED2, this.getRed2().getId());
		values.put(RED3, this.getRed3().getId());
		values.put(BLUE1, this.getBlue1().getId());
		values.put(BLUE2, this.getBlue2().getId());
		values.put(BLUE3, this.getBlue3().getId());
		values.put(REDSCORE, this.getRedScore());
		values.put(BLUESCORE, this.getBlueScore());
		values.put(TYPE, this.getType());
		values.put(SET, this.getSet());
		values.put(OF, this.getOf());
		return values;
	}

	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==getCompetition()) {
			error.add(COMPETITION);
		}
		if(0==this.getNumber()) {
			error.add(NUMBER);
		}
		if(null==getRed1()) {
			error.add(RED1);
		}
		if(null==getRed2()) {
			error.add(RED2);
		}
		if(null==getRed3()) {
			error.add(RED3);
		}
		if(null==getBlue1()) {
			error.add(BLUE1);
		}
		if(null==getBlue2()) {
			error.add(BLUE2);
		}
		if(null==getBlue3()) {
			error.add(BLUE3);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setData(Competition.newFromViewCursor(cursor),
				cursor.getInt(cursor.getColumnIndexOrThrow(NUMBER)),
				Team.newFromViewCursor(cursor,RED1),
				Team.newFromViewCursor(cursor,RED2),
				Team.newFromViewCursor(cursor,RED3),
				Team.newFromViewCursor(cursor,BLUE1),
				Team.newFromViewCursor(cursor,BLUE2),
				Team.newFromViewCursor(cursor,BLUE3),
				cursor.getInt(cursor.getColumnIndexOrThrow(REDSCORE)),
				cursor.getInt(cursor.getColumnIndexOrThrow(BLUESCORE)),
				cursor.getInt(cursor.getColumnIndexOrThrow(TYPE)),
				cursor.getInt(cursor.getColumnIndexOrThrow(SET)),
				cursor.getInt(cursor.getColumnIndexOrThrow(OF)));
	}
	
	public static Match newFromCursor(Cursor cursor) {
		Match data = new Match();
		data.fromCursor(cursor);
		return data;
	}
	
	
}
