package com.mechinn.android.ouralliance.view.frc2013;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.TeamScoutingWheelDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamDetailFragment;
import com.mechinn.android.ouralliance.view.TeamListActivity;

public class TeamDetail2013 extends TeamDetailFragment<TeamScouting2013> {
	private static final String TAG = "TeamDetail2013";
	TeamScouting2013DataSource dataSource;
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"fragments: "+this.isTwoPane());
		if(!this.isTwoPane()) {
			updateScouting();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataSource = new TeamScouting2013DataSource(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = super.createBaseView(inflater.inflate(R.layout.fragment_team_detail_2013, container, false));
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getLoaderManager().initLoader(TeamListActivity.LOADER_TEAMSCOUTING, null, this);
	}
	
	public void setViewFromCursor(Cursor cursor) {
		if(cursor!=null) {
			try {
				this.setScouting(TeamScouting2013DataSource.getSingle(cursor));
				//start loading the wheels while we set the view elements
				this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMWHEEL, null, this);
				setView();
				return;
			} catch (OurAllianceException e) {
			}
		}
		errorRemoveThisFrag();
	}
	
	public void updateScouting() {
		super.updateScouting();
		
		try {
			dataSource.update(this.getScouting());
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				return dataSource.get(getSeasonId(), getTeamId());
			case TeamListActivity.LOADER_TEAMWHEEL:
				return getTeamScoutingWheelData().getScouting(getSeasonId(), getTeamId());
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				setViewFromCursor(cursor);
				break;
			case TeamListActivity.LOADER_TEAMWHEEL:
				createWheelsFromCursor(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case TeamListActivity.LOADER_TEAMSCOUTING:
				setViewFromCursor(null);
				break;
			case TeamListActivity.LOADER_TEAMWHEEL:
				createWheelsFromCursor(null);
				break;
		}
	}
}
