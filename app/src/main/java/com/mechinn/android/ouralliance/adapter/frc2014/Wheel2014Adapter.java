package com.mechinn.android.ouralliance.adapter.frc2014;

import android.content.Context;
import android.database.Cursor;

import com.mechinn.android.ouralliance.adapter.WheelAdapter;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;

/**
 * Created by mechinn on 2/23/15.
 */
public class Wheel2014Adapter extends WheelAdapter<Wheel2014> {
    public Wheel2014Adapter(Context context, Cursor wheels) {
        super(context, wheels);
    }

    @Override
    public Wheel2014 wheelFromCursor(Cursor cursor) {
        return new Wheel2014(cursor);
    }
}
