package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mechinn.android.ouralliance.data.GraphDataSet;
import com.mechinn.android.ouralliance.data.TeamGraph;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by mechinn on 3/27/15.
 */
public class TeamGraphAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    ArrayList<TeamGraph> teams;
    Context context;
    LayoutInflater inflater;
    public TeamGraphAdapter(Context context) {
        this(context,null);
    }
    public TeamGraphAdapter(Context context, ArrayList<TeamGraph> teams) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.teams = teams;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public long getHeaderId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
