package com.mechinn.android.ouralliance.data.frc2015;

/**
 * Created by mechinn on 3/26/14.
 */


public enum Sort2015 {
    RANK
    ,NUMBER
    ,ORIENTATION
    ,DRIVE_TRAIN
    ,WIDTH
    ,LENGTH
    ,HEIGHT
    ,COOP
    ,DRIVER_EXPERIENCE
    ,PICKUP_MECHANISM
    ,MAX_TOTE_STACK
    ,MAX_CONTAINER_STACK
    ,MAX_TOTES_AND_CONTAINER_LITTER
    ,HUMAN_PLAYER
    ,LANDFILL_AUTO
    ,AUTONOMOUS;

    public String toString() {
        switch(this) {
            case NUMBER:
                return "Team Number";
            case ORIENTATION:
                return "Orientation";
            case DRIVE_TRAIN:
                return "Drive Train";
            case WIDTH:
                return "Width";
            case LENGTH:
                return "Length";
            case HEIGHT:
                return "Height";
            case COOP:
                return "Co-op";
            case DRIVER_EXPERIENCE:
                return "Driver Experience";
            case PICKUP_MECHANISM:
                return "Pickup Mechanism";
            case MAX_TOTE_STACK:
                return "Max Tote Stack";
            case MAX_CONTAINER_STACK:
                return "Max Tote Stack Can Place Container";
            case MAX_TOTES_AND_CONTAINER_LITTER:
                return "Max Totes and Container to Place Litter In";
            case HUMAN_PLAYER:
                return "Human Player";
            case LANDFILL_AUTO:
                return "Landfill totes moved in Autonomous";
            case AUTONOMOUS:
                return "Autonomous";
            default:
                return "User Order";
        }
    }
}