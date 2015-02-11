package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.greenDao.EventTeam;
import com.mobeta.android.dslv.DragSortListView;

import java.util.List;

public class EventTeamDragSortListAdapter extends EventTeamAdapter implements DragSortListView.DropListener {
    public static final String TAG = "EventTeamDragSortListAdapter";
    private int dragable;

    public EventTeamDragSortListAdapter(Context context, List<EventTeam> teams) {
        super(context,teams);
        showDrag(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LinearLayout container = (LinearLayout) view;
        if(!isEmpty()) {
            if(null==view) {
                container = (LinearLayout) getInflater().inflate(R.layout.drag_list_item, viewGroup, false);
            }
            TextView summary = (TextView)container.findViewById(R.id.text);
            summary.setText(getItem(position).getTeam().toString());
            ImageView dragHandle = (ImageView)container.findViewById(R.id.drag_handle);
            dragHandle.setVisibility(dragable);
            CheckBox scouted = (CheckBox)container.findViewById(R.id.scouted);
            scouted.setTag(getItem(position));
            scouted.setChecked(getItem(position).getScouted());
            scouted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox check = (CheckBox) v;
                    EventTeam team = (EventTeam) v.getTag();
                    team.setScouted(check.isChecked());
                    ((OurAlliance)getContext().getApplicationContext()).getAsyncSession().updateInTx(EventTeam.class,team);
                }
            });
        }
        return container;
    }

    @Override
    public void drop(int from, int to) {
//        for(EventTeam team : this.getTeams()) {
//            Log.d(TAG,"team: "+team);
//        }
        if (from > to) {
            for (int i = to; i < from; ++i) {
                Log.d(TAG, "team: " + getItem(i) + " new rank: " + (i + 1));
                getItem(i).setRank(i + 1);
                Log.d(TAG, "team: " + getItem(i));
            }
        } else {
            for (int i = from + 1; i <= to; ++i) {
                Log.d(TAG, "team: " + getItem(i) + " new rank: " + (i - 1));
                getItem(i).setRank(i - 1);
                Log.d(TAG, "team: " + getItem(i));
            }
        }
        Log.d(TAG, "team: " + getItem(from) + " new rank: " + to);
        getItem(from).setRank(to);
        Log.d(TAG, "team: " + getItem(from));
        ((OurAlliance)this.getContext().getApplicationContext()).getAsyncSession().updateInTx(EventTeam.class,this.getTeams());
    }

    public void showDrag(boolean yes) {
        if(yes) {
            dragable = ImageView.VISIBLE;
        } else {
            dragable = ImageView.GONE;
        }
        this.notifyDataSetChanged();
    }
}
