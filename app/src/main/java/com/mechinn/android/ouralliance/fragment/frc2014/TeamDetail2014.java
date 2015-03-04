package com.mechinn.android.ouralliance.fragment.frc2014;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.frc2014.TeamScouting2014FilterAdapter;
import com.mechinn.android.ouralliance.data.OurAllianceObject;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

public class TeamDetail2014 extends TeamDetailFragment {
    public static final String TAG = "TeamDetail2014";
    public static final int maxPerimeter = 112;
    public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

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
    private CheckBox noAuto;
    private CheckBox driveAuto;
    private CheckBox lowAuto;
    private CheckBox highAuto;
    private CheckBox hotAuto;

	private TeamScouting2014FilterAdapter orientationsAdapter;
	private TeamScouting2014FilterAdapter driveTrainsAdapter;

    public TeamScouting2014 getScouting() {
        return (TeamScouting2014) super.getScouting();
    }
    public void setScouting(TeamScouting2014 scouting) {
        super.setScouting(scouting);
    }

    public void setScoutingFromCursor(Cursor cursor) {
        TeamScouting2014 scouting = new TeamScouting2014();
        scouting.loadFromCursor(cursor);
        setScouting(scouting);
    }

    public void shooterTypeNone() {
        shooterGroup.setVisibility(View.GONE);
        lowGoal.setChecked(false);
        highGoal.setChecked(false);
        shootingDistanceGroup.setVisibility(View.GONE);
        shootingDistance.setText("");
        lowAuto.setVisibility(View.GONE);
        lowAuto.setChecked(false);
        highAuto.setVisibility(View.GONE);
        highAuto.setChecked(false);
        hotAuto.setVisibility(View.GONE);
        hotAuto.setChecked(false);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
        orientation = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014orientation);
        driveTrain = (AutoCompleteTextView) seasonView.findViewById(R.id.team2014driveTrain);
        width = (EditText) seasonView.findViewById(R.id.team2014width);
        width.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
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
        length = (EditText) seasonView.findViewById(R.id.team2014length);
        length.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
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
        heightShooter = (EditText) seasonView.findViewById(R.id.team2014heightShooter);
        heightShooter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (null != event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
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
        heightShooter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkShooterHeight();
                }
            }
        });
        heightMax = (EditText) seasonView.findViewById(R.id.team2014heightMax);
        heightMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (null != event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
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
        heightMax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkMaxHeight();
                }
            }
        });
        shooterTypes = (UncheckableRadioGroup) seasonView.findViewById(R.id.team2014shooterType);
        shooterGroup = (LinearLayout) seasonView.findViewById(R.id.team2014shooterGroup);
        lowGoal = (CheckBox) seasonView.findViewById(R.id.team2014lowGoal);
        highGoal = (CheckBox) seasonView.findViewById(R.id.team2014highGoal);
        shootingDistanceGroup = (LinearLayout) seasonView.findViewById(R.id.team2014shootingDistanceGroup);
        shootingDistance = (EditText) seasonView.findViewById(R.id.team2014shootingDistance);
        shootingDistance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!event.isShiftPressed()) {
                        checkShootingDistance();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });
        shootingDistance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
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
        noAuto = (CheckBox) seasonView.findViewById(R.id.team2014noAuto);
        driveAuto = (CheckBox) seasonView.findViewById(R.id.team2014driveAuto);
        lowAuto = (CheckBox) seasonView.findViewById(R.id.team2014lowAuto);
        highAuto = (CheckBox) seasonView.findViewById(R.id.team2014highAuto);
        hotAuto = (CheckBox) seasonView.findViewById(R.id.team2014hotAuto);
        shooterTypeNone();
		shooterTypes.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                super.onCheckedChanged(group, checkedId);
                if (null != getScouting()) {
                    switch (checkedId) {
                        default:
                        case R.id.none:
                            shooterTypeNone();
                            break;
                        case R.id.dumper:
                            shooterGroup.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getLowGoal()) {
                                lowGoal.setChecked(getScouting().getLowGoal());
                            }
                            highGoal.setVisibility(View.GONE);
                            highGoal.setChecked(false);
                            shootingDistanceGroup.setVisibility(View.GONE);
                            shootingDistance.setText("");
                            lowAuto.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getLowAuto()) {
                                lowAuto.setChecked(getScouting().getLowAuto());
                            }
                            highAuto.setVisibility(View.GONE);
                            highAuto.setChecked(false);
                            hotAuto.setVisibility(View.GONE);
                            hotAuto.setChecked(false);
                            break;
                        case R.id.shooter:
                            shooterGroup.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getLowGoal()) {
                                lowGoal.setChecked(getScouting().getLowGoal());
                            }
                            highGoal.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getHighGoal()) {
                                highGoal.setChecked(getScouting().getHighGoal());
                            }
                            shootingDistanceGroup.setVisibility(View.VISIBLE);
                            if (null != getScouting().getShootingDistance() && 0 != getScouting().getShootingDistance()) {
                                shootingDistance.setText(Double.toString(getScouting().getShootingDistance()));
                            }
                            if(null!=getScouting().getLowAuto()) {
                                lowAuto.setChecked(getScouting().getLowAuto());
                            }
                            lowAuto.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getHighAuto()) {
                                highAuto.setChecked(getScouting().getHighAuto());
                            }
                            highAuto.setVisibility(View.VISIBLE);
                            if(null!=getScouting().getHotAuto()) {
                                hotAuto.setChecked(getScouting().getHotAuto());
                            }
                            hotAuto.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
		getSeason().addView(seasonView);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orientationsAdapter = new TeamScouting2014FilterAdapter(getActivity(), null, TeamScouting2014FilterAdapter.Type.ORIENTATION);
        orientation.setAdapter(orientationsAdapter);
        orientation.setThreshold(1);
        driveTrainsAdapter = new TeamScouting2014FilterAdapter(getActivity(), null, TeamScouting2014FilterAdapter.Type.DRIVETRAIN);
        driveTrain.setAdapter(driveTrainsAdapter);
        driveTrain.setThreshold(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getPrefs().getYear() != 0 && getTeamId() != 0) {
            loadOrientations();
            loadDriveTrains();
        }
    }
	
	@Override
	public void setView() {
		super.setView();
		String num;
        getAddWheel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWheels();
                Timber.d( "add wheel");
                Wheel newWheel = new Wheel2014();
                newWheel.setTeamScouting(getScouting());
                newWheel.asyncSave();
            }
        });
		orientation.setText(getScouting().getOrientation());
		driveTrain.setText(getScouting().getDriveTrain());
		//check if its 0, if so empty the string so the user doesnt go crazy
		if(null!=getScouting().getWidth() && getScouting().getWidth()>0) {
			num = Double.toString(getScouting().getWidth());
			width.setText(num);
		}
		if(null!=getScouting().getLength() && getScouting().getLength()>0) {
			num = Double.toString(getScouting().getLength());
			length.setText(num);
		}
		if(null!=getScouting().getHeightShooter() && getScouting().getHeightShooter()>0) {
			num = Double.toString(getScouting().getHeightShooter());
			heightShooter.setText(num);
		}
		if(null!=getScouting().getHeightMax() && getScouting().getHeightMax()>0) {
			num = Double.toString(getScouting().getHeightMax());
			heightMax.setText(num);
		}
        if(null!=getScouting().getShooterType()) {
            switch (getScouting().getShooterType()) {
                case NONE:
                    shooterTypes.programaticallyCheck(R.id.none);
                    break;
                case DUMPER:
                    shooterTypes.programaticallyCheck(R.id.dumper);
                    break;
                case SHOOTER:
                    shooterTypes.programaticallyCheck(R.id.shooter);
                    break;
            }
        }
        if(null!=getScouting().getLowGoal()) {
            lowGoal.setChecked(getScouting().getLowGoal());
        }
        if(null!=getScouting().getHighGoal()) {
            highGoal.setChecked(getScouting().getHighGoal());
        }
        if(null!=getScouting().getShootingDistance() && getScouting().getShootingDistance()>0) {
            num = Double.toString(getScouting().getShootingDistance());
            shootingDistance.setText(num);
        }
        if(null!=getScouting().getPassGround()) {
            passGround.setChecked(getScouting().getPassGround());
        }
        if(null!=getScouting().getPassAir()) {
            passAir.setChecked(getScouting().getPassAir());
        }
        if(null!=getScouting().getPassTruss()) {
            passTruss.setChecked(getScouting().getPassTruss());
        }
        if(null!=getScouting().getPickupGround()) {
            pickupGround.setChecked(getScouting().getPickupGround());
        }
        if(null!=getScouting().getPickupCatch()) {
            pickupCatch.setChecked(getScouting().getPickupCatch());
        }
        if(null!=getScouting().getPusher()) {
            pusher.setChecked(getScouting().getPusher());
        }
        if(null!=getScouting().getBlocker()) {
            blocker.setChecked(getScouting().getBlocker());
        }
        if(null!=getScouting().getHumanPlayer()) {
            humanPlayer.setRating(getScouting().getHumanPlayer());
        }
        if(null!=getScouting().getNoAuto()) {
            noAuto.setChecked(getScouting().getNoAuto());
        }
        if(null!=getScouting().getDriveAuto()) {
            driveAuto.setChecked(getScouting().getDriveAuto());
        }
        if(null!=getScouting().getLowAuto()) {
            lowAuto.setChecked(getScouting().getLowAuto());
        }
        if(null!=getScouting().getHighAuto()) {
            highAuto.setChecked(getScouting().getHighAuto());
        }
        if(null!=getScouting().getHotAuto()) {
            hotAuto.setChecked(getScouting().getHotAuto());
        }
	}

    public void updateWheels() {
        for(int wheelNum=0;wheelNum<getWheels().getChildCount();wheelNum++) {
            LinearLayout wheelItem = (LinearLayout) getWheels().getChildAt(wheelNum);
            Wheel2014 wheel = (Wheel2014) wheelItem.getTag();
            AutoCompleteTextView wheelType = (AutoCompleteTextView) wheelItem.findViewById(R.id.wheelType);
            EditText wheelSize = (EditText) wheelItem.findViewById(R.id.wheelSize);
            EditText wheelCount = (EditText) wheelItem.findViewById(R.id.wheelCount);
            if(null!=wheelType && null!=wheelSize && null!=wheelCount) {
                String wheelTypeString = wheelType.getText().toString();
                Double wheelSizeDouble = null;
                Integer wheelCountInteger = null;
                try {
                    wheelSizeDouble = Double.parseDouble(wheelSize.getText().toString());
                } catch (NumberFormatException e) {
                    Timber.e("Invalid wheel size",e);
                }
                try {
                    wheelCountInteger = Integer.parseInt(wheelCount.getText().toString());
                } catch (NumberFormatException e) {
                    Timber.e("Invalid wheel count",e);
                }
                if(!wheelTypeString.equals("") && null!=wheelSizeDouble && wheelSizeDouble>0 && null!=wheelCountInteger && wheelCountInteger>0) {
                    wheel.setWheelType(wheelTypeString);
                    wheel.setWheelSize(wheelSizeDouble);
                    wheel.setWheelCount(wheelCountInteger);
                    wheel.asyncSave();
                }
            }
        }
    }
	
	@Override
	public void updateScouting() {
		super.updateScouting();
        updateWheels();
		getScouting().setOrientation(orientation.getText().toString());
		getScouting().setDriveTrain(driveTrain.getText().toString());
		getScouting().setWidth(Utility.getDoubleFromText(width.getText()));
		getScouting().setLength(Utility.getDoubleFromText(length.getText()));
		getScouting().setHeightShooter(Utility.getDoubleFromText(heightShooter.getText()));
		getScouting().setHeightMax(Utility.getDoubleFromText(heightMax.getText()));
        switch(shooterTypes.getCheckedRadioButtonId()) {
            case R.id.none:
                getScouting().setShooterType(TeamScouting2014.ShooterType.NONE);
                break;
            case R.id.dumper:
                getScouting().setShooterType(TeamScouting2014.ShooterType.DUMPER);
                break;
            case R.id.shooter:
                getScouting().setShooterType(TeamScouting2014.ShooterType.SHOOTER);
                break;
            default:
                getScouting().setShooterType(TeamScouting2014.ShooterType.UNKNOWN);
        }
		getScouting().setLowGoal(lowGoal.isChecked());
        getScouting().setHighGoal(highGoal.isChecked());
        getScouting().setShootingDistance(Utility.getDoubleFromText(shootingDistance.getText()));
		getScouting().setPassGround(passGround.isChecked());
		getScouting().setPassAir(passAir.isChecked());
		getScouting().setPassTruss(passTruss.isChecked());
		getScouting().setPickupGround(pickupGround.isChecked());
		getScouting().setPickupCatch(pickupCatch.isChecked());
		getScouting().setPusher(pusher.isChecked());
		getScouting().setBlocker(blocker.isChecked());
        getScouting().setHumanPlayer(humanPlayer.getRating());
        getScouting().setNoAuto(noAuto.isChecked());
        getScouting().setDriveAuto(driveAuto.isChecked());
        getScouting().setLowAuto(lowAuto.isChecked());
        getScouting().setHighAuto(highAuto.isChecked());
        getScouting().setHotAuto(hotAuto.isChecked());
	}
	
	public void checkPerimeter() {
		try {
	        int widthVal = Integer.parseInt(width.getText().toString());
	        int lengthVal = Integer.parseInt(length.getText().toString());
	        int perimeter = 2*widthVal+2*lengthVal;
	        if(perimeter>maxPerimeter) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Perimeter exceeds "+maxPerimeter+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkShooterHeight() {
		try {
	        int shooterHeight = Integer.parseInt(heightShooter.getText().toString());
	        if(shooterHeight>maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Shooter height exceeds "+maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkMaxHeight() {
		try {
	        int maxHeight = Integer.parseInt(heightMax.getText().toString());
	        if(maxHeight>maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Max height exceeds "+maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

    public void checkShootingDistance() {
        try {
            int maxDistance = Integer.parseInt(shootingDistance.getText().toString());
            if(maxDistance>maxDistance) {
                Toast.makeText(TeamDetail2014.this.getActivity(), "Max distance exceeds "+maxDistance, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void loadOrientations() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2014> orientations = new Select().from(TeamScouting2014.class).where(TeamScouting2014.ORIENTATION+" IS NOT NULL").groupBy(TeamScouting2014.ORIENTATION).execute();
                if(null!=orientations) {
                    EventBus.getDefault().post(new LoadOrientations(orientations));
                } else {
                    Timber.d("null orientations");
                }
            }
        });
    }
    public void onEventMainThread(TeamScouting2014 scoutingChanged) {
        loadScouting();
        loadOrientations();
        loadDriveTrains();
    }
    public void onEventMainThread(LoadOrientations orientations) {
        Timber.d("orientations: "+orientations.getOrientations().size());
        orientationsAdapter.swapList(orientations.getOrientations());
    }
    private class LoadOrientations {
        List<TeamScouting2014> orientations;
        public LoadOrientations(List<TeamScouting2014> orientations) {
            this.orientations = orientations;
        }
        public List<TeamScouting2014> getOrientations() {
            return orientations;
        }
    }

    public void loadDriveTrains() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2014> driveTrains = new Select().from(TeamScouting2014.class).where(TeamScouting2014.DRIVE_TRAIN+" IS NOT NULL").groupBy(TeamScouting2014.DRIVE_TRAIN).execute();
                if(null!=driveTrains) {
                    EventBus.getDefault().post(new LoadDriveTrains(driveTrains));
                } else {
                    Timber.d("null drive trains");
                }
            }
        });
    }
    public void onEventMainThread(LoadDriveTrains driveTrains) {
        Timber.d("drive trains: "+driveTrains.getDriveTrains().size());
        driveTrainsAdapter.swapList(driveTrains.getDriveTrains());
    }
    private class LoadDriveTrains {
        List<TeamScouting2014> driveTrains;
        public LoadDriveTrains(List<TeamScouting2014> driveTrains) {
            this.driveTrains = driveTrains;
        }
        public List<TeamScouting2014> getDriveTrains() {
            return driveTrains;
        }
    }
    public void loadScouting() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                TeamScouting2014 scouting = null;
                int year = getPrefs().getYear();
                try {
                    scouting = new Select().from(TeamScouting2014.class).where(TeamScouting2014.TEAM+"=?", getTeamId()).executeSingle();
                } catch(NullPointerException e) {
                    Team team = Model.load(Team.class, getTeamId());
                    scouting = (TeamScouting2014) new TeamScouting2014();
                    scouting.setTeam(team);
                }
                if(null!=scouting) {
                    EventBus.getDefault().post(new LoadScouting(scouting));
                }
            }
        });
    }
    public void onEventMainThread(LoadScouting scouting) {
        setScouting(scouting.getScouting());
        setView();
        getRootView().setVisibility(View.VISIBLE);
        loadWheels();
    }
    private class LoadScouting {
        TeamScouting2014 scouting;
        public LoadScouting(TeamScouting2014 scouting) {
            this.scouting = scouting;
        }
        public TeamScouting2014 getScouting() {
            return scouting;
        }
    }
    public void loadWheelTypes() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<Wheel> wheelTypes = new Select().from(Wheel2014.class).where(Wheel2014.WHEEL_TYPE+" IS NOT NULL").groupBy(Wheel2014.WHEEL_TYPE).execute();
                if (null != wheelTypes) {
                    EventBus.getDefault().post(new LoadWheelTypes(wheelTypes));
                } else {
                    Timber.d( "No wheels found ");
                }
            }
        });
    }
    public void onEventMainThread(Wheel2014 wheelsChanged) {
        loadWheelTypes();
        loadWheels();
    }
    public void onEventMainThread(LoadWheelTypes event) {
        Timber.d( "Wheels " + event.getWheels().size());
        getWheelTypesAdapter().swapList(event.getWheels());
    }
    private class LoadWheelTypes {
        List<Wheel> wheels;
        public LoadWheelTypes(List<Wheel> wheels) {
            this.wheels = wheels;
        }
        public List<Wheel> getWheels() {
            return wheels;
        }
    }
    public void loadWheels() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<Wheel> wheels = new Select().from(Wheel2014.class).where(Wheel2014.TEAM_SCOUTING+"=?", getScouting().getId()).execute();
                if (null != wheels) {
                    EventBus.getDefault().post(new LoadWheels(wheels));
                } else {
                    Timber.d("No wheels found for team "+getTeamId());
                }
            }
        });
    }
    public void onEventMainThread(LoadWheels event) {
        Timber.d( "Wheels for team " + event.getWheels().size());
        getWheels().removeAllViews();
        for(Wheel wheel : event.getWheels()) {
            createWheel(wheel);
        }
    }
    private class LoadWheels {
        List<Wheel> wheels;
        public LoadWheels(List<Wheel> wheels) {
            this.wheels = wheels;
        }
        public List<Wheel> getWheels() {
            return wheels;
        }
    }



}
