package com.mechinn.android.ouralliance.adapter.frc2014;

import android.content.Context;
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

import se.emilsjolander.sprinkles.ModelList;

public class TeamScouting2014Adapter extends BaseAdapter implements Filterable {
    public static final String TAG = "TeamScouting2014Adapter";
    public static final int ORIENTATION = 0;
    public static final int DRIVETRAIN = 1;
	Context context;
    ModelList<TeamScouting2014> teams;
    int field;
	List<CharSequence> original;
    List<CharSequence> filtered;

	public TeamScouting2014Adapter(Context context, ModelList<TeamScouting2014> teams, int field) {
		this.context = context;
        this.field = field;
        swapList(teams);
	}
	
	public void swapList(ModelList<TeamScouting2014> teams) {
        this.teams = teams;
		this.original = new ArrayList<CharSequence>();
        if(!isEmpty()) {
            for(TeamScouting2014 each : this.teams) {
                switch(field) {
                    case ORIENTATION:
                        original.add(each.getOrientation());
                        break;
                    case DRIVETRAIN:
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
	public Object getItem(int position) {
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
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();
                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0) {
                    results.values = original;
                    results.count = original.size();
                } else {
                    List<CharSequence> filterResultsData = new ArrayList<CharSequence>();
                    for(CharSequence data : original) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.toString().toLowerCase().matches(".*"+charSequence.toString().toLowerCase()+".*")) {
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtered = (ArrayList<CharSequence>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
