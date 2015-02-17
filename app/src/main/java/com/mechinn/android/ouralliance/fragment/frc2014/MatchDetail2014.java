package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RatingBar;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;
import com.mechinn.android.ouralliance.greenDao.MatchScouting2014;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MatchDetail2014 extends MatchDetailFragment<MatchScouting2014> {
    public static final String TAG = "MatchDetail2014";

    @InjectView(R.id.match2014hotShots) private NumberPicker hotShots;
    @InjectView(R.id.match2014shotsMade) private NumberPicker shotsMade;
    @InjectView(R.id.match2014shotsMissed) private NumberPicker shotsMissed;
    private NumberPicker[] shots;
    @InjectView(R.id.match2014moveFwd) private RatingBar moveFwd;
    @InjectView(R.id.match2014shooter) private CheckBox shooter;
    @InjectView(R.id.match2014catcher) private CheckBox catcher;
    @InjectView(R.id.match2014passer) private CheckBox passer;
    @InjectView(R.id.match2014driveTrain) private RatingBar driveTrain;
    @InjectView(R.id.match2014ballAccuracy) private RatingBar ballAccuracy;
    @InjectView(R.id.match2014ground) private CheckBox ground;
    @InjectView(R.id.match2014overTruss) private CheckBox overTruss;
    @InjectView(R.id.match2014low) private CheckBox low;
    @InjectView(R.id.match2014high) private CheckBox high;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_match_detail_2014, getSeason(), false);
        ButterKnife.inject(this,seasonView);
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
        if (this.getTeamId() != 0) {
            setOnMatchLoaded(getAsync().load(MatchScouting2014.class,this.getTeamId()));
        }
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
}
