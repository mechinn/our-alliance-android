package com.mechinn.android.ouralliance.view.frc2014;

import java.sql.SQLException;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2013.Match2013;
import com.mechinn.android.ouralliance.data.source.frc2013.Match2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.MatchListFragment;

public class MatchList2014 extends MatchListFragment<Match2013, Match2013DataSource> {
	@Override
	public Match2013DataSource createDataSouce() {
		return new Match2013DataSource(this.getActivity());
	}
	
	public void insertMatch(Match match) {
		Log.d(TAG, "id: "+match);
		//try inserting the team first in case it doesnt exist
		try {
			match.setCompetition(this.getComp());
			if(match.getType()>0) {
				match.setOf(1);
				this.getScouting().insertBase(match);
				match.setOf(2);
				this.getScouting().insertBase(match);
				match.setOf(3);
				this.getScouting().insertBase(match);
			} else {
				this.getScouting().insertBase(match);
			}
		} catch (OurAllianceException e) {
			Log.d(TAG, e.getMessage());
			Toast.makeText(this.getActivity(), "Cannot create match without "+e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage(), e);
			Toast.makeText(this.getActivity(), "This match number already exists", Toast.LENGTH_SHORT).show();
		}
	}

	public void updateMatch(Match match) {
		Log.d(TAG, "id: "+match);
		try {
			getScouting().updateBase(match);
		} catch (OurAllianceException e) {
			Toast.makeText(this.getActivity(), "Cannot update match without "+e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {
			Toast.makeText(this.getActivity(), "Match does not exist, creating it and adding to competition", Toast.LENGTH_SHORT).show();
			insertMatch(match);
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case LOADER_MATCHES:
				return this.getScouting().getAllMatches(getCompId());
			default:
				return super.onCreateLoader(id, args);
		}
	}

}
