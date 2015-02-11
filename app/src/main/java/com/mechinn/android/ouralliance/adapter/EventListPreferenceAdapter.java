package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.mechinn.android.ouralliance.greenDao.Event;

import java.util.List;

/**
 * Created by mechinn on 2/19/14.
 */
public class EventListPreferenceAdapter extends BaseAdapter {
    public static final String TAG = "EventListPreferenceAdapter";

    private long id;
    private Context context;
    private List<Event> list;
    private CharSequence[] entries;
    private CharSequence[] values;
    private String defaultSummary;

    public EventListPreferenceAdapter(Context context) {
        this.context = context;
        swapList(null, 0);
    }

    public void setDefaultSummary(String summary) {
        defaultSummary = summary;
    }

    public void swapList(List<Event> list, long id) {
        this.id = id;
        this.list = list;
        if(!isEmpty()) {
            this.entries = new CharSequence[list.size()];
            this.values = new CharSequence[list.size()];
            for(int i=0;i<list.size();++i) {
                this.entries[i] = list.get(i).toString();
                this.values[i] = Long.toString(list.get(i).getId());
            }
        } else {
            this.entries = new CharSequence[1];
            this.entries[0] = ("");
            this.values = new CharSequence[1];
            this.values[0] = ("0");
        }
        this.notifyDataSetChanged();
    }
    public CharSequence[] getEntries() {
        return this.entries;
    }
    public CharSequence getEntry(int which) {
        return this.entries[which];
    }
    public CharSequence getEntry(CharSequence value) {
        for(int i=0;i<this.values.length;++i) {
            if(value.equals(this.values[i])) {
                return this.entries[i];
            }
        }
        return defaultSummary;
    }
    public CharSequence[] getEntryValues() {
        return this.values;
    }
    public CharSequence getValue(int which) {
        return this.values[which];
    }
    public int getPosition(CharSequence value) {
        for(int i=0;i<this.values.length;++i) {
            if(value.equals(this.values[i])) {
                return i;
            }
        }
        return -1;
    }
    public Event getSelected(CharSequence value) {
        if(!isEmpty()) {
            for(int i=0;i<values.length;++i) {
                if(value.equals(values[i])) {
                    return list.get(i);
                }
            }
        }
        return null;
    }
    public boolean isEmpty() {
        if(null!=list) {
            return list.size()<1;
        } else {
            return true;
        }
    }
    public int getCount() {
        if(isEmpty()) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Event getItem(int position) {
        if(isEmpty()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        CheckedTextView container = (CheckedTextView) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (CheckedTextView) inflater.inflate(android.R.layout.select_dialog_singlechoice, parent, false);
            }
            container.setText(entries[position]);
        }
        return container;
    }
}
