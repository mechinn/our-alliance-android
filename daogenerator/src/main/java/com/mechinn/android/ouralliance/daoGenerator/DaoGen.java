package com.mechinn.android.ouralliance.daoGenerator;

import java.io.IOException;
import java.io.ObjectStreamClass;
import java.util.UUID;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DaoGen {
    String schemaName = "com.mechinn.android.ouralliance.greenDao";
    String folder;
    Entity team;
    Entity multimedia;
    Schema schema;

    public static void main(String args[]) throws Exception {
        new DaoGen(args[0]);
    }

    public DaoGen(String folder) throws Exception {
        this.folder = folder;
        schema = new Schema(1, this.schemaName);
        schema.setDefaultJavaPackageTest(this.schemaName + ".test");
        schema.setDefaultJavaPackageDao(this.schemaName + ".dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        team = schema.addEntity("Team");
        team.addIdProperty();
        team.addDateProperty("modified").notNull();
        team.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        team.implementsInterface("Comparable<Team>");
        team.addIntProperty("teamNumber").notNull().unique();
        team.addStringProperty("nickname");
        team.addStringProperty("website");
        team.addStringProperty("locality");
        team.addStringProperty("region");
        team.addStringProperty("country");
        team.addStringProperty("key");
        team.addIntProperty("rookieYear");

        multimedia = schema.addEntity("Multimedia");
        multimedia.addIdProperty();
        multimedia.addDateProperty("modified").notNull();
        multimedia.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        multimedia.implementsInterface("Comparable<Multimedia>");
        multimedia.addIntProperty("teamNumber").notNull().unique();
        multimedia.addStringProperty("type");
        multimedia.addStringProperty("key");
        this.frc2014();
        new DaoGenerator().generateAll(schema, folder);
    }

    public void frc2014() throws Exception {
        Entity event = schema.addEntity("Event2014");
        event.addIdProperty();
        event.addDateProperty("modified").notNull();
        event.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        event.implementsInterface("Comparable<Event2014>");
        event.addStringProperty("shortName").notNull();
        event.addStringProperty("eventCode").notNull().unique();
        event.addIntProperty("eventType").notNull();
        event.addIntProperty("eventDistrict").notNull();
        event.addIntProperty("year").notNull();
        event.addStringProperty("venueAddress");
        event.addStringProperty("website");
        event.addDateProperty("startDate");
        event.addDateProperty("endDate");
        event.addBooleanProperty("official");

        Entity eventTeam = schema.addEntity("EventTeam2014");
        eventTeam.addIdProperty();
        eventTeam.addDateProperty("modified").notNull();
        eventTeam.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        eventTeam.implementsInterface("Comparable<EventTeam2014>");
        Property eventTeamEvent = eventTeam.addLongProperty("EventId").notNull().getProperty();
        eventTeam.addToOne(event,eventTeamEvent);
        Property eventTeamTeam = eventTeam.addLongProperty("teamId").notNull().getProperty();
        eventTeam.addToOne(team, eventTeamTeam);
        eventTeam.addIntProperty("rank");
        eventTeam.addBooleanProperty("scouted");

        Index eventTeamUnique = new Index();
        eventTeamUnique.addProperty(eventTeamEvent);
        eventTeamUnique.addProperty(eventTeamTeam);
        eventTeamUnique.makeUnique();
        eventTeam.addIndex(eventTeamUnique);

        ToMany eventToEventTeam = event.addToMany(eventTeam, eventTeamEvent);
        eventToEventTeam.setName("teams");

        ToMany teamToEventTeam = team.addToMany(eventTeam, eventTeamEvent);
        teamToEventTeam.setName("events");

        Entity match = schema.addEntity("Match2014");
        match.addIdProperty();
        match.addDateProperty("modified").notNull();
        match.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        match.implementsInterface("Comparable<Match2014>");
        match.addStringProperty("compLevel").notNull();
        match.addIntProperty("setNumber");
        match.addDateProperty("time");
        match.addIntProperty("redScore");
        match.addIntProperty("blueScore");
        Property matchNum = match.addIntProperty("matchNum").getProperty();
        Property eventMatch = match.addLongProperty("eventId").getProperty();
        match.addToOne(event,eventMatch);
        Property matchMultimedia = match.addLongProperty("multimediaId").getProperty();

        Index matchUnique = new Index();
        matchUnique.addProperty(eventMatch);
        matchUnique.addProperty(matchNum);
        matchUnique.makeUnique();
        match.addIndex(matchUnique);

        ToMany eventToMatches = event.addToMany(match, eventMatch);
        eventToMatches.setName("matches");
        eventToMatches.orderAsc(matchNum);

        Entity teamScouting = schema.addEntity("TeamScouting2014");
        teamScouting.addIdProperty();
        teamScouting.addDateProperty("modified").notNull();
        teamScouting.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        teamScouting.implementsInterface("Comparable<TeamScouting2014>");
        Property teamScoutingteam = teamScouting.addLongProperty("teamId").notNull().unique().getProperty();
        teamScouting.addToOne(team,teamScoutingteam);
        teamScouting.addStringProperty("notes");
        teamScouting.addStringProperty("orientation");
        teamScouting.addStringProperty("driveTrain");
        teamScouting.addDoubleProperty("width");
        teamScouting.addDoubleProperty("length");
        teamScouting.addDoubleProperty("heightShooter");
        teamScouting.addDoubleProperty("heightMax");
        teamScouting.addIntProperty("shooterType");
        teamScouting.addBooleanProperty("lowGoal");
        teamScouting.addBooleanProperty("highGoal");
        teamScouting.addDoubleProperty("shootingDistance");
        teamScouting.addBooleanProperty("passGround");
        teamScouting.addBooleanProperty("passAir");
        teamScouting.addBooleanProperty("passTruss");
        teamScouting.addBooleanProperty("pickupGround");
        teamScouting.addBooleanProperty("pickupCatch");
        teamScouting.addBooleanProperty("pusher");
        teamScouting.addBooleanProperty("blocker");
        teamScouting.addDoubleProperty("humanPlayer");
        teamScouting.addBooleanProperty("noAuto");
        teamScouting.addBooleanProperty("driveAuto");
        teamScouting.addBooleanProperty("lowAuto");
        teamScouting.addBooleanProperty("highAuto");
        teamScouting.addBooleanProperty("hotAuto");
        Property teamMultimedia = teamScouting.addLongProperty("multimediaId").getProperty();

        Entity wheel = schema.addEntity("Wheel2014");
        wheel.addIdProperty();
        wheel.addDateProperty("modified").notNull();
        wheel.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        wheel.implementsInterface("Comparable<Wheel2014>");
        Property teamScoutingWheel = wheel.addLongProperty("teamId").notNull().getProperty();
        wheel.addToOne(teamScouting,teamScoutingWheel);
        Property wheelType = wheel.addStringProperty("wheelType").notNull().getProperty();
        wheel.addDoubleProperty("wheelSize").notNull();
        wheel.addIntProperty("wheelCount").notNull();

        Index wheelUnique = new Index();
        wheelUnique.addProperty(teamScoutingWheel);
        wheelUnique.addProperty(wheelType);
        wheelUnique.makeUnique();
        wheel.addIndex(wheelUnique);

        ToMany teamScoutingToWheels = teamScouting.addToMany(wheel, teamScoutingWheel);
        teamScoutingToWheels.setName("wheels");
        teamScoutingToWheels.orderAsc(wheelType);

        Entity matchScouting = schema.addEntity("MatchScouting2014");
        matchScouting.addIdProperty();
        matchScouting.addDateProperty("modified").notNull();
        matchScouting.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        matchScouting.implementsInterface("Comparable<MatchScouting2014>");
        Property matchScoutingmatch = matchScouting.addLongProperty("matchId").notNull().getProperty();
        matchScouting.addToOne(match,matchScoutingmatch);
        Property matchScoutingteam = matchScouting.addLongProperty("teamId").notNull().getProperty();
        matchScouting.addToOne(teamScouting,matchScoutingteam);
        matchScouting.addBooleanProperty("alliance");
        matchScouting.addIntProperty("position");
        matchScouting.addStringProperty("notes");
        matchScouting.addIntProperty("hotShots");
        matchScouting.addIntProperty("shotsMade");
        matchScouting.addIntProperty("shotsMissed");
        matchScouting.addDoubleProperty("moveForward");
        matchScouting.addBooleanProperty("shooter");
        matchScouting.addBooleanProperty("catcher");
        matchScouting.addBooleanProperty("passer");
        matchScouting.addDoubleProperty("driveTrainRating");
        matchScouting.addDoubleProperty("ballAccuracyRating");
        matchScouting.addBooleanProperty("ground");
        matchScouting.addBooleanProperty("overTruss");
        matchScouting.addBooleanProperty("low");
        matchScouting.addBooleanProperty("high");

        Index matchScoutingUnique = new Index();
        matchScoutingUnique.addProperty(matchScoutingmatch);
        matchScoutingUnique.addProperty(matchScoutingteam);
        matchScoutingUnique.makeUnique();
        matchScouting.addIndex(matchScoutingUnique);

        ToMany matchToMatchScouting = match.addToMany(matchScouting, matchScoutingmatch);
        matchToMatchScouting.setName("teams");
        matchToMatchScouting.orderAsc(matchScoutingmatch);

        ToMany teamScoutingToMatches = teamScouting.addToMany(matchScouting, matchScoutingteam);
        teamScoutingToMatches.setName("matches");
        teamScoutingToMatches.orderAsc(matchScoutingmatch);

        ToMany matchToMultimedia = match.addToMany(multimedia, matchMultimedia);
        matchToMultimedia.setName("multimedia");

        ToMany teamToMultimedia = teamScouting.addToMany(multimedia, teamMultimedia);
        teamToMultimedia.setName("multimedia");
    }
}