package com.mechinn.android.ouralliance;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class AddTeams implements LoaderCallbacks<Cursor> {
	private static final int INITALIZETEAMS = 100000;
	private Context context;
	private Prefs prefs;
	private TeamDataSource teamData;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	
	public AddTeams(Context context) {
		this.context = context;
		this.prefs = new Prefs(context);
		this.teamData = new TeamDataSource(context);
		this.seasonData = new SeasonDataSource(context);
		this.competitionData = new CompetitionDataSource(context);
		teamData.insert(new Team(869, "Power Cord"));
		teamData.insert(new Team(1676, ""));
		teamData.insert(new Team(3637, "The Daleks"));
		teamData.insert(new Team(25, "Raider Robotix"));
		teamData.insert(new Team(75, "RoboRaiders"));
		teamData.insert(new Team(11, "Chief Delphi"));
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case INITALIZETEAMS:
				return seasonData.get(2013);
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}

}
