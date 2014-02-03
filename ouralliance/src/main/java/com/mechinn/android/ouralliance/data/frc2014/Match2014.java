package com.mechinn.android.ouralliance.data.frc2014;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.provider.DataProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Match2014 extends Match {
	private static final long serialVersionUID = -3255822533528181976L;
	public static final String TAG = Match2014.class.getSimpleName();
	public static final String TABLE = Match.TABLE+"2014";
	public static final String[] ALLCOLUMNS2014 = {  };
	public static final String[] ALLCOLUMNS = ArrayUtils.addAll(Match2014.ALLCOLUMNS, ALLCOLUMNS2014);
    
	public static final String VIEW = TABLE+"view";
	public static final String[] VIEWCOLUMNS = ArrayUtils.addAll(Match.VIEWCOLUMNS, ALLCOLUMNS2014);

	public static final Uri URI = Uri.parse(DataProvider.BASE_URI_STRING+TABLE);
	public static final String URITYPE = DataProvider.AUTHORITY+"."+TAG;

	public static final String DISTINCT = "d/"+TABLE;
	public static final Uri DISTINCTURI = Uri.parse(DataProvider.BASE_URI_STRING+DISTINCT);
	
	public Match2014() {
		super();
	}
	public Match2014(Match match) {
		super(match.getId(),
				match.getModified(),
				match.getCompetition(),
				match.getNumber(),
				match.getRed1(),
				match.getRed2(),
				match.getRed3(),
				match.getBlue1(),
				match.getBlue2(),
				match.getBlue3(),
				match.getRedScore(),
				match.getBlueScore(),
				match.getType(),
				match.getSet(),
				match.getOf());
	}
	public Match2014(Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int type) {
		super(competition, number, red1, red2, red3, blue1, blue2, blue3, type);
	}
	public Match2014(Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int type, int set, int of) {
		super(competition, number, red1, red2, red3, blue1, blue2, blue3, type, set, of);
	}
	public Match2014(Competition competition, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int redScore, int blueScore, int type, int set, int of) {
		super(competition, red1, red2, red3, blue1, blue2, blue3, redScore, blueScore, type, type, of);
	}
	public Match2014(long id, Date mod, Competition competition, int number, Team red1, Team red2, Team red3, Team blue1, Team blue2, Team blue3, int redScore, int blueScore, int type, int set, int of) {
		super(id, mod, competition, number, red1, red2, red3, blue1, blue2, blue3, redScore, blueScore, type, type, of);
	}
	private void setData() {
		
	}
	public boolean equals(Match2014 data) {
		return super.equals(data);
	}

	@Override
	public ContentValues toCV() {
		return super.toCV();
	}

	@Override
	public List<String> checkNotNulls() {
		return super.checkNotNulls();
	}

	@Override
	public void fromCursor(Cursor cursor) {
		super.fromCursor(cursor);
		setData();
	}
	
	public static Match2014 newFromCursor(Cursor cursor) {
		Match2014 data = new Match2014();
		data.fromCursor(cursor);
		return data;
	}
}
