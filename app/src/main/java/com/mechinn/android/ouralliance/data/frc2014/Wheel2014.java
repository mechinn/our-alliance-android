package com.mechinn.android.ouralliance.data.frc2014;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.Wheel;

@Table(name=Wheel2014.TAG, id = Wheel2014.ID)
public class Wheel2014 extends Wheel<TeamScouting2014> {
    public final static String TAG = "Wheel2014";
    public Wheel2014() {}
    public Wheel2014(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
}
