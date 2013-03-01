package com.mechinn.android.ouralliance;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class Utility {
	public static final String TAG = Utility.class.getSimpleName();
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
	public static void restartApp(Context context) {
		String pack = context.getPackageName();
		Log.d(TAG,pack);
		Intent i = context.getPackageManager().getLaunchIntentForPackage(pack);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	public static boolean shortToBool(short val) {
		return val==0?false:true;
	}
	public static short boolToShort(boolean val) {
		return (short) (val?1:0);
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
