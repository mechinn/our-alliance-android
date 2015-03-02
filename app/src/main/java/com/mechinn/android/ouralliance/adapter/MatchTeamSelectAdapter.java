package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.util.List;

public class MatchTeamSelectAdapter extends BaseAdapter implements SpinnerAdapter {
    public static final String TAG = "MatchTeamSelectAdapter";

    private Context context;
    private List<EventTeam> teams;
    private LayoutInflater inflater;
    private int team;
    private SparseIntArray disabled;

    public MatchTeamSelectAdapter(Context context, List<EventTeam> teams, int team) {
        this.context = context;
        inflater = LayoutInflater.from(getContext());
        swapList(teams);
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

    public LayoutInflater getInflater() {
        return inflater;
    }

    public List<EventTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<EventTeam> teams) {
        this.teams = teams;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void swapList(List<EventTeam> teams) {
        this.teams = teams;
        this.notifyDataSetChanged();
    }

    public boolean isEmpty() {
        if(null!=teams) {
            return teams.size()<1;
        } else {
            return true;
        }
    }

    @Override
    public int getCount() {
        if(isEmpty()) {
            return 0;
        }
        return teams.size();
    }

    @Override
    public EventTeam getItem(int position) {
        if(isEmpty()) {
            return null;
        }

        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
