package com.mechinn.android.ouralliance.view;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.TeamScouting;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MultimediaAdapter extends PagerAdapter implements OnClickListener {
	private static final String TAG = "MultimediaAdapter";
	private List<MultimediaBitmap> thumbs;
	
	public MultimediaAdapter(Activity activity, TeamScouting team) {
		Log.d(TAG, team.toString());
		thumbs = new ArrayList<MultimediaBitmap>();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File teamScoutingFile = new File(activity.getExternalFilesDir(null).getAbsoluteFile() + "/" + team.getSeason().getYear() + "/" + team.getTeam().getNumber());
			Log.d(TAG, teamScoutingFile.getAbsolutePath());
			if(!teamScoutingFile.mkdirs()) {
				File[] files = teamScoutingFile.listFiles();
				for(File file : files) {
					Log.d(TAG, file.getAbsolutePath());
					String type = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
					Log.d(TAG, type);
					Bitmap thumb;
					if(type.startsWith("image")) {
						Log.d(TAG, "creating image thumbnail for: "+file);
						thumb = Utility.decodeSampledFile(file.getAbsolutePath(), 96, 96);
					} else if(type.startsWith("video")) {
						Log.d(TAG, "creating video thumbnail for: "+file);
						thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
					} else {
						thumb = null;
					}
					if(null!=thumb && thumb.getWidth()>0 && thumb.getHeight()>0) {
						thumbs.add(new MultimediaBitmap(file, thumb));
					}
				}
			}
        }
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if(!thumbs.isEmpty()) {
			File file = thumbs.get(position).getFile();
			Bitmap bitmap = thumbs.get(position).getBitmap();
			Log.d(TAG, "creating image for: "+file+" width: "+bitmap.getWidth()+" height: "+bitmap.getHeight());
			ImageView image = new ImageView(container.getContext());
			image.setImageBitmap(bitmap);
			image.setTag(R.string.file,file);
			image.setOnClickListener(this);
			container.addView(image);
			return image;
		}
		return null;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return thumbs.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (View) arg1;
	}

	public void onClick(View v) {
		File filename = (File) v.getTag(R.string.file);
		Log.d(TAG, filename.getAbsolutePath());
		String type = URLConnection.guessContentTypeFromName(filename.getAbsolutePath());
		Log.d(TAG, type);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + filename.getAbsolutePath()), type);
		try {
			v.getContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(v.getContext(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
		}
	}
	
	private class MultimediaBitmap {
		private File file;
		private Bitmap bitmap;
		
		public MultimediaBitmap(File file, Bitmap bitmap) {
			this.file = file;
			this.bitmap = bitmap;
		}
		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
	}
}
