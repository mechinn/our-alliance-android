package com.mechinn.android.ouralliance.data;

import java.util.List;

public class Alliances {
    private Alliance blue;
    private Alliance red;
    public Alliances() {}
    public Alliance getBlue() {
        return blue;
    }
    public Alliance getRed() {
        return red;
    }
    public class Alliance {
        private int score;
        private List<String> teams;
        public Alliance() {}
        public int getScore() {
            return score;
        }
        public List<String> getTeams() {
            return teams;
        }
    }
}
