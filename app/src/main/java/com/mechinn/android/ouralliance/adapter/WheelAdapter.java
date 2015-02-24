package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Wheel;

import java.util.List;

/**
 * Created by mechinn on 2/22/15.
 */
public class WheelAdapter extends BaseAdapter {
    WheelTypesAdapter wheelTypesAdapter;
    List<? extends Wheel> wheels;
    Context context;

    public WheelAdapter(Context context, List<? extends Wheel> wheels) {
        this.context = context;
        this.wheelTypesAdapter = new WheelTypesAdapter(context, wheels, R.id.wheelType);
        this.wheels = wheels;
    }

    @Override
    public int getCount() {
        if(null!=wheels) {
            return wheels.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(!isEmpty()) {
            return wheels.get(position);
        } else {
            return null;
        }
    }

    public Wheel getWheel(int position) {
        return (Wheel) getItem(position);
    }

    @Override
    public long getItemId(int position) {
        if(!isEmpty()) {
            return wheels.get(position).getId();
        } else {
            return 0;
        }
    }

    public void swapList(List<Wheel> wheels) {
        this.wheels = wheels;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout container = (LinearLayout) convertView;
        if(!isEmpty()) {
            container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.fragment_team_detail_wheel, parent, false);
            Wheel wheel = getWheel(position);
            container.setTag(wheel);
            //1 is the type field
            AutoCompleteTextView type = (AutoCompleteTextView)container.findViewById(R.id.wheelType);
            type.setText(wheel.getWheelType());
            type.setThreshold(1);
            type.setAdapter(wheelTypesAdapter);
            type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    AutoCompleteTextView textView = (AutoCompleteTextView) v;
                    if(hasFocus) {
                        v.setTag(textView.getText().toString());
//				} else {
//					String oldString = (String) textView.getTag();
//					if(null!=oldString && !wheelTypes.contains(oldString)) {
//						wheelTypesAdapter.remove(oldString);
//					}
//					String newString = textView.getText().toString();
//					if(null!=newString && !newString.isEmpty()) {
//						wheelTypesAdapter.add(newString);
//					}
                    }
                }
            });
            String num;
            //if the size is currently 0 dont show it for the user's sake
            if(0!=wheel.getWheelSize()) {
                //get the number
                num = Double.toString(wheel.getWheelSize());
                TextView size = (TextView)container.findViewById(R.id.wheelSize);
                size.setText(num);
            }
            //if the size is currently 0 dont show it for the user's sake
            if(0!=wheel.getWheelCount()) {
                //get the number
                num = Integer.toString(wheel.getWheelCount());
                //6 is the count field
                TextView count = (TextView)container.findViewById(R.id.wheelCount);
                count.setText(num);
            }
            //7 is the delete button, if clicked delete the wheel
            container.findViewById(R.id.deleteWheel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View view = ((View) v.getParent());
                    Wheel wheel = (Wheel) view.getTag();
                    wheel.delete();
                }
            });
        }
        return container;
    }
}
