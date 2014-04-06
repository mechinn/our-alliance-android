package com.mechinn.android.ouralliance;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.rest.thebluealliance.AlarmTheBlueAlliance;
import com.mechinn.android.ouralliance.serializers.*;
import com.mechinn.android.ouralliance.serializers.frc2014.MatchScouting2014Serializer;
import com.mechinn.android.ouralliance.serializers.frc2014.TeamScouting2014Serializer;

import se.emilsjolander.sprinkles.*;

/**
 * Created by mechinn on 2/18/14.
 */
public class OurAlliance extends Application {
    public static final String TAG = "OurAlliance";
    private BluetoothReceive receiver;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        stopBluetoothReceiver();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        startBluetoothReceiver();
                        break;
                }
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        sprinkles.registerType(CharSequence.class, new CharSequenceSerializer());
        sprinkles.registerType(Team.class, new TeamSerializer());
        sprinkles.registerType(Competition.class, new CompetitionSerializer());
        sprinkles.registerType(CompetitionTeam.class, new CompetitionTeamSerializer());
        sprinkles.registerType(TeamScoutingWheel.class, new TeamScoutingWheelSerializer());
        sprinkles.registerType(TeamScouting2014.class, new TeamScouting2014Serializer());
        sprinkles.registerType(Match.class, new MatchSerializer());
        sprinkles.registerType(Match.Type.class,new MatchTypeSerializer());
        sprinkles.registerType(MatchScouting2014.class, new MatchScouting2014Serializer());

        Migration v1 = new Migration();
        v1.createTable(Team.class);
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

        String v3TeamColumns = Team._ID
                +","+Team.MODIFIED
                +","+Team.NUMBER
                +","+Team.NAME;
        Migration v3 = new Migration();
        v3.dropTable(MatchScouting2014.class);
        v3.dropTable(Match.class);
        v3.createTable(MatchScouting2014.class);
        v3.createTable(Match.class);
        v3.renameTable(Team.TAG, Team.TAG + "_OLD");
        v3.createTable(Team.class);
        v3.addRawStatement("INSERT INTO " + Team.TAG + "("+ v3TeamColumns +") SELECT " + v3TeamColumns + " FROM " + Team.TAG + "_OLD;");
        v3.addRawStatement("DROP TABLE " + Team.TAG + "_OLD;");
        sprinkles.addMigration(v3);

        Migration v4 = new Migration();
        sprinkles.addMigration(v4);

        String v5teamScouting2014Columns = TeamScouting2014._ID
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
                +","+TeamScouting2014.SHOOTINGDISTANCE
                +","+TeamScouting2014.PASSGROUND
                +","+TeamScouting2014.PASSAIR
                +","+TeamScouting2014.PASSTRUSS
                +","+TeamScouting2014.PICKUPGROUND
                +","+TeamScouting2014.PICKUPCATCH
                +","+TeamScouting2014.PUSHER
                +","+TeamScouting2014.BLOCKER
                +","+TeamScouting2014.HUMANPLAYER;
        Migration v5 = new Migration();
        v5.renameTable(TeamScouting2014.TAG, TeamScouting2014.TAG+"_OLD");
        v5.createTable(TeamScouting2014.class);
        v5.addRawStatement("INSERT INTO " + TeamScouting2014.TAG + "("+ v5teamScouting2014Columns +") SELECT " + v5teamScouting2014Columns + " FROM " + TeamScouting2014.TAG + "_OLD;");
        v5.addRawStatement("DROP TABLE " + TeamScouting2014.TAG + "_OLD;");
        sprinkles.addMigration(v5);

        Migration v6 = new Migration();
        String v6competitionColumns = Competition._ID
                + "," + Competition.MODIFIED
                + "," + Competition.NAME
                + "," + Competition.CODE;
        v6.renameTable(Competition.TAG, Competition.TAG + "_OLD");
        v6.createTable(Competition.class);
        v6.addRawStatement("INSERT INTO " + Competition.TAG + "(" + v6competitionColumns + "," + Competition.YEAR + "," + Competition.OFFICIAL + ") SELECT " + v6competitionColumns +",2014,1 FROM " + Competition.TAG + "_OLD;");
        v6.addRawStatement("DROP TABLE " + Competition.TAG + "_OLD;");

        String v6TeamScoutingWheelColumns = TeamScoutingWheel._ID
                + "," + TeamScoutingWheel.MODIFIED
                + "," + TeamScoutingWheel.TEAM
                + "," + TeamScoutingWheel.TYPE
                + "," + TeamScoutingWheel.SIZE
                + "," + TeamScoutingWheel.COUNT;
        v6.renameTable(TeamScoutingWheel.TAG, TeamScoutingWheel.TAG + "_OLD");
        v6.createTable(TeamScoutingWheel.class);
        v6.addRawStatement("INSERT INTO " + TeamScoutingWheel.TAG + "(" + v6TeamScoutingWheelColumns + "," + TeamScoutingWheel.YEAR + ") SELECT " + v6TeamScoutingWheelColumns + ",2014 FROM " + TeamScoutingWheel.TAG + "_OLD;");
        v6.addRawStatement("DROP TABLE " + TeamScoutingWheel.TAG + "_OLD;");

        String v6matchColumns = Match._ID
                +","+Match.MODIFIED
                +","+Match.COMPETITION
                +","+Match.NUMBER
                +","+Match.REDSCORE
                +","+Match.BLUESCORE
                +","+Match.MATCHTYPE
                +","+Match.MATCHSET;
        v6.renameTable(Match.TAG, Match.TAG+"_OLD");
        v6.createTable(Match.class);
        v6.addRawStatement("INSERT INTO " + Match.TAG + "("+ v6matchColumns +") SELECT " + v6matchColumns +" FROM " + Match.TAG + "_OLD;");
        v6.addRawStatement("DROP TABLE " + Match.TAG + "_OLD;");
        v6.addRawStatement("DROP TABLE IF EXISTS Season;");
        sprinkles.addMigration(v6);

        String v7matchScouting2014Columns = MatchScouting2014._ID
                +","+MatchScouting2014.MODIFIED
                +","+MatchScouting2014.MATCH
                +","+MatchScouting2014.NOTES
                +","+MatchScouting2014.HOTSHOTS
                +","+MatchScouting2014.SHOTSMADE
                +","+MatchScouting2014.SHOTSMISSED
                +","+MatchScouting2014.MOVEFWD
                +","+MatchScouting2014.SHOOTER
                +","+MatchScouting2014.CATCHER
                +","+MatchScouting2014.PASSER
                +","+MatchScouting2014.DRIVETRAIN
                +","+MatchScouting2014.BALLACCURACY
                +","+MatchScouting2014.GROUND
                +","+MatchScouting2014.OVERTRUSS
                +","+MatchScouting2014.LOW
                +","+MatchScouting2014.HIGH;
        Migration v7 = new Migration();
        v7.renameTable(MatchScouting2014.TAG, MatchScouting2014.TAG+"_OLD");
        v7.createTable(MatchScouting2014.class);
        v7.addRawStatement("INSERT INTO " + MatchScouting2014.TAG + "("+ v7matchScouting2014Columns +","+MatchScouting2014.COMPETITIONTEAM+") SELECT " + v7matchScouting2014Columns +","+MatchScouting2014.TEAM+ " FROM " + MatchScouting2014.TAG + "_OLD;");
        v7.addRawStatement("DROP TABLE " + MatchScouting2014.TAG + "_OLD;");
        sprinkles.addMigration(v7);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);
        if(null==receiver && null!=BluetoothAdapter.getDefaultAdapter() && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            startBluetoothReceiver();
        }

//        new AlarmTheBlueAlliance(this.getApplicationContext()).setAlarm();
    }

    public void startBluetoothReceiver() {
        receiver = new BluetoothReceive(this,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(null!=msg.getData().getString(Import.RESULT)) {
                    Toast.makeText(OurAlliance.this,msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(null!=msg.getData().getString(BluetoothReceive.STATUS)) {
                    Toast.makeText(OurAlliance.this,"Bluetooth was disabled on device",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        }));
        receiver.start();
        Toast.makeText(OurAlliance.this,"Listening for bluetooth connections",Toast.LENGTH_SHORT).show();
    }

    public void stopBluetoothReceiver() {
        if(null!=receiver) {
            receiver.interrupt();
        }
        Toast.makeText(OurAlliance.this,"Bluetooth was disabled on device",Toast.LENGTH_SHORT).show();
    }
    public boolean isBluetoothReceiverState() {
        if(null!=receiver) {
            return receiver.isServerOn();
        }
        return false;
    }
}
