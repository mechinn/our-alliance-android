package com.mechinn.android.ouralliance.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.adapter.ListPreferenceAdapter;
import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.fragment.PreferenceFragment;

import se.emilsjolander.sprinkles.ModelList;

/**
 * Created by mechinn on 2/19/14.
 */
public class ListPreference<A extends AOurAllianceData> extends android.preference.ListPreference {
    public static final String TAG = "ListPreference";
    ListPreferenceAdapter<A> listPreferenceAdapter = null;
    Context context;
    Prefs prefs;

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        prefs = new Prefs(context);
        listPreferenceAdapter = new ListPreferenceAdapter<A>(context);
    }

    public void setDefaultSummary(String summary) {
        listPreferenceAdapter.setDefaultSummary(summary);
    }

    public void swapAdapter(ModelList<A> list, long id) {
        listPreferenceAdapter.swapList(list, id);
        if(null!=list) {
            this.setSummary(listPreferenceAdapter.getEntry(this.getValue()));
        }
    }

    public A get() {
        return listPreferenceAdapter.getSelected(this.getValue());
    }

    @Override
    public CharSequence[] getEntries() {
        return listPreferenceAdapter.getEntries();
    }

    @Override
    public CharSequence getEntry() {
        return listPreferenceAdapter.getEntry(this.getValue());
    }

    @Override
    public CharSequence[] getEntryValues() {
        return listPreferenceAdapter.getEntryValues();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(null,null).setAdapter(listPreferenceAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ListPreference.this.setValue(listPreferenceAdapter.getValue(which).toString());
                setSummary(listPreferenceAdapter.getEntry(which));
            }
        });
    }
}