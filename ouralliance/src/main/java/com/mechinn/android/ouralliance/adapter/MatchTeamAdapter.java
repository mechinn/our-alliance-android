package com.mechinn.android.ouralliance.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MatchTeamAdapter extends BaseAdapter {
	public static final String TAG = MatchTeamAdapter.class.getSimpleName();
	Context context;
	List<CompetitionTeam> teams;
	
	public MatchTeamAdapter(Context context, Match match) {
		this.context = context;
        swapMatch(match);
	}
	
	public void swapMatch(Match match) {
        teams = new ArrayList<CompetitionTeam>();
        if(null!=match) {
            teams.add(match.getRed1());
            teams.add(match.getRed2());
            teams.add(match.getRed3());
            teams.add(match.getBlue1());
            teams.add(match.getBlue2());
            teams.add(match.getBlue3());
        }
        this.notifyDataSetChanged();
	}

    public boolean isEmpty() {
        return teams.size()<1;
    }
	
	public int getCount() {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView container = (TextView) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
		    container.setText(teams.get(position).getTeam().toString());
        }
		return container;
	}
}
