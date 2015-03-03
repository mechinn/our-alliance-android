package com.mechinn.android.ouralliance.adapter.frc2015;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mechinn.android.ouralliance.adapter.FilterableAdapter;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;

import java.util.List;

public class TeamScouting2015FilterAdapter extends FilterableAdapter {
    public static final String TAG = "TS2015Filter";
    public enum Type {ORIENTATION,DRIVETRAIN,MECHANISM}
    Type field;

	public TeamScouting2015FilterAdapter(Context context, List<TeamScouting2015> teams, Type field) {
		super(context);
        this.field = field;
        swapList(teams);
	}

	public void swapList(List<TeamScouting2015> teams) {
        emptyStrings();
        if(null!=teams && teams.size()>0) {
            for(TeamScouting2015 each : teams) {
                Log.d(TAG,"scouting "+each);
                switch(field) {
                    case ORIENTATION:
                        Log.d(TAG,"orientation "+each);
                        addString(each.getOrientation());
                        break;
                    case DRIVETRAIN:
                        Log.d(TAG,"drivetrain "+each);
                        addString(each.getDriveTrain());
                        break;
                    case MECHANISM:
                        Log.d(TAG,"drivetrain "+each);
                        addString(each.getPickupMechanism());
                        break;
                }
            }
        }
        this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView container = (TextView) convertView;
		if(!isEmpty()) {
            if(null==convertView) {
                container = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            container.setText(getItem(position));
		}
		return container;
	}


}
