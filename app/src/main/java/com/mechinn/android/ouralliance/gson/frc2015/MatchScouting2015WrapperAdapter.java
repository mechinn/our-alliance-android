package com.mechinn.android.ouralliance.gson.frc2015;

import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015Wrapper;
import com.mechinn.android.ouralliance.gson.JsonWrapperAdapter;

/**
 * Created by mechinn on 3/29/15.
 */
public class MatchScouting2015WrapperAdapter extends JsonWrapperAdapter<MatchScouting2015> {
    @Override
    public JsonWrapper newAdapter() {
        return new MatchScouting2015Wrapper();
    }
}
