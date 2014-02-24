package com.mechinn.android.ouralliance;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.serializers.CharSequenceSerializer;
import com.mechinn.android.ouralliance.serializers.CompetitionSerializer;
import com.mechinn.android.ouralliance.serializers.CompetitionTeamSerializer;
import com.mechinn.android.ouralliance.serializers.MatchSerializer;
import com.mechinn.android.ouralliance.serializers.SeasonSerializer;
import com.mechinn.android.ouralliance.serializers.TeamScoutingWheelSerializer;
import com.mechinn.android.ouralliance.serializers.TeamSerializer;
import com.mechinn.android.ouralliance.serializers.frc2014.MatchScouting2014Serializer;
import com.mechinn.android.ouralliance.serializers.frc2014.TeamScouting2014Serializer;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        sprinkles.registerType(CharSequence.class, new CharSequenceSerializer());
        sprinkles.registerType(Team.class, new TeamSerializer());
        sprinkles.registerType(Season.class, new SeasonSerializer());
        sprinkles.registerType(Competition.class, new CompetitionSerializer());
        sprinkles.registerType(CompetitionTeam.class, new CompetitionTeamSerializer());
        sprinkles.registerType(TeamScoutingWheel.class, new TeamScoutingWheelSerializer());
        sprinkles.registerType(TeamScouting2014.class, new TeamScouting2014Serializer());
        sprinkles.registerType(Match.class, new MatchSerializer());
        sprinkles.registerType(MatchScouting2014.class, new MatchScouting2014Serializer());

        Migration initialMigration = new Migration();
        initialMigration.createTable(Team.class);
        initialMigration.createTable(Season.class);
        initialMigration.createTable(Competition.class);
        initialMigration.createTable(CompetitionTeam.class);
        initialMigration.createTable(TeamScoutingWheel.class);
        initialMigration.createTable(TeamScouting2014.class);
        initialMigration.createTable(Match.class);
        initialMigration.createTable(MatchScouting2014.class);
        sprinkles.addMigration(initialMigration);
    }
}
