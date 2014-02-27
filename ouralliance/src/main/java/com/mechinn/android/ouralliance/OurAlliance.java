package com.mechinn.android.ouralliance;

import android.app.Application;
import android.provider.BaseColumns;

import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.AOurAllianceData;
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

        Migration v16 = new Migration();
        v16.createTable(Team.class);
        v16.createTable(Season.class);
        v16.createTable(Competition.class);
        v16.createTable(CompetitionTeam.class);
        v16.createTable(TeamScoutingWheel.class);
        v16.createTable(TeamScouting2014.class);
        v16.createTable(Match.class);
        v16.createTable(MatchScouting2014.class);
        sprinkles.addMigration(v16);


        String v17matchScouting2014Columns = AOurAllianceData._ID+","+AOurAllianceData.MODIFIED+","+MatchScouting2014.MATCH+","+MatchScouting2014.TEAM+","+MatchScouting2014.NOTES;
        Migration v17 = new Migration();
        v17.renameTable(MatchScouting2014.TAG, MatchScouting2014.TAG+"_OLD");
        v17.createTable(MatchScouting2014.class);
        v17.addRawStatement("INSERT INTO " + MatchScouting2014.TAG + "("+ v17matchScouting2014Columns +") SELECT " + v17matchScouting2014Columns + " FROM " + MatchScouting2014.TAG + "_OLD;");
        v17.addRawStatement("DROP TABLE " + MatchScouting2014.TAG + "_OLD;");
        sprinkles.addMigration(v17);
    }
}
