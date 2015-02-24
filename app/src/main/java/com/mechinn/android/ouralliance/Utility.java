package com.mechinn.android.ouralliance;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	public static int getIntFromText(CharSequence text) {
		try {
			return Integer.parseInt(text.toString());
		} catch (Exception e) {
			return 0;
		}
	}
	public static float getFloatFromText(CharSequence text) {
		try {
			return Float.parseFloat(text.toString());
		} catch (Exception e) {
			return 0;
		}
	}
	public static double getDoubleFromText(CharSequence text) {
		try {
			return Double.parseDouble(text.toString());
		} catch (Exception e) {
			return 0;
		}
	}
}
