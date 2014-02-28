package com.mechinn.android.ouralliance.serializers;

import android.content.ContentValues;
import android.util.Log;

import com.mechinn.android.ouralliance.data.AOurAllianceData;

import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by mechinn on 2/18/14.
 */
public abstract class AOurAllianceDataSerializer<A extends AOurAllianceData> implements TypeSerializer<A> {
    public static final String TAG = AOurAllianceDataSerializer.class.getSimpleName();

    @Override
    public void pack(A object, ContentValues cv, String name) {
        Log.d(TAG,name+" "+object);
        cv.put(name, object.getId());
    }

    @Override
    public String toSql(A object) {
        return ""+object.getId();
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.INTEGER;
    }
}
