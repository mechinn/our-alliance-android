package com.mechinn.android.ouralliance.fragment.frc2014;

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
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;
import com.mechinn.android.ouralliance.widget.UncheckableRadioButton;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

import se.emilsjolander.sprinkles.Query;

public class MatchDetail2014 extends MatchDetailFragment<MatchScouting2014> {
    public static final String TAG = "MatchDetail2014";

    NumberPicker hotShots;
    NumberPicker shotsMade;
    NumberPicker shotsMissed;
    RatingBar moveFwd;
    CheckBox shooter;
    CheckBox catcher;
    CheckBox passer;
    RatingBar driveTrain;
    RatingBar ballAccuracy;
    CheckBox ground;
    CheckBox overTruss;
    CheckBox low;
    CheckBox high;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_match_detail_2014, getSeason(), false);
        hotShots = (NumberPicker) seasonView.findViewById(R.id.match2014hotShots);
        shotsMade = (NumberPicker) seasonView.findViewById(R.id.match2014shotsMade);
        shotsMissed = (NumberPicker) seasonView.findViewById(R.id.match2014shotsMissed);
        String[] nums = new String[100];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }
        hotShots.setMinValue(0);
        shotsMade.setMinValue(0);
        shotsMissed.setMinValue(0);
        hotShots.setMaxValue(99);
        shotsMade.setMaxValue(99);
        shotsMissed.setMaxValue(99);
        hotShots.setDisplayedValues(nums);
        shotsMade.setDisplayedValues(nums);
        shotsMissed.setDisplayedValues(nums);
        hotShots.setWrapSelectorWheel(false);
        shotsMade.setWrapSelectorWheel(false);
        shotsMissed.setWrapSelectorWheel(false);
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
            Query.one(MatchScouting2014.class,"select * from "+MatchScouting2014.TAG+" where "+MatchScouting2014._ID+"=?",this.getTeamId()).getAsync(getLoaderManager(),getOnMatchLoaded());
        }
    }
	
	@Override
	public void setView() {
		super.setView();
        hotShots.setValue(getMatch().getHotShots());
        shotsMade.setValue(getMatch().getShotsMade());
        shotsMissed.setValue(getMatch().getShotsMissed());
        moveFwd.setRating((float)getMatch().getMoveFwd());
        shooter.setChecked(getMatch().isShooter());
        catcher.setChecked(getMatch().isCatcher());
        passer.setChecked(getMatch().isPasser());
        driveTrain.setRating((float)getMatch().getDriveTrain());
        ballAccuracy.setRating((float)getMatch().getBallAccuracy());
        ground.setChecked(getMatch().isGround());
        overTruss.setChecked(getMatch().isOverTruss());
        low.setChecked(getMatch().isLow());
        high.setChecked(getMatch().isHigh());
	}
	
	@Override
	public void updateMatch() {
		super.updateMatch();
        getMatch().setHotShots(hotShots.getValue());
        getMatch().setShotsMade(shotsMade.getValue());
        getMatch().setShotsMissed(shotsMissed.getValue());
        getMatch().setMoveFwd(moveFwd.getRating());
        getMatch().setShooter(shooter.isChecked());
        getMatch().setCatcher(catcher.isChecked());
        getMatch().setPasser(passer.isChecked());
        getMatch().setDriveTrain(driveTrain.getRating());
        getMatch().setBallAccuracy(ballAccuracy.getRating());
        getMatch().setGround(ground.isChecked());
        getMatch().setOverTruss(overTruss.isChecked());
        getMatch().setLow(low.isChecked());
        getMatch().setHigh(high.isChecked());
	}
}
