package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.mechinn.android.ouralliance.data.Graph;

import java.util.ArrayList;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by mechinn on 3/23/15.
 */
public class GraphAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    ArrayList<Graph> teamGraphs;
    ArrayList<Graph> matchGraphs;
    Map<String,ArrayList<Graph>> map;
    Context context;
    LayoutInflater inflater;
    public GraphAdapter(Context context) {
        this(context,null,null);
    }
    public GraphAdapter(Context context, ArrayList<Graph> teamGraphs, ArrayList<Graph> matchGraphs) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.teamGraphs = teamGraphs;
        this.matchGraphs = matchGraphs;
    }

    public void changeList(ArrayList<Graph> teamGraphs, ArrayList<Graph> matchGraphs) {
        this.teamGraphs = teamGraphs;
        this.matchGraphs = matchGraphs;
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
        if (null!=teamGraphs && null!=matchGraphs) {
            if(position<teamGraphs.size()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null!=teamGraphs) {
            count += teamGraphs.size();
        }
        if (null!=matchGraphs) {
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (null!=teamGraphs && null!=matchGraphs) {
            if(position<teamGraphs.size()) {
                return teamGraphs.get(position);
            } else {
                return matchGraphs.get(position-teamGraphs.size());
            }
        } else if (null!=teamGraphs) {
            return teamGraphs.get(position);
        } else if (null!=matchGraphs) {
            return matchGraphs.get(position);
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
            Graph graph = (Graph) getItem(position);
            container.setText(graph.getName());
            container.setChecked(graph.isEnabled());
        }
        return container;
    }

    public boolean isEmpty() {
        boolean empty = true;
        if(null!=teamGraphs && !teamGraphs.isEmpty()) {
            empty = false;
        } else if(null!=matchGraphs && !matchGraphs.isEmpty()) {
            empty = false;
        }
        return empty;
    }
}
