package com.mechinn.android.ouralliance.view;

import java.util.Collections;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.comparator.TeamScoutingTeamNumberOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeamScoutingListAdapter extends ArrayAdapter<TeamScouting> {
	 
		private List<TeamScouting> items;
		private int resource;
		private int textViewResource;
		private Context c;
	 
		public TeamScoutingListAdapter(Context context, int resource, int textViewResource, List<TeamScouting> items) {
			super(context, resource, textViewResource, items);
			this.c = context;
			this.resource = resource;
			this.textViewResource = textViewResource;
			Collections.sort(items, new TeamScoutingTeamNumberOrder());
			this.items = items;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(resource, null);
			}
	 
			Team o = items.get(position).getTeam();
			if (o != null) {
				TextView tt = (TextView) v.findViewById(textViewResource);
				if (tt != null) {
					tt.setText(o.toString());
				}
			}
			return v;
		}
	}
