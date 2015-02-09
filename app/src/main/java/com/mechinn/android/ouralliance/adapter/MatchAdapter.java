package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.emilsjolander.sprinkles.ModelList;

import java.util.Collections;

public class MatchAdapter extends BaseAdapter {
    public static final String TAG = "MatchAdapter";
    Context context;
    ModelList<Match> matches;
	 
	public MatchAdapter(Context context, ModelList<Match> matches) {
        this.context = context;
        swapList(matches);
	}

    public void swapList(ModelList<Match> matches) {
        this.matches = matches;
        if(null!=this.matches) {
            Collections.sort(this.matches);
        }
        this.notifyDataSetChanged();
    }
    public boolean isEmpty() {
        if(null!=matches) {
            return matches.size()<1;
        } else {
            return true;
        }
    }
    public int getCount() {
        if(isEmpty()) {
            return 0;
        }
        return matches.size();
    }

    public Competition getComp() {
        if(isEmpty()) {
            return null;
        }
        return getItem(0).getCompetition();
    }

    @Override
    public Match getItem(int position) {
        if(isEmpty()) {
            return null;
        }
        return matches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup container = (ViewGroup) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                container = (ViewGroup) inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView summary = (TextView)container.findViewById(android.R.id.text1);
            TextView subText = (TextView)container.findViewById(android.R.id.text2);
            summary.setText(matches.get(position).toString());
            if(matches.get(position).getRedScore()>-1 && matches.get(position).getRedScore()>-1) {
                subText.setText("Red: "+matches.get(position).getRedScore()+" | Blue: "+matches.get(position).getBlueScore());
            }
        }
        return container;
    }
}
