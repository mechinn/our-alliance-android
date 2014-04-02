package com.mechinn.android.ouralliance.serializers;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.Team;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class TeamSerializer extends AOurAllianceDataSerializer<Team> {
    public static final String TAG = "TeamSerializer";

    @Override
    public Team unpack(Cursor c, String name) {
        return Query.one(Team.class, "select * from "+Team.TAG+" where "+Team._ID+"=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
