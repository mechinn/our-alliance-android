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

import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

import java.util.ArrayList;
import java.util.List;

public class TeamScouting2014FilterAdapter extends BaseAdapter implements Filterable {
    public static final String TAG = "TS2014Filter";
    public enum Type {ORIENTATION,DRIVETRAIN}
	Context context;
    List<TeamScouting2014> teams;
    Type field;
	List<String> original;
    List<String> filtered;
    TeamScouting2014Filter filter;

	public TeamScouting2014FilterAdapter(Context context, List<TeamScouting2014> teams, Type field) {
		this.context = context;
        this.field = field;
        this.filter = new TeamScouting2014Filter();
        swapList(teams);
	}
	
	public void swapList(List<TeamScouting2014> teams) {
        this.teams = teams;
		this.original = new ArrayList<>();
        if(!isEmpty()) {
            for(TeamScouting2014 each : this.teams) {
                Log.d(TAG,"scouting "+each);
                switch(field) {
                    case ORIENTATION:
                        Log.d(TAG,"orientation "+each);
                        original.add(each.getOrientation());
                        break;
                    case DRIVETRAIN:
                        Log.d(TAG,"drivetrain "+each);
                        original.add(each.getDriveTrain());
                        break;
                }
            }
        }
        this.notifyDataSetChanged();
	}

    public boolean isEmpty() {
        if(null!=teams) {
            return teams.size()<1;
        } else {
            return true;
        }
    }
	
	public int getCount() {
		if(isEmpty()||null==filtered) {
			return 0;
		}
		return filtered.size();
	}

	@Override
	public CharSequence getItem(int position) {
		if(isEmpty()||null==filtered) {
			return null;
		}
		return filtered.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView container = (TextView) convertView;
		if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            container.setText(filtered.get(position).toString());
		}
		return container;
	}

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class TeamScouting2014Filter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {

            Filter.FilterResults results = new Filter.FilterResults();
            //If there's nothing to filter on, return the original data for your list
            if(charSequence == null || charSequence.length() == 0) {
                results.values = original;
                results.count = original.size();
            } else {
                String filteringString = charSequence.toString().toLowerCase();
                Log.d(TAG, "filtering on "+filteringString);
                List<String> filterResultsData = new ArrayList<>();
                for(String data : original) {
                    //In this loop, you'll filter through originalData and compare each item to charSequence.
                    //If you find a match, add it to your new ArrayList
                    //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                    Log.d(TAG, "compare "+data);
                    if(null!=data && data.toLowerCase().contains(filteringString)) {
                        filterResultsData.add(data);
                    }
                }
                results.values = filterResultsData;
                results.count = filterResultsData.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            filtered = (ArrayList<String>)filterResults.values;
            notifyDataSetChanged();
        }
    };
}
