package com.mechinn.android.ouralliance.serializers;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.TeamScoutingWheel;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class TeamScoutingWheelSerializer extends AOurAllianceDataSerializer<TeamScoutingWheel> {
    public static final String TAG = TeamScoutingWheelSerializer.class.getSimpleName();

    @Override
    public TeamScoutingWheel unpack(Cursor c, String name) {
        return Query.one(TeamScoutingWheel.class, "select * from TeamScoutingWheel where _id=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
