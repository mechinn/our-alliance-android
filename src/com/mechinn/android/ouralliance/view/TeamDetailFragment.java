package com.mechinn.android.ouralliance.view;

import java.util.List;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.source.IOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingWheelDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

/**
 * A fragment representing a single Team detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public abstract class TeamDetailFragment<A extends TeamScouting, B extends TeamScoutingDataSource<A>> extends Fragment implements LoaderCallbacks<Cursor> {
	private static final String TAG = "TeamDetailFragment";

	public static final String ARG_TWOPANE = "twoPane";
	public static final String ARG_SEASON = "season";
	public static final String ARG_YEAR = "year";
	public static final String ARG_TEAM = "team";
//	private TeamScoutingDataSource teamScoutingData;
	private TeamScoutingWheelDataSource teamScoutingWheelData;
	private A scouting;
	private B dataSource;
	private boolean twoPane;
	private long seasonId;
	private long teamId;
	private TextView team;
	private TextView orientation;
	private TextView driveTrain;
	private TextView width;
	private TextView length;
	private TextView height;
	private RatingBar autonomous;
	private Button addWheel;
	private LinearLayout wheels;
	private TextView notes;
	private LinearLayout season;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamDetailFragment() {
	}

	public TeamScoutingWheelDataSource getTeamScoutingWheelData() {
		return teamScoutingWheelData;
	}
	public void setTeamScoutingWheelData(TeamScoutingWheelDataSource teamScoutingWheelData) {
		this.teamScoutingWheelData = teamScoutingWheelData;
	}
	public A getScouting() {
		return scouting;
	}
	public void setScouting(A scouting) {
		this.scouting = scouting;
	}
	public B getDataSource() {
		return dataSource;
	}
	public void setDataSource(B dataSource) {
		this.dataSource = dataSource;
	}
	public boolean isTwoPane() {
		return twoPane;
	}
	public void setTwoPane(boolean twoPane) {
		this.twoPane = twoPane;
	}
	public long getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(long seasonId) {
		this.seasonId = seasonId;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public LinearLayout getWheels() {
		return wheels;
	}
	public void setWheels(LinearLayout wheels) {
		this.wheels = wheels;
	}
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"fragments: "+this.isTwoPane());
		if(!this.isTwoPane()) {
			finish();
		}
	}
	
	public void finish() {
		updateScouting();
		try {
			getDataSource().update(this.getScouting());
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		LoaderManager.enableDebugLogging(true);

		teamScoutingWheelData = new TeamScoutingWheelDataSource(this.getActivity());
		twoPane = getArguments().getBoolean(ARG_TWOPANE, false);
		seasonId = getArguments().getLong(ARG_SEASON, 0);
		teamId = getArguments().getLong(ARG_TEAM, 0);
		Log.d(TAG, "season: "+seasonId);
		Log.d(TAG, "team: "+teamId);
		setDataSource(createDataSouce());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMSCOUTING, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);
		team = (TextView) rootView.findViewById(R.id.team);
		
		orientation = (TextView) rootView.findViewById(R.id.orientation);
		driveTrain = (TextView) rootView.findViewById(R.id.driveTrain);
		width = (TextView) rootView.findViewById(R.id.width);
		length = (TextView) rootView.findViewById(R.id.length);
		height = (TextView) rootView.findViewById(R.id.height);
		autonomous = (RatingBar) rootView.findViewById(R.id.autonomous);
		addWheel = (Button) rootView.findViewById(R.id.addWheel);
		addWheel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TeamScoutingWheel newWheel = new TeamScoutingWheel();
				newWheel.setSeason(scouting.getSeason());
				newWheel.setTeam(scouting.getTeam());
				createWheel(newWheel);
		    }
		});
		wheels = (LinearLayout) rootView.findViewById(R.id.wheels);
		notes = (TextView) rootView.findViewById(R.id.notes);
		season = (LinearLayout) rootView.findViewById(R.id.season);
		
		return rootView;
	}
	
	public void createWheelsFromCursor(Cursor cursor) {
		if(cursor!=null) {
			try {
				List<TeamScoutingWheel> teamScoutingWheels = TeamScoutingWheelDataSource.getList(cursor);
				Log.d(TAG, "Count: "+teamScoutingWheels.size());
				for(TeamScoutingWheel each : teamScoutingWheels) {
					createWheel(each);
				}
			} catch (OurAllianceException e) {
				e.printStackTrace();
			}
		}
	}
	
	public LinearLayout createWheel(TeamScoutingWheel thisWheel) {
		LinearLayout view = (LinearLayout) this.getActivity().getLayoutInflater().inflate(R.layout.fragment_team_detail_wheel, wheels, false);
		view.setTag(thisWheel);
		//1 is the type field
		TextView type = (TextView)view.getChildAt(1);
		type.setText(thisWheel.getType());
		//get the number
		String num = Integer.toString(thisWheel.getSize());
		//3 is the size field, if the size is currently 0 dont show it for the user's sake
		TextView size = (TextView)view.getChildAt(3);
		size.setText(num.equals("0")?"":num);
		//4 is the delete button, if clicked delete the wheel
		view.getChildAt(4).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				View view = ((View) v.getParent());
				TeamScoutingWheel wheel = (TeamScoutingWheel) view.getTag();
				//try to delete the wheel
				try {
					teamScoutingWheelData.delete(wheel);
				} catch (Exception e) {
					if(wheel.getId()>0) {
						Toast.makeText(TeamDetailFragment.this.getActivity(), "cannot delete wheel", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
//				TeamDetailFragment.this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMDELETEWHEEL, null, TeamDetailFragment.this);
				wheels.removeView(view);
			}
		});
		wheels.addView(view);
		return view;
	}
	
	public void errorRemoveThisFrag() {
		team.setText("Error");
		if(this.isVisible()) {
			//if we get here bail, something went very wrong
			Log.d(TAG, "removing");
			Toast.makeText(this.getActivity(), "Error loading team details", Toast.LENGTH_SHORT).show();
			this.getActivity().getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
		}
	}
	
	public void setView() {
		team.setText(Integer.toString(scouting.getTeam().getNumber())+": "+scouting.getTeam().getName());
		orientation.setText(scouting.getOrientation());
		driveTrain.setText(scouting.getDriveTrain());
		//check if its 0, if so empty the string so the user doesnt go crazy
		String num = Integer.toString(scouting.getWidth());
		width.setText(num.equals("0")?"":num);
		num = Integer.toString(scouting.getLength());
		length.setText(num.equals("0")?"":num);
		num = Integer.toString(scouting.getHeight());
		height.setText(num.equals("0")?"":num);
		autonomous.setRating(scouting.getAutonomous());
		notes.setText(scouting.getNotes());
	}
	
	public void updateScouting() {
		for(int i=0;i<wheels.getChildCount(); ++i) {
			LinearLayout theWheelView = (LinearLayout) wheels.getChildAt(i);
			TeamScoutingWheel theWheel = (TeamScoutingWheel) theWheelView.getTag();
			//1 is the type
			CharSequence type = ((TextView) theWheelView.getChildAt(1)).getText();
			theWheel.setType(type);
			//3 is the size
			CharSequence size = ((TextView) theWheelView.getChildAt(3)).getText();
			theWheel.setSize(size);
			//see if we should update or insert or just tell the user there isnt enough info
			try {
				if(theWheel.update()) {
					teamScoutingWheelData.update(theWheel);
				} else {
					teamScoutingWheelData.insert(theWheel);
				}
				System.out.println("Saved "+theWheel);
			} catch (OurAllianceException e) {
				String message = "Cannot save because no "+e.getMessage()+" was given.";
				Log.d(TAG,message);
				Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
			}
		}
		scouting.setOrientation(orientation.getText());
		scouting.setDriveTrain(driveTrain.getText());
		scouting.setWidth(width.getText());
		scouting.setLength(length.getText());
		scouting.setHeight(height.getText());
		scouting.setAutonomous(autonomous.getRating());
		scouting.setNotes(notes.getText());
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case TeamListActivity.LOADER_TEAMWHEEL:
				return getTeamScoutingWheelData().getScouting(getSeasonId(), getTeamId());
			case TeamListActivity.LOADER_TEAMSCOUTING:
				return dataSource.get(getSeasonId(), getTeamId());
		}
		return null;
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
	
	public void setViewFromCursor(Cursor cursor) {
		if(cursor!=null) {
			try {
				this.setScouting(setScoutingFromCursor(cursor));
				//start loading the wheels while we set the view elements
				this.getLoaderManager().restartLoader(TeamListActivity.LOADER_TEAMWHEEL, null, this);
				setView();
				return;
			} catch (OurAllianceException e) {
			}
		}
		errorRemoveThisFrag();
	}
	
	public abstract A setScoutingFromCursor(Cursor cursor) throws OurAllianceException;
	public abstract B createDataSouce();
}
