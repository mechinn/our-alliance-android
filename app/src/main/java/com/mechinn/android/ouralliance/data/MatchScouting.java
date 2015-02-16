package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.greenDao.Match;
import com.mechinn.android.ouralliance.greenDao.TeamScouting2014;

import java.util.Date;

/**
 * Created by mechinn on 2/14/2015.
 */
public abstract class MatchScouting extends OurAllianceObject {
    public abstract long getMatchId();
    public abstract void setMatchId(long matchId);
    public abstract long getTeamId();
    public abstract void setTeamId(long teamId);
    public abstract Boolean getAlliance();
    public abstract void setAlliance(Boolean alliance);
    public abstract Integer getPosition();
    public abstract void setPosition(Integer position);
    public abstract String getNotes();
    public abstract void setNotes(String notes);
    public abstract Match getMatch();
    public abstract void setMatch(Match match);
    public abstract TeamScouting getTeamScouting();
    public abstract void setTeamScouting(TeamScouting teamScouting);
}
