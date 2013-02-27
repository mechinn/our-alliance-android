package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.provider.DataProvider;
import com.mobeta.android.dslv.DragSortCursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CompetitionTeamCursorAdapter extends DragSortCursorAdapter {
	public static final String TAG = CompetitionTeamCursorAdapter.class.getSimpleName();
	 
		public CompetitionTeamCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			CompetitionTeam team = CompetitionTeam.newFromCursor(cursor);
			TextView summary = (TextView)view.findViewById(R.id.text);
			summary.setText(team.getTeam().toString());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
		
		public CompetitionTeam get(int position) {
			Cursor cursor = this.getCursor();
			if(cursor.moveToPosition(position)) {
				return CompetitionTeam.newFromCursor(cursor);
			}
			return null;
		}
		
		public boolean contains(CompetitionTeam team) {
			return CompetitionTeamDataSource.contains(this.getCursor(), team);
		}
		
		public boolean contains(Team team) {
			return CompetitionTeamDataSource.contains(this.getCursor(), team);
		}
		
		public Competition getComp() throws OurAllianceException {
			if(this.getCursor().getCount()>0) {
				return get(0).getCompetition();
			} else {
				throw new OurAllianceException("No compettion");
			}
		}
	}
