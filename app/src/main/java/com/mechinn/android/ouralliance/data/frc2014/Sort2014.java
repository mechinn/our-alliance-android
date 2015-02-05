package com.mechinn.android.ouralliance.data.frc2014;

import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

/**
 * Created by mechinn on 3/26/14.
 */


public enum Sort2014 {
    RANK
    ,NUMBER
    ,ORIENTATION
    ,DRIVETRAIN
    ,WIDTH
    ,LENGTH
    ,HEIGHTSHOOTER
    ,HEIGHTMAX
    ,SHOOTERTYPE
    ,SHOOTGOAL
    ,SHOOTINGDISTANCE
    ,PASS
    ,PICKUP
    ,PUSHER
    ,BLOCKER
    ,HUMANPLAYER
    ,AUTONOMOUS;

    public String toString() {
        switch(this) {
            case NUMBER:
                return "Team Number";
            case ORIENTATION:
                return "Orientation";
            case DRIVETRAIN:
                return "Drive Train";
            case WIDTH:
                return "Width";
            case LENGTH:
                return "Length";
            case HEIGHTSHOOTER:
                return "Shooter Height";
            case HEIGHTMAX:
                return "Max Height";
            case SHOOTERTYPE:
                return "Shooter Type";
            case SHOOTGOAL:
                return "Shootable Goal";
            case SHOOTINGDISTANCE:
                return "Shooting Distance";
            case PASS:
                return "Passing";
            case PICKUP:
                return "Pickup";
            case PUSHER:
                return "Pusher";
            case BLOCKER:
                return "Blocker";
            case HUMANPLAYER:
                return "Human Player";
            case AUTONOMOUS:
                return "Autonomous";
            default:
                return "User Order";
        }
    }

    public String getValue() {
        switch(this) {
            case NUMBER:
                return Team.TAG+"."+Team.NUMBER;
            case ORIENTATION:
                return TeamScouting2014.TAG+"."+TeamScouting2014.ORIENTATION;
            case DRIVETRAIN:
                return TeamScouting2014.TAG+"."+TeamScouting2014.DRIVETRAIN;
            case HEIGHTSHOOTER:
                return TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHTSHOOTER+" DESC";
            case HEIGHTMAX:
                return TeamScouting2014.TAG+"."+TeamScouting2014.HEIGHTMAX+" DESC";
            case SHOOTERTYPE:
                return TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTERTYPE+" DESC";
            case SHOOTGOAL:
                return TeamScouting2014.TAG+"."+TeamScouting2014.HIGHGOAL+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.LOWGOAL+" DESC";
            case SHOOTINGDISTANCE:
                return TeamScouting2014.TAG+"."+TeamScouting2014.SHOOTINGDISTANCE+" DESC";
            case PASS:
                return TeamScouting2014.TAG+"."+TeamScouting2014.PASSTRUSS+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.PASSAIR+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.PASSGROUND+" DESC";
            case PICKUP:
                return TeamScouting2014.TAG+"."+TeamScouting2014.PICKUPCATCH+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.PICKUPGROUND+" DESC";
            case PUSHER:
                return TeamScouting2014.TAG+"."+TeamScouting2014.PUSHER;
            case BLOCKER:
                return TeamScouting2014.TAG+"."+TeamScouting2014.BLOCKER;
            case HUMANPLAYER:
                return TeamScouting2014.TAG+"."+TeamScouting2014.HUMANPLAYER;
            case AUTONOMOUS:
                return TeamScouting2014.TAG+"."+TeamScouting2014.HOTAUTO+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.HIGHAUTO+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.LOWAUTO+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.DRIVEAUTO+" DESC,"+TeamScouting2014.TAG+"."+TeamScouting2014.NOAUTO+" DESC";
            default:
                return CompetitionTeam.TAG+"."+CompetitionTeam.RANK;
        }
    }
}