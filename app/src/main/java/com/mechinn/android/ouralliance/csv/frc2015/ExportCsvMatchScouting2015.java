package com.mechinn.android.ouralliance.csv.frc2015;

import android.content.Context;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.csv.ExportCsvMatchScouting;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportCsvMatchScouting2015 extends ExportCsvMatchScouting {
    public ExportCsvMatchScouting2015(Context context) {
        super(context);
        setHeader(new String[] {
                Match.COMPETITION_LEVEL
                ,Match.MATCH_NUMBER
                ,Match.SET_NUMBER
                ,Match.TIME
                ,Match.RED_SCORE
                ,Match.BLUE_SCORE
                ,MatchScouting2015.ALLIANCE
                ,MatchScouting2015.POSITION
                ,MatchScouting2015.NOTES
                ,MatchScouting2015.TEAM
                ,MatchScouting2015.AUTO_STACKED
                ,MatchScouting2015.AUTO_TOTES
                ,MatchScouting2015.AUTO_CONTAINERS
                ,MatchScouting2015.AUTO_LANDFILL
                ,MatchScouting2015.AUTO_MOVE
                ,MatchScouting2015.COOP
                ,MatchScouting2015.TOTES
                ,MatchScouting2015.CONTAINERS
                ,MatchScouting2015.LITTER
                ,"fouls"
                ,MatchScouting2015.HUMAN_ATTEMPT
                ,MatchScouting2015.HUMAN_SUCCESS
        });
    }
    public void run() throws IOException {
        List<MatchScouting2015> teams = new Select().from(MatchScouting2015.class).join(Match.class).on(MatchScouting2015.TAG+"."+MatchScouting2015.MATCH+"="+Match.TAG+"."+Match.ID).where(Match.TAG+"."+Match.EVENT+"=?",getPrefs().getComp()).execute();
        for (MatchScouting2015 team : teams) {
            List<String> line = new ArrayList<>();
            line.add(team.getMatch().getCompLevel());
            line.add(fmtInteger(team.getMatch().getMatchNumber()));
            line.add(fmtInteger(team.getMatch().getSetNumber()));
            line.add(fmtDate(team.getMatch().getTime()));
            line.add(fmtInteger(team.getMatch().getRedScore()));
            line.add(fmtInteger(team.getMatch().getBlueScore()));
            if(team.isAlliance()) {
                line.add("blue");
                line.add(fmtInteger(team.getPosition()-2));
            } else {
                line.add("red");
                line.add(fmtInteger(team.getPosition()+1));
            }
            line.add(team.getNotes());
            line.add(fmtInteger(team.getTeamScouting2015().getTeam().getTeamNumber()));
            line.add(fmtBoolean(team.getAutoStacked()));
            line.add(fmtInteger(team.getAutoTotes()));
            line.add(fmtInteger(team.getAutoContainers()));
            line.add(fmtInteger(team.getAutoLandfill()));
            line.add(fmtFloat(team.getAutoMove()));
            line.add(fmtBoolean(team.getCoop()));
            line.add(fmtInteger(team.getTotes()));
            line.add(fmtInteger(team.getContainers()));
            line.add(fmtInteger(team.getLitter()));
            line.add(fmtInteger(team.getFouls()));
            line.add(fmtInteger(team.getHumanAttempt()));
            line.add(fmtInteger(team.getHumanSuccess()));
            addToList(line);
        }
        super.run();
    }
}
