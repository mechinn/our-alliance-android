package com.mechinn.android.ouralliance.data;

import com.mechinn.android.ouralliance.data.frc2014.Sort2014;

import java.util.ArrayList;

/**
 * Created by mechinn on 3/26/14.
 */
public class Sort {
    public static final ArrayList<Sort2014> sort2014List;
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
    }
}
