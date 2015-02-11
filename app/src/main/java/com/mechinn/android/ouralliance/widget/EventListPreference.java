package com.mechinn.android.ouralliance.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import com.mechinn.android.ouralliance.adapter.EventListPreferenceAdapter;
import com.mechinn.android.ouralliance.greenDao.Event;

import java.util.List;

/**
 * Created by mechinn on 2/19/14.
 */
public class EventListPreference extends android.preference.ListPreference {
    public static final String TAG = "CompetitionListPreference";
    private EventListPreferenceAdapter listAdapter = null;

    public EventListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        listAdapter = new EventListPreferenceAdapter(context);
    }

    public void setDefaultSummary(String summary) {
        listAdapter.setDefaultSummary(summary);
    }

    public void swapAdapter(List<Event> list, long id) {
        listAdapter.swapList(list, id);
        if(null!=list && list.size()>0) {
            this.setSummary(listAdapter.getEntry(this.getValue()));
        } else {
            this.setSummary("Must download competitions");
        }
    }

    public Event get() {
        return listAdapter.getSelected(this.getValue());
    }

    @Override
    public CharSequence[] getEntries() {
        return listAdapter.getEntries();
    }

    @Override
    public CharSequence getEntry() {
        return listAdapter.getEntry(this.getValue());
    }

    @Override
    public CharSequence[] getEntryValues() {
        return listAdapter.getEntryValues();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(null,null).setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EventListPreference.this.setValue(listAdapter.getValue(which).toString());
                setSummary(listAdapter.getEntry(which));
            }
        });
    }
}