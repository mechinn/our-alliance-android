package com.mechinn.android.ouralliance.data.comparator;

import java.util.Comparator;

import com.mechinn.android.ouralliance.data.TeamScouting;

public class TeamScoutingTeamNumberOrder implements Comparator<TeamScouting> {
	public int compare(TeamScouting lhs, TeamScouting rhs) {
		return lhs.getTeam().compareTo(rhs.getTeam());
	}
}
