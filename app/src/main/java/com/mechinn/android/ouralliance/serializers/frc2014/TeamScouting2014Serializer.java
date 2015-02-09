package com.mechinn.android.ouralliance.serializers.frc2014;

import android.database.Cursor;

import com.mechinn.android.ouralliance.serializers.AOurAllianceDataSerializer;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class TeamScouting2014Serializer extends AOurAllianceDataSerializer<TeamScouting2014> {
    public static final String TAG = "TeamScouting2014Serializer";

    @Override
    public TeamScouting2014 unpack(Cursor c, String name) {
        return Query.one(TeamScouting2014.class, "select * from "+TeamScouting2014.TAG+" where "+TeamScouting2014._ID+"=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
