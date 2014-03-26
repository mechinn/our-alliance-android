package com.mechinn.android.ouralliance.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mobeta.android.dslv.DragSortListView;

import se.emilsjolander.sprinkles.ModelList;

import java.util.ArrayList;

public class CompetitionTeamDragSortListAdapter extends CompetitionTeamAdapter implements DragSortListView.DropListener {
    public static final String TAG = "CompetitionTeamDragSortListAdapter";
    private int dragable;

    public CompetitionTeamDragSortListAdapter(Context context, ModelList<CompetitionTeam> teams) {
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
            scouted.setChecked(getItem(position).isScouted());
            scouted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox check = (CheckBox) v;
                    CompetitionTeam team = (CompetitionTeam) v.getTag();
                    team.setScouted(check.isChecked());
                    team.asyncSave();
                }
            });
        }
        return container;
    }

    @Override
    public void drop(int from, int to) {
        for(CompetitionTeam team : this.getTeams()) {
            Log.d(TAG,"team: "+team);
        }
        if(from>to) {
            for(int i=to;i<from;++i) {
                Log.d(TAG,"team: "+getItem(i)+" new rank: "+(i+1));
                getItem(i).setRank(i + 1);
                Log.d(TAG, "team: " + getItem(i));
            }
        } else {
            for(int i=from+1;i<=to;++i) {
                Log.d(TAG,"team: "+getItem(i)+" new rank: "+(i-1));
                getItem(i).setRank(i - 1);
                Log.d(TAG, "team: " + getItem(i));
            }
        }
        Log.d(TAG, "team: " + getItem(from) + " new rank: " + to);
        getItem(from).setRank(to);
        Log.d(TAG,"team: "+getItem(from));
        this.getTeams().saveAll();
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
