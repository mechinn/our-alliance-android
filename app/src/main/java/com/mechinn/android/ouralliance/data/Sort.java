package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.data.frc2014.Sort2014;
import com.mechinn.android.ouralliance.data.frc2015.Sort2015;

import java.util.ArrayList;

/**
 * Created by mechinn on 3/26/14.
 */
public class Sort {
    public static final ArrayList<Sort2014> sort2014List;
    public static final ArrayList<Sort2015> sort2015List;
    static {
        ArrayList<Sort2014> temp2014List = new ArrayList<Sort2014>(16);
        temp2014List.add(Sort2014.RANK);
        temp2014List.add(Sort2014.NUMBER);
        temp2014List.add(Sort2014.ORIENTATION);
        temp2014List.add(Sort2014.DRIVETRAIN);
        temp2014List.add(Sort2014.WIDTH);
        temp2014List.add(Sort2014.LENGTH);
        temp2014List.add(Sort2014.HEIGHTSHOOTER);
        temp2014List.add(Sort2014.HEIGHTMAX);
        temp2014List.add(Sort2014.SHOOTERTYPE);
        temp2014List.add(Sort2014.SHOOTGOAL);
        temp2014List.add(Sort2014.SHOOTINGDISTANCE);
        temp2014List.add(Sort2014.PASS);
        temp2014List.add(Sort2014.PICKUP);
        temp2014List.add(Sort2014.PUSHER);
        temp2014List.add(Sort2014.BLOCKER);
        temp2014List.add(Sort2014.HUMANPLAYER);
        temp2014List.add(Sort2014.AUTONOMOUS);
        sort2014List = temp2014List;
        ArrayList<Sort2015> temp2015List = new ArrayList<Sort2015>(15);
        temp2015List.add(Sort2015.RANK);
        temp2015List.add(Sort2015.NUMBER);
        temp2015List.add(Sort2015.ORIENTATION);
        temp2015List.add(Sort2015.DRIVE_TRAIN);
        temp2015List.add(Sort2015.WIDTH);
        temp2015List.add(Sort2015.LENGTH);
        temp2015List.add(Sort2015.HEIGHT);
        temp2015List.add(Sort2015.COOP);
        temp2015List.add(Sort2015.DRIVER_EXPERIENCE);
        temp2015List.add(Sort2015.PICKUP_MECHANISM);
        temp2015List.add(Sort2015.MAX_TOTE_STACK);
        temp2015List.add(Sort2015.MAX_CONTAINER_STACK);
        temp2015List.add(Sort2015.MAX_TOTES_AND_CONTAINER_LITTER);
        temp2015List.add(Sort2015.HUMAN_PLAYER);
        temp2015List.add(Sort2015.LANDFILL_AUTO);
        temp2015List.add(Sort2015.AUTONOMOUS);
        sort2015List = temp2015List;
    }
}
