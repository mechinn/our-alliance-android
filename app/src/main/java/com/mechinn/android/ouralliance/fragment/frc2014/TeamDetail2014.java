package com.mechinn.android.ouralliance.fragment.frc2014;

import android.database.Cursor;
import android.os.Bundle;
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

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.frc2014.TeamScouting2014FilterAdapter;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class TeamDetail2014 extends TeamDetailFragment<TeamScouting2014> {
    public static final String TAG = "TeamDetail2014";
    public static final int maxPerimeter = 112;
    public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

    @OnClick(R.id.addWheel) protected void addWheel(View v) {
        Wheel newWheel = new Wheel2014();
        newWheel.setTeamScouting(getScouting());
        newWheel.asyncSave();
    }
	@InjectView(R.id.team2014orientation) protected AutoCompleteTextView orientation;
    @InjectView(R.id.team2014driveTrain) protected AutoCompleteTextView driveTrain;
    @InjectView(R.id.team2014width) protected EditText width;
    @OnEditorAction(R.id.team2014width) protected boolean widthEdit(TextView v, int actionId, KeyEvent event) {
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
    @OnFocusChange(R.id.team2014width) protected void widthFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkPerimeter();
        }
    }
    @InjectView(R.id.team2014length) protected EditText length;
    @OnEditorAction(R.id.team2014length) protected boolean lengthEdit(TextView v, int actionId, KeyEvent event) {
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
    @OnFocusChange(R.id.team2014length) protected void lengthFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkPerimeter();
        }
    }
    @InjectView(R.id.team2014heightShooter) protected EditText heightShooter;
    @OnEditorAction(R.id.team2014heightShooter) protected boolean heightShooterEdit(TextView v, int actionId, KeyEvent event) {
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
    @OnFocusChange(R.id.team2014heightShooter) protected void heightShooterFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkShooterHeight();
        }
    }
    @InjectView(R.id.team2014heightMax) protected EditText heightMax;
    @OnEditorAction(R.id.team2014heightMax) protected boolean heightMaxEdit(TextView v, int actionId, KeyEvent event) {
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
    @OnFocusChange(R.id.team2014heightMax) protected void heightMaxFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkMaxHeight();
        }
    }
    @InjectView(R.id.team2014shooterType) protected UncheckableRadioGroup shooterTypes;
    @InjectView(R.id.team2014shooterGroup) protected LinearLayout shooterGroup;
    @InjectView(R.id.team2014lowGoal) protected CheckBox lowGoal;
    @InjectView(R.id.team2014highGoal) protected CheckBox highGoal;
    @InjectView(R.id.team2014shootingDistanceGroup) protected LinearLayout shootingDistanceGroup;
    @InjectView(R.id.team2014shootingDistance) protected EditText shootingDistance;
    @OnEditorAction(R.id.team2014shootingDistance) protected boolean shootingDistanceEdit(TextView v, int actionId, KeyEvent event) {
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
    @OnFocusChange(R.id.team2014shootingDistance) protected void shootingDistanceFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkShootingDistance();
        }
    }
    @InjectView(R.id.team2014passGround) protected CheckBox passGround;
    @InjectView(R.id.team2014passAir) protected CheckBox passAir;
    @InjectView(R.id.team2014passTruss) protected CheckBox passTruss;
    @InjectView(R.id.team2014pickupGround) protected CheckBox pickupGround;
    @InjectView(R.id.team2014pickupCatch) protected CheckBox pickupCatch;
    @InjectView(R.id.team2014pusher) protected CheckBox pusher;
    @InjectView(R.id.team2014blocker) protected CheckBox blocker;
    @InjectView(R.id.team2014humanPlayer) protected RatingBar humanPlayer;
    @InjectView(R.id.team2014noAuto) protected CheckBox noAuto;
    @InjectView(R.id.team2014driveAuto) protected CheckBox driveAuto;
    @InjectView(R.id.team2014lowAuto) protected CheckBox lowAuto;
    @InjectView(R.id.team2014highAuto) protected CheckBox highAuto;
    @InjectView(R.id.team2014hotAuto) protected CheckBox hotAuto;

	private TeamScouting2014FilterAdapter orientationsAdapter;
	private TeamScouting2014FilterAdapter driveTrainsAdapter;

    public void setScoutingFromCursor(Cursor cursor) {
        TeamScouting2014 scouting = new TeamScouting2014();
        scouting.loadFromCursor(cursor);
        setScouting(scouting);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
        ButterKnife.inject(this, seasonView);
		shooterTypes.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                super.onCheckedChanged(group, checkedId);
                if (null != getScouting()) {
                    switch (checkedId) {
                        default:
                        case R.id.none:
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
                            break;
                        case R.id.dumper:
                            shooterGroup.setVisibility(View.VISIBLE);
                            lowGoal.setChecked(getScouting().getLowGoal());
                            highGoal.setVisibility(View.GONE);
                            highGoal.setChecked(false);
                            shootingDistanceGroup.setVisibility(View.GONE);
                            shootingDistance.setText("");
                            lowAuto.setVisibility(View.VISIBLE);
                            lowAuto.setChecked(getScouting().getLowAuto());
                            highAuto.setVisibility(View.GONE);
                            highAuto.setChecked(false);
                            hotAuto.setVisibility(View.GONE);
                            hotAuto.setChecked(false);
                            break;
                        case R.id.shooter:
                            shooterGroup.setVisibility(View.VISIBLE);
                            lowGoal.setChecked(getScouting().getLowGoal());
                            highGoal.setVisibility(View.VISIBLE);
                            highGoal.setChecked(getScouting().getHighGoal());
                            shootingDistanceGroup.setVisibility(View.VISIBLE);
                            if (0 != getScouting().getShootingDistance()) {
                                shootingDistance.setText(Double.toString(getScouting().getShootingDistance()));
                            }
                            lowAuto.setChecked(getScouting().getLowAuto());
                            lowAuto.setVisibility(View.VISIBLE);
                            highAuto.setChecked(getScouting().getHighAuto());
                            highAuto.setVisibility(View.VISIBLE);
                            hotAuto.setChecked(getScouting().getHotAuto());
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
	public void onStart() {
		super.onStart();
        EventBus.getDefault().register(this);
	}

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
		orientation.setText(getScouting().getOrientation());
		driveTrain.setText(getScouting().getDriveTrain());
		//check if its 0, if so empty the string so the user doesnt go crazy
		if(0!=getScouting().getWidth()) {
			num = Double.toString(getScouting().getWidth());
			width.setText(num);
		}
		if(0!=getScouting().getLength()) {
			num = Double.toString(getScouting().getLength());
			length.setText(num);
		}
		if(0!=getScouting().getHeightShooter()) {
			num = Double.toString(getScouting().getHeightShooter());
			heightShooter.setText(num);
		}
		if(0!=getScouting().getHeightMax()) {
			num = Double.toString(getScouting().getHeightMax());
			heightMax.setText(num);
		}
        switch(getScouting().getShooterType()) {
            case 0:
                shooterTypes.programaticallyCheck(R.id.none);
                break;
            case 1:
                shooterTypes.programaticallyCheck(R.id.dumper);
                break;
            case 2:
                shooterTypes.programaticallyCheck(R.id.shooter);
                break;
        }
		lowGoal.setChecked(getScouting().getLowGoal());
        highGoal.setChecked(getScouting().getHighGoal());
        if(0!=getScouting().getShootingDistance()) {
            num = Double.toString(getScouting().getShootingDistance());
            shootingDistance.setText(num);
        }
        passGround.setChecked(getScouting().getPassGround());
        passAir.setChecked(getScouting().getPassAir());
        passTruss.setChecked(getScouting().getPassTruss());
        pickupGround.setChecked(getScouting().getPickupGround());
        pickupCatch.setChecked(getScouting().getPickupCatch());
        pusher.setChecked(getScouting().getPusher());
		blocker.setChecked(getScouting().getBlocker());
        humanPlayer.setRating(getScouting().getHumanPlayer().floatValue());
        noAuto.setChecked(getScouting().getNoAuto());
        driveAuto.setChecked(getScouting().getDriveAuto());
        lowAuto.setChecked(getScouting().getLowAuto());
        highAuto.setChecked(getScouting().getHighAuto());
        hotAuto.setChecked(getScouting().getHotAuto());
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
		getScouting().setOrientation(orientation.getText().toString());
		getScouting().setDriveTrain(driveTrain.getText().toString());
		getScouting().setWidth(Utility.getDoubleFromText(width.getText()));
		getScouting().setLength(Utility.getDoubleFromText(length.getText()));
		getScouting().setHeightShooter(Utility.getDoubleFromText(heightShooter.getText()));
		getScouting().setHeightMax(Utility.getDoubleFromText(heightMax.getText()));
        switch(shooterTypes.getCheckedRadioButtonId()) {
            case R.id.none:
                getScouting().setShooterType(0);
                break;
            case R.id.dumper:
                getScouting().setShooterType(1);
                break;
            case R.id.shooter:
                getScouting().setShooterType(2);
                break;
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
        getScouting().setHumanPlayer((double)humanPlayer.getRating());
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
                List<TeamScouting2014> orientations = new Select().from(TeamScouting2014.class).groupBy(TeamScouting2014.ORIENTATION).execute();
                EventBus.getDefault().post(new LoadOrientations(orientations));
            }
        });
    }
    public void onEventMainThread(TeamScouting2014 scoutingChanged) {
        loadOrientations();
        loadDriveTrains();
    }
    public void onEventMainThread(LoadOrientations orientations) {
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
                List<TeamScouting2014> driveTrains = new Select().from(TeamScouting2014.class).groupBy(TeamScouting2014.DRIVE_TRAIN).execute();
                EventBus.getDefault().post(new LoadDriveTrains(driveTrains));
            }
        });
    }
    public void onEventMainThread(LoadDriveTrains driveTrains) {
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
}
