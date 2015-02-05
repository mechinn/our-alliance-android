package com.mechinn.android.ouralliance.serializers;

import android.database.Cursor;

import com.mechinn.android.ouralliance.data.CompetitionTeam;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 2/18/14.
 */
public class CompetitionTeamSerializer extends AOurAllianceDataSerializer<CompetitionTeam> {
    public static final String TAG = "CompetitionTeamSerializer";

    @Override
    public CompetitionTeam unpack(Cursor c, String name) {
        return Query.one(CompetitionTeam.class, "select * from "+CompetitionTeam.TAG+" where "+CompetitionTeam._ID+"=?",c.getLong(c.getColumnIndexOrThrow(name))).get();
    }
}
