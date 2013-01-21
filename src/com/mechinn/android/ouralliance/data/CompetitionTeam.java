package com.mechinn.android.ouralliance.data;

import java.io.Serializable;
import java.util.Date;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mechinn.android.ouralliance.DataProvider;
import com.mechinn.android.ouralliance.Database;

public class CompetitionTeam  implements Serializable {
	public static final String CLASS = "CompetitionTeam";
	public static final String TABLE = "competitionteam";
	public static final String COMPETITION = Competition.TABLE;
    public static final String TEAM = Team.TABLE;
	
	public static final String[] ALLCOLUMNS = { BaseColumns._ID, Database.MODIFIED, COMPETITION, TEAM };

	public static final String PATH = TABLE+"s/";
	public static final String IDPATH = PATH+"id/";
	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+PATH);
	public static final Uri URI_ID = Uri.parse(DataProvider.BASE_URI_STRING+IDPATH);

	public static final String DIRTYPE = DataProvider.BASE_DIR+CLASS;
	public static final String ITEMTYPE = DataProvider.BASE_ITEM+CLASS;
	
	private long id;
	private Date modified;
	private Competition competition;
	private Team team;
	public CompetitionTeam() {
	}
	public CompetitionTeam(Competition competition, Team team) {
		this.competition = competition;
		this.team = team;
	}
	public CompetitionTeam(int id, Date mod, Competition competition, Team team) {
		this(competition, team);
		this.id = id;
		this.modified = mod;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
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
	public String toString() {
		return this.competition+" - "+this.team;
	}
	public ContentValues toCV() {
		ContentValues values = new ContentValues();
		values.put(Database.MODIFIED, new Date().getTime());
		values.put(CompetitionTeam.COMPETITION, this.getCompetition().getId());
		values.put(CompetitionTeam.TEAM, this.getTeam().getId());
		return values;
	}
}