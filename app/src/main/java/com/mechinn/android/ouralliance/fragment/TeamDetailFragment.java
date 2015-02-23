package com.mechinn.android.ouralliance.fragment;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.activeandroid.content.ContentProvider;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.adapter.WheelAdapter;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.Wheel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public abstract class TeamDetailFragment<Scouting extends TeamScouting> extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "TeamDetailFragment";
	public static final String TEAM_ARG = "team";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

	private Prefs prefs;
	private View rootView;
	@InjectView(R.id.picture) protected Button picture;
    @OnClick(R.id.picture) protected void takePicture(View v) {
        // Create a media file name
        if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
            String timeStamp = dateFormat.format(new Date());
            File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "IMG_"+ timeStamp + ".jpg");
            Log.d(TAG,mediaFile.getAbsolutePath());
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, R.id.picture_capture_code);
        }
    }
    @InjectView(R.id.video) protected Button video;
    @OnClick(R.id.video) protected void takeVideo(View v) {
        // Create a media file name
        if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
            String timeStamp = dateFormat.format(new Date());
            File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "VID_"+ timeStamp + ".mp4");
            Log.d(TAG,mediaFile.getAbsolutePath());
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

            // start the image capture Intent
            startActivityForResult(intent, R.id.video_capture_code);
        }
    }
	@InjectView(R.id.gallery) protected TwoWayView gallery;
    @InjectView(R.id.notes) protected TextView notes;
	@InjectView(R.id.addWheel) protected Button addWheel;
    @InjectView(R.id.wheels) protected LinearLayout wheels;
    private ArrayList<Wheel> wheelCursor;
	private MultimediaAdapter multimedia;
    public WheelAdapter getWheelsAdapter() {
        return wheelsAdapter;
    }
    public void setWheelsAdapter(WheelAdapter wheelsAdapter) {
        this.wheelsAdapter = wheelsAdapter;
    }

    private WheelAdapter wheelsAdapter;

    @InjectView(R.id.season) protected LinearLayout season;
	private long teamId;
	private Scouting scouting;
    private Event event;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.load_event:
                return new CursorLoader(getActivity(), ContentProvider.createUri(Event.class, null), null, Event.ID+"=?", new String[] {prefs.getCompString()}, null );
            case R.id.load_wheel_types:
                return new CursorLoader(getActivity(), ContentProvider.createUri(Wheel.class,null), null, null, null, null);
            case R.id.load_team_wheels:
                return new CursorLoader(getActivity(), ContentProvider.createUri(Wheel.class,null), null, Wheel.TEAM_SCOUTING+"=?", new String[] {Long.toString(getTeamId())}, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case R.id.load_event:
                setEventFromCursor(data);
                break;
            case R.id.load_team_detail_scouting:
                setScoutingFromCursor(data);
                setView();
                rootView.setVisibility(View.VISIBLE);
                break;
            case R.id.load_wheel_types:
                wheelsAdapter.swapWheelTypes(data);
                break;
            case R.id.load_team_wheels:
                wheelsAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case R.id.load_event:
                break;
            case R.id.load_team_detail_scouting:
                rootView.setVisibility(View.GONE);
                break;
            case R.id.load_wheel_types:
                break;
            case R.id.load_team_wheels:
                break;
        }
    }
	public Prefs getPrefs() {
		return prefs;
	}
	public void setPrefs(Prefs prefs) {
		this.prefs = prefs;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public Scouting getScouting() {
		return scouting;
	}
	public void setScouting(Scouting scouting) {
		this.scouting = scouting;
	}
    public abstract void setScoutingFromCursor(Cursor cursor);
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public void setEventFromCursor(Cursor cursor) {
        Event event = new Event();
        event.loadFromCursor(cursor);
        setEvent(event);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	setRetainInstance(true);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
    		teamId = savedInstanceState.getLong(Team.TAG, 0);
    		Log.d(TAG, "team: "+teamId);
        }
        
        rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);
		rootView.setVisibility(View.GONE);
        ButterKnife.inject(this, rootView);
		if (this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			if(!Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_IMAGE_CAPTURE)) {
		        picture.setVisibility(View.GONE);
			}
			if(!Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_VIDEO_CAPTURE)) {
		        video.setVisibility(View.GONE);
			}
	    } else {
	        picture.setVisibility(View.GONE);
	        video.setVisibility(View.GONE);
	    }
        gallery.setHasFixedSize(true);
        gallery.setLongClickable(true);
        final ItemClickSupport galleryClick = ItemClickSupport.addTo(gallery);
        galleryClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int i, long l) {
                File filename = (File) view.getTag(R.string.file);
                Log.d(TAG, filename.toString());
                String type = URLConnection.guessContentTypeFromName("file://" + filename.getAbsolutePath());
                Log.d(TAG, type);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + filename.getAbsolutePath()), type);
                try {
                    TeamDetailFragment.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(TeamDetailFragment.this.getActivity(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
                }
            }
        });
        galleryClick.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long l) {
                DialogFragment dialog = new MultimediaContextDialogFragment();
                Bundle dialogArgs = new Bundle();
                File filename = (File) view.getTag(R.string.file);
                dialogArgs.putSerializable(MultimediaContextDialogFragment.IMAGE, filename);
                dialog.setArguments(dialogArgs);
                dialog.show(TeamDetailFragment.this.getFragmentManager(), "Multimedia context menu");
                return false;
            }
        });
		return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.picture_capture_code) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
            	if(null!=data && null!=data.getData()) {
            		Toast.makeText(this.getActivity(), "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            	} else {
            		Toast.makeText(this.getActivity(), "Image saved", Toast.LENGTH_LONG).show();
            	}
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        } else if (requestCode == R.id.video_capture_code) {
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
            	if(null!=data && null!=data.getData()) {
	                Toast.makeText(this.getActivity(), "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
	        	} else {
	        		Toast.makeText(this.getActivity(), "Video saved", Toast.LENGTH_LONG).show();
	        	}
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
    		teamId = getArguments().getLong(TEAM_ARG, 0);
    		Log.d(TAG, "team: "+teamId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getYear() != 0 && teamId != 0) {
            this.getLoaderManager().initLoader(R.id.load_event,null,this);
            this.getLoaderManager().initLoader(R.id.load_wheel_types,null,this);
            this.getLoaderManager().initLoader(R.id.load_team_wheels,null,this);
        }
    }

	@Override
	public void onPause() {
		if(null!=scouting) {
			updateScouting();
			commitUpdatedScouting();
		}
		super.onPause();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TEAM_ARG, teamId);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setView() {
		this.getActivity().setTitle(Integer.toString(scouting.getTeam().getTeamNumber())+": "+scouting.getTeam().getNickname());
		multimedia = new MultimediaAdapter(this.getActivity(),scouting);
		Log.d(TAG,"thumbs: "+multimedia.getItemCount());
		gallery.setAdapter(multimedia);
		Log.d(TAG,"imageviews: "+gallery.getChildCount());
		notes.setText(scouting.getNotes());
	}
	
	public void resetMultimediaAdapter() {
		if(null!=multimedia) {
            multimedia.buildImageSet(scouting);
		}
	}
	
	public void updateScouting() {
		for(int i=0;i<wheels.getChildCount(); ++i) {
			LinearLayout theWheelView = (LinearLayout) wheels.getChildAt(i);
            Wheel theWheel = (Wheel) theWheelView.getTag();
			CharSequence type = ((TextView) theWheelView.getChildAt(R.id.wheelType)).getText();
			theWheel.setWheelType(type.toString());
			CharSequence size = ((TextView) theWheelView.getChildAt(R.id.wheelSize)).getText();
			theWheel.setWheelSize(Utility.getDoubleFromText(size));
			CharSequence count = ((TextView) theWheelView.getChildAt(R.id.wheelCount)).getText();
			theWheel.setWheelCount(Utility.getIntFromText(count));
			//see if we should update or insert or just tell the user there isnt enough info
            theWheel.asyncSave();
		}
		scouting.setNotes(notes.getText().toString());
	}
	
	public void commitUpdatedScouting() {
        this.getScouting().asyncSave();
	}
}
