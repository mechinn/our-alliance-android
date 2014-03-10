package com.mechinn.android.ouralliance.serializers;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.Competition;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class CompetitionSerializer extends AOurAllianceDataSerializer<Competition> {
    public static final String TAG = "CompetitionSerializer";

    @Override
    public Competition unpack(Cursor c, String name) {
        return Query.one(Competition.class, "select * from Competition where _id=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
