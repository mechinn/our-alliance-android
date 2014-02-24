package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.fragment.MatchDetailFragment;

import se.emilsjolander.sprinkles.Query;

public class MatchDetail2014 extends MatchDetailFragment<MatchScouting2014> {
	public final static String TAG = MatchDetail2014.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
		Log.wtf(TAG, "2014 match scouting not implemented yet");
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();

	}

    @Override
    public void onResume() {
        super.onResume();
        if (this.getMatchId() != 0) {
            Query.one(MatchScouting2014.class,"select * from MatchScouting2014 where match=?",this.getMatchId()).getAsync(getLoaderManager(),getOnMatchLoaded());
        }
    }
	
	@Override
	public void setView() {
		super.setView();
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
	}
}
