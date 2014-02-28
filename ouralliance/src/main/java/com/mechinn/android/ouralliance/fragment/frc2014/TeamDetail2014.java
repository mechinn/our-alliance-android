package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.mechinn.android.ouralliance.adapter.frc2014.TeamScouting2014Adapter;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.widget.UncheckableRadioButton;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;

public class TeamDetail2014 extends TeamDetailFragment<TeamScouting2014> {
	public final static String TAG = TeamDetail2014.class.getSimpleName();

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

	private TeamScouting2014Adapter orientationsAdapter;
	private TeamScouting2014Adapter driveTrainsAdapter;

    private ManyQuery.ResultHandler<TeamScouting2014> onOrientationsLoaded =
            new ManyQuery.ResultHandler<TeamScouting2014>() {

                @Override
                public boolean handleResult(CursorList<TeamScouting2014> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<TeamScouting2014> model = ModelList.from(result);
                        result.close();
                        orientationsAdapter.swapList(model);
                        Log.d(TAG, "Count: " + model.size());
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<TeamScouting2014> onDriveTrainsLoaded =
            new ManyQuery.ResultHandler<TeamScouting2014>() {

                @Override
                public boolean handleResult(CursorList<TeamScouting2014> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<TeamScouting2014> model = ModelList.from(result);
                        result.close();
                        driveTrainsAdapter.swapList(model);
                        Log.d(TAG, "Count: " + model.size());
                        return true;
                    } else {
                        return false;
                    }
                }
            };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
		orientation = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014orientation);
		driveTrain = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014driveTrain);
		width = (EditText) seasonView.findViewById(R.id.team2014width);
		width.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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
				if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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
				if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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
				if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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
                if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orientationsAdapter = new TeamScouting2014Adapter(getActivity(), null, TeamScouting2014Adapter.ORIENTATION);
        orientation.setAdapter(orientationsAdapter);
        orientation.setThreshold(1);
        driveTrainsAdapter = new TeamScouting2014Adapter(getActivity(), null, TeamScouting2014Adapter.DRIVETRAIN);
        driveTrain.setAdapter(driveTrainsAdapter);
        driveTrain.setThreshold(1);
    }
	
	@Override
	public void onStart() {
		super.onStart();
	}

    @Override
    public void onResume() {
        super.onResume();
        if (this.getPrefs().getSeason() != 0 && getTeamId() != 0) {
            Query.one(TeamScouting2014.class, "select * from TeamScouting2014 where team=?", getTeamId()).getAsync(this.getLoaderManager(), getOnScoutingLoaded());
            Query.many(TeamScouting2014.class, "select * from TeamScouting2014 group by orientation", this.getPrefs().getSeason(), getTeamId()).getAsync(this.getLoaderManager(), this.onOrientationsLoaded);
            Query.many(TeamScouting2014.class, "select * from TeamScouting2014 group by driveTrain", this.getPrefs().getSeason(), getTeamId()).getAsync(this.getLoaderManager(), this.onDriveTrainsLoaded);
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
}
