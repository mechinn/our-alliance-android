package com.mechinn.android.ouralliance.fragment.frc2014;

import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 3/10/14.
 */
public class MatchTeamList2014Fragment extends MatchTeamListFragment {
    @Override
    public void onResume() {
        super.onResume();
        if(0!=getMatchId()) {
            Query.many(MatchScouting2014.class, "select * from " + MatchScouting2014.TAG + " where " + MatchScouting2014.MATCH + "=?", getMatchId()).getAsync(getLoaderManager(), getOnMatchLoaded());
        }
    }
}
