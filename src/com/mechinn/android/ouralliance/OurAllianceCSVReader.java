package com.mechinn.android.ouralliance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.MatchScoutingInterface;
import com.mechinn.android.ouralliance.data.TeamRankingsInterface;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class OurAllianceCSVReader {
	private final String logTag = "csvtool";
	private Activity activity;
	private SimpleDateFormat timeFormatter;
	private File dir;
	private File matchListFile;
	private File matchScoutingFile;
	private File teamRankingsFile;
	private File teamScoutingFile;
	
	private MatchListInterface matchList;
	private MatchScoutingInterface matchScouting;
	private TeamRankingsInterface teamRankings;
	private TeamScoutingInterface teamScouting;
	
	private List<String[]> matchListStrings;
	private List<String[]> matchScoutingStrings;
	private List<String[]> teamRankingsStrings;
	private List<String[]> teamScoutingStrings;
	
	public OurAllianceCSVReader(Activity act) {
		activity = act;
        timeFormatter = new SimpleDateFormat("hh:mma");
        timeFormatter.setTimeZone(TimeZone.getDefault());
        String packageName = activity.getPackageName();
        File externalPath = Environment.getExternalStorageDirectory();
        File appFiles = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
		dir = new File(appFiles.getAbsolutePath()+"/csvExport/");
		matchList = new MatchListInterface(activity);
		matchScouting = new MatchScoutingInterface(activity);
		teamRankings = new TeamRankingsInterface(activity);
		teamScouting = new TeamScoutingInterface(activity);
		matchListFile = new File(dir.getAbsolutePath(),"matchList-"+timeFormatter.format(new Date())+".csv");
		Log.i(logTag,matchListFile.toString());
		matchScoutingFile = new File(dir.getAbsolutePath(),"matchScouting-"+timeFormatter.format(new Date())+".csv");
		Log.i(logTag,matchScoutingFile.toString());
		teamRankingsFile = new File(dir.getAbsolutePath(),"teamRankings-"+timeFormatter.format(new Date())+".csv");
		Log.i(logTag,teamRankingsFile.toString());
		teamScoutingFile = new File(dir.getAbsolutePath(),"teamScouting-"+timeFormatter.format(new Date())+".csv");
		Log.i(logTag,teamScoutingFile.toString());
		matchListStrings = new ArrayList<String[]>();
		matchScoutingStrings = new ArrayList<String[]>();
		teamRankingsStrings = new ArrayList<String[]>();
		teamScoutingStrings = new ArrayList<String[]>();
	}
}
