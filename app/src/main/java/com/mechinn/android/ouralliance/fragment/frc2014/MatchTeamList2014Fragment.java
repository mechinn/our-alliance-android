package com.mechinn.android.ouralliance.fragment.frc2014;

import com.mechinn.android.ouralliance.fragment.MatchTeamListFragment;
import com.mechinn.android.ouralliance.greenDao.dao.MatchScouting2014Dao;

/**
 * Created by mechinn on 3/10/14.
 */
public class MatchTeamList2014Fragment extends MatchTeamListFragment {
    @Override
    public void onResume() {
        super.onResume();
        if(0!=getMatchId()) {
            setOnMatchLoaded(getAsync().queryList(getDaoSession().getMatchScouting2014Dao().queryBuilder().where(MatchScouting2014Dao.Properties.MatchId.eq(getMatchId())).build()));
        }
    }
}
