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

    public static void main(String args[]) throws Exception {
        String exportFolder = args[0];
        new DaoGen(exportFolder).frc2014();
    }

    public DaoGen(String folder){
        this.folder = folder;
    }

    public void frc2014() throws Exception {
        String schemaName = this.schemaName+".frc2014";
        Schema schema = new Schema(1, schemaName);
        schema.setDefaultJavaPackageTest(schemaName+".test");
        schema.setDefaultJavaPackageDao(schemaName+".dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        Entity team = schema.addEntity("Team");
        team.addIdProperty();
        team.addDateProperty("modified").notNull();
        team.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        team.addIntProperty("teamNumber").notNull().unique();
        team.addStringProperty("nickname");

        Entity competition = schema.addEntity("Competition");
        competition.addIdProperty();
        competition.addDateProperty("modified").notNull();
        competition.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        Property nameCompetitition = competition.addStringProperty("name").notNull().getProperty();
        competition.addStringProperty("eventCode").notNull().unique();
        competition.addStringProperty("location");
        Property officialCompetitition = competition.addBooleanProperty("official").getProperty();

        Entity competitionTeam = schema.addEntity("CompetitionTeam");
        competitionTeam.addIdProperty();
        competitionTeam.addDateProperty("modified").notNull();
        competitionTeam.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        Property competitionTeamCompetition = competitionTeam.addLongProperty("competition").notNull().getProperty();
        competitionTeam.addToOne(competition,competitionTeamCompetition);
        Property competitionTeamTeam = competitionTeam.addLongProperty("team").notNull().getProperty();
        competitionTeam.addToOne(team, competitionTeamTeam);
        competitionTeam.addIntProperty("rank");
        competitionTeam.addBooleanProperty("scouted");

        Index competitionTeamUnique = new Index();
        competitionTeamUnique.addProperty(competitionTeamCompetition);
        competitionTeamUnique.addProperty(competitionTeamTeam);
        competitionTeamUnique.makeUnique();
        competitionTeam.addIndex(competitionTeamUnique);

        Entity match = schema.addEntity("Match");
        match.addIdProperty();
        match.addDateProperty("modified").notNull();
        match.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        match.addIntProperty("matchType").notNull();
        match.addIntProperty("matchSet");
        match.addIntProperty("redScore");
        match.addIntProperty("blueScore");
        match.addStringProperty("compLevel").notNull();
        Property matchNum = match.addStringProperty("matchNum").getProperty();
        Property competitionMatch = match.addLongProperty("competition").getProperty();
        match.addToOne(competition,competitionMatch);

        Index matchUnique = new Index();
        matchUnique.addProperty(competitionMatch);
        matchUnique.addProperty(matchNum);
        matchUnique.makeUnique();
        match.addIndex(matchUnique);

        ToMany competitionToMatches = competition.addToMany(match, competitionMatch);
        competitionToMatches.setName("matches");
        competitionToMatches.orderAsc(matchNum);

        Entity teamScouting = schema.addEntity("TeamScouting");
        teamScouting.addIdProperty();
        teamScouting.addDateProperty("modified").notNull();
        teamScouting.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        Property teamScoutingteam = teamScouting.addLongProperty("team").notNull().unique().getProperty();
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

        Entity wheel = schema.addEntity("Wheel");
        wheel.addIdProperty();
        wheel.addDateProperty("modified").notNull();
        wheel.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        Property teamScoutingWheel = wheel.addLongProperty("team").notNull().getProperty();
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

        Entity matchScouting = schema.addEntity("MatchScouting");
        matchScouting.addIdProperty();
        matchScouting.addDateProperty("modified").notNull();
        matchScouting.setSuperclass("com.mechinn.android.ouralliance.OurAllianceObject");
        Property matchScoutingmatch = matchScouting.addLongProperty("match").notNull().getProperty();
        matchScouting.addToOne(match,matchScoutingmatch);
        Property matchScoutingteam = matchScouting.addLongProperty("team").notNull().getProperty();
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

        new DaoGenerator().generateAll(schema, folder);
    }
}