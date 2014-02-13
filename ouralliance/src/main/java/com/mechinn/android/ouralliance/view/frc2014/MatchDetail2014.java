package com.mechinn.android.ouralliance.view.frc2014;

import java.sql.SQLException;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.frc2014.Match2014;
import com.mechinn.android.ouralliance.data.source.frc2014.Match2014DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.MatchDetailFragment;

public class MatchDetail2014 extends MatchDetailFragment<Match2014, Match2014DataSource> {
	public final static String TAG = MatchDetail2014.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
		Log.wtf(TAG, "2014 match scouting not implemented yet");
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
	public Match2014 setMatchFromCursor(Cursor cursor) throws OurAllianceException, SQLException {
		return Match2014DataSource.getSingle(cursor);
	}

	@Override
	public Match2014DataSource createDataSouce() {
		return new Match2014DataSource(this.getActivity());
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_MATCH:
				return this.getDataSource().get(this.getMatchId());
		}
		return null;
	}
}
