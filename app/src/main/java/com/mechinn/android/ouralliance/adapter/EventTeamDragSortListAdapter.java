package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.event.Transaction;
import com.mobeta.android.dslv.DragSortListView;

import java.util.List;

import timber.log.Timber;

public class EventTeamDragSortListAdapter extends BaseAdapter implements ListAdapter, DragSortListView.DropListener {
    public static final String TAG = "EventTeamDSLAdapter";
    private int dragable;
    private Context context;
    private List<EventTeam> teams;

    public EventTeamDragSortListAdapter(Context context, List<EventTeam> teams) {
        this.context = context;
        this.teams = teams;
        showDrag(true);
    }

    @Override
    public int getCount() {
        if(null!=teams) {
            return teams.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(!isEmpty()) {
            return teams.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if(!isEmpty()) {
            return teams.get(position).getId();
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout container = (LinearLayout) convertView;
        if(!isEmpty()) {
            container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.drag_list_item, parent, false);
            TextView summary = (TextView)container.findViewById(R.id.text);
            ImageView dragHandle = (ImageView)container.findViewById(R.id.drag_handle);
            CheckBox scouted = (CheckBox)container.findViewById(R.id.scouted);
            if(dragable == ImageView.VISIBLE) {
                dragHandle.setVisibility(ImageView.VISIBLE);
            } else {
                dragHandle.setVisibility(ImageView.GONE);
            }
            EventTeam team = this.getTeam(position);
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
        return container;
    }

    @Override
    public void drop(int from, int to) {
//        for(EventTeam team : this.getTeams()) {
//            Timber.d("team: "+team);
//        }
        if (from > to) {
            for (int i = to; i < from; ++i) {
                Timber.d("team: " + getTeam(i) + " new rank: " + (i + 1));
                getTeam(i).setRank(i + 1);
                Timber.d( "team: " + getTeam(i));
            }
        } else {
            for (int i = from + 1; i <= to; ++i) {
                Timber.d( "team: " + getTeam(i) + " new rank: " + (i - 1));
                getTeam(i).setRank(i - 1);
                Timber.d( "team: " + getTeam(i));
            }
        }
        Timber.d( "team: " + getTeam(from) + " new rank: " + to);
        getTeam(from).setRank(to);
        Timber.d( "team: " + getTeam(from));
        Transaction.asyncSave(EventTeam.class, this.getTeams());
    }

    public EventTeam getTeam(int position) {
        return (EventTeam) this.getItem(position);
    }

    public List<EventTeam> getTeams() {
        return this.teams;
    }

    public void swapList(List<EventTeam> teams) {
        this.teams = teams;
        this.notifyDataSetChanged();
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
