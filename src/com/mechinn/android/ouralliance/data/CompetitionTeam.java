package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mechinn.android.ouralliance.provider.Database;

public class CompetitionTeam extends AOurAllianceData implements Comparable<CompetitionTeam> {
	private static final long serialVersionUID = 1458046534212642950L;
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
    public static final String VIEW_RANK = TABLE+RANK;
	public static final String[] VIEWCOLUMNS = { BaseColumns._ID, Database.MODIFIED, COMPETITION, TEAM, RANK,
		Competition.VIEW_ID, Competition.VIEW_MODIFIED, Competition.VIEW_SEASON, Competition.VIEW_NAME, Competition.VIEW_CODE,
		Season.VIEW_ID, Season.VIEW_MODIFIED, Season.VIEW_YEAR, Season.VIEW_TITLE,
		Team.VIEW_ID, Team.VIEW_MODIFIED, Team.VIEW_NUMBER, Team.VIEW_NAME };

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+CLASS;
	
	private Competition competition;
	private Team team;
	private int rank;
	public CompetitionTeam() {
		super();
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
	public boolean equals(CompetitionTeam data) {
		return super.equals(data) &&
				getCompetition().equals(data.getCompetition()) &&
				getTeam().equals(data.getTeam()) &&
				getRank() == data.getRank();
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
	
	@Override
	public List<String> checkNotNulls() {
		List<String> error = new ArrayList<String>();
		if(null==getCompetition()) {
			error.add(COMPETITION);
		}
		if(null==getTeam()) {
			error.add(TEAM);
		}
		if(0==this.getRank()) {
			error.add(RANK);
		}
		return error;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setCompetition(Competition.newFromViewCursor(cursor));
		setTeam(Team.newFromViewCursor(cursor));
		setRank(cursor.getInt(cursor.getColumnIndexOrThrow(RANK)));
	}
	
	public static CompetitionTeam newFromCursor(Cursor cursor) {
		CompetitionTeam data = new CompetitionTeam();
		data.fromCursor(cursor);
		return data;
	}
	
	public static CompetitionTeam newFromViewCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_ID));
		Date mod = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(VIEW_MODIFIED)));
		Competition comp = Competition.newFromViewCursor(cursor);;
		Team team = Team.newFromViewCursor(cursor);
		int rank = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_RANK));
		return new CompetitionTeam(id, mod, comp, team, rank);
	}
}