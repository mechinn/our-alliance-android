package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MatchCursorAdapter extends CursorAdapter {
	public static final String TAG = MatchCursorAdapter.class.getSimpleName();
	 
	public MatchCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Match match = Match.newFromCursor(cursor);
		TextView summary = (TextView)view.findViewById(android.R.id.text1);
		TextView subText = (TextView)view.findViewById(android.R.id.text2);
		summary.setText(match.toString());
		subText.setText(match.getTeams());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		bindView(v, context, cursor);
		return v;
	}
	
	public Match get(int position) {
		Cursor cursor = this.getCursor();
		if(cursor.moveToPosition(position)) {
			return Match.newFromCursor(cursor);
		}
		return null;
	}
	
	public Competition getComp() throws OurAllianceException {
		if(this.getCursor().getCount()>0) {
			return get(0).getCompetition();
		} else {
			throw new OurAllianceException("No compettion");
		}
	}
}
