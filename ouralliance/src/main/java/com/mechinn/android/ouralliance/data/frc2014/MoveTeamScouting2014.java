package com.mechinn.android.ouralliance.data.frc2014;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.processor.FmtTeam;
import com.mechinn.android.ouralliance.processor.ParseTeam;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import se.emilsjolander.sprinkles.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mechinn on 3/10/14.
 */
public class MoveTeamScouting2014 {
    public static final String TAG = "MoveTeamScouting2014";

    public static final String[] FIELD_MAPPING = new String[] {
            TeamScouting2014.TAG+TeamScouting2014.MODIFIED
            ,TeamScouting2014.TEAM
            ,TeamScouting2014.NOTES
            ,TeamScouting2014.ORIENTATION
            ,TeamScouting2014.DRIVETRAIN
            ,TeamScouting2014.WIDTH
            ,TeamScouting2014.LENGTH
            ,TeamScouting2014.HEIGHTSHOOTER
            ,TeamScouting2014.HEIGHTMAX
            ,TeamScouting2014.SHOOTERTYPE
            ,TeamScouting2014.LOWGOAL
            ,TeamScouting2014.HIGHGOAL
            ,TeamScouting2014.SHOOTINGDISTANCE
            ,TeamScouting2014.PASSGROUND
            ,TeamScouting2014.PASSAIR
            ,TeamScouting2014.PASSTRUSS
            ,TeamScouting2014.PICKUPGROUND
            ,TeamScouting2014.PICKUPCATCH
            ,TeamScouting2014.PUSHER
            ,TeamScouting2014.BLOCKER
            ,TeamScouting2014.HUMANPLAYER
            ,TeamScouting2014.NOAUTO
            ,TeamScouting2014.DRIVEAUTO
            ,TeamScouting2014.LOWAUTO
            ,TeamScouting2014.HIGHAUTO
            ,TeamScouting2014.HOTAUTO
            ,TeamScoutingWheel.TAG+TeamScoutingWheel.MODIFIED
            ,TeamScoutingWheel.TYPE
            ,TeamScoutingWheel.SIZE
            ,TeamScoutingWheel.COUNT
            ,CompetitionTeam.TAG+CompetitionTeam.MODIFIED
            ,CompetitionTeam.RANK
            ,CompetitionTeam.SCOUTED
    };

    public static final CellProcessor[] writeProcessor = new CellProcessor[] {
            new FmtDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,new FmtTeam()                      //TEAM
            ,null                               //NOTES
            ,null                               //ORIENTATION
            ,null                               //DRIVETRAIN
            ,null                               //WIDTH
            ,null                               //LENGTH
            ,null                               //HEIGHTSHOOTER
            ,null                               //HEIGHTMAX
            ,new HashMapper(TeamScouting2014.shootersWrite,0)    //SHOOTERTYPE
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //LOWGOAL
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //HIGHGOAL
            ,null                               //SHOOTINGDISTANCE
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PASSGROUND
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PASSAIR
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PASSTRUSS
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PICKUPGROUND
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PICKUPCATCH
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //PUSHER
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //BLOCKER
            ,null                               //HUMANPLAYER
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //NOAUTO
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //DRIVEAUTO
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //LOWAUTO
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //HIGHAUTO
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //HOTAUTO
            ,new FmtDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,null
            ,null
            ,null
            ,new FmtDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,null
            ,new FmtBool(TeamScouting2014.TRUE,TeamScouting2014.FALSE)            //HOTAUTO
    };

    public static final CellProcessor[] readProcessor = new CellProcessor[] {
            new ParseDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,new ParseTeam()                    //TEAM
            ,null                               //NOTES
            ,null                               //ORIENTATION
            ,null                               //DRIVETRAIN
            ,new ParseDouble()                  //WIDTH
            ,new ParseDouble()                  //LENGTH
            ,new ParseDouble()                  //HEIGHTSHOOTER
            ,new ParseDouble()                  //HEIGHTMAX
            ,new HashMapper(TeamScouting2014.shootersRead,0)     //SHOOTERTYPE
            ,new ParseBool()          //LOWGOAL
            ,new ParseBool()          //HIGHGOAL
            ,new ParseDouble()                  //SHOOTINGDISTANCE
            ,new ParseBool()          //PASSGROUND
            ,new ParseBool()          //PASSAIR
            ,new ParseBool()          //PASSTRUSS
            ,new ParseBool()          //PICKUPGROUND
            ,new ParseBool()          //PICKUPCATCH
            ,new ParseBool()          //PUSHER
            ,new ParseBool()          //BLOCKER
            ,new ParseDouble()                  //HUMANPLAYER
            ,new ParseBool()          //NOAUTO
            ,new ParseBool()          //DRIVEAUTO
            ,new ParseBool()          //LOWAUTO
            ,new ParseBool()          //HIGHAUTO
            ,new ParseBool()          //HOTAUTO
            ,new ParseDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,null
            ,new ParseDouble()
            ,new ParseInt()
            ,new ParseDate("yyyy.MM.dd.HH.mm.ss")  //MODIFIED
            ,new ParseInt()
            ,new ParseBool()
    };

    private TeamScouting2014 team;
    private TeamScoutingWheel wheel;
    private Date modified;
    private int rank;
    private boolean scouted;
    public MoveTeamScouting2014() {
        this.team = new TeamScouting2014();
        this.wheel = new TeamScoutingWheel();
        this.modified = new Date(0);
    }
    public MoveTeamScouting2014(TeamScouting2014 team,CompetitionTeam competitionTeam) {
        this.team = team;
        this.wheel = new TeamScoutingWheel();
        this.modified = competitionTeam.getModified();
        this.rank = competitionTeam.getRank();
        this.scouted = competitionTeam.isScouted();
    }
    public MoveTeamScouting2014(TeamScouting2014 team, TeamScoutingWheel wheel,CompetitionTeam competitionTeam) {
        this.team = team;
        this.wheel = wheel;
        this.modified = competitionTeam.getModified();
        this.rank = competitionTeam.getRank();
        this.scouted = competitionTeam.isScouted();
    }
    public Date getTeamScouting2014modified() {
        return team.getModified();
    }
    public void setTeamScouting2014modified(Date modified) {
        this.team.setModified(modified);
    }
    public Team getTeam() {
        return team.getTeam();
    }
    public void setTeam(Team team) {
        this.team.setTeam(team);
        this.wheel.setTeam(team);
    }
    public String getNotes() {
        return team.getNotes();
    }
    public void setNotes(String notes) {
        team.setNotes(notes);
    }
    public void setNotes(CharSequence notes) {
        team.setNotes(notes);
    }
    public String getOrientation() {
        return team.getOrientation();
    }
    public void setOrientation(String orientation) {
        team.setOrientation(orientation);
    }
    public void setOrientation(CharSequence orientation) {
        team.setOrientation(orientation);
    }
    public String getDriveTrain() {
        return team.getDriveTrain();
    }
    public void setDriveTrain(String driveTrain) {
        team.setDriveTrain(driveTrain);
    }
    public void setDriveTrain(CharSequence driveTrain) {
        team.setDriveTrain(driveTrain);
    }
    public double getWidth() {
        return team.getWidth();
    }
    public void setWidth(double width) {
        team.setWidth(width);
    }
    public double getLength() {
        return team.getLength();
    }
    public void setLength(double length) {
        team.setLength(length);
    }
    public double getHeightShooter() {
        return team.getHeightShooter();
    }
    public void setHeightShooter(double heightShooter) {
        team.setHeightShooter(heightShooter);
    }
    public double getHeightMax() {
        return team.getHeightMax();
    }
    public void setHeightMax(double heightMax) {
        team.setHeightMax(heightMax);
    }
    public int getShooterType() {
        return team.getShooterType();
    }
    public void setShooterType(int shooterType) {
        team.setShooterType(shooterType);
    }
    public boolean isLowGoal() {
        return team.isLowGoal();
    }
    public void setLowGoal(boolean lowGoal) {
        team.setLowGoal(lowGoal);
    }
    public boolean isHighGoal() {
        return team.isHighGoal();
    }
    public void setHighGoal(boolean highGoal) {
        team.setHighGoal(highGoal);
    }
    public double getShootingDistance() {
        return team.getShootingDistance();
    }
    public void setShootingDistance(double shootingDistance) {
        team.setShootingDistance(shootingDistance);
    }
    public boolean isPassGround() {
        return team.isPassGround();
    }
    public void setPassGround(boolean passGround) {
        team.setPassGround(passGround);
    }
    public boolean isPassAir() {
        return team.isPassAir();
    }
    public void setPassAir(boolean passAir) {
        team.setPassAir(passAir);
    }
    public boolean isPassTruss() {
        return team.isPassTruss();
    }
    public void setPassTruss(boolean passTruss) {
        team.setPassTruss(passTruss);
    }
    public boolean isPickupGround() {
        return team.isPickupGround();
    }
    public void setPickupGround(boolean pickupGround) {
        team.setPickupGround(pickupGround);
    }
    public boolean isPickupCatch() {
        return team.isPickupCatch();
    }
    public void setPickupCatch(boolean pickupCatch) {
        team.setPickupCatch(pickupCatch);
    }
    public boolean isPusher() {
        return team.isPusher();
    }
    public void setPusher(boolean pusher) {
        team.setPusher(pusher);
    }
    public boolean isBlocker() {
        return team.isBlocker();
    }
    public void setBlocker(boolean blocker) {
        team.setBlocker(blocker);
    }
    public double getHumanPlayer() {
        return team.getHumanPlayer();
    }
    public void setHumanPlayer(double humanPlayer) {
        team.setHumanPlayer(humanPlayer);
    }
    public boolean isNoAuto() {
        return team.isNoAuto();
    }
    public void setNoAuto(boolean noAuto) {
        team.setNoAuto(noAuto);
    }
    public boolean isDriveAuto() {
        return team.isDriveAuto();
    }
    public void setDriveAuto(boolean driveAuto) {
        team.setDriveAuto(driveAuto);
    }
    public boolean isLowAuto() {
        return team.isLowAuto();
    }
    public void setLowAuto(boolean lowAuto) {
        team.setLowAuto(lowAuto);
    }
    public boolean isHighAuto() {
        return team.isHighAuto();
    }
    public void setHighAuto(boolean highAuto) {
        team.setHighAuto(highAuto);
    }
    public boolean isHotAuto() {
        return team.isHotAuto();
    }
    public void setHotAuto(boolean hotAuto) {
        team.setHotAuto(hotAuto);
    }
    public String getWheelType() {
        return wheel.getWheelType();
    }
    public Date getTeamScoutingWheelmodified() {
        return wheel.getModified();
    }
    public void setTeamScoutingWheelmodified(Date modified) {
        this.wheel.setModified(modified);
    }
    public void setWheelType(String type) {
        wheel.setWheelType(type);
    }
    public void setWheelType(CharSequence type) {
        wheel.setWheelType(type);
    }
    public double getWheelSize() {
        return wheel.getWheelSize();
    }
    public void setWheelSize(double size) {
        wheel.setWheelSize(size);
    }
    public int getWheelCount() {
        return wheel.getWheelCount();
    }
    public void setWheelCount(int count) {
        wheel.setWheelCount(count);
    }
    public Date getCompetitionTeammodified() {
        return modified;
    }
    public void setCompetitionTeammodified(Date modified) {
        this.modified = modified;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public boolean isScouted() {
        return scouted;
    }
    public void setScouted(boolean scouted) {
        this.scouted = scouted;
    }
    public String toString() {
        return "team: "+team.toString()+" wheel: "+wheel.toString();
    }
    public void save(Transaction t, int season, Competition competition) {
        team.save(t);
        if(wheel.getWheelType()!="") {
            wheel.setYear(season);
            wheel.save(t);
        }
        new CompetitionTeam(modified,competition,team.getTeam(),rank,scouted).save(t);
    }
}
