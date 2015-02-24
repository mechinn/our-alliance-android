package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Wheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WheelTypesAdapter extends BaseAdapter implements Filterable {
    public static final String TAG = "TeamScoutingWheelAdapter";
	Context context;
    List<? extends Wheel> wheels;
    int field;
	List<CharSequence> original;
    List<CharSequence> filtered;

	public WheelTypesAdapter(Context context, List<? extends Wheel> wheels, int field) {
		this.context = context;
        this.field = field;
        swapList(wheels);
	}
	
	public void swapList(List<? extends Wheel> wheels) {
        this.wheels = wheels;
        this.original = new ArrayList<>();
        if(null!=this.wheels) {
            Collections.sort(this.wheels);
            for(Wheel each : this.wheels) {
                switch(field) {
                    case R.id.wheelType:
                        original.add(each.getWheelType());
                        break;
                }
            }
        }
        this.notifyDataSetChanged();
	}

    public boolean isEmpty() {
        if(null!=this.filtered) {
            return filtered.size()<1;
        } else {
            return true;
        }
    }
	
	public int getCount() {
		if(isEmpty()) {
			return 0;
		}
		return filtered.size();
	}

	@Override
	public CharSequence getItem(int position) {
		if(isEmpty()) {
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
