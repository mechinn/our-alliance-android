package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 3/27/15.
 */
//@Table(name = TeamGraph.TAG, id = TeamGraph.ID)
public class TeamGraph implements Comparable<TeamGraph>, java.io.Serializable {
    public final static String TAG = "GraphDataSet";
    public final static String YEAR = "year";
    public final static String LABEL = "label";
    public final static String ENABLED = "enabled";
//    @Column(name=YEAR, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private int year;
//    @Column(name=LABEL, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    public String label;
//    @Column(name=ENABLED)
    private boolean enabled;
    public TeamGraph() {
        this.enabled = true;
    }
    public TeamGraph(int year, String label, boolean enabled) {
        this.year = year;
        this.label = label;
        this.enabled = enabled;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void switchEnabled() {
        enabled = !enabled;
    }

    public String toString() {
        return getYear()+" | "+getLabel()+" | "+(isEnabled()?"enabled":"disabled");
    }
//    public static Event load(int year, String label) {
//        return new Select().from(TeamGraph.class).where(TeamGraph.YEAR+"=?",year).and(TeamGraph.LABEL+"=?",label).executeSingle();
//    }
    public boolean copy(TeamGraph data) {
        if(this.equals(data)) {
//            super.copy(data);
            this.setYear(data.getYear());
            this.setLabel(data.getLabel());
            this.setEnabled(data.isEnabled());
            return true;
        }
        return false;
    }
    public boolean equals(TeamGraph data) {
        try {
            return getYear() == data.getYear() &&
                    getLabel().equals(data.getLabel());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public int compareTo(TeamGraph another) {
        int compare = this.getYear()-another.getYear();
        if(0==compare) {
            compare = this.getLabel().compareTo(another.getLabel());
        }
        return compare;
    }
//    public void asyncSave() {
//        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
//            @Override
//            public void run() throws Exception {
//                saveMod();
//                EventBus.getDefault().post(TeamGraph.this);
//            }
//        });
//    }
//    public void asyncDelete() {
//        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
//            @Override
//            public void run() throws Exception {
//                delete();
//                EventBus.getDefault().post(TeamGraph.this);
//            }
//        });
//    }
}
