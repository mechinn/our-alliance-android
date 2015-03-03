package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mechinn.android.ouralliance.data.Wheel;

import java.util.Collections;
import java.util.List;

public class WheelTypesAdapter extends FilterableAdapter {
    public static final String TAG = "TeamScoutingWheelAdapter";

	public WheelTypesAdapter(Context context, List<? extends Wheel> wheels) {
		super(context);
        swapList(wheels);
	}
	
	public void swapList(List<? extends Wheel> wheels) {
        emptyStrings();
        if(null!=wheels && wheels.size()>0) {
            Collections.sort(wheels);
            for(Wheel each : wheels) {
                addString(each.getWheelType());
            }
        }
        this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView container = (TextView) convertView;
        if(!isEmpty()) {
            if(null==convertView) {
                container = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            container.setText(getItem(position));
        }
		return container;
	}
}
