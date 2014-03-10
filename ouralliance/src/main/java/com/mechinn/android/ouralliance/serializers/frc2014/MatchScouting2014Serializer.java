package com.mechinn.android.ouralliance.serializers.frc2014;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.serializers.AOurAllianceDataSerializer;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class MatchScouting2014Serializer extends AOurAllianceDataSerializer<MatchScouting2014> {
    public static final String TAG = "MatchScouting2014Serializer";

    @Override
    public MatchScouting2014 unpack(Cursor c, String name) {
        return Query.one(MatchScouting2014.class, "select * from MatchScouting2014 where _id=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
