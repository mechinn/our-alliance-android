package com.mechinn.android.ouralliance.view.frc2014;

import java.sql.SQLException;
import java.util.List;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.frc2014.TeamScouting2014DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamDetailFragment;
import com.mechinn.android.ouralliance.widget.UncheckableRadioButton;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

public class TeamDetail2014 extends TeamDetailFragment<TeamScouting2014, TeamScouting2014DataSource> {
	public final static String TAG = TeamDetail2014.class.getSimpleName();
	public static final int LOADER_ORIENTATIONS = 20140;
	public static final int LOADER_DRIVETRAINS = 20141;

	private AutoCompleteTextView orientation;
	private AutoCompleteTextView driveTrain;
	private EditText width;
	private EditText length;
	private EditText heightShooter;
	private EditText heightMax;
	private UncheckableRadioGroup shooterTypes;
	private LinearLayout shooterGroup;
	private CheckBox lowGoal;
	private CheckBox highGoal;
	private CheckBox hotGoal;
    private LinearLayout shootingDistanceGroup;
    private EditText shootingDistance;
    private CheckBox passGround;
    private CheckBox passAir;
    private CheckBox passTruss;
    private CheckBox pickupGround;
    private CheckBox pickupCatch;
    private CheckBox pusher;
    private CheckBox blocker;
    private RatingBar humanPlayer;
    private UncheckableRadioButton noAuto;
    private UncheckableRadioButton driveAuto;
    private UncheckableRadioButton lowAuto;
    private UncheckableRadioButton highAuto;
    private UncheckableRadioButton hotAuto;
    private UncheckableRadioGroup autoMode;
	
	private Cursor currentOrientations;
	private List<String> orientations;
	private ArrayAdapter<String> orientationsAdapter;
	private Cursor currentDriveTrains;
	private List<String> driveTrains;
	private ArrayAdapter<String> driveTrainsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
		orientation = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014orientation);
		driveTrain = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014driveTrain);
		width = (EditText) seasonView.findViewById(R.id.team2014width);
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
		length = (EditText) seasonView.findViewById(R.id.team2014length);
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
		heightShooter = (EditText) seasonView.findViewById(R.id.team2014heightShooter);
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
		heightMax = (EditText) seasonView.findViewById(R.id.team2014heightMax);
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
		shooterTypes = (UncheckableRadioGroup) seasonView.findViewById(R.id.team2014shooterType);
		shooterTypes.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                super.onCheckedChanged(group, checkedId);
                switch (checkedId) {
                    default:
                    case R.id.none:
                        shooterGroup.setVisibility(View.GONE);
                        lowGoal.setChecked(false);
                        highGoal.setChecked(false);
                        hotGoal.setChecked(false);
                        shootingDistanceGroup.setVisibility(View.GONE);
                        shootingDistance.setText("");
                        lowAuto.setVisibility(View.GONE);
                        lowAuto.setChecked(false);
                        highAuto.setVisibility(View.GONE);
                        highAuto.setChecked(false);
                        hotAuto.setVisibility(View.GONE);
                        hotAuto.setChecked(false);
                        break;
                    case R.id.dumper:
                        shooterGroup.setVisibility(View.VISIBLE);
                        lowGoal.setChecked(getScouting().isLowGoal());
                        highGoal.setVisibility(View.GONE);
                        highGoal.setChecked(false);
                        hotGoal.setVisibility(View.GONE);
                        hotGoal.setChecked(false);
                        shootingDistanceGroup.setVisibility(View.GONE);
                        shootingDistance.setText("");
                        lowAuto.setVisibility(View.VISIBLE);
                        highAuto.setVisibility(View.GONE);
                        hotAuto.setVisibility(View.GONE);
                        autoMode.programaticallyCheck(getScouting().getAutoMode());
                        break;
                    case R.id.shooter:
                        shooterGroup.setVisibility(View.VISIBLE);
                        lowGoal.setChecked(getScouting().isLowGoal());
                        highGoal.setVisibility(View.VISIBLE);
                        highGoal.setChecked(getScouting().isHighGoal());
                        hotGoal.setVisibility(View.VISIBLE);
                        hotGoal.setChecked(getScouting().isHotGoal());
                        shootingDistanceGroup.setVisibility(View.VISIBLE);
                        if(0!=getScouting().getShootingDistance()) {
                            shootingDistance.setText(Float.toString(getScouting().getShootingDistance()));
                        }
                        lowAuto.setVisibility(View.VISIBLE);
                        highAuto.setVisibility(View.VISIBLE);
                        hotAuto.setVisibility(View.VISIBLE);
                        switch(getScouting().getAutoMode()){
                            case R.id.team2014noAuto:
                                noAuto.setChecked(true);
                                break;
                            case R.id.team2014driveAuto:
                                driveAuto.setChecked(true);
                                break;
                            case R.id.team2014lowAuto:
                                lowAuto.setChecked(true);
                                break;
                            case R.id.team2014highAuto:
                                highAuto.setChecked(true);
                                break;
                            case R.id.team2014hotAuto:
                                hotAuto.setChecked(true);
                                break;
                        }
                        break;
                }
            }
        });
        shooterGroup = (LinearLayout) seasonView.findViewById(R.id.team2014shooterGroup);
		lowGoal = (CheckBox) seasonView.findViewById(R.id.team2014lowGoal);
		highGoal = (CheckBox) seasonView.findViewById(R.id.team2014highGoal);
		hotGoal = (CheckBox) seasonView.findViewById(R.id.team2014hotGoal);
        shootingDistanceGroup = (LinearLayout) seasonView.findViewById(R.id.team2014shootingDistanceGroup);
        shootingDistance = (EditText) seasonView.findViewById(R.id.team2014shootingDistance);
        shootingDistance.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        checkShootingDistance();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners. 
            }

        });
        shootingDistance.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkShootingDistance();
                }
            }
        });
        passGround = (CheckBox) seasonView.findViewById(R.id.team2014passGround);
        passAir = (CheckBox) seasonView.findViewById(R.id.team2014passAir);
        passTruss = (CheckBox) seasonView.findViewById(R.id.team2014passTruss);
        pickupGround = (CheckBox) seasonView.findViewById(R.id.team2014pickupGround);
        pickupCatch = (CheckBox) seasonView.findViewById(R.id.team2014pickupCatch);
        pusher = (CheckBox) seasonView.findViewById(R.id.team2014pusher);
        blocker = (CheckBox) seasonView.findViewById(R.id.team2014blocker);
        humanPlayer = (RatingBar) seasonView.findViewById(R.id.team2014humanPlayer);
        noAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.team2014noAuto);
        driveAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.team2014driveAuto);
        lowAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.team2014lowAuto);
        highAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.team2014highAuto);
        hotAuto = (UncheckableRadioButton) seasonView.findViewById(R.id.team2014hotAuto);
        autoMode = (UncheckableRadioGroup) seasonView.findViewById(R.id.team2014autoMode);
		getSeason().addView(seasonView);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
        if (this.getPrefs().getSeason() != 0 && getTeamId() != 0) {
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
		shooterTypes.programaticallyCheck(getScouting().getShooterType());
		lowGoal.setChecked(getScouting().isLowGoal());
        highGoal.setChecked(getScouting().isHighGoal());
        hotGoal.setChecked(getScouting().isHotGoal());
        if(0!=getScouting().getShootingDistance()) {
            num = Float.toString(getScouting().getShootingDistance());
            shootingDistance.setText(num);
        }
        passGround.setChecked(getScouting().isPassGround());
        passAir.setChecked(getScouting().isPassAir());
        passTruss.setChecked(getScouting().isPassTruss());
        pickupGround.setChecked(getScouting().isPickupGround());
        pickupCatch.setChecked(getScouting().isPickupCatch());
        pusher.setChecked(getScouting().isPusher());
		blocker.setChecked(getScouting().isBlocker());
        humanPlayer.setRating(getScouting().getHumanPlayer());
        autoMode.programaticallyCheck(getScouting().getAutoMode());
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
		getScouting().setOrientation(orientation.getText());
		getScouting().setDriveTrain(driveTrain.getText());
		getScouting().setWidth(Utility.getFloatFromText(width.getText()));
		getScouting().setLength(Utility.getFloatFromText(length.getText()));
		getScouting().setHeightShooter(Utility.getFloatFromText(heightShooter.getText()));
		getScouting().setHeightMax(Utility.getFloatFromText(heightMax.getText()));
		getScouting().setShooterType(shooterTypes.getCheckedRadioButtonId());
		getScouting().setLowGoal(lowGoal.isChecked());
        getScouting().setHighGoal(highGoal.isChecked());
		getScouting().setHotGoal(hotGoal.isChecked());
        getScouting().setShootingDistance(Utility.getFloatFromText(shootingDistance.getText()));
		getScouting().setPassGround(passGround.isChecked());
		getScouting().setPassAir(passAir.isChecked());
		getScouting().setPassTruss(passTruss.isChecked());
		getScouting().setPickupGround(pickupGround.isChecked());
		getScouting().setPickupCatch(pickupCatch.isChecked());
		getScouting().setPusher(pusher.isChecked());
		getScouting().setBlocker(blocker.isChecked());
        getScouting().setHumanPlayer(humanPlayer.getRating());
        getScouting().setAutoMode(autoMode.getCheckedRadioButtonId());
	}
	
	@Override
	public TeamScouting2014 setScoutingFromCursor(Cursor cursor) throws OurAllianceException, SQLException {
		return TeamScouting2014DataSource.getSingle(cursor);
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
	public TeamScouting2014DataSource createDataSouce() {
		return new TeamScouting2014DataSource(this.getActivity());
	}
	
	public void checkPerimeter() {
		try {
	        int widthVal = Integer.parseInt(width.getText().toString());
	        int lengthVal = Integer.parseInt(length.getText().toString());
	        int perimeter = 2*widthVal+2*lengthVal;
	        if(perimeter>TeamScouting2014.maxPerimeter) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Perimeter exceeds "+TeamScouting2014.maxPerimeter+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkShooterHeight() {
		try {
	        int shooterHeight = Integer.parseInt(heightShooter.getText().toString());
	        if(shooterHeight>TeamScouting2014.maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Shooter height exceeds "+TeamScouting2014.maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkMaxHeight() {
		try {
	        int maxHeight = Integer.parseInt(heightMax.getText().toString());
	        if(maxHeight>TeamScouting2014.maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Max height exceeds "+TeamScouting2014.maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

    public void checkShootingDistance() {
        try {
            int maxDistance = Integer.parseInt(shootingDistance.getText().toString());
            if(maxDistance>TeamScouting2014.maxDistance) {
                Toast.makeText(TeamDetail2014.this.getActivity(), "Max distance exceeds "+TeamScouting2014.maxDistance, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_ORIENTATIONS:
				return getDataSource().getAllDistinct(TeamScouting2014.ORIENTATION);
			case LOADER_DRIVETRAINS:
				return getDataSource().getAllDistinct(TeamScouting2014.DRIVETRAIN);
		}
		return super.onCreateLoader(id, bundle);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "2014 loader finished");
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
		Log.d(TAG, "2014 loader reset");
		switch(loader.getId()) {
			default:
				super.onLoaderReset(loader);
		}
	}
}
