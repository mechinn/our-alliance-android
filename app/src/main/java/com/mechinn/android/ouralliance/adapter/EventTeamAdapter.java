package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.mechinn.android.ouralliance.greenDao.EventTeam;

import java.util.List;

public abstract class EventTeamAdapter extends BaseAdapter {
    public static final String TAG = "EventTeamAdapter";

    private Context context;
    private List<EventTeam> teams;
    private LayoutInflater inflater;

    public EventTeamAdapter(Context context, List<EventTeam> teams) {
        this.context = context;
        inflater = LayoutInflater.from(getContext());
        swapList(teams);
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
