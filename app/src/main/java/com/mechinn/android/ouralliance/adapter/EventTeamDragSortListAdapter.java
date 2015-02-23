package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mobeta.android.dslv.DragSortCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventTeamDragSortListAdapter extends DragSortCursorAdapter {
    public static final String TAG = "EventTeamDragSortListAdapter";
    private int dragable;

    public EventTeamDragSortListAdapter(Context context, Cursor teams) {
        super(context, teams, FLAG_REGISTER_CONTENT_OBSERVER);
        showDrag(true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.drag_list_item, parent, false);
    }
    @Override
    public void bindView(View container, Context context, Cursor cursor) {
        TextView summary = (TextView)container.findViewById(R.id.text);
        ImageView dragHandle = (ImageView)container.findViewById(R.id.drag_handle);
        CheckBox scouted = (CheckBox)container.findViewById(R.id.scouted);
        dragHandle.setVisibility(dragable);
        EventTeam team = new EventTeam(cursor);
        summary.setText(team.getTeam().toString());
        scouted.setTag(team);
        scouted.setChecked(team.isScouted());
        scouted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox check = (CheckBox) v;
                EventTeam team = (EventTeam) v.getTag();
                team.setScouted(check.isChecked());
                team.asyncSave();
            }
        });
    }

    @Override
    public void drop(int from, int to) {
//        for(EventTeam team : this.getTeams()) {
//            Log.d(TAG,"team: "+team);
//        }
        if (from > to) {
            for (int i = to; i < from; ++i) {
                Log.d(TAG, "team: " + getTeam(i) + " new rank: " + (i + 1));
                getTeam(i).setRank(i + 1);
                Log.d(TAG, "team: " + getTeam(i));
            }
        } else {
            for (int i = from + 1; i <= to; ++i) {
                Log.d(TAG, "team: " + getTeam(i) + " new rank: " + (i - 1));
                getTeam(i).setRank(i - 1);
                Log.d(TAG, "team: " + getTeam(i));
            }
        }
        Log.d(TAG, "team: " + getTeam(from) + " new rank: " + to);
        getTeam(from).setRank(to);
        Log.d(TAG, "team: " + getTeam(from));
        Transaction.save(this.getTeams());
    }

    public EventTeam getTeam(int position) {
        Cursor cursor = this.getCursor();
        if(null!=cursor && cursor.moveToPosition(position)) {
            return new EventTeam(cursor);
        } else {
            return null;
        }
    }

    public List<EventTeam> getTeams() {
        Cursor cursor = this.getCursor();
        if(null!=cursor && cursor.moveToPosition(-1)) {
            List<EventTeam> teams = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                teams.add(new EventTeam(cursor));
            }
            return teams;
        } else {
            return null;
        }
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
