package com.mechinn.android.ouralliance.data.frc2014;

import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Date;

import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class MatchScouting2014 extends MatchScouting implements Comparable<MatchScouting>  {
	public static final String TAG = MatchScouting2014.class.getSimpleName();
	private static final long serialVersionUID = 2234995463512680398L;
	public static final String SEASON = Season.TAG;
	public static final String TEAM = Team.TAG;
    public static final String MATCH = Match.TAG;
    public static final String NOTES = "notes";
	public static final String[] ALLCOLUMNSBASE = { SEASON, TEAM, NOTES };

    @Column
    private int hotShots;
    @Column
    private int shotsMade;
    @Column
    private int shotsMissed;
    @Column
    private int moveFwd;
    @Column
    private boolean shooter;
    @Column
    private boolean catcher;
    @Column
    private boolean passer;
    @Column
    private int driveTrain;
    @Column
    private int ballAccuracy;
    @Column
    private boolean ground;
    @Column
    private boolean overTruss;
    @Column
    private boolean low;
    @Column
    private boolean high;

	public MatchScouting2014() {
		super();
	}
    public MatchScouting2014(long id) {
        super(id);
    }
	public MatchScouting2014(Match match, CompetitionTeam team) {
        super(match, team);
	}
	public MatchScouting2014(long id, Date mod, Match match, CompetitionTeam team, CharSequence notes) {
		super(id, mod, match, team, notes);
	}
}
