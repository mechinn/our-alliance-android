package com.mechinn.android.ouralliance.serializers;

import android.content.ContentValues;
import android.database.Cursor;
import com.mechinn.android.ouralliance.data.Match;
import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by mechinn on 3/10/14.
 */
public class MatchTypeSerializer implements TypeSerializer<Match.Type> {

    @Override
    public Match.Type unpack(Cursor c, String name) {
        int value = c.getInt(c.getColumnIndexOrThrow(name));
        return Match.getType(value);
    }

    @Override
    public void pack(Match.Type object, ContentValues cv, String name) {
        cv.put(name, object.getValue());
    }

    @Override
    public String toSql(Match.Type object) {
        return Integer.toString(object.getValue());
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.INTEGER;
    }
}