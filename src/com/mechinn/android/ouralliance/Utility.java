package com.mechinn.android.ouralliance;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
}
