package com.mechinn.android.ouralliance.fragment.frc2014;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

/**
 * Created by mechinn on 3/10/14.
 */
public class MatchTeamList2014Fragment extends MatchTeamListFragment<MatchScouting2014> {
    public void load() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<MatchScouting2014> events = new Select().from(MatchScouting2014.class).where(MatchScouting2014.MATCH+"=?",getMatchId()).execute();
                EventBus.getDefault().post(new LoadMatchScouting(events));
            }
        });
    }
}
