package com.mechinn.android.ouralliance.fragment.frc2014;

import android.util.Log;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.adapter.MatchTeamAdapter;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/10/14.
 */
public class MatchTeamList2014Fragment extends MatchTeamListFragment {
    public MatchTeamAdapter<MatchScouting2014> getAdapter() {
        return (MatchTeamAdapter<MatchScouting2014>) super.getAdapter();
    }
    public void setAdapter(MatchTeamAdapter<? extends MatchScouting> adapter) {
        super.setAdapter(adapter);
    }
    public void load() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<MatchScouting2014> events = new Select().from(MatchScouting2014.class).where(MatchScouting2014.MATCH+"=?",getMatchId()).execute();
                EventBus.getDefault().post(new LoadMatchScouting(events));
            }
        });
    }

    public void onEventMainThread(MatchScouting2014 matchScoutingChanged) {
        load();
    }

    public void onEventMainThread(LoadMatchScouting scouting) {
        List<MatchScouting2014> result = scouting.getScouting();
        Timber.d("Count: " + result.size());
        getAdapter().swapMatch(result);
    }

    protected class LoadMatchScouting {
        List<MatchScouting2014> scouting;
        public LoadMatchScouting(List<MatchScouting2014> scouting) {
            this.scouting = scouting;
        }
        public List<MatchScouting2014> getScouting() {
            return scouting;
        }
    }
}
