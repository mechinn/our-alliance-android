package com.mechinn.android.ouralliance.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;

import com.mechinn.android.ouralliance.fragment.frc2015.TeamAnalysisFragment2015;

import java.util.ArrayList;
import java.util.List;

public class TeamAnalysisPagerAdapter extends FragmentStatePagerAdapter {
    private ActionBarActivity context;
    private List<Fragment> fragments;
    public TeamAnalysisPagerAdapter(ActionBarActivity context) {
        super(context.getSupportFragmentManager());
        this.context = context;
        fragments = new ArrayList<>();
        for(int i=0;i<TeamAnalysisFragment2015.COUNT;++i) {
            fragments.add(TeamAnalysisFragment2015.newInstance(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TeamAnalysisFragment2015.positionColumn(position);
    }
}
