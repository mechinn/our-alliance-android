package com.mechinn.android.ouralliance.fragment;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.adapter.WheelTypesAdapter;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.event.MultimediaDeletedEvent;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;
import com.mechinn.android.ouralliance.gson.TeamAdapter;
import com.mechinn.android.ouralliance.rest.JsonDateAdapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public abstract class TeamDetailFragment extends Fragment {
    public static final String TAG = "TeamDetailFragment";
	public static final String TEAM_ARG = "team";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    private final static int PICTURE_CAPTURE_CODE = 100;
    private final static int VIDEO_CAPTURE_CODE = 101;

    private Prefs prefs;
    private View rootView;
	private Button picture;
    private Button video;
	private TwoWayView gallery;
    private TextView notes;
    private Button addWheel;
    private LinearLayout wheels;
	private MultimediaAdapter multimedia;
    private WheelTypesAdapter wheelTypesAdapter;

    private LinearLayout season;
	private long teamId;
	private TeamScouting scouting;

    public View getRootView() {
        return rootView;
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
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
	public TeamScouting getScouting() {
		return scouting;
	}
	public void setScouting(TeamScouting scouting) {
		this.scouting = scouting;
	}
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}
    public Button getAddWheel() {
        return addWheel;
    }
    public void setAddWheel(Button addWheel) {
        this.addWheel = addWheel;
    }

    public WheelTypesAdapter getWheelTypesAdapter() {
        return wheelTypesAdapter;
    }

    public void setWheelTypesAdapter(WheelTypesAdapter wheelTypesAdapter) {
        this.wheelTypesAdapter = wheelTypesAdapter;
    }

    public LinearLayout getWheels() {
        return wheels;
    }

    public void setWheels(LinearLayout wheels) {
        this.wheels = wheels;
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
    		Timber.d("team: " + teamId);
        }
        rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);
		rootView.setVisibility(View.GONE);
        picture = (Button) rootView.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a media file name
                if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
                    String timeStamp = dateFormat.format(new Date());
                    File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "IMG_"+ timeStamp + ".jpg");
                    Timber.d(mediaFile.getAbsolutePath());
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name

                    // start the image capture Intent
                    startActivityForResult(intent, PICTURE_CAPTURE_CODE);
                }
            }
        });
        video = (Button) rootView.findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a media file name
                if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
                    String timeStamp = dateFormat.format(new Date());
                    File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "VID_"+ timeStamp + ".mp4");
                    Timber.d(mediaFile.getAbsolutePath());
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                    // start the image capture Intent
                    startActivityForResult(intent, VIDEO_CAPTURE_CODE);
                }
            }
        });
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
        gallery = (TwoWayView) rootView.findViewById(R.id.gallery);
        gallery.setHasFixedSize(true);
        gallery.setLongClickable(true);
        final ItemClickSupport galleryClick = ItemClickSupport.addTo(gallery);
        galleryClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int i, long l) {
                File filename = (File) view.getTag(R.string.file);
                Timber.d( filename.toString());
                String type = URLConnection.guessContentTypeFromName("file://" + filename.getAbsolutePath());
                Timber.d( type);
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
        notes = (TextView) rootView.findViewById(R.id.notes);
        wheels = (LinearLayout) rootView.findViewById(R.id.wheels);
        addWheel = (Button) rootView.findViewById(R.id.addWheel);
        season = (LinearLayout) rootView.findViewById(R.id.season);
		return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        wheelTypesAdapter = new WheelTypesAdapter(this.getActivity(), null);
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
    		Timber.d( "team: "+teamId);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getYear() != 0 && teamId != 0) {
            loadScouting();
            loadWheelTypes();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.team_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        BluetoothEvent bluetooth = EventBus.getDefault().getStickyEvent(BluetoothEvent.class);
        menu.findItem(R.id.matchList).setVisible(prefs.getComp() > 0 && null != scouting);
//        menu.findItem(R.id.importTeamScouting).setVisible(prefs.getComp() > 0);
//        menu.findItem(R.id.exportTeamScouting).setVisible(null != scouting);
        menu.findItem(R.id.bluetoothTeamScouting).setVisible(null != scouting  && bluetooth.isEnabled());
        if(bluetooth.isOn()) {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
        } else {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth);
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
//            case R.id.exportTeamScouting:
//                String json = OurAllianceGson.BUILDER.toJson(scouting);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setView() {
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(scouting.getTeam().toString());
		multimedia = new MultimediaAdapter(this.getActivity(),scouting);
		Timber.d("thumbs: "+multimedia.getItemCount());
		gallery.setAdapter(multimedia);
		Timber.d("imageviews: "+gallery.getChildCount());
		notes.setText(scouting.getNotes());
	}
	
	public void updateScouting() {
		scouting.setNotes(notes.getText().toString());
	}
	
	public void commitUpdatedScouting() {
        this.getScouting().asyncSave();
	}

    public void onEventMainThread(MultimediaDeletedEvent event) {
        if (null != multimedia) {
            multimedia.buildImageSet(scouting);
        }
    }
    public LinearLayout createWheel(Wheel thisWheel) {
        LinearLayout view = (LinearLayout) this.getActivity().getLayoutInflater().inflate(R.layout.fragment_team_detail_wheel, wheels, false);
        view.setTag(thisWheel);
        //1 is the type field
        AutoCompleteTextView type = (AutoCompleteTextView) view.findViewById(R.id.wheelType);
        TextView count = (TextView) view.findViewById(R.id.wheelCount);
        TextView size = (TextView) view.findViewById(R.id.wheelSize);
        Button delete = (Button) view.findViewById(R.id.deleteWheel);
        type.setText(thisWheel.getWheelType());
        type.setThreshold(1);
        type.setAdapter(getWheelTypesAdapter());
        type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        if(null!=thisWheel.getWheelSize() && thisWheel.getWheelSize()>0) {
            //get the number
            num = Double.toString(thisWheel.getWheelSize());
            size.setText(num);
        }
        //if the size is currently 0 dont show it for the user's sake
        if(null!=thisWheel.getWheelCount() && thisWheel.getWheelCount()>0) {
            //get the number
            num = Integer.toString(thisWheel.getWheelCount());
            //6 is the count field
            count.setText(num);
        }
        //7 is the delete button, if clicked delete the wheel
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateWheels();
                View view = ((View) v.getParent());
                Wheel wheel = (Wheel) view.getTag();
                wheel.asyncDelete();
            }
        });
        wheels.addView(view);
        return view;
    }
    public abstract void loadScouting();
    public void scoutingLoaded() {
        getActivity().invalidateOptionsMenu();
        setView();
        getRootView().setVisibility(View.VISIBLE);
        loadWheels();
    }
    public abstract void loadWheelTypes();
    public abstract void loadWheels();
    public abstract void updateWheels();
}
