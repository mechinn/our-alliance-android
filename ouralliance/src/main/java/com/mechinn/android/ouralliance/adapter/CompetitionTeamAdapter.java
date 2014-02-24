package com.mechinn.android.ouralliance.adapter;

import com.mechinn.android.ouralliance.data.CompetitionTeam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import se.emilsjolander.sprinkles.ModelList;

public abstract class CompetitionTeamAdapter extends BaseAdapter {
	public static final String TAG = CompetitionTeamAdapter.class.getSimpleName();

    private Context context;
    private ModelList<CompetitionTeam> teams;
    private LayoutInflater inflater;

    public CompetitionTeamAdapter(Context context, ModelList<CompetitionTeam> teams) {
        this.context = context;
        inflater = LayoutInflater.from(getContext());
        swapList(teams);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public ModelList<CompetitionTeam> getTeams() {
        return teams;
    }

    public void setTeams(ModelList<CompetitionTeam> teams) {
        this.teams = teams;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void swapList(ModelList<CompetitionTeam> teams) {
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
    public CompetitionTeam getItem(int position) {
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
