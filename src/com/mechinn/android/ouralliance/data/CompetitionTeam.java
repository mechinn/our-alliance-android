package com.mechinn.android.ouralliance.data;

import java.util.Date;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class CompetitionTeam extends AOurAllianceData implements Comparable<CompetitionTeam> {
	public static final String CLASS = "CompetitionTeam";
	public static final String TABLE = "competitionteam";
	public static final String COMPETITION = Competition.TABLE;
    public static final String TEAM = Team.TABLE;
    public static final String RANK = "rank";
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, COMPETITION, TEAM, RANK };

	public static final String VIEW = TABLE+"view";
    public static final String VIEW_ID = TABLE+BaseColumns._ID;
    public static final String VIEW_MODIFIED = TABLE+Database.MODIFIED;
    public static final String VIEW_COMPETITION = TABLE+COMPETITION;
    public static final String VIEW_TEAM = TABLE+TEAM;
	public static final String[] VIEWCOLUMNS = { BaseColumns._ID, Database.MODIFIED, COMPETITION, TEAM, RANK,
		Competition.VIEW_ID, Competition.VIEW_MODIFIED, Competition.VIEW_SEASON, Competition.VIEW_NAME, Competition.VIEW_CODE,
		Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
		Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final String TEAMPATH = PATH+Team.TABLE+"/";
	public static final String COMPPATH = PATH+Competition.TABLE+"/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final String URI_ID = DataProvider.BASE_URI_STRING+IDPATH;
	public static final String URI_TEAM = DataProvider.BASE_URI_STRING+TEAMPATH;
	public static final String URI_COMP = DataProvider.BASE_URI_STRING+COMPPATH;

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private Competition competition;
	private Team team;
	private int rank;
	public CompetitionTeam() {
		super();
		this.setRank(999);
	}
	public CompetitionTeam(Competition competition, Team team) {
		setData(competition, team, 999);
	}
	public CompetitionTeam(Competition competition, Team team, int rank) {
		setData(competition, team, rank);
	}
	public CompetitionTeam(long id, Date mod, Competition competition, Team team, int rank) {
		super(id, mod);
		setData(competition, team, rank);
	}
	private void setData(Competition competition, Team team, int rank) {
		this.setCompetition(competition);
		this.setTeam(team);
		this.setRank(rank);
	}
	public static Uri uriFromId(long id) {
		return Uri.parse(URI_ID + id);
	}
	public static Uri uriFromId(CompetitionTeam id) {
		return uriFromId(id.getId());
	}
	public static Uri uriFromTeam(Team id) {
		return uriFromTeam(id.getId());
	}
	public static Uri uriFromTeam(long id) {
		return Uri.parse(URI_TEAM + id);
	}
	public static Uri uriFromComp(Competition id) {
		return uriFromComp(id.getId());
	}
	public static Uri uriFromComp(long id) {
		return Uri.parse(URI_COMP + id);
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
	public String toString() {
		return this.competition+" # "+this.rank+" "+this.team;
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(CompetitionTeam.COMPETITION, this.getCompetition().getId());
		values.put(CompetitionTeam.TEAM, this.getTeam().getId());
		values.put(CompetitionTeam.RANK, this.getRank());
		return values;
	}
	public int compareTo(CompetitionTeam another) {
		int diff = this.getRank() - another.getRank();
		if(diff==0) {
			return this.getTeam().compareTo(another.getTeam());
		}
		return diff;
	}
}