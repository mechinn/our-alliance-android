package com.mechinn.android.ouralliance.data.frc2014;

import com.mechinn.android.ouralliance.data.*;

import com.mechinn.android.ouralliance.processor.FmtSeason;
import com.mechinn.android.ouralliance.processor.FmtTeam;
import com.mechinn.android.ouralliance.processor.ParseSeason;
import com.mechinn.android.ouralliance.processor.ParseTeam;

import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table(MatchScouting2014.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class MatchScouting2014 extends MatchScouting implements Comparable<MatchScouting>  {
    public static final String TAG = "MatchScouting2014";
	private static final long serialVersionUID = 2234995463512680398L;
    public static final String HOTSHOTS = "hotShots";
    public static final String SHOTSMADE = "shotsMade";
    public static final String SHOTSMISSED = "shotsMissed";
    public static final String MOVEFWD = "moveForward";
    public static final String SHOOTER = "shooter";
    public static final String CATCHER = "catcher";
    public static final String PASSER = "passer";
    public static final String DRIVETRAIN = "driveTrainRating";
    public static final String BALLACCURACY = "ballAccuracyRating";
    public static final String GROUND = "ground";
    public static final String OVERTRUSS = "overTruss";
    public static final String LOW = "low";
    public static final String HIGH = "high";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,Match.MATCHTYPE
            ,Match.MATCHSET
            ,Match.MATCHOF
            ,Match.NUMBER
            ,TEAM
            ,ALLIANCE
            ,NOTES
            ,HOTSHOTS
            ,SHOTSMADE
            ,SHOTSMISSED
            ,MOVEFWD
            ,SHOOTER
            ,CATCHER
            ,PASSER
            ,DRIVETRAIN
            ,BALLACCURACY
            ,GROUND
            ,OVERTRUSS
            ,LOW
            ,HIGH
    };

    public static final CellProcessor[] writeProcessor = new CellProcessor[] {
            new FmtDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,null                     //Match.MATCHTYPE
            ,null                     //Match.MATCHSET
            ,null                     //Match.MATCHOF
            ,null                     //Match.NUMBER
            ,new FmtTeam()                      //TEAM
            ,null                       //ALLIANCE
            ,null                               //NOTES
            ,null                               //HOTSHOTS
            ,null                               //SHOTSMADE
            ,null                               //SHOTSMISSED
            ,null                               //MOVEFWD
            ,new FmtBool(TRUE,FALSE)            //SHOOTER
            ,new FmtBool(TRUE,FALSE)            //CATCHER
            ,new FmtBool(TRUE,FALSE)            //PASSER
            ,null                               //DRIVETRAIN
            ,null                               //BALLACCURACY
            ,new FmtBool(TRUE,FALSE)            //GROUND
            ,new FmtBool(TRUE,FALSE)            //OVERTRUSS
            ,new FmtBool(TRUE,FALSE)            //LOW
            ,new FmtBool(TRUE,FALSE)            //HIGH
    };

    public static final CellProcessor[] readProcessor = new CellProcessor[] {
            new ParseDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,new ParseInt()                     //Match.MATCHTYPE
            ,new ParseInt()                     //Match.MATCHSET
            ,new ParseInt()                     //Match.MATCHOF
            ,new ParseInt()                     //Match.NUMBER
            ,new ParseTeam()                      //TEAM
            ,new ParseBool("blue","red")            //ALLIANCE
            ,null                                 //NOTES
            ,new ParseInt()                                 //HOTSHOTS
            ,new ParseInt()                                 //SHOTSMADE
            ,new ParseInt()                                 //SHOTSMISSED
            ,new ParseDouble()                                 //MOVEFWD
            ,new ParseBool()            //SHOOTER
            ,new ParseBool()            //CATCHER
            ,new ParseBool()            //PASSER
            ,new ParseDouble()                                 //DRIVETRAIN
            ,new ParseDouble()                                 //BALLACCURACY
            ,new ParseBool()            //GROUND
            ,new ParseBool()            //OVERTRUSS
            ,new ParseBool()            //LOW
            ,new ParseBool()            //HIGH
    };

    @Column(HOTSHOTS)
    private int hotShots;
    @Column(SHOTSMADE)
    private int shotsMade;
    @Column(SHOTSMISSED)
    private int shotsMissed;
    @Column(MOVEFWD)
    private double moveForward;
    @Column(SHOOTER)
    private boolean shooter;
    @Column(CATCHER)
    private boolean catcher;
    @Column(PASSER)
    private boolean passer;
    @Column(DRIVETRAIN)
    private double driveTrainRating;
    @Column(BALLACCURACY)
    private double ballAccuracyRating;
    @Column(GROUND)
    private boolean ground;
    @Column(OVERTRUSS)
    private boolean overTruss;
    @Column(LOW)
    private boolean low;
    @Column(HIGH)
    private boolean high;

	public MatchScouting2014() {
		super();
	}
    public MatchScouting2014(long id) {
        super(id);
    }
	public MatchScouting2014(Match match, CompetitionTeam team, boolean blue) {
        super(match, team, blue);
	}
    public int getHotShots() {
        return hotShots;
    }
    public void setHotShots(int hotShots) {
        this.hotShots = hotShots;
    }
    public int getShotsMade() {
        return shotsMade;
    }
    public void setShotsMade(int shotsMade) {
        this.shotsMade = shotsMade;
    }
    public int getShotsMissed() {
        return shotsMissed;
    }
    public void setShotsMissed(int shotsMissed) {
        this.shotsMissed = shotsMissed;
    }
    public double getMoveForward() {
        return moveForward;
    }
    public void setMoveForward(double moveForward) {
        this.moveForward = moveForward;
    }
    public boolean isShooter() {
        return shooter;
    }
    public void setShooter(boolean shooter) {
        this.shooter = shooter;
    }
    public boolean isCatcher() {
        return catcher;
    }
    public void setCatcher(boolean catcher) {
        this.catcher = catcher;
    }
    public boolean isPasser() {
        return passer;
    }
    public void setPasser(boolean passer) {
        this.passer = passer;
    }
    public double getDriveTrainRating() {
        return driveTrainRating;
    }
    public void setDriveTrainRating(double driveTrainRating) {
        this.driveTrainRating = driveTrainRating;
    }
    public double getBallAccuracyRating() {
        return ballAccuracyRating;
    }
    public void setBallAccuracyRating(double ballAccuracyRating) {
        this.ballAccuracyRating = ballAccuracyRating;
    }
    public boolean isGround() {
        return ground;
    }
    public void setGround(boolean ground) {
        this.ground = ground;
    }
    public boolean isOverTruss() {
        return overTruss;
    }
    public void setOverTruss(boolean overTruss) {
        this.overTruss = overTruss;
    }
    public boolean isLow() {
        return low;
    }
    public void setLow(boolean low) {
        this.low = low;
    }
    public boolean isHigh() {
        return high;
    }
    public void setHigh(boolean high) {
        this.high = high;
    }
    public boolean equals(MatchScouting2014 data) {
        return super.equals(data) &&
                getHotShots()==data.getHotShots() &&
                getShotsMade()==data.getShotsMade() &&
                getShotsMissed()==data.getShotsMissed() &&
                getMoveForward()==data.getMoveForward() &&
                isShooter()==data.isShooter() &&
                isCatcher()==data.isCatcher() &&
                isPasser()==data.isPasser() &&
                getDriveTrainRating()==data.getDriveTrainRating() &&
                getBallAccuracyRating()==data.getBallAccuracyRating() &&
                isGround()==data.isGround() &&
                isOverTruss()==data.isOverTruss() &&
                isLow()==data.isLow() &&
                isHigh()==data.isHigh();
    }

    public AOurAllianceData validate() {
        return Query.one(MatchScouting2014.class, "SELECT * FROM " + TAG + " WHERE " + MATCH + "=? AND " + TEAM + "=? LIMIT 1", getMatch().getId(), getCompetitionTeam().getId()).get();
    }
    public boolean empty() {
        return (null==getMatch() || getMatch().empty())
                && (null==getCompetitionTeam() || getCompetitionTeam().empty())
                && isAlliance()==false
                && getNotes()==""
                && getHotShots()==0
                && getShotsMade()==0
                && getShotsMissed()==0
                && getMoveForward()==0
                && isShooter()==false
                && isCatcher()==false
                && isPasser()==false
                && getDriveTrainRating()==0
                && getBallAccuracyRating()==0
                && isGround()==false
                && isOverTruss()==false
                && isLow()==false
                && isHigh()==false;
    }
}
