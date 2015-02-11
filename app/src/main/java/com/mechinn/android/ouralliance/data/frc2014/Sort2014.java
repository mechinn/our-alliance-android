package com.mechinn.android.ouralliance.data.frc2014;

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
}