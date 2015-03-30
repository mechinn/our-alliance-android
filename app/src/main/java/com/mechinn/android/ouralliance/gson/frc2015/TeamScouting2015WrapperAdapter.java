package com.mechinn.android.ouralliance.gson.frc2015;

import com.mechinn.android.ouralliance.data.JsonWrapper;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015Wrapper;
import com.mechinn.android.ouralliance.gson.JsonWrapperAdapter;
/**
 * Created by mechinn on 3/29/15.
 */
public class TeamScouting2015WrapperAdapter extends JsonWrapperAdapter<TeamScouting2015> {
    @Override
    public JsonWrapper newAdapter() {
        return new TeamScouting2015Wrapper();
    }
}
