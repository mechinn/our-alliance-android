package com.mechinn.android.ouralliance.view.frc2013;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamDetailFragment;
import com.mechinn.android.ouralliance.view.TeamListActivity;

public class TeamDetail2013 extends TeamDetailFragment<TeamScouting2013, TeamScouting2013DataSource> {
	private static final String TAG = "TeamDetail2013";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setDataSource(new TeamScouting2013DataSource(this.getActivity()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		
		return rootView;
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
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
		}
		return super.onCreateLoader(id, bundle);
	}
	
	@Override
	public TeamScouting2013 setScoutingFromCursor(Cursor cursor) throws OurAllianceException {
		return TeamScouting2013DataSource.getSingle(cursor);
	}
}
