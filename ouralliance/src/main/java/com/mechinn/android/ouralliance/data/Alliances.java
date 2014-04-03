package com.mechinn.android.ouralliance.data;

import android.util.Log;
import com.mechinn.android.ouralliance.rest.TheBlueAlliance;
import se.emilsjolander.sprinkles.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 4/2/14.
 */
public class Alliances {
    public static final String TAG = "Alliances";
    private Alliance red;
    private Alliance blue;
    public Alliance getRed() {
        return red;
    }
    public Alliance getBlue() {
        return blue;
    }
    public class Alliance {
        public static final String TAG = "Alliance";
        private int score = -1;
        private List<String> teams;
        private List<Team> foundTeams;

        public int getScore() {
            return score;
        }

        public List<Team> getFoundTeams() {
            if(null!=foundTeams) {
                return foundTeams;
            }
            foundTeams = new ArrayList<Team>();
            for(String team : teams) {
                Log.d(TAG, "team number: " + Integer.parseInt(team.substring(3)));
                Team foundTeam = Query.one(Team.class, "SELECT * FROM " + Team.TAG + " WHERE " + Team.NUMBER + "=?", Integer.parseInt(team.substring(3))).get();
                if(null==foundTeam) {
                    Log.d(TAG,"unable to find locally "+team);
                    foundTeam = TheBlueAlliance.getService().getTeam(team);
                    foundTeam.save();
                }
                foundTeams.add(foundTeam);
            }
            return foundTeams;
        }
    }
}
