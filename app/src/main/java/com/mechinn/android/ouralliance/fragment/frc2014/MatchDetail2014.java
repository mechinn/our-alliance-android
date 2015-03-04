package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.util.Log;
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
import timber.log.Timber;

public class MatchDetail2014 extends MatchDetailFragment {
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
    public MatchScouting2014 getMatch() {
        return (MatchScouting2014) super.getMatch();
    }
    public void setMatch(MatchScouting2014 match) {
        super.setMatch(match);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_match_detail_2014, getSeason(), false);
        hotShots = (NumberPicker) seasonView.findViewById(R.id.match2014hotShots);
        shotsMade = (NumberPicker) seasonView.findViewById(R.id.match2014shotsMade);
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
	public void setView() {
		super.setView();
        if(null!=getMatch().getHotShots()) {
            hotShots.setValue(getMatch().getHotShots());
        }
        if(null!=getMatch().getShotsMade()) {
            shotsMade.setValue(getMatch().getShotsMade());
        }
        if(null!=getMatch().getShotsMissed()) {
            shotsMissed.setValue(getMatch().getShotsMissed());
        }
        if(null!=getMatch().getMoveForward()) {
            moveFwd.setRating(getMatch().getMoveForward());
        }
        if(null!=getMatch().getShooter()) {
            shooter.setChecked(getMatch().getShooter());
        }
        if(null!=getMatch().getCatcher()) {
            catcher.setChecked(getMatch().getCatcher());
        }
        if(null!=getMatch().getPasser()) {
            passer.setChecked(getMatch().getPasser());
        }
        if(null!=getMatch().getDriveTrainRating()) {
            driveTrain.setRating(getMatch().getDriveTrainRating());
        }
        if(null!=getMatch().getBallAccuracyRating()) {
            ballAccuracy.setRating(getMatch().getBallAccuracyRating());
        }
        if(null!=getMatch().getGround()) {
            ground.setChecked(getMatch().getGround());
        }
        if(null!=getMatch().getOverTruss()) {
            overTruss.setChecked(getMatch().getOverTruss());
        }
        if(null!=getMatch().getLow()) {
            low.setChecked(getMatch().getLow());
        }
        if(null!=getMatch().getHigh()) {
            high.setChecked(getMatch().getHigh());
        }
	}
	
	@Override
	public void updateMatch() {
		super.updateMatch();
        getMatch().setHotShots(hotShots.getValue());
        getMatch().setShotsMade(shotsMade.getValue());
        getMatch().setShotsMissed(shotsMissed.getValue());
        getMatch().setMoveForward(moveFwd.getRating());
        getMatch().setShooter(shooter.isChecked());
        getMatch().setCatcher(catcher.isChecked());
        getMatch().setPasser(passer.isChecked());
        getMatch().setDriveTrainRating(driveTrain.getRating());
        getMatch().setBallAccuracyRating(ballAccuracy.getRating());
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
                    if(null!=scouting) {
                        EventBus.getDefault().post(new LoadMatchScouting(scouting));
                    } else {
                        Timber.d("match scouting null " + getScoutingId());
                    }
                } else {
                    Timber.d("match scouting id 0 == "+getScoutingId());
                }
            }
        });
    }
    public void onEventMainThread(MatchScouting2014 scoutingChanged) {
        loadMatchScouting();
    }
    public void onEventMainThread(LoadMatchScouting scouting) {
        MatchScouting2014 result = scouting.getScouting();
        Timber.d( "result: " + result);
        setMatch(result);
        setView();
        getRootView().setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }
    protected class LoadMatchScouting {
        MatchScouting2014 scouting;
        public LoadMatchScouting(MatchScouting2014 scouting) {
            this.scouting = scouting;
        }
        public MatchScouting2014 getScouting() {
            return scouting;
        }
    }
}
