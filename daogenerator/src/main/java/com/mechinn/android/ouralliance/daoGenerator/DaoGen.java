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
    Entity event;
    Entity eventTeam;
    Entity match;
    Property matchMultimedia;
    Entity wheel;
    Property teamScoutingWheel;
    Property wheelType;
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
        team.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        team.implementsInterface("Comparable<Team>");
        team.implementsSerializable();
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
        multimedia.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        multimedia.implementsInterface("Comparable<Multimedia>");
        multimedia.implementsSerializable();
        multimedia.addIntProperty("teamNumber").notNull().unique();
        multimedia.addStringProperty("type");
        multimedia.addStringProperty("key");

        event = schema.addEntity("Event");
        event.addIdProperty();
        event.addDateProperty("modified").notNull();
        event.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        event.implementsInterface("Comparable<Event>");
        event.implementsSerializable();
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

        eventTeam = schema.addEntity("EventTeam");
        eventTeam.addIdProperty();
        eventTeam.addDateProperty("modified").notNull();
        eventTeam.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        eventTeam.implementsInterface("Comparable<EventTeam>");
        eventTeam.implementsSerializable();
        Property eventTeamEvent = eventTeam.addLongProperty("eventId").notNull().getProperty();
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

        match = schema.addEntity("Match");
        match.addIdProperty();
        match.addDateProperty("modified").notNull();
        match.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        match.implementsInterface("Comparable<Match>");
        match.implementsSerializable();
        match.addIntProperty("compLevel").notNull();
        match.addIntProperty("setNumber");
        match.addDateProperty("time");
        match.addIntProperty("redScore");
        match.addIntProperty("blueScore");
        Property matchNum = match.addIntProperty("matchNum").getProperty();
        Property eventMatch = match.addLongProperty("eventId").getProperty();
        match.addToOne(event,eventMatch);
        matchMultimedia = match.addLongProperty("matchMultimediaId").getProperty();

        Index matchUnique = new Index();
        matchUnique.addProperty(eventMatch);
        matchUnique.addProperty(matchNum);
        matchUnique.makeUnique();
        match.addIndex(matchUnique);

        ToMany eventToMatches = event.addToMany(match, eventMatch);
        eventToMatches.setName("matches");
        eventToMatches.orderAsc(matchNum);

        wheel = schema.addEntity("Wheel");
        wheel.addIdProperty();
        wheel.addDateProperty("modified").notNull();
        wheel.setSuperclass("com.mechinn.android.ouralliance.data.OurAllianceObject");
        wheel.implementsInterface("Comparable<Wheel>");
        wheel.implementsSerializable();
        teamScoutingWheel = wheel.addLongProperty("teamId").notNull().getProperty();
        wheelType = wheel.addStringProperty("wheelType").notNull().getProperty();
        wheel.addDoubleProperty("wheelSize").notNull();
        wheel.addIntProperty("wheelCount").notNull();

        Index wheelUnique = new Index();
        wheelUnique.addProperty(teamScoutingWheel);
        wheelUnique.addProperty(wheelType);
        wheelUnique.makeUnique();
        wheel.addIndex(wheelUnique);

        this.frc2014();
        new DaoGenerator().generateAll(schema, folder);
    }

    public void frc2014() throws Exception {
        Entity teamScouting = schema.addEntity("TeamScouting2014");
        teamScouting.addIdProperty();
        teamScouting.addDateProperty("modified").notNull();
        teamScouting.setSuperclass("com.mechinn.android.ouralliance.data.TeamScouting");
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
        Property teamMultimedia = teamScouting.addLongProperty("teamMultimediaId").getProperty();

        wheel.addToOne(teamScouting,teamScoutingWheel);

        ToMany teamScoutingToWheels = teamScouting.addToMany(wheel, teamScoutingWheel);
        teamScoutingToWheels.setName("wheels");
        teamScoutingToWheels.orderAsc(wheelType);

        Entity matchScouting = schema.addEntity("MatchScouting2014");
        matchScouting.addIdProperty();
        matchScouting.addDateProperty("modified").notNull();
        matchScouting.setSuperclass("com.mechinn.android.ouralliance.data.MatchScouting");
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
        Property matchMultimedia = matchScouting.addLongProperty("multimediaId").getProperty();

        Index matchScoutingUnique = new Index();
        matchScoutingUnique.addProperty(matchScoutingmatch);
        matchScoutingUnique.addProperty(matchScoutingteam);
        matchScoutingUnique.makeUnique();
        matchScouting.addIndex(matchScoutingUnique);

        ToMany matchToMatchScouting = match.addToMany(matchScouting, matchScoutingmatch);
        matchToMatchScouting.setName("teams2014");
        matchToMatchScouting.orderAsc(matchScoutingmatch);

        ToMany teamScoutingToMatches = teamScouting.addToMany(matchScouting, matchScoutingteam);
        teamScoutingToMatches.setName("matches2014");
        teamScoutingToMatches.orderAsc(matchScoutingmatch);

        ToMany matchToMultimedia = matchScouting.addToMany(multimedia, matchMultimedia);
        matchToMultimedia.setName("multimedia");

        ToMany teamToMultimedia = teamScouting.addToMany(multimedia, teamMultimedia);
        teamToMultimedia.setName("multimedia");
    }
}