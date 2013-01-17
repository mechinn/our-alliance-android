package com.mechinn.android.ouralliance.view;

import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Team;

import android.content.Context;
import android.graphics.Color;
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
			this.items = items;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(resource, null);
			}
	 
			Team o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(textViewResource);
				if (tt != null) {
					tt.setText(o.toString());
				}
			}
			return v;
		}
	}
