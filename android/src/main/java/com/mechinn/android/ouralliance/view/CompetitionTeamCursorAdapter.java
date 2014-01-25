package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mobeta.android.dslv.DragSortCursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CompetitionTeamCursorAdapter extends DragSortCursorAdapter {
	public static final String TAG = CompetitionTeamCursorAdapter.class.getSimpleName();
	private CompetitionTeamDataSource competitionTeamData;
    private Context mContext;

	public CompetitionTeamCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mContext = context;
		competitionTeamData = new CompetitionTeamDataSource(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        mContext = context;
		CompetitionTeam team = CompetitionTeam.newFromCursor(cursor);
		TextView summary = (TextView)view.findViewById(R.id.text);
		summary.setText(team.getTeam().toString());
		CheckBox scouted = (CheckBox)view.findViewById(R.id.scouted);
		scouted.setTag(team);
		scouted.setChecked(team.isScouted());
		scouted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox check = (CheckBox) v;
				CompetitionTeam team = (CompetitionTeam) v.getTag();
				team.setScouted(check.isChecked());
				try {
					competitionTeamData.update(team);
				} catch (OurAllianceException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_item, parent, false);
		bindView(v, context, cursor);
		return v;
	}
	
	@Override
	public void drop(int from, int to) {
		super.drop(from, to);
		for(int position=0;position<getCursorPositions().size();++position) {
        	updatePosition(position);
        }
		this.notifyDataSetChanged();
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
	
	private void updatePosition(int position) {
		Log.d(TAG, "map: "+getCursorPositions().get(position));
		if(null!=getCursorPositions().get(position)) {
    		CompetitionTeam team = this.get(getCursorPositions().get(position));
    		if(position==team.getRank()) {
    			return;
    		}
    		team.setRank(position);
    		try {
				competitionTeamData.update(team);
				return;
			} catch (OurAllianceException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		Toast.makeText(this.mContext, "Could not save rank of "+team, Toast.LENGTH_SHORT).show();
		}
	}
}
