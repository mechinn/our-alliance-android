package com.mechinn.android.ouralliance.view;

import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MatchTeamCursorAdapter extends BaseAdapter {
	public static final String TAG = MatchTeamCursorAdapter.class.getSimpleName();
	Context context;
	List<Team> teams;
	
	public MatchTeamCursorAdapter(Context context, Match match) {
		this.context = context;
		if(null!=match) {
			teamsFromMatch(match);
		}
	}
	
	private void teamsFromMatch(Match match) {
		teams = new ArrayList<Team>();
		teams.add(match.getRed1());
		teams.add(match.getRed2());
		teams.add(match.getRed3());
		teams.add(match.getBlue1());
		teams.add(match.getBlue2());
		teams.add(match.getBlue3());
	}
	
	public void swapMatch(Match match) {
		teamsFromMatch(match);
	}
	
	public int getCount() {
		if(null==teams) {
			return 0;
		}
		return teams.size();
	}

	@Override
	public Object getItem(int position) {
		if(null==teams) {
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
		if(null==convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			container = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		container.setText(teams.get(position).toString());
		return container;
	}
}
