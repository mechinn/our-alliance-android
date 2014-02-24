package com.mechinn.android.ouralliance.serializers;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.Season;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class SeasonSerializer extends AOurAllianceDataSerializer<Season> {
    public static final String TAG = SeasonSerializer.class.getSimpleName();

    @Override
    public Season unpack(Cursor c, String name) {
        return Query.one(Season.class, "select * from Season where _id=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
