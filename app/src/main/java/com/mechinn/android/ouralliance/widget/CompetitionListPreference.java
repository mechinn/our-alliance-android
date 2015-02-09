package com.mechinn.android.ouralliance.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import com.mechinn.android.ouralliance.adapter.CompetitionListPreferenceAdapter;

import se.emilsjolander.sprinkles.ModelList;

/**
 * Created by mechinn on 2/19/14.
 */
public class CompetitionListPreference extends android.preference.ListPreference {
    public static final String TAG = "CompetitionListPreference";
    private CompetitionListPreferenceAdapter listAdapter = null;

    public CompetitionListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        listAdapter = new CompetitionListPreferenceAdapter(context);
    }

    public void setDefaultSummary(String summary) {
        listAdapter.setDefaultSummary(summary);
    }

    public void swapAdapter(ModelList<Competition> list, long id) {
        listAdapter.swapList(list, id);
        if(null!=list && list.size()>0) {
            this.setSummary(listAdapter.getEntry(this.getValue()));
        } else {
            this.setSummary("Must download competitions");
        }
    }

    public Competition get() {
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
                CompetitionListPreference.this.setValue(listAdapter.getValue(which).toString());
                setSummary(listAdapter.getEntry(which));
            }
        });
    }
}