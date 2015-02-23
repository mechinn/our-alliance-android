package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Wheel;

/**
 * Created by mechinn on 2/22/15.
 */
public abstract class WheelAdapter<WheelYear extends Wheel> extends CursorAdapter implements Filterable {
    WheelTypesAdapter wheelTypesAdapter;

    public WheelAdapter(Context context, Cursor wheels) {
        super(context, wheels, FLAG_REGISTER_CONTENT_OBSERVER);
        this.wheelTypesAdapter = new WheelTypesAdapter(context, null, WheelTypesAdapter.TYPE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_team_detail_wheel, parent, false);
    }

    public abstract WheelYear wheelFromCursor(Cursor cursor);

    public void swapWheelTypes(Cursor cursor) {
        wheelTypesAdapter.swapCursor(cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        WheelYear wheel = wheelFromCursor(cursor);
        view.setTag(wheel);
        //1 is the type field
        AutoCompleteTextView type = (AutoCompleteTextView)view.findViewById(R.id.wheelType);
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
            TextView size = (TextView)view.findViewById(R.id.wheelSize);
            size.setText(num);
        }
        //if the size is currently 0 dont show it for the user's sake
        if(0!=wheel.getWheelCount()) {
            //get the number
            num = Integer.toString(wheel.getWheelCount());
            //6 is the count field
            TextView count = (TextView)view.findViewById(R.id.wheelCount);
            count.setText(num);
        }
        //7 is the delete button, if clicked delete the wheel
        view.findViewById(R.id.deleteWheel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = ((View) v.getParent());
                Wheel wheel = (Wheel) view.getTag();
                wheel.delete();
            }
        });
    }
}
