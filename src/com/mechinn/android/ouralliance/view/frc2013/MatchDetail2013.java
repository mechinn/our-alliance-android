package com.mechinn.android.ouralliance.view.frc2013;

import java.sql.SQLException;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mechinn.android.ouralliance.data.frc2013.Match2013;
import com.mechinn.android.ouralliance.data.source.frc2013.Match2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.MatchDetailFragment;

public class MatchDetail2013 extends MatchDetailFragment<Match2013, Match2013DataSource> {
	public final static String TAG = MatchDetail2013.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		Log.wtf(TAG, "2013 match scouting not implemented yet");
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void setView() {
		super.setView();
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
	}
	
	@Override
	public Match2013 setMatchFromCursor(Cursor cursor) throws OurAllianceException, SQLException {
		return Match2013DataSource.getSingle(cursor);
	}

	@Override
	public Match2013DataSource createDataSouce() {
		return new Match2013DataSource(this.getActivity());
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_MATCH:
				return this.getDataSource().get(this.getMatchId());
		}
		return null;
	}
}
