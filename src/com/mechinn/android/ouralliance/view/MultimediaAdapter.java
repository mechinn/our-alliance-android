package com.mechinn.android.ouralliance.view;

import java.io.File;
import java.net.URLConnection;

import com.mechinn.android.ouralliance.ImageFetcher;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScouting;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MultimediaAdapter extends BaseAdapter implements OnClickListener, OnLongClickListener {
	public static final String TAG = MultimediaAdapter.class.getName();
	private String[] multimedia;
    private ImageFetcher imageFetcher;
    private File teamFileDirectory;
    private Activity activity;
	
	public File getTeamFileDirectory() {
		return teamFileDirectory;
	}

	public void setTeamFileDirectory(File teamFileDirectory) {
		this.teamFileDirectory = teamFileDirectory;
	}

	public MultimediaAdapter(Activity activity, TeamScouting team) {
		this.activity = activity;
		Log.d(TAG, team.toString());
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			teamFileDirectory = new File(activity.getExternalFilesDir(null).getAbsoluteFile() + "/" + team.getSeason().getYear() + "/" + team.getTeam().getNumber());
			Log.d(TAG, teamFileDirectory.getAbsolutePath());
			if(!teamFileDirectory.mkdirs()) {
				File[] files = teamFileDirectory.listFiles();
				multimedia = new String[files.length];
				for(int i=0;i<files.length;++i) {
					multimedia[i] = "file://"+files[i].getAbsolutePath();
					Log.d(TAG, multimedia[i]);
				}
			}
        }
	}

	public void onClick(View v) {
		openMedia(v);
	}
	public static void openMedia(View v) {
		String filename = (String) v.getTag(R.string.file);
		Log.d(TAG, filename);
		String type = URLConnection.guessContentTypeFromName(filename);
		Log.d(TAG, type);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(filename), type);
		try {
			v.getContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(v.getContext(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
		}
	}
	public int getCount() {
		if(null==multimedia) {
			return 0;
		}
		return multimedia.length;
	}
	public Object getItem(int position) {
		if(null==multimedia) {
			return null;
		}
		return multimedia[position];
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View imageView, ViewGroup parent) {
		if(null!=multimedia) {
			Log.d(TAG, "creating thumbnail for: "+multimedia[position]);
			if(null==imageView) {
				imageView = new ImageView(parent.getContext());
			}
			imageView.setTag(R.string.file,multimedia[position]);
			imageView.setOnClickListener(this);
			imageView.setOnLongClickListener(this);
			setImageBad(multimedia[position], (ImageView) imageView);
//			imageFetcher.loadImage(multimedia[position], (ImageView) imageView);
			return imageView;
		}
		return null;
	}
	
	public static void setImageBad(String file, ImageView imageView) {
		file = file.replaceFirst("file://", "");
		String type = URLConnection.guessContentTypeFromName(file);
		Log.d(TAG, "creating thumbnail for: "+type);
		Log.d(TAG, type);
		Bitmap bitmap = null;
		if(null!=type) {
            if(type.startsWith("image")) {
    			Log.d(TAG, "creating image thumbnail for: "+file);
                bitmap = ImageFetcher.decodeSampledBitmapFromFile(file, 96, 96);
    		} else if(type.startsWith("video")) {
    			Log.d(TAG, "creating video thumbnail for: "+file);
    			bitmap = ThumbnailUtils.createVideoThumbnail(file, MediaStore.Images.Thumbnails.MICRO_KIND);
    		}
		}
        ((ImageView) imageView).setImageBitmap(bitmap);
	}

	public boolean onLongClick(View v) {
		DialogFragment dialog = new MultimediaContextDialog();
		Bundle dialogArgs = new Bundle();
		String filename = (String) v.getTag(R.string.file);
		dialogArgs.putString(MultimediaContextDialog.IMAGE, filename);
		dialog.setArguments(dialogArgs);
        dialog.show(activity.getFragmentManager(), "Multimedia context menu");
		return false;
	}
}
