package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mechinn.android.ouralliance.data.MatchScouting;

import java.util.Collections;
import java.util.List;

public class MatchTeamAdapter<MatchTeam extends MatchScouting> extends BaseAdapter implements ListAdapter {
    public static final String TAG = "MatchTeamAdapter";
	Context context;
    List<MatchTeam> teams;
	
	public MatchTeamAdapter(Context context, List<MatchTeam> match) {
		this.context = context;
        swapMatch(match);
	}
	
	public void swapMatch(List<MatchTeam> match) {
        teams = match;
        if(null!=this.teams) {
            Collections.sort(this.teams);
        }
        this.notifyDataSetChanged();
	}

    public boolean isEmpty() {
        if(null!=teams) {
            return teams.size()<1;
        } else {
            return true;
        }
    }
	
	public int getCount() {
        if(isEmpty()) {
            return 0;
        }
		return teams.size();
	}

	@Override
	public MatchTeam getItem(int position) {
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
		    container.setText(teams.get(position).getTeamScouting().getTeam().toString());
            if(teams.get(position).isAlliance()) {
                container.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                container.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        }
		return container;
	}
}
