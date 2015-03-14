package com.mechinn.android.ouralliance.csv.frc2015;

import android.content.Context;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.csv.ExportCsvTeamScouting;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechinn on 3/11/15.
 */
public class ExportCsvTeamScouting2015 extends ExportCsvTeamScouting {
    public ExportCsvTeamScouting2015(Context context) {
        super(context);
        setHeader(new String[] {
                EventTeam.RANK
                ,EventTeam.SCOUTED
                ,TeamScouting2015.TEAM
                ,TeamScouting2015.NOTES
                ,TeamScouting2015.ORIENTATION
                ,TeamScouting2015.DRIVE_TRAIN
                ,TeamScouting2015.WIDTH
                ,TeamScouting2015.LENGTH
                ,TeamScouting2015.HEIGHT
                ,TeamScouting2015.COOP
                ,TeamScouting2015.DRIVER_EXPERIENCE
                ,TeamScouting2015.PICKUP_MECHANISM
                ,TeamScouting2015.MAX_TOTE_STACK
                ,TeamScouting2015.MAX_CONTAINER_STACK
                ,TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER
                ,TeamScouting2015.HUMAN_PLAYER
                ,TeamScouting2015.NO_AUTO
                ,TeamScouting2015.DRIVE_AUTO
                ,TeamScouting2015.TOTE_AUTO
                ,TeamScouting2015.CONTAINER_AUTO
                ,TeamScouting2015.STACKED_AUTO
                ,TeamScouting2015.LANDFILL_AUTO
                ,Wheel.WHEEL_TYPE
                ,Wheel.WHEEL_SIZE
                ,Wheel.WHEEL_COUNT
        });
    }
    public void run() throws IOException {
        List<TeamScouting2015> teams = new Select().from(TeamScouting2015.class).join(EventTeam.class).on(TeamScouting2015.TAG+"."+TeamScouting2015.TEAM+"="+EventTeam.TAG+"."+EventTeam.TEAM).where(EventTeam.TAG+"."+EventTeam.EVENT+"=?",getPrefs().getComp()).execute();
        for (TeamScouting2015 team : teams) {
            List<String> line = new ArrayList<>();
            EventTeam eventTeam = team.getEventTeam(getPrefs().getComp());
            line.add(fmtInteger(eventTeam.getRank()));
            line.add(fmtBoolean(eventTeam.isScouted()));
            line.add(fmtInteger(team.getTeam().getTeamNumber()));
            line.add(team.getNotes());
            line.add(team.getOrientation());
            line.add(team.getDriveTrain());
            line.add(fmtDouble(team.getWidth()));
            line.add(fmtDouble(team.getLength()));
            line.add(fmtDouble(team.getHeight()));
            line.add(fmtBoolean(team.getCoop()));
            line.add(fmtFloat(team.getDriverExperience()));
            line.add(team.getPickupMechanism());
            line.add(fmtInteger(team.getMaxToteStack()));
            line.add(fmtInteger(team.getMaxTotesStackContainer()));
            line.add(fmtInteger(team.getMaxTotesAndContainerLitter()));
            line.add(fmtFloat(team.getHumanPlayer()));
            line.add(fmtBoolean(team.getNoAuto()));
            line.add(fmtBoolean(team.getDriveAuto()));
            line.add(fmtBoolean(team.getToteAuto()));
            line.add(fmtBoolean(team.getContainerAuto()));
            line.add(fmtBoolean(team.getStackedAuto()));
            line.add(fmtInteger(team.getLandfillAuto()));
            List<? extends Wheel> wheels = team.getWheels();
            for (Wheel wheel : wheels) {
                line.add(wheel.getWheelType());
                line.add(fmtDouble(wheel.getWheelSize()));
                line.add(fmtInteger(wheel.getWheelCount()));
            }
            addToList(line);
        }
        super.run();
    }
}
