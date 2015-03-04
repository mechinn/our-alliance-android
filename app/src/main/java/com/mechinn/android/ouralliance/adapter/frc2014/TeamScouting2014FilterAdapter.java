package com.mechinn.android.ouralliance.adapter.frc2014;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mechinn.android.ouralliance.adapter.FilterableAdapter;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TeamScouting2014FilterAdapter extends FilterableAdapter {
    public static final String TAG = "TS2014Filter";
    public enum Type {ORIENTATION,DRIVETRAIN}
    Type field;

	public TeamScouting2014FilterAdapter(Context context, List<TeamScouting2014> teams, Type field) {
		super(context);
        this.field = field;
        swapList(teams);
	}
	
	public void swapList(List<TeamScouting2014> teams) {
        emptyStrings();
        if(!isEmpty()) {
            for(TeamScouting2014 each : teams) {
                Timber.d("scouting " + each);
                switch(field) {
                    case ORIENTATION:
                        Timber.d("orientation "+each);
                        addString(each.getOrientation());
                        break;
                    case DRIVETRAIN:
                        Timber.d("drivetrain "+each);
                        addString(each.getDriveTrain());
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
