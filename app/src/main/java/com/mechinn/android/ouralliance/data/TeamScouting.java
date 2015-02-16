package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.greenDao.MatchScouting2014;
import com.mechinn.android.ouralliance.greenDao.Multimedia;
import com.mechinn.android.ouralliance.greenDao.Team;
import com.mechinn.android.ouralliance.greenDao.Wheel;

import java.util.List;

/**
 * Created by mechinn on 2/14/2015.
 */
public abstract class TeamScouting extends OurAllianceObject {
    public abstract long getTeamId();
    public abstract void setTeamId(long teamId);
    public abstract String getNotes();
    public abstract void setNotes(String notes);
    public abstract Team getTeam();
    public abstract void setTeam(Team team);
    public abstract List<Wheel> getWheels();
    public abstract void resetWheels();
    public abstract List<? extends MatchScouting> getMatches();
    public abstract void resetMatches();
    public abstract Long getMultimediaId();
    public abstract void setMultimediaId(Long multimediaId);
    public abstract List<Multimedia> getMultimedia();
    public abstract void resetMultimedia();
}
