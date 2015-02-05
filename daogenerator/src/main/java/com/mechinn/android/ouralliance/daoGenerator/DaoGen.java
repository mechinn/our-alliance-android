package com.mechinn.android.ouralliance.daoGenerator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
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
        String schema2014Name = this.schemaName+".frc2014";
        Schema schema2014 = new Schema(1, schema2014Name);
        schema2014.setDefaultJavaPackageTest(schema2014Name+".test");
        schema2014.setDefaultJavaPackageDao(schema2014Name+".dao");
        schema2014.enableKeepSectionsByDefault();
        schema2014.enableActiveEntitiesByDefault();

        Entity team = schema2014.addEntity("Team");
        team.addIdProperty();
        team.addDateProperty("modified");
        team.addIntProperty("teamNumber");
        team.addStringProperty("nickname");

        Entity competition = schema2014.addEntity("Competition");
        competition.addIdProperty();
        competition.addDateProperty("modified");
        competition.addStringProperty("name");
        competition.addStringProperty("eventCode");
        competition.addStringProperty("location");
        competition.addBooleanProperty("official");

        Entity competitionTeam = schema2014.addEntity("CompetitionTeam");
        competitionTeam.addIdProperty();
        competitionTeam.addDateProperty("modified");
        Property competitionTeamCompetition = competitionTeam.addLongProperty("competition").getProperty();
        competitionTeam.addToOne(competition,competitionTeamCompetition);
        Property competitionTeamTeam = competitionTeam.addLongProperty("team").getProperty();
        competitionTeam.addToOne(team, competitionTeamTeam);
        competitionTeam.addIntProperty("rank");
        competitionTeam.addBooleanProperty("scouted");

        Entity match = schema2014.addEntity("Match");
        match.addIdProperty();
        match.addDateProperty("modified");
        match.addIntProperty("matchType");
        match.addIntProperty("matchSet");
        match.addIntProperty("redScore");
        match.addIntProperty("blueScore");
        match.addStringProperty("compLevel");
        Property matchNum = match.addStringProperty("matchNum").getProperty();
        Property competitionMatch = match.addLongProperty("competition").getProperty();
        match.addToOne(competition,competitionMatch);

        ToMany competitionToMatches = competition.addToMany(match, competitionMatch);
        competitionToMatches.setName("matches");
        competitionToMatches.orderAsc(matchNum);

        Entity teamScouting = schema2014.addEntity("TeamScouting");
        teamScouting.addIdProperty();
        teamScouting.addDateProperty("modified");
        Property teamScoutingteam = teamScouting.addLongProperty("team").getProperty();
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

        Entity wheel = schema2014.addEntity("Wheel");
        wheel.addIdProperty();
        wheel.addDateProperty("modified");
        Property teamScoutingWheel = wheel.addLongProperty("team").getProperty();
        wheel.addToOne(teamScouting,teamScoutingWheel);
        Property wheelType = wheel.addStringProperty("wheelType").getProperty();
        wheel.addDoubleProperty("wheelSize");
        wheel.addIntProperty("wheelCount");

        ToMany teamScoutingToWheels = teamScouting.addToMany(wheel, teamScoutingWheel);
        teamScoutingToWheels.setName("wheels");
        teamScoutingToWheels.orderAsc(wheelType);

        Entity matchScouting = schema2014.addEntity("MatchScouting");
        matchScouting.addIdProperty();
        matchScouting.addDateProperty("modified");
        Property matchScoutingmatch = matchScouting.addLongProperty("match").getProperty();
        matchScouting.addToOne(match,matchScoutingmatch);
        Property matchScoutingteam = matchScouting.addLongProperty("team").getProperty();
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

        ToMany matchToMatchScouting = match.addToMany(matchScouting, matchScoutingmatch);
        matchToMatchScouting.setName("teams");
        matchToMatchScouting.orderAsc(matchScoutingmatch);

        ToMany teamScoutingToMatches = teamScouting.addToMany(matchScouting, matchScoutingteam);
        teamScoutingToMatches.setName("matches");
        teamScoutingToMatches.orderAsc(matchScoutingmatch);

        new DaoGenerator().generateAll(schema2014, folder);
    }
}