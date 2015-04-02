package com.mechinn.android.ouralliance.fragment.frc2015;

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
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.frc2015.TeamScouting2015FilterAdapter;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

public class TeamDetail2015 extends TeamDetailFragment {
    public static final String TAG = "TeamDetail2015";
    public static final int maxPerimeter = 140;
    public static final int maxHeight = 78;
    public static final int maxDistance = 9999;

    private NumberPicker[] pickers;
	private AutoCompleteTextView orientation;
    private AutoCompleteTextView driveTrain;
    private EditText width;
    private EditText length;
    private EditText height;
    private CheckBox coop;
    private RatingBar driverExperience;
    private AutoCompleteTextView mechanism;
    private NumberPicker maxToteStack;
    private NumberPicker maxTotesStackContainer;
    private NumberPicker maxTotesAndContainerLitter;
    private RatingBar humanPlayer;
    private CheckBox noAuto;
    private CheckBox driveAuto;
    private CheckBox toteAuto;
    private CheckBox containerAuto;
    private CheckBox stackedAuto;
    private NumberPicker landfillAuto;

	private TeamScouting2015FilterAdapter orientationsAdapter;
	private TeamScouting2015FilterAdapter driveTrainsAdapter;
    private TeamScouting2015FilterAdapter mechanismsAdapter;

    public TeamScouting2015 getScouting() {
        return (TeamScouting2015) super.getScouting();
    }
    public void setScouting(TeamScouting2015 scouting) {
        super.setScouting(scouting);
    }

    public void setScoutingFromCursor(Cursor cursor) {
        TeamScouting2015 scouting = new TeamScouting2015();
        scouting.loadFromCursor(cursor);
        setScouting(scouting);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2015, getSeason(), false);
        orientation = (AutoCompleteTextView) seasonView.findViewById(R.id.team2015orientation);
        driveTrain = (AutoCompleteTextView) seasonView.findViewById(R.id.team2015driveTrain);
        width = (EditText) seasonView.findViewById(R.id.team2015width);
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
        length = (EditText) seasonView.findViewById(R.id.team2015length);
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
        height = (EditText) seasonView.findViewById(R.id.team2015height);
        height.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (null != event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!event.isShiftPressed()) {
                        checkHeight();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });
        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkHeight();
                }
            }
        });
        coop = (CheckBox) seasonView.findViewById(R.id.team2015coop);
        driverExperience = (RatingBar) seasonView.findViewById(R.id.team2015driverExperience);
        mechanism = (AutoCompleteTextView) seasonView.findViewById(R.id.team2015mechanism);
        maxToteStack = (NumberPicker) seasonView.findViewById(R.id.team2015maxToteStack);
        maxTotesStackContainer = (NumberPicker) seasonView.findViewById(R.id.team2015maxTotesStackContainer);
        maxTotesAndContainerLitter = (NumberPicker) seasonView.findViewById(R.id.team2015maxTotesAndContainerLitter);
        humanPlayer = (RatingBar) seasonView.findViewById(R.id.team2015humanPlayer);
        noAuto = (CheckBox) seasonView.findViewById(R.id.team2015noAuto);
        driveAuto = (CheckBox) seasonView.findViewById(R.id.team2015driveAuto);
        toteAuto = (CheckBox) seasonView.findViewById(R.id.team2015toteAuto);
        containerAuto = (CheckBox) seasonView.findViewById(R.id.team2015containerAuto);
        stackedAuto = (CheckBox) seasonView.findViewById(R.id.team2015stackedAuto);
        landfillAuto = (NumberPicker) seasonView.findViewById(R.id.team2015landfillAuto);
        String[] nums = new String[100];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }
        pickers = new NumberPicker[] {maxToteStack, maxTotesStackContainer, maxTotesAndContainerLitter, landfillAuto};
        for(NumberPicker picker : pickers) {
            picker.setMinValue(0);
            picker.setMaxValue(99);
            picker.setDisplayedValues(nums);
            picker.setWrapSelectorWheel(false);
        }
		getSeason().addView(seasonView);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orientationsAdapter = new TeamScouting2015FilterAdapter(getActivity(), null, TeamScouting2015FilterAdapter.Type.ORIENTATION);
        orientation.setAdapter(orientationsAdapter);
        orientation.setThreshold(1);
        driveTrainsAdapter = new TeamScouting2015FilterAdapter(getActivity(), null, TeamScouting2015FilterAdapter.Type.DRIVETRAIN);
        driveTrain.setAdapter(driveTrainsAdapter);
        driveTrain.setThreshold(1);
        mechanismsAdapter = new TeamScouting2015FilterAdapter(getActivity(), null, TeamScouting2015FilterAdapter.Type.MECHANISM);
        mechanism.setAdapter(mechanismsAdapter);
        mechanism.setThreshold(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getPrefs().getYear() != 0 && getTeamId() != 0) {
            loadOrientations();
            loadDriveTrains();
            loadMechanisms();
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
                Wheel newWheel = new Wheel2015();
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
		if(null!=getScouting().getHeight() && getScouting().getHeight()>0) {
			num = Double.toString(getScouting().getHeight());
			height.setText(num);
		}
        if(null!=getScouting().getCoop()) {
            coop.setChecked(getScouting().getCoop());
        }
        if(null!=getScouting().getDriverExperience()) {
            driverExperience.setRating(getScouting().getDriverExperience());
        }
        mechanism.setText(getScouting().getPickupMechanism());
        if(null!=getScouting().getMaxToteStack()) {
            maxToteStack.setValue(getScouting().getMaxToteStack());
        }
        if(null!=getScouting().getMaxTotesStackContainer()) {
            maxTotesStackContainer.setValue(getScouting().getMaxTotesStackContainer());
        }
        if(null!=getScouting().getMaxTotesAndContainerLitter()) {
            maxTotesAndContainerLitter.setValue(getScouting().getMaxTotesAndContainerLitter());
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
        if(null!=getScouting().getToteAuto()) {
            toteAuto.setChecked(getScouting().getToteAuto());
        }
        if(null!=getScouting().getContainerAuto()) {
            containerAuto.setChecked(getScouting().getContainerAuto());
        }
        if(null!=getScouting().getStackedAuto()) {
            stackedAuto.setChecked(getScouting().getStackedAuto());
        }
        if(null!=getScouting().getLandfillAuto()) {
            landfillAuto.setValue(getScouting().getLandfillAuto());
        }
	}

    public void updateWheels() {
        for(int wheelNum=0;wheelNum<getWheels().getChildCount();wheelNum++) {
            LinearLayout wheelItem = (LinearLayout) getWheels().getChildAt(wheelNum);
            Wheel2015 wheel = (Wheel2015) wheelItem.getTag();
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
                    Timber.e(e,"Invalid wheel size");
                }
                try {
                    wheelCountInteger = Integer.parseInt(wheelCount.getText().toString());
                } catch (NumberFormatException e) {
                    Timber.e(e,"Invalid wheel count");
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
		getScouting().setHeight(Utility.getDoubleFromText(height.getText()));
		getScouting().setCoop(coop.isChecked());
        getScouting().setDriverExperience(driverExperience.getRating());
        getScouting().setPickupMechanism(mechanism.getText().toString());
		getScouting().setMaxToteStack(maxToteStack.getValue());
		getScouting().setMaxTotesStackContainer(maxTotesStackContainer.getValue());
		getScouting().setMaxTotesAndContainerLitter(maxTotesAndContainerLitter.getValue());
        getScouting().setHumanPlayer(humanPlayer.getRating());
        getScouting().setNoAuto(noAuto.isChecked());
        getScouting().setDriveAuto(driveAuto.isChecked());
        getScouting().setToteAuto(toteAuto.isChecked());
        getScouting().setContainerAuto(containerAuto.isChecked());
        getScouting().setStackedAuto(stackedAuto.isChecked());
        getScouting().setLandfillAuto(landfillAuto.getValue());
	}
	
	public void checkPerimeter() {
		try {
	        int widthVal = Integer.parseInt(width.getText().toString());
	        int lengthVal = Integer.parseInt(length.getText().toString());
	        int perimeter = 2*widthVal+2*lengthVal;
	        if(perimeter>maxPerimeter) {
	     	   Toast.makeText(TeamDetail2015.this.getActivity(), "Perimeter exceeds "+maxPerimeter+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkHeight() {
		try {
	        int shooterHeight = Integer.parseInt(height.getText().toString());
	        if(shooterHeight>maxHeight) {
	     	   Toast.makeText(TeamDetail2015.this.getActivity(), "Height exceeds "+maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

    public void loadOrientations() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2015> orientations = new Select().from(TeamScouting2015.class).where(TeamScouting2015.ORIENTATION+" IS NOT NULL").groupBy(TeamScouting2015.ORIENTATION).execute();
                if(null!=orientations) {
                    EventBus.getDefault().post(new LoadOrientations(orientations));
                } else {
                    Timber.d("null orientations");
                }
            }
        });
    }
    public void onEventMainThread(TeamScouting2015 scoutingChanged) {
        loadScouting();
        loadOrientations();
        loadDriveTrains();
    }
    public void onEventMainThread(LoadOrientations orientations) {
        Timber.d("orientations: "+orientations.getOrientations().size());
        orientationsAdapter.swapList(orientations.getOrientations());
    }
    private class LoadOrientations {
        List<TeamScouting2015> orientations;
        public LoadOrientations(List<TeamScouting2015> orientations) {
            this.orientations = orientations;
        }
        public List<TeamScouting2015> getOrientations() {
            return orientations;
        }
    }

    public void loadDriveTrains() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2015> driveTrains = new Select().from(TeamScouting2015.class).where(TeamScouting2015.DRIVE_TRAIN+" IS NOT NULL").groupBy(TeamScouting2015.DRIVE_TRAIN).execute();
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
        List<TeamScouting2015> driveTrains;
        public LoadDriveTrains(List<TeamScouting2015> driveTrains) {
            this.driveTrains = driveTrains;
        }
        public List<TeamScouting2015> getDriveTrains() {
            return driveTrains;
        }
    }
    public void loadMechanisms() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<TeamScouting2015> mechanisms = new Select().from(TeamScouting2015.class).where(TeamScouting2015.PICKUP_MECHANISM+" IS NOT NULL").groupBy(TeamScouting2015.PICKUP_MECHANISM).execute();
                if(null!=mechanisms) {
                    EventBus.getDefault().post(new LoadMechanisms(mechanisms));
                } else {
                    Timber.d("null mechanisms");
                }
            }
        });
    }
    public void onEventMainThread(LoadMechanisms mechanisms) {
        Timber.d("mechanisms: "+mechanisms.getMechanisms().size());
        mechanismsAdapter.swapList(mechanisms.getMechanisms());
    }
    private class LoadMechanisms {
        List<TeamScouting2015> mechanisms;
        public LoadMechanisms(List<TeamScouting2015> mechanisms) {
            this.mechanisms = mechanisms;
        }
        public List<TeamScouting2015> getMechanisms() {
            return mechanisms;
        }
    }
    public void loadScouting() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                TeamScouting2015 scouting = null;
                int year = getPrefs().getYear();
                try {
                    switch (year) {
                        case 2015:
                            scouting = new Select().from(TeamScouting2015.class).where(TeamScouting2015.TEAM+"=?", getTeamId()).executeSingle();
                            break;
                    }
                } catch(NullPointerException e) {
                    switch (year) {
                        case 2015:
                            Team team = Model.load(Team.class, getTeamId());
                            scouting = (TeamScouting2015) new TeamScouting2015();
                            scouting.setTeam(team);
                            break;
                    }
                }
                if(null!=scouting) {
                    EventBus.getDefault().post(new LoadScouting(scouting));
                }
            }
        });
    }
    public void onEventMainThread(LoadScouting scouting) {
        setScouting(scouting.getScouting());
        scoutingLoaded();
    }
    private class LoadScouting {
        TeamScouting2015 scouting;
        public LoadScouting(TeamScouting2015 scouting) {
            this.scouting = scouting;
        }
        public TeamScouting2015 getScouting() {
            return scouting;
        }
    }
    public void loadWheelTypes() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<Wheel> wheelTypes = new Select().from(Wheel2015.class).where(Wheel2015.WHEEL_TYPE+" IS NOT NULL").groupBy(Wheel2015.WHEEL_TYPE).execute();
                if (null != wheelTypes) {
                    EventBus.getDefault().post(new LoadWheelTypes(wheelTypes));
                } else {
                    Timber.d( "No wheels found ");
                }
            }
        });
    }
    public void onEventMainThread(Wheel2015 wheelsChanged) {
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
                List<Wheel> wheels = new Select().from(Wheel2015.class).where(Wheel2015.TEAM_SCOUTING+"=?", getScouting().getId()).execute();
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
