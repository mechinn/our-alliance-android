package com.mechinn.android.ouralliance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by mechinn on 3/15/15.
 */
public abstract class TeamAnalysisFragment extends Fragment {
    public static final String TAG = "TeamAnalysisFragment";
    public static final String ARG_SECTION = "section";
    private BarChart chart;
    private Prefs prefs;
    private int position;

    public Prefs getPrefs() {
        return prefs;
    }

    public int getPosition() {
        return position;
    }

    public BarEntry getBarEntry(Object obj, int index) {
        float value = 0;
        if(null!=obj) {
            if(obj instanceof Boolean) {
                value = ((Boolean)obj)?1:0;
            } else if(obj instanceof Integer) {
                value = ((Integer)obj).floatValue();
            } else if(obj instanceof Float) {
                value = ((Float)obj).floatValue();
            } else if(obj instanceof Double) {
                value = ((Double)obj).floatValue();
            }
        }
        return new BarEntry(value,index);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_analysis, container, false);
        chart = (BarChart) rootView.findViewById(R.id.chart);
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(true);
        chart.setHighlightEnabled(true);
        chart.setHighlightIndicatorEnabled(true);
        chart.animateXY(3000, 3000);
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=1;i<=4;i++) {
            xAxis.add("Q"+i);
        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(100,0));
        entries.add(new BarEntry(50,1));
        BarDataSet dataSet = new BarDataSet(entries,"entries");
        BarData barChart = new BarData(xAxis,dataSet);
        chart.setData(barChart);
        chart.invalidate();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_SECTION)) {
            position = savedInstanceState.getInt(ARG_SECTION);
        } else if (this.getArguments() != null && this.getArguments().containsKey(ARG_SECTION)) {
            position = this.getArguments().getInt(ARG_SECTION);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("start");
        EventBus.getDefault().register(this);
        load();
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_SECTION, position);
        super.onSaveInstanceState(outState);
    }
    public abstract void load();
    public void setChartData(BarData data) {
        chart.setData(data);
        chart.invalidate();
    }
}
