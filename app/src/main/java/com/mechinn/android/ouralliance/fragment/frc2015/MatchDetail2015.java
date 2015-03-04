package com.mechinn.android.ouralliance.fragment.frc2015;

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
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class MatchDetail2015 extends MatchDetailFragment {
    public static final String TAG = "MatchDetail2015";

    private NumberPicker[] pickers;
    private CheckBox autoStacked;
    private NumberPicker autoTotes;
    private NumberPicker autoContainers;
    private NumberPicker autoLandfill;
    private RatingBar autoMove;
    private CheckBox coop;
    private NumberPicker totes;
    private NumberPicker containers;
    private NumberPicker litter;
    private NumberPicker fowls;
    private NumberPicker humanAttempt;
    private NumberPicker humanSuccess;
    public MatchScouting2015 getMatch() {
        return (MatchScouting2015) super.getMatch();
    }
    public void setMatch(MatchScouting2015 match) {
        super.setMatch(match);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_match_detail_2015, getSeason(), false);
        autoStacked = (CheckBox) seasonView.findViewById(R.id.match2015autoStacked);
        autoTotes = (NumberPicker) seasonView.findViewById(R.id.match2015autoTotes);
        autoContainers = (NumberPicker) seasonView.findViewById(R.id.match2015autoContainers);
        autoLandfill = (NumberPicker) seasonView.findViewById(R.id.match2015autoLandfill);
        autoMove = (RatingBar) seasonView.findViewById(R.id.match2015autoMove);
        coop = (CheckBox) seasonView.findViewById(R.id.match2015coop);
        totes = (NumberPicker) seasonView.findViewById(R.id.match2015totes);
        containers = (NumberPicker) seasonView.findViewById(R.id.match2015containers);
        litter = (NumberPicker) seasonView.findViewById(R.id.match2015litter);
        fowls = (NumberPicker) seasonView.findViewById(R.id.match2015fowls);
        humanAttempt = (NumberPicker) seasonView.findViewById(R.id.match2015humanAttempt);
        humanSuccess = (NumberPicker) seasonView.findViewById(R.id.match2015humanSuccess);
        String[] nums = new String[100];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }
        pickers = new NumberPicker[] {autoTotes, autoContainers, autoLandfill, totes, containers, litter, fowls, humanAttempt, humanSuccess};
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
	public void setView() {
		super.setView();
        if(null!=getMatch().getAutoStacked()) {
            autoStacked.setChecked(getMatch().getAutoStacked());
        }
        if(null!=getMatch().getAutoTotes()) {
            autoTotes.setValue(getMatch().getAutoTotes());
        }
        if(null!=getMatch().getAutoContainers()) {
            autoContainers.setValue(getMatch().getAutoContainers());
        }
        if(null!=getMatch().getAutoLandfill()) {
            autoLandfill.setValue(getMatch().getAutoLandfill());
        }
        if(null!=getMatch().getAutoMove()) {
            autoMove.setRating(getMatch().getAutoMove());
        }
        if(null!=getMatch().getCoop()) {
            coop.setChecked(getMatch().getCoop());
        }
        if(null!=getMatch().getTotes()) {
            totes.setValue(getMatch().getTotes());
        }
        if(null!=getMatch().getContainers()) {
            containers.setValue(getMatch().getContainers());
        }
        if(null!=getMatch().getLitter()) {
            litter.setValue(getMatch().getLitter());
        }
        if(null!=getMatch().getFowls()) {
            fowls.setValue(getMatch().getFowls());
        }
        if(null!=getMatch().getHumanAttempt()) {
            humanAttempt.setValue(getMatch().getHumanAttempt());
        }
        if(null!=getMatch().getHumanSuccess()) {
            humanSuccess.setValue(getMatch().getHumanSuccess());
        }
	}
	
	@Override
	public void updateMatch() {
		super.updateMatch();
        getMatch().setAutoStacked(autoStacked.isChecked());
        getMatch().setAutoTotes(autoTotes.getValue());
        getMatch().setAutoContainers(autoContainers.getValue());
        getMatch().setAutoLandfill(autoLandfill.getValue());
        getMatch().setAutoMove(autoMove.getRating());
        getMatch().setCoop(coop.isChecked());
        getMatch().setTotes(totes.getValue());
        getMatch().setContainers(containers.getValue());
        getMatch().setLitter(litter.getValue());
        getMatch().setFowls(fowls.getValue());
        getMatch().setHumanAttempt(humanAttempt.getValue());
        getMatch().setHumanSuccess(humanSuccess.getValue());
	}

    public void loadMatchScouting() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                if (getScoutingId() != 0) {
                    MatchScouting2015 scouting = Model.load(MatchScouting2015.class, getScoutingId());
                    if(null!=scouting) {
                        EventBus.getDefault().post(new LoadMatchScouting(scouting));
                    } else {
                        Log.d(TAG,"match scouting null "+getScoutingId());
                    }
                } else {
                    Log.d(TAG,"match scouting id 0 == "+getScoutingId());
                }
            }
        });
    }
    public void onEventMainThread(MatchScouting2015 scoutingChanged) {
        loadMatchScouting();
    }
    public void onEventMainThread(LoadMatchScouting scouting) {
        MatchScouting2015 result = scouting.getScouting();
        Log.d(TAG, "result: " + result);
        setMatch(result);
        setView();
        getRootView().setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }
    protected class LoadMatchScouting {
        MatchScouting2015 scouting;
        public LoadMatchScouting(MatchScouting2015 scouting) {
            this.scouting = scouting;
        }
        public MatchScouting2015 getScouting() {
            return scouting;
        }
    }
}
