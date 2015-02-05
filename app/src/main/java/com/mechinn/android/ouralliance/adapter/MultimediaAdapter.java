package com.mechinn.android.ouralliance.adapter;

import java.io.File;
import java.net.URLConnection;

import android.util.TypedValue;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.fragment.MultimediaContextDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import static android.widget.ImageView.ScaleType.FIT_START;

public class MultimediaAdapter extends BaseAdapter implements OnClickListener, OnLongClickListener, Callback {
    public static final String TAG = "MultimediaAdapter";
    private File[] multimedia;
    private File teamFileDirectory;
    private Activity activity;
    private Prefs prefs;
	
	public File getTeamFileDirectory() {
		return teamFileDirectory;
	}

	public void setTeamFileDirectory(File teamFileDirectory) {
		this.teamFileDirectory = teamFileDirectory;
	}

	public MultimediaAdapter(Activity activity, TeamScouting team, ViewGroup group) {
		this.activity = activity;
		prefs = new Prefs(activity);
		Log.d(TAG, team.toString());
        Log.d(TAG,"find images");
        buildImageSet(team);
	}
    public void buildImageSet(TeamScouting team) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            teamFileDirectory = new File(activity.getExternalFilesDir(null).getAbsoluteFile() + File.separator + prefs.getYear() + File.separator + team.getTeam().getTeamNumber());
            Log.d(TAG, teamFileDirectory.getAbsolutePath());
            if(!teamFileDirectory.mkdirs()) {
                multimedia = teamFileDirectory.listFiles();
            }
        }
        Log.d(TAG, "images: "+getCount());
        for(int i=0;i<getCount();++i) {
            Log.d(TAG, "file: "+getItem(i));
            String type = URLConnection.guessContentTypeFromName(getItem(i).getAbsolutePath());
            Log.d(TAG, "creating thumbnail for: "+type);
        }


        this.notifyDataSetChanged();
    }
	public int getCount() {
		if(null==multimedia) {
			return 0;
		}
		return multimedia.length;
	}
	public File getItem(int position) {
		if(null==multimedia) {
			return null;
		}
        return multimedia[position];
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(activity);
        }
        view.setMaxWidth(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 93, activity.getResources().getDisplayMetrics())));
        view.setMaxHeight(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 165, activity.getResources().getDisplayMetrics())));
        view.setScaleType(FIT_START);
        view.setAdjustViewBounds(true);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        view.setTag(R.string.file,getItem(position));

        // Get the image URL for the current position.
        File file = getItem(position);

        Log.d(TAG, "get image");
        // Trigger the download of the URL asynchronously into the image view.
        final float scale = activity.getResources().getDisplayMetrics().density;
        Picasso.with(activity) //
                .load(file) //
                .fit().centerInside()
                .placeholder(R.drawable.ic_empty) //
                .error(R.drawable.ic_error) //
                .into(view, this);

        return view;
	}

    public void onClick(View v) {
        openMedia(v);
    }
    public static void openMedia(View v) {
        File filename = (File) v.getTag(R.string.file);
        Log.d(TAG, filename.toString());
        String type = URLConnection.guessContentTypeFromName("file://"+filename.getAbsolutePath());
        Log.d(TAG, type);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+filename.getAbsolutePath()), type);
        try {
            v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(v.getContext(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
        }
    }

	public boolean onLongClick(View v) {
		DialogFragment dialog = new MultimediaContextDialogFragment();
		Bundle dialogArgs = new Bundle();
        File filename = (File) v.getTag(R.string.file);
		dialogArgs.putSerializable(MultimediaContextDialogFragment.IMAGE, filename);
		dialog.setArguments(dialogArgs);
        dialog.show(activity.getFragmentManager(), "Multimedia context menu");
		return false;
	}

    @Override
    public void onSuccess() {
        Log.d(TAG,"get image: success");
    }

    @Override
    public void onError() {
        Log.d(TAG,"get image: error");
        Toast.makeText(activity, "error loading image", Toast.LENGTH_SHORT).show();
    }
}
