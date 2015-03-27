package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;

import com.mechinn.android.ouralliance.data.GraphDataSet;
import com.mechinn.android.ouralliance.data.TeamGraph;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by mechinn on 3/27/15.
 */
public class TeamGraphAdapter extends BaseAdapter implements ListAdapter {
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

    public void changeList(ArrayList<TeamGraph> teams) {
        this.teams = teams;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (!isEmpty()) {
            return teams.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(position<getCount()) {
            return teams.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView container = (CheckedTextView) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (CheckedTextView) inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
            }
            TeamGraph team = (TeamGraph) getItem(position);
            container.setText(team.getLabel());
            container.setTextColor(context.getResources().getColor(android.R.color.white));
            container.setChecked(team.isEnabled());
        }
        return container;
    }

    public boolean isEmpty() {
        if (null!=teams) {
            return teams.isEmpty();
        }
        return true;
    }
}
