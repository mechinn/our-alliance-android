package com.mechinn.android.ouralliance;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;

import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.serializers.*;
import com.mechinn.android.ouralliance.serializers.frc2014.MatchScouting2014Serializer;
import com.mechinn.android.ouralliance.serializers.frc2014.TeamScouting2014Serializer;

import se.emilsjolander.sprinkles.*;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    public static final String TAG = "OurAlliance";
    BluetoothReceive receiver;
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
        sprinkles.registerType(Match.Type.class,new MatchTypeSerializer());
        sprinkles.registerType(MatchScouting2014.class, new MatchScouting2014Serializer());

        Migration v1 = new Migration();
        v1.createTable(Team.class);
        v1.createTable(Season.class);
        v1.createTable(Competition.class);
        v1.createTable(CompetitionTeam.class);
        v1.createTable(TeamScoutingWheel.class);
        v1.createTable(TeamScouting2014.class);
        v1.createTable(Match.class);
        v1.createTable(MatchScouting2014.class);
        sprinkles.addMigration(v1);

        String v2matchScouting2014Columns = MatchScouting2014._ID
                +","+MatchScouting2014.MODIFIED
                +","+MatchScouting2014.MATCH
                +","+MatchScouting2014.TEAM
                +","+MatchScouting2014.NOTES;
        Migration v2 = new Migration();
        v2.renameTable(MatchScouting2014.TAG, MatchScouting2014.TAG+"_OLD");
        v2.createTable(MatchScouting2014.class);
        v2.addRawStatement("INSERT INTO " + MatchScouting2014.TAG + "("+ v2matchScouting2014Columns +") SELECT " + v2matchScouting2014Columns + " FROM " + MatchScouting2014.TAG + "_OLD;");
        v2.addRawStatement("DROP TABLE " + MatchScouting2014.TAG + "_OLD;");
        sprinkles.addMigration(v2);

        String v3TeamScoutingWheelColumns = TeamScoutingWheel._ID
                +","+TeamScoutingWheel.MODIFIED
                +","+TeamScoutingWheel.SEASON
                +","+TeamScoutingWheel.TEAM
                +","+TeamScoutingWheel.TYPE
                +","+TeamScoutingWheel.SIZE
                +","+TeamScoutingWheel.COUNT;
        String v3TeamScouting2014Columns = TeamScouting2014._ID
                +","+TeamScouting2014.MODIFIED
                +","+TeamScouting2014.TEAM
                +","+TeamScouting2014.NOTES
                +","+TeamScouting2014.ORIENTATION
                +","+TeamScouting2014.DRIVETRAIN
                +","+TeamScouting2014.WIDTH
                +","+TeamScouting2014.LENGTH
                +","+TeamScouting2014.HEIGHTSHOOTER
                +","+TeamScouting2014.HEIGHTMAX
                +","+TeamScouting2014.SHOOTERTYPE
                +","+TeamScouting2014.LOWGOAL
                +","+TeamScouting2014.HIGHGOAL
                +","+TeamScouting2014.HOTGOAL
                +","+TeamScouting2014.SHOOTINGDISTANCE
                +","+TeamScouting2014.PASSGROUND
                +","+TeamScouting2014.PASSAIR
                +","+TeamScouting2014.PASSTRUSS
                +","+TeamScouting2014.PICKUPGROUND
                +","+TeamScouting2014.PICKUPCATCH
                +","+TeamScouting2014.PUSHER
                +","+TeamScouting2014.BLOCKER
                +","+TeamScouting2014.HUMANPLAYER
                +","+TeamScouting2014.AUTOMODE;
        String v3CompetitionColumns = Competition._ID
                +","+Competition.MODIFIED
                +","+Competition.SEASON
                +","+Competition.NAME
                +","+Competition.CODE;
        String v3SeasonColumns = Season._ID
                +","+Season.MODIFIED
                +","+Season.YEAR
                +","+Season.TITLE;
        String v3TeamColumns = Team._ID
                +","+Team.MODIFIED
                +","+Team.NUMBER
                +","+Team.NAME;
        Migration v3 = new Migration();
        v3.dropTable(MatchScouting2014.class);
        v3.dropTable(Match.class);
        v3.createTable(MatchScouting2014.class);
        v3.createTable(Match.class);
        v3.renameTable(TeamScoutingWheel.TAG, TeamScoutingWheel.TAG + "_OLD");
        v3.createTable(TeamScoutingWheel.class);
        v3.addRawStatement("INSERT INTO " + TeamScoutingWheel.TAG + "(" + v3TeamScoutingWheelColumns + ") SELECT " + v3TeamScoutingWheelColumns + " FROM " + TeamScoutingWheel.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + TeamScoutingWheel.TAG + "_OLD;");
        v3.renameTable(TeamScouting2014.TAG, TeamScouting2014.TAG + "_OLD");
        v3.createTable(TeamScouting2014.class);
        v3.addRawStatement("INSERT INTO " + TeamScouting2014.TAG + "(" + v3TeamScouting2014Columns + ") SELECT " + v3TeamScouting2014Columns + " FROM " + TeamScouting2014.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + TeamScouting2014.TAG + "_OLD;");
        v3.renameTable(Competition.TAG, Competition.TAG + "_OLD");
        v3.createTable(Competition.class);
        v3.addRawStatement("INSERT INTO " + Competition.TAG + "(" + v3CompetitionColumns + ") SELECT " + v3CompetitionColumns + " FROM " + Competition.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + Competition.TAG + "_OLD;");
        v3.renameTable(Season.TAG, Season.TAG + "_OLD");
        v3.createTable(Season.class);
        v3.addRawStatement("INSERT INTO " + Season.TAG + "(" + v3SeasonColumns + ") SELECT " + v3SeasonColumns + " FROM " + Season.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + Season.TAG + "_OLD;");
        v3.renameTable(Team.TAG, Team.TAG + "_OLD");
        v3.createTable(Team.class);
        v3.addRawStatement("INSERT INTO " + Team.TAG + "("+ v3TeamColumns +") SELECT " + v3TeamColumns + " FROM " + Team.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + Team.TAG + "_OLD;");
        sprinkles.addMigration(v3);

        Log.d(TAG,"starting bluetooth receiver");
        receiver = new BluetoothReceive(this,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(null!=msg.getData().getString(Import.RESULT)) {
                    Toast.makeText(OurAlliance.this,msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(null!=msg.getData().getString(BluetoothReceive.STATUS)) {
                    Toast.makeText(OurAlliance.this,msg.getData().getString(BluetoothReceive.STATUS),Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        }));
        startBluetoothReceiver();
    }

    public void startBluetoothReceiver() {
        receiver.start();
    }

    public void stopBluetoothReceiver() {
        receiver.interrupt();
    }
}
