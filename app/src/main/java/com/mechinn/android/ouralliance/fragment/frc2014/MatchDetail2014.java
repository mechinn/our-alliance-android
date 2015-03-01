package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RatingBar;

import com.activeandroid.Model;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class MatchDetail2014 extends MatchDetailFragment<MatchScouting2014> {
    public static final String TAG = "MatchDetail2014";

    private NumberPicker hotShots;
    private NumberPicker shotsMade;
    private NumberPicker shotsMissed;
    private NumberPicker[] shots;
    private RatingBar moveFwd;
    private CheckBox shooter;
    private CheckBox catcher;
    private CheckBox passer;
    private RatingBar driveTrain;
    private RatingBar ballAccuracy;
    private CheckBox ground;
    private CheckBox overTruss;
    private CheckBox low;
    private CheckBox high;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_match_detail_2014, getSeason(), false);
        hotShots = (NumberPicker) seasonView.findViewById(R.id.match2014hotShots);
        shotsMissed = (NumberPicker) seasonView.findViewById(R.id.match2014shotsMissed);
        moveFwd = (RatingBar) seasonView.findViewById(R.id.match2014moveFwd);
        shooter = (CheckBox) seasonView.findViewById(R.id.match2014shooter);
        catcher = (CheckBox) seasonView.findViewById(R.id.match2014catcher);
        passer = (CheckBox) seasonView.findViewById(R.id.match2014passer);
        driveTrain = (RatingBar) seasonView.findViewById(R.id.match2014driveTrain);
        ballAccuracy = (RatingBar) seasonView.findViewById(R.id.match2014ballAccuracy);
        ground = (CheckBox) seasonView.findViewById(R.id.match2014ground);
        overTruss = (CheckBox) seasonView.findViewById(R.id.match2014overTruss);
        low = (CheckBox) seasonView.findViewById(R.id.match2014low);
        high = (CheckBox) seasonView.findViewById(R.id.match2014high);
        String[] nums = new String[100];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }
        shots = new NumberPicker[] {hotShots, shotsMade, shotsMissed};
        for(NumberPicker picker : shots) {
            picker.setMinValue(0);
            picker.setMaxValue(99);
            picker.setDisplayedValues(nums);
            picker.setWrapSelectorWheel(false);
        }
        getSeason().addView(seasonView);
        return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}

    @Override
    public void onResume() {
        super.onResume();
        loadMatchScouting();
    }
	
	@Override
	public void setView() {
		super.setView();
        hotShots.setValue(getMatch().getHotShots());
        shotsMade.setValue(getMatch().getShotsMade());
        shotsMissed.setValue(getMatch().getShotsMissed());
        moveFwd.setRating(getMatch().getMoveForward().floatValue());
        shooter.setChecked(getMatch().getShooter());
        catcher.setChecked(getMatch().getCatcher());
        passer.setChecked(getMatch().getPasser());
        driveTrain.setRating(getMatch().getDriveTrainRating().floatValue());
        ballAccuracy.setRating(getMatch().getBallAccuracyRating().floatValue());
        ground.setChecked(getMatch().getGround());
        overTruss.setChecked(getMatch().getOverTruss());
        low.setChecked(getMatch().getLow());
        high.setChecked(getMatch().getHigh());
	}
	
	@Override
	public void updateMatch() {
		super.updateMatch();
        getMatch().setHotShots(hotShots.getValue());
        getMatch().setShotsMade(shotsMade.getValue());
        getMatch().setShotsMissed(shotsMissed.getValue());
        getMatch().setMoveForward((double)moveFwd.getRating());
        getMatch().setShooter(shooter.isChecked());
        getMatch().setCatcher(catcher.isChecked());
        getMatch().setPasser(passer.isChecked());
        getMatch().setDriveTrainRating((double)driveTrain.getRating());
        getMatch().setBallAccuracyRating((double)ballAccuracy.getRating());
        getMatch().setGround(ground.isChecked());
        getMatch().setOverTruss(overTruss.isChecked());
        getMatch().setLow(low.isChecked());
        getMatch().setHigh(high.isChecked());
	}

    public void loadMatchScouting() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                if (getScoutingId() != 0) {
                    MatchScouting2014 scouting = Model.load(MatchScouting2014.class, getScoutingId());
                    EventBus.getDefault().post(new LoadMatcheScouting(scouting));
                }
            }
        });
    }
}
