package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.mechinn.android.ouralliance.data.GraphDataSet;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by mechinn on 3/23/15.
 */
public class GraphDataSetAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    ArrayList<GraphDataSet> graphs;
    Context context;
    LayoutInflater inflater;
    public GraphDataSetAdapter(Context context) {
        this(context,null);
    }
    public GraphDataSetAdapter(Context context, ArrayList<GraphDataSet> graphs) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.graphs = graphs;
    }

    public void changeList(ArrayList<GraphDataSet> graphs) {
        this.graphs = graphs;
        this.notifyDataSetChanged();
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup viewGroup) {
        TextView container = (TextView) view;
        if(!isEmpty()) {
            if(null==view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            }
            switch((int) getHeaderId(position)) {
                default:
                case 0:
                    container.setText("Team Scouting");
                    break;
                case 1:
                    container.setText("Match Scouting");
                    break;
            }
            container.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        return container;
    }

    @Override
    public long getHeaderId(int position) {
        if (position<getCount()) {
            switch(graphs.get(position).getType()) {
                default:
                case TEAM:
                    return 0;
                case MATCH:
                    return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getCount() {
        if (!isEmpty()) {
            return graphs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (position<getCount()) {
            return graphs.get(position);
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
            GraphDataSet graph = (GraphDataSet) getItem(position);
            container.setText(graph.getLabel());
            container.setTextColor(context.getResources().getColor(android.R.color.white));
            container.setChecked(graph.isEnabled());
        }
        return container;
    }

    public boolean isEmpty() {
        if (null!=graphs) {
            return graphs.isEmpty();
        }
        return true;
    }
}
