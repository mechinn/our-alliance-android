package com.mechinn.android.ouralliance.view;

import java.util.Collections;
import java.util.List;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TeamCursorAdapter extends CursorAdapter {
		private static final String TAG = "TeamCursorAdapter";
	 
		public TeamCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Log.d(TAG, "binding view");
			Team team = TeamDataSource.cursorToTeam(cursor);
			TextView summary = (TextView)view.findViewById(R.id.team_list_item);
			summary.setText(team.toString());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			Log.d(TAG, "creating view");
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.fragment_team_list, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
