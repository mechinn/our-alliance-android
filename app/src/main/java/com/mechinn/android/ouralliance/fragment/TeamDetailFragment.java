package com.mechinn.android.ouralliance.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.adapter.WheelAdapter;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.greenDao.EventTeam;
import com.mechinn.android.ouralliance.greenDao.Team;
import com.mechinn.android.ouralliance.greenDao.TeamScouting2014;
import com.mechinn.android.ouralliance.greenDao.Wheel;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import com.mechinn.android.ouralliance.greenDao.dao.EventDao;
import com.mechinn.android.ouralliance.greenDao.dao.EventTeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamScouting2014Dao;
import com.mechinn.android.ouralliance.greenDao.dao.WheelDao;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.lucasr.twowayview.widget.TwoWayView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;

public abstract class TeamDetailFragment<Scouting extends TeamScouting> extends Fragment implements AsyncOperationListener {
    public static final String TAG = "TeamDetailFragment";
	public static final String TEAM_ARG = "team";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
	private final static int PICTURE_CAPTURE_CODE = 100;
	private final static int VIDEO_CAPTURE_CODE = 101;

	private Prefs prefs;
	private View rootView;
	@InjectView(R.id.picture) private Button picture;
    @OnClick(R.id.picture) private void takePicture(View v) {
        // Create a media file name
        if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
            String timeStamp = dateFormat.format(new Date());
            File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "IMG_"+ timeStamp + ".jpg");
            Log.d(TAG,mediaFile.getAbsolutePath());
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, PICTURE_CAPTURE_CODE);
        }
    }
    @InjectView(R.id.video) private Button video;
    @OnClick(R.id.video) private void takeVideo(View v) {
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
            startActivityForResult(intent, VIDEO_CAPTURE_CODE);
        }
    }
	@InjectView(R.id.gallery) private TwoWayView gallery;
    @InjectView(R.id.notes) private TextView notes;
	@InjectView(R.id.addWheel) private Button addWheel;
    @OnClick(R.id.addWheel) private void addWheel(View v) {
        Wheel newWheel = new Wheel();
        newWheel.setTeamScouting(scouting);
        createWheel(newWheel);
    }
    @InjectView(R.id.wheels) private LinearLayout wheels;
    private List<Wheel> wheelCursor;
	private MultimediaAdapter multimedia;

	private WheelAdapter wheelTypesAdapter;

    @InjectView(R.id.season) private LinearLayout season;
	private long teamId;
	private Scouting scouting;
    private Event event;
    private DaoSession daoSession;
    private AsyncSession async;
    private AsyncOperation onEventLoaded;
    private AsyncOperation onScoutingLoaded;
    private AsyncOperation onWheelTypesLoaded;
    private AsyncOperation onTeamWheelsLoaded;

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if(onEventLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                Event result = (Event) operation.getResult();
                Log.d(TAG, "result: " + result);
                setEvent(result);
            } else {

            }
        } else if(onScoutingLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                Scouting result = (Scouting) operation.getResult();
                Log.d(TAG, "result: " + result);
                setScouting(result);
                setView();
                rootView.setVisibility(View.VISIBLE);
            } else {
                rootView.setVisibility(View.GONE);
            }
        } else if(onWheelTypesLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                List<Wheel> result = (List<Wheel>) operation.getResult();
                wheelTypesAdapter.swapList(result);
                Log.d(TAG, "Count: " + result.size());
            } else {

            }
        } else if(onTeamWheelsLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                List<Wheel> result = (List<Wheel>) operation.getResult();
                for(Wheel each : result) {
                    createWheel(each);
                }
            } else {

            }
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
        daoSession = ((OurAlliance) this.getActivity().getApplication()).getDaoSession();
        async = ((OurAlliance) this.getActivity().getApplication()).getAsyncSession();
        async.setListener(this);
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
		return rootView;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_CAPTURE_CODE) {
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
        } else if (requestCode == VIDEO_CAPTURE_CODE) {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wheelTypesAdapter = new WheelAdapter(getActivity(), null, WheelAdapter.TYPE);
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
            onEventLoaded = async.load(Event.class,prefs.getComp());
            onWheelTypesLoaded = async.loadAll(Wheel.class);
            onTeamWheelsLoaded = async.queryList(daoSession.getWheelDao().queryBuilder().where(WheelDao.Properties.TeamId.eq(getTeamId())).build());
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
		multimedia = new MultimediaAdapter(this.getActivity(),scouting,gallery);
		Log.d(TAG,"thumbs: "+multimedia.getCount());
		gallery.setAdapter(multimedia);
		Log.d(TAG,"imageviews: "+gallery.getChildCount());
		notes.setText(scouting.getNotes());
	}
	
	public void resetMultimediaAdapter() {
		if(null!=multimedia) {
            multimedia.buildImageSet(scouting);
		}
	}
	
	public LinearLayout createWheel(Wheel thisWheel) {
		LinearLayout view = (LinearLayout) this.getActivity().getLayoutInflater().inflate(R.layout.fragment_team_detail_wheel, wheels, false);
		view.setTag(thisWheel);
		//1 is the type field
		AutoCompleteTextView type = (AutoCompleteTextView)view.getChildAt(Wheel.FIELD_TYPE);
		type.setText(thisWheel.getWheelType());
		type.setThreshold(1);
		type.setAdapter(wheelTypesAdapter);
		type.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView textView = (AutoCompleteTextView) v; 
				if(hasFocus) {
					v.setTag(textView.getText().toString());
//				} else {
//					String oldString = (String) textView.getTag();
//					if(null!=oldString && !wheelTypes.contains(oldString)) {
//						wheelTypesAdapter.remove(oldString);
//					}
//					String newString = textView.getText().toString();
//					if(null!=newString && !newString.isEmpty()) {
//						wheelTypesAdapter.add(newString);
//					}
				}
			}
		});
		String num;
		//if the size is currently 0 dont show it for the user's sake
		if(0!=thisWheel.getWheelSize()) {
			//get the number 
			num = Double.toString(thisWheel.getWheelSize());
			TextView size = (TextView)view.getChildAt(Wheel.FIELD_SIZE);
			size.setText(num);
		}
		//if the size is currently 0 dont show it for the user's sake
		if(0!=thisWheel.getWheelCount()) {
			//get the number 
			num = Integer.toString(thisWheel.getWheelCount());
			//6 is the count field
			TextView count = (TextView)view.getChildAt(Wheel.FIELD_COUNT);
			count.setText(num);
		}
		//7 is the delete button, if clicked delete the wheel
		view.getChildAt(Wheel.FIELD_DELETE).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				View view = ((View) v.getParent());
                Wheel wheel = (Wheel) view.getTag();
                wheel.delete();
				wheels.removeView(view);
			}
		});
		wheels.addView(view);
		return view;
	}
	
	public void updateScouting() {
		for(int i=0;i<wheels.getChildCount(); ++i) {
			LinearLayout theWheelView = (LinearLayout) wheels.getChildAt(i);
            Wheel theWheel = (Wheel) theWheelView.getTag();
			CharSequence type = ((TextView) theWheelView.getChildAt(Wheel.FIELD_TYPE)).getText();
			theWheel.setWheelType(type);
			CharSequence size = ((TextView) theWheelView.getChildAt(Wheel.FIELD_SIZE)).getText();
			theWheel.setWheelSize(Utility.getFloatFromText(size));
			CharSequence count = ((TextView) theWheelView.getChildAt(Wheel.FIELD_COUNT)).getText();
			theWheel.setWheelCount(Utility.getIntFromText(count));
			//see if we should update or insert or just tell the user there isnt enough info
            ((OurAlliance)this.getActivity().getApplication()).getAsyncSession().update(theWheel);
		}
		scouting.setNotes(notes.getText().toString());
	}
	
	public void commitUpdatedScouting() {
        ((OurAlliance)this.getActivity().getApplication()).getAsyncSession().update(this.getScouting());
	}
}
