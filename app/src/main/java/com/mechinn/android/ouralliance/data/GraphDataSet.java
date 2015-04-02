package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 3/23/15.
 */
//@Table(name = GraphDataSet.TAG, id = GraphDataSet.ID)
public class GraphDataSet implements Comparable<GraphDataSet>, java.io.Serializable  {
    public final static String TAG = "GraphDataSet";
    public final static String YEAR = "year";
    public final static String GRAPH_TYPE = "graph_type";
    public final static String ENABLED = "enabled";
//    @Column(name=YEAR, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int year;
//    @Column(name=GRAPH_TYPE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int type;
//    @Column(name=ENABLED)
    private boolean enabled;
    private LineDataSet dataSet;

    public GraphDataSet() {
        enabled = true;
    }

    public GraphDataSet(ArrayList<Entry> yVals, String label, int year, Type type, boolean enabled) {
        this.dataSet = new LineDataSet(yVals,label);
        this.year = year;
        this.type = type.getValue();
        this.enabled = enabled;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Type getType() {
        switch(type) {
            default:
            case 0:
                return Type.TEAM;
            case 1:
                return Type.MATCH;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type.getValue();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDataSet(LineDataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void switchEnabled() {
        enabled = !enabled;
    }

    public LineDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<Entry> yVals, String label) {
        this.dataSet = new LineDataSet(yVals,label);
    }

    public String toString() {
        return getYear()+" | "+getType()+" | "+(isEnabled()?"enabled":"disabled");
    }
//    public static Event load(int year, Type type) {
//        return new Select().from(GraphDataSet.class).where(GraphDataSet.YEAR+"=?",year).and(GraphDataSet.GRAPH_TYPE+"=?",type.getValue()).executeSingle();
//    }
    public boolean copy(GraphDataSet data) {
        if(this.equals(data)) {
//            super.copy(data);
            this.setYear(data.getYear());
            this.setType(data.getType());
            this.setEnabled(data.isEnabled());
            this.setDataSet(data.getDataSet());
            return true;
        }
        return false;
    }
    public boolean equals(GraphDataSet data) {
        try {
            return getYear() == data.getYear() &&
                    getType().equals(data.getType());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public int compareTo(GraphDataSet another) {
        int compare = this.getYear()-another.getYear();
        if(0==compare) {
            compare = this.getType().getValue()-another.getType().getValue();
        }
        return compare;
    }
//    public void asyncSave() {
//        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
//            @Override
//            public void run() throws Exception {
//                saveMod();
//                EventBus.getDefault().post(GraphDataSet.this);
//            }
//        });
//    }
//    public void asyncDelete() {
//        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
//            @Override
//            public void run() throws Exception {
//                delete();
//                EventBus.getDefault().post(GraphDataSet.this);
//            }
//        });
//    }

    public enum Type {
        TEAM(0),MATCH(1);
        private int value;
        private Type(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
}