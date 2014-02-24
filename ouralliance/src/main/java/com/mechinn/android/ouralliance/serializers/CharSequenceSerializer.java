package com.mechinn.android.ouralliance.serializers;

import android.content.ContentValues;
import android.database.Cursor;

import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by emilsjolander on 27/12/13.
 */
public class CharSequenceSerializer implements TypeSerializer<CharSequence> {

    @Override
    public CharSequence unpack(Cursor c, String name) {
        return c.getString(c.getColumnIndexOrThrow(name));
    }

    @Override
    public void pack(CharSequence object, ContentValues cv, String name) {
        if(null!=object) {
            cv.put(name, object.toString());
        } else {
            cv.put(name, new String());
        }
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.TEXT;
    }

}
