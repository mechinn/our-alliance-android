package com.mechinn.android.ouralliance.serializers;

import android.content.ContentValues;
import android.database.Cursor;

import com.mechinn.android.ouralliance.data.Match;

import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by mechinn on 2/18/14.
 */
public class MatchSerializer extends AOurAllianceDataSerializer<Match> {
    public static final String TAG = "MatchSerializer";

    @Override
    public Match unpack(Cursor c, String name) {
        return Query.one(Match.class, "select * from Match where _id=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
