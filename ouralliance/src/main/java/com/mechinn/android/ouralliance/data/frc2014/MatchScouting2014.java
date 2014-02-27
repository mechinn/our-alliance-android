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

@Table("MatchScouting2014")
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class MatchScouting2014 extends MatchScouting implements Comparable<MatchScouting>  {
	public static final String TAG = MatchScouting2014.class.getSimpleName();
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

    @Column(HOTSHOTS)
    private int hotShots;
    @Column(SHOTSMADE)
    private int shotsMade;
    @Column(SHOTSMISSED)
    private int shotsMissed;
    @Column(MOVEFWD)
    private float moveFwd;
    @Column(SHOOTER)
    private boolean shooter;
    @Column(CATCHER)
    private boolean catcher;
    @Column(PASSER)
    private boolean passer;
    @Column(DRIVETRAIN)
    private float driveTrain;
    @Column(BALLACCURACY)
    private float ballAccuracy;
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
	public MatchScouting2014(Match match, CompetitionTeam team) {
        super(match, team);
	}
	public MatchScouting2014(long id, Date mod, Match match, CompetitionTeam team, CharSequence notes) {
		super(id, mod, match, team, notes);
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
    public float getMoveFwd() {
        return moveFwd;
    }
    public void setMoveFwd(float moveFwd) {
        this.moveFwd = moveFwd;
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
    public float getDriveTrain() {
        return driveTrain;
    }
    public void setDriveTrain(float driveTrain) {
        this.driveTrain = driveTrain;
    }
    public float getBallAccuracy() {
        return ballAccuracy;
    }
    public void setBallAccuracy(float ballAccuracy) {
        this.ballAccuracy = ballAccuracy;
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
                getMoveFwd()==data.getMoveFwd() &&
                isShooter()==data.isShooter() &&
                isCatcher()==data.isCatcher() &&
                isPasser()==data.isPasser() &&
                getDriveTrain()==data.getDriveTrain() &&
                getBallAccuracy()==data.getBallAccuracy() &&
                isGround()==data.isGround() &&
                isOverTruss()==data.isOverTruss() &&
                isLow()==data.isLow() &&
                isHigh()==data.isHigh();
    }
}
