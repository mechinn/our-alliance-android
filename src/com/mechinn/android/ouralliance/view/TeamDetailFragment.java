package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;
import java.util.List;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingWheelDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class TeamDetailFragment<A extends TeamScouting, B extends AOurAllianceDataSource<A>> extends Fragment implements LoaderCallbacks<Cursor> {
	private static final String TAG = "TeamDetailFragment";
	public static final int LOADER_TEAMSCOUTING = 0;
	public static final int LOADER_TEAMWHEEL = 1;
	final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    
    private View rootView;
	private TextView notes;
	private TextView orientation;
	private TextView driveTrain;
	private TextView width;
	private TextView length;
	private TextView height;
	private RatingBar autonomous;
	private Button addWheel;
	private LinearLayout wheels;
	private Cursor currentView;
	private Cursor wheelCursor;

	private LinearLayout season;
	private long seasonId;
	private long teamId;
	private A scouting;
	private B dataSource;
	private TeamScoutingWheelDataSource teamScoutingWheelData;
	
	public abstract A setScoutingFromCursor(Cursor cursor) throws OurAllianceException, SQLException;
	public abstract B createDataSouce();
	
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
	public TeamScoutingWheelDataSource getTeamScoutingWheelData() {
		return teamScoutingWheelData;
	}
	public void setTeamScoutingWheelData(
			TeamScoutingWheelDataSource teamScoutingWheelData) {
		this.teamScoutingWheelData = teamScoutingWheelData;
	}
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		LoaderManager.enableDebugLogging(true);
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		teamScoutingWheelData = new TeamScoutingWheelDataSource(this.getActivity());
		setDataSource(createDataSouce());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
    		seasonId = savedInstanceState.getLong(Season.CLASS, 0);
    		teamId = savedInstanceState.getLong(Team.CLASS, 0);
    		Log.d(TAG, "season: "+seasonId);
    		Log.d(TAG, "team: "+teamId);
        }
        
        rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);
		rootView.setVisibility(View.GONE);
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

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
    		seasonId = getArguments().getLong(Season.CLASS, 0);
    		teamId = getArguments().getLong(Team.CLASS, 0);
    		Log.d(TAG, "season: "+seasonId);
    		Log.d(TAG, "team: "+teamId);
        }
        if (seasonId != 0 && teamId != 0) {
    		this.getLoaderManager().restartLoader(LOADER_TEAMSCOUTING, null, this);
        }
    }
	
	@Override
	public void onPause() {
		super.onPause();
		updateScouting();
		try {
			getDataSource().update(this.getScouting());
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Season.CLASS, seasonId);
        outState.putLong(Team.CLASS, teamId);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setView() {
		this.getActivity().setTitle(Integer.toString(scouting.getTeam().getNumber())+": "+scouting.getTeam().getName());
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
		rootView.setVisibility(View.VISIBLE);
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
				wheels.removeView(view);
			}
		});
		wheels.addView(view);
		return view;
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
				try {
					teamScoutingWheelData.update(theWheel);
					System.out.println("Saved "+theWheel);
				} catch (SQLException e) {
					try {
						theWheel = teamScoutingWheelData.insert(theWheel);
						System.out.println("Saved "+theWheel);
					} catch (SQLException e1) {
						String message = "Cannot save because conflicting "+e.getMessage()+" was given.";
						Log.d(TAG,message);
						Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
					}
				}
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
	
	public void setViewFromCursor(Cursor cursor) {
		if(null!=currentView) {
			currentView.close();
		}
		currentView = cursor;
		try {
			this.setScouting(setScoutingFromCursor(currentView));
			//start loading the wheels while we set the view elements
			this.getLoaderManager().restartLoader(LOADER_TEAMWHEEL, null, this);
			setView();
			return;
		} catch (OurAllianceException e) {
		} catch (SQLException e) {
		}
		rootView.setVisibility(View.VISIBLE);
	}
	
	public void createWheelsFromCursor(Cursor cursor) {
		if(wheelCursor!=null) {
			wheelCursor.close();
		}
		wheelCursor = cursor;
		try {
			List<TeamScoutingWheel> teamScoutingWheels = TeamScoutingWheelDataSource.getList(wheelCursor);
			Log.d(TAG, "Count: "+teamScoutingWheels.size());
			for(TeamScoutingWheel each : teamScoutingWheels) {
				createWheel(each);
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_TEAMWHEEL:
				return teamScoutingWheelData.get(getSeasonId(), getTeamId());
			case LOADER_TEAMSCOUTING:
				return dataSource.get(getSeasonId(), getTeamId());
		}
		return null;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case LOADER_TEAMSCOUTING:
				setViewFromCursor(cursor);
				break;
			case LOADER_TEAMWHEEL:
				createWheelsFromCursor(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case LOADER_TEAMSCOUTING:
				setViewFromCursor(null);
				break;
			case LOADER_TEAMWHEEL:
				createWheelsFromCursor(null);
				break;
		}
	}
}
