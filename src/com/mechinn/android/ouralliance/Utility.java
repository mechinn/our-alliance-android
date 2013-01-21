package com.mechinn.android.ouralliance;

import java.io.File;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.TeamDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Activity;
import android.util.Log;

public class Utility {
	public static final String TAG = "Utility";
	//utility functions for specific uses throughout the app
	public static void deleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory()) {
	    	Log.d(TAG, "found dir: "+fileOrDirectory.getName());
	        for (File child : fileOrDirectory.listFiles()) {
	        	Log.d(TAG, "each file: "+fileOrDirectory.getName());
	            deleteRecursive(child);
	        }
	    }
    	Log.d(TAG, "delete file/dir: "+fileOrDirectory.getName());
	    if(fileOrDirectory.delete()) {
	    	Log.d(TAG, "deleted: "+fileOrDirectory.getName());
	    }
	}
}
