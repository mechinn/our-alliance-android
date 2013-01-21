package com.mechinn.android.ouralliance.view;

import java.util.Collections;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeamListAdapter extends ArrayAdapter<Team> {
	 
		private List<Team> items;
		private int resource;
		private int textViewResource;
		private Context c;
	 
		public TeamListAdapter(Context context, int resource, int textViewResource, List<Team> items) {
			super(context, resource, textViewResource, items);
			this.c = context;
			this.resource = resource;
			this.textViewResource = textViewResource;
			Collections.sort(items);
			this.items = items;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater)this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(resource, null);
			}
	 
			Team team = items.get(position);
			if (team != null) {
				TextView text = (TextView) view.findViewById(textViewResource);
				if (text != null) {
					text.setText(team.toString());
				}
			}
			return view;
		}
	}
