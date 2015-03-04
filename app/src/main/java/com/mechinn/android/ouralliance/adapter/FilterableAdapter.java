package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by mechinn on 3/2/15.
 */
public abstract class FilterableAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "FilterableAdapter";
    private Context context;
    private List<String> original;
    private List<String> filtered;
    private AdapterFilter filter;

    public FilterableAdapter(Context context) {
        this.context = context;
        this.filter = new AdapterFilter();
        this.original = new ArrayList<>();
    }

    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    public void emptyStrings() {
        original.clear();
    }

    public void addString(String string) {
        original.add(string);
    }

    public boolean isEmpty() {
        if(null!=original) {
            return original.size()<1;
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
    public String getItem(int position) {
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
    public Filter getFilter() {
        return filter;
    }

    private class AdapterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            //If there's nothing to filter on, return the original data for your list
            if(charSequence == null || charSequence.length() == 0) {
                results.values = original;
                results.count = original.size();
            } else {
                String filteringString = charSequence.toString().toLowerCase();
                Timber.d("filtering on " + filteringString);
                List<String> filterResultsData = new ArrayList<>();
                for(String data : original) {
                    //In this loop, you'll filter through originalData and compare each item to charSequence.
                    //If you find a match, add it to your new ArrayList
                    //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                    Timber.d( "compare "+data);
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
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered = (ArrayList<String>)filterResults.values;
            notifyDataSetChanged();
        }
    };
}
