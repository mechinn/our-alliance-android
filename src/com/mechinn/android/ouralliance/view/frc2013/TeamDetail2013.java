package com.mechinn.android.ouralliance.view.frc2013;

import java.sql.SQLException;
import java.util.List;

import org.apmem.tools.layouts.FlowLayout;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mechinn.android.ouralliance.ImageWorker;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamDetailFragment;
import com.mechinn.android.ouralliance.widget.UncheckableRadioButton;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

public class TeamDetail2013 extends TeamDetailFragment<TeamScouting2013, TeamScouting2013DataSource> {
	public final static String TAG = TeamDetail2013.class.getName();
	public static final int LOADER_ORIENTATIONS = 20130;
	public static final int LOADER_DRIVETRAINS = 20131;

	private AutoCompleteTextView orientation;
	private AutoCompleteTextView driveTrain;
	private RatingBar humanPlayer;
	private EditText width;
	private EditText length;
	private EditText heightShooter;
	private EditText heightMax;
	private UncheckableRadioGroup maxClimb;
	private LinearLayout climbTimeContainer;
	private EditText climbTime;
	private UncheckableRadioGroup shooterTypes;
	private FlowLayout continuousShootingContainer;
	private UncheckableRadioGroup continuousShooting;
	private FlowLayout shootableGoals;
	private CheckBox lowGoal;
	private CheckBox midGoal;
	private CheckBox highGoal;
	private CheckBox pyramidGoal;
	private UncheckableRadioGroup autoMode;
	private UncheckableRadioButton lowAuto;
	private UncheckableRadioButton midAuto;
	private UncheckableRadioButton highAuto;
	private CheckBox slot;
	private CheckBox ground;
	private LinearLayout autoPickupContainer;
	private Switch autoPickup;
	private FlowLayout reloadSpeedContainer;
	private RatingBar reloadSpeed;
	private LinearLayout safeShooterContainer;
	private Switch safeShooter;
	private LinearLayout loaderShooterContainer;
	private Switch loaderShooter;
	private Switch blocker;
	
	private Cursor currentOrientations;
	private List<String> orientations;
	private ArrayAdapter<String> orientationsAdapter;
	private Cursor currentDriveTrains;
	private List<String> driveTrains;
	private ArrayAdapter<String> driveTrainsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2013, getSeason(), false);
		orientation = (AutoCompleteTextView) seasonView.findViewById(R.id.orientation);
		driveTrain = (AutoCompleteTextView) seasonView.findViewById(R.id.driveTrain);
		humanPlayer = (RatingBar) seasonView.findViewById(R.id.humanPlayer);
		width = (EditText) seasonView.findViewById(R.id.width);
		width.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	checkPerimeter();
			        	return true; // consume.
			        }                
			    }
			    return false; // pass on to other listeners. 
			}
			
		});
		width.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkPerimeter();
				}
			}
		});
		length = (EditText) seasonView.findViewById(R.id.length);
		length.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	checkPerimeter();
			        	return true; // consume.
			        }                
			    }
			    return false; // pass on to other listeners. 
			}
			
		});
		length.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkPerimeter();
				}
			}
		});
		heightShooter = (EditText) seasonView.findViewById(R.id.heightShooter);
		heightShooter.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	checkShooterHeight();
			        	return true; // consume.
			        }                
			    }
			    return false; // pass on to other listeners. 
			}
			
		});
		heightShooter.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkShooterHeight();
				}
			}
		});
		heightMax = (EditText) seasonView.findViewById(R.id.heightMax);
		heightMax.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	checkMaxHeight();
			        	return true; // consume.
			        }                
			    }
			    return false; // pass on to other listeners. 
			}
			
		});
		heightMax.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkMaxHeight();
				}
			}
		});
		maxClimb = (UncheckableRadioGroup) seasonView.findViewById(R.id.climbLevels);
		maxClimb.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				super.onCheckedChanged(group, checkedId);
				switch(checkedId) {
					default:
					case R.id.level0:
						climbTimeContainer.setVisibility(View.GONE);
						climbTime.setText("");
						break;
					case R.id.level1:
					case R.id.level2:
					case R.id.level3:
						climbTimeContainer.setVisibility(View.VISIBLE);
						if(0!=getScouting().getClimbTime()) {
							String num = Float.toString(getScouting().getClimbTime());
							climbTime.setText(num);
						}
						break;
				}
			}
		});
		climbTimeContainer = (LinearLayout) seasonView.findViewById(R.id.climbTimeContainer);
		climbTime = (EditText) seasonView.findViewById(R.id.climbTime);
		shooterTypes = (UncheckableRadioGroup) seasonView.findViewById(R.id.shooterTypes);
		shooterTypes.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				super.onCheckedChanged(group, checkedId);
				switch(checkedId) {
					default:
					case R.id.none:
						shootableGoals.setVisibility(View.GONE);
						lowGoal.setVisibility(View.GONE);
						lowGoal.setChecked(false);
						midGoal.setVisibility(View.GONE);
						midGoal.setChecked(false);
						highGoal.setVisibility(View.GONE);
						highGoal.setChecked(false);
						pyramidGoal.setVisibility(View.GONE);
						pyramidGoal.setChecked(false);
						continuousShootingContainer.setVisibility(View.GONE);
						continuousShooting.clearCheck();
						reloadSpeedContainer.setVisibility(View.GONE);
						reloadSpeed.setRating(0);
						safeShooterContainer.setVisibility(View.GONE);
						safeShooter.setChecked(false);
						loaderShooterContainer.setVisibility(View.GONE);
						loaderShooter.setChecked(false);
						break;
					case R.id.dumper:
						lowGoal.setVisibility(View.VISIBLE);
						lowGoal.setChecked(getScouting().isLowGoal());
						midGoal.setVisibility(View.GONE);
						midGoal.setChecked(false);
						highGoal.setVisibility(View.GONE);
						highGoal.setChecked(false);
						pyramidGoal.setVisibility(View.VISIBLE);
						pyramidGoal.setChecked(getScouting().isPyramidGoal());
						shootableGoals.setVisibility(View.VISIBLE);
						continuousShootingContainer.setVisibility(View.GONE);
						continuousShooting.clearCheck();
						reloadSpeedContainer.setVisibility(View.VISIBLE);
						reloadSpeed.setRating(getScouting().getReloadSpeed());
						safeShooterContainer.setVisibility(View.VISIBLE);
						safeShooter.setChecked(getScouting().isSafeShooter());
						loaderShooterContainer.setVisibility(View.VISIBLE);
						loaderShooter.setChecked(getScouting().isLoaderShooter());
						break;
					case R.id.shooter:
						lowGoal.setVisibility(View.VISIBLE);
						lowGoal.setChecked(getScouting().isLowGoal());
						midGoal.setVisibility(View.VISIBLE);
						midGoal.setChecked(getScouting().isMidGoal());
						highGoal.setVisibility(View.VISIBLE);
						highGoal.setChecked(getScouting().isHighGoal());
						pyramidGoal.setVisibility(View.VISIBLE);
						pyramidGoal.setChecked(getScouting().isPyramidGoal());
						shootableGoals.setVisibility(View.VISIBLE);
						continuousShootingContainer.setVisibility(View.VISIBLE);
						continuousShooting.programaticallyCheck(getScouting().getContinuousShooting());
						reloadSpeedContainer.setVisibility(View.VISIBLE);
						reloadSpeed.setRating(getScouting().getReloadSpeed());
						safeShooterContainer.setVisibility(View.VISIBLE);
						safeShooter.setChecked(getScouting().isSafeShooter());
						loaderShooterContainer.setVisibility(View.VISIBLE);
						loaderShooter.setChecked(getScouting().isLoaderShooter());
						break;
				}
			}
		});
		continuousShootingContainer = (FlowLayout) seasonView.findViewById(R.id.continuousShootingContainer);
		continuousShooting = (UncheckableRadioGroup) seasonView.findViewById(R.id.continuousShooting);
		shootableGoals = (FlowLayout) seasonView.findViewById(R.id.shootableGoals);
		lowGoal = (CheckBox) seasonView.findViewById(R.id.lowGoal);
		lowGoal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					lowAuto.setVisibility(View.VISIBLE);
					if(R.id.lowAuto==getScouting().getAutoMode()) {
						lowAuto.setChecked(true);
					}
				} else {
					lowAuto.setVisibility(View.GONE);
					lowAuto.setChecked(false);
				}
			}
		});
		midGoal = (CheckBox) seasonView.findViewById(R.id.midGoal);
		midGoal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					midAuto.setVisibility(View.VISIBLE);
					if(R.id.midAuto==getScouting().getAutoMode()) {
						lowAuto.setChecked(true);
					}
				} else {
					midAuto.setVisibility(View.GONE);
					midAuto.setChecked(false);
				}
			}
		});
		highGoal = (CheckBox) seasonView.findViewById(R.id.highGoal);
		highGoal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					highAuto.setVisibility(View.VISIBLE);
					if(R.id.highAuto==getScouting().getAutoMode()) {
						lowAuto.setChecked(true);
					}
				} else {
					highAuto.setVisibility(View.GONE);
					highAuto.setChecked(false);
				}
			}
		});
		pyramidGoal = (CheckBox) seasonView.findViewById(R.id.pyramidGoal);
		autoMode = (UncheckableRadioGroup) seasonView.findViewById(R.id.autoMode);
		lowAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.lowAuto);
		midAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.midAuto);
		highAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.highAuto);
		slot = (CheckBox) seasonView.findViewById(R.id.slot);
		ground = (CheckBox) seasonView.findViewById(R.id.ground);
		ground.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					autoPickupContainer.setVisibility(View.VISIBLE);
					autoPickup.setChecked(true);
				} else {
					autoPickupContainer.setVisibility(View.GONE);
					autoPickup.setChecked(false);
				}
			}
		});
		autoPickupContainer = (LinearLayout) seasonView.findViewById(R.id.autoPickupContainer);
		autoPickup = (Switch) seasonView.findViewById(R.id.autoPickup);
		reloadSpeed = (RatingBar) seasonView.findViewById(R.id.reloadSpeed);
		reloadSpeedContainer = (FlowLayout) seasonView.findViewById(R.id.reloadSpeedContainer);
		safeShooterContainer = (LinearLayout) seasonView.findViewById(R.id.safeShooterContainer);
		safeShooter = (Switch) seasonView.findViewById(R.id.safeShooter);
		loaderShooterContainer = (LinearLayout) seasonView.findViewById(R.id.loaderShooterContainer);
		loaderShooter = (Switch) seasonView.findViewById(R.id.loaderShooter);
		blocker = (Switch) seasonView.findViewById(R.id.blocker);
		getSeason().addView(seasonView);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
        if (getSeasonId() != 0 && getTeamId() != 0) {
    		this.getLoaderManager().restartLoader(LOADER_ORIENTATIONS, null, this);
    		this.getLoaderManager().restartLoader(LOADER_DRIVETRAINS, null, this);
        }
	}
	
	@Override
	public void setView() {
		super.setView();
		String num;
		orientation.setText(getScouting().getOrientation());
		driveTrain.setText(getScouting().getDriveTrain());
		humanPlayer.setRating(getScouting().getHumanPlayer());
		//check if its 0, if so empty the string so the user doesnt go crazy
		if(0!=getScouting().getWidth()) {
			num = Float.toString(getScouting().getWidth());
			width.setText(num);
		}
		if(0!=getScouting().getLength()) {
			num = Float.toString(getScouting().getLength());
			length.setText(num);
		}
		if(0!=getScouting().getHeightShooter()) {
			num = Float.toString(getScouting().getHeightShooter());
			heightShooter.setText(num);
		}
		if(0!=getScouting().getHeightMax()) {
			num = Float.toString(getScouting().getHeightMax());
			heightMax.setText(num);
		}
		maxClimb.programaticallyCheck(getScouting().getMaxClimb());
		if(0!=getScouting().getClimbTime()) {
			num = Float.toString(getScouting().getClimbTime());
			climbTime.setText(num);
		}
		shooterTypes.programaticallyCheck(getScouting().getShooterType());
		continuousShooting.programaticallyCheck(getScouting().getContinuousShooting());
		lowGoal.setChecked(getScouting().isLowGoal());
		midGoal.setChecked(getScouting().isMidGoal());
		highGoal.setChecked(getScouting().isHighGoal());
		pyramidGoal.setChecked(getScouting().isPyramidGoal());
		autoMode.programaticallyCheck(getScouting().getAutoMode());
		slot.setChecked(getScouting().isSlot());
		ground.setChecked(getScouting().isGround());
		autoPickup.setChecked(getScouting().isAutoPickup());
		reloadSpeed.setRating(getScouting().getReloadSpeed());
		safeShooter.setChecked(getScouting().isSafeShooter());
		loaderShooter.setChecked(getScouting().isLoaderShooter());
		blocker.setChecked(getScouting().isBlocker());
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
		getScouting().setOrientation(orientation.getText());
		getScouting().setDriveTrain(driveTrain.getText());
		getScouting().setHumanPlayer(humanPlayer.getRating());
		getScouting().setWidth(Utility.getFloatFromText(width.getText()));
		getScouting().setLength(Utility.getFloatFromText(length.getText()));
		getScouting().setHeightShooter(Utility.getFloatFromText(heightShooter.getText()));
		getScouting().setHeightMax(Utility.getFloatFromText(heightMax.getText()));
		getScouting().setMaxClimb(maxClimb.getCheckedRadioButtonId());
		getScouting().setClimbTime(Utility.getFloatFromText(climbTime.getText()));
		getScouting().setShooterType(shooterTypes.getCheckedRadioButtonId());
		getScouting().setContinuousShooting(continuousShooting.getCheckedRadioButtonId());
		getScouting().setLowGoal(lowGoal.isChecked());
		getScouting().setMidGoal(midGoal.isChecked());
		getScouting().setHighGoal(highGoal.isChecked());
		getScouting().setPyramidGoal(pyramidGoal.isChecked());
		getScouting().setAutoMode(autoMode.getCheckedRadioButtonId());
		getScouting().setSlot(slot.isChecked());
		getScouting().setGround(ground.isChecked());
		getScouting().setReloadSpeed(reloadSpeed.getRating());
		getScouting().setSafeShooter(safeShooter.isChecked());
		getScouting().setLoaderShooter(loaderShooter.isChecked());
		getScouting().setBlocker(blocker.isChecked());
	}
	
	@Override
	public TeamScouting2013 setScoutingFromCursor(Cursor cursor) throws OurAllianceException, SQLException {
		return TeamScouting2013DataSource.getSingle(cursor);
	}
	
	public void setOrientationsFromCursor(Cursor cursor) {
		if(null!=currentOrientations) {
			currentOrientations.close();
		}
		currentOrientations = cursor;
		try {
			orientations = AOurAllianceDataSource.getStringList(currentOrientations);
			orientationsAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, orientations);
			orientation.setAdapter(orientationsAdapter);
			orientation.setThreshold(1);
			for(int i=0; i<orientation.getAdapter().getCount();++i) {
				Log.d(TAG, "orientation: "+orientation.getAdapter().getItem(i));
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		this.getLoaderManager().destroyLoader(LOADER_ORIENTATIONS);
	}
	
	public void setDriveTrainsFromCursor(Cursor cursor) {
		if(null!=currentDriveTrains) {
			currentDriveTrains.close();
		}
		currentDriveTrains = cursor;
		try {
			driveTrains = AOurAllianceDataSource.getStringList(currentDriveTrains);
			driveTrainsAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, driveTrains);
			driveTrain.setAdapter(driveTrainsAdapter);
			driveTrain.setThreshold(1);
			for(int i=0; i<driveTrain.getAdapter().getCount();++i) {
				Log.d(TAG, "driveTrain: "+driveTrain.getAdapter().getItem(i));
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		this.getLoaderManager().destroyLoader(LOADER_DRIVETRAINS);
	}

	@Override
	public TeamScouting2013DataSource createDataSouce() {
		return new TeamScouting2013DataSource(this.getActivity());
	}
	
	public void checkPerimeter() {
		try {
	        int widthVal = Integer.parseInt(width.getText().toString());
	        int lengthVal = Integer.parseInt(length.getText().toString());
	        int perimeter = 2*widthVal+2*lengthVal;
	        if(perimeter>TeamScouting2013.maxPerimeter) {
	     	   Toast.makeText(TeamDetail2013.this.getActivity(), "Perimeter exceeds "+TeamScouting2013.maxPerimeter+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkShooterHeight() {
		try {
	        int shooterHeight = Integer.parseInt(heightShooter.getText().toString());
	        if(shooterHeight>TeamScouting2013.maxHeight) {
	     	   Toast.makeText(TeamDetail2013.this.getActivity(), "Shooter height exceeds "+TeamScouting2013.maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkMaxHeight() {
		try {
	        int maxHeight = Integer.parseInt(heightMax.getText().toString());
	        if(maxHeight>TeamScouting2013.maxHeight) {
	     	   Toast.makeText(TeamDetail2013.this.getActivity(), "Max height exceeds "+TeamScouting2013.maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_ORIENTATIONS:
				return getDataSource().getAllDistinct(TeamScouting2013.ORIENTATION);
			case LOADER_DRIVETRAINS:
				return getDataSource().getAllDistinct(TeamScouting2013.DRIVETRAIN);
		}
		return super.onCreateLoader(id, bundle);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "2013 loader finished");
		switch(loader.getId()) {
			case LOADER_ORIENTATIONS:
				setOrientationsFromCursor(cursor);
				break;
			case LOADER_DRIVETRAINS:
				setDriveTrainsFromCursor(cursor);
				break;
			default:
				super.onLoadFinished(loader, cursor);
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "2013 loader reset");
		switch(loader.getId()) {
			default:
				super.onLoaderReset(loader);
		}
	}
}
