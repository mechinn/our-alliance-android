package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;

import se.emilsjolander.sprinkles.ModelList;

public class MatchTeamSelectAdapter extends CompetitionTeamAdapter {
    public static final String TAG = "MatchTeamSelectAdapter";
    public static final int RED1 = 0;
    public static final int RED2 = 1;
    public static final int RED3 = 2;
    public static final int BLUE1 = 3;
    public static final int BLUE2 = 4;
    public static final int BLUE3 = 5;

    private int team;
    private SparseIntArray disabled;

    public MatchTeamSelectAdapter(Context context, ModelList<CompetitionTeam> teams, int team) {
        super(context, teams);
        this.team = team;
        disabled = new SparseIntArray(5);
    }

    public void disablePosition(int team, int position) {
        if(this.team!=team) {
            disabled.put(team, position);
        }
    }

    public int getTeam() {
        return team;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        for(int i=0;i<disabled.size();++i) {
            if(position==disabled.valueAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(!isEmpty() && isEnabled(position)) {
            view = (TextView) getInflater().inflate(android.R.layout.simple_spinner_dropdown_item, viewGroup, false);
            ((TextView)view).setText(getItem(position).getTeam().toString());
            return view;
        } else {
            return getInflater().inflate(R.layout.null_item, null);
        }
    }
}
