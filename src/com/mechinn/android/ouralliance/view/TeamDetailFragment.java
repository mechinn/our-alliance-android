package com.mechinn.android.ouralliance.view;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.devsmart.android.ui.HorizontalListView;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.data.source.TeamScoutingWheelDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class TeamDetailFragment<A extends TeamScouting, B extends AOurAllianceDataSource<A>> extends Fragment implements LoaderCallbacks<Cursor> {
	public static final String TAG = TeamDetailFragment.class.getSimpleName();
	public static final String TEAM_ARG = "team";
	public static final int LOADER_TEAMSCOUTING = 0;
	public static final int LOADER_TEAMWHEEL = 1;
	public static final int LOADER_WHEELTYPES = 2;
	final static String ARG_POSITION = "position";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
	private final static int PICTURE_CAPTURE_CODE = 100;
	private final static int VIDEO_CAPTURE_CODE = 101;

	private Prefs prefs;
	private View rootView;
	private Button picture;
	private Button video;
	private HorizontalListView gallery;
	private TextView notes;
	private Button addWheel;
	private LinearLayout wheels;
	private Cursor currentView;
	private Cursor wheelCursor;
	private MultimediaAdapter multimedia;

	private Cursor currentWheelTypes;
	private List<String> wheelTypes;
	private ArrayAdapter<String> wheelTypesAdapter;

	private LinearLayout season;
	private long teamId;
	private A scouting;
	private B dataSource;
	private TeamScoutingWheelDataSource teamScoutingWheelData;
	
	public abstract A setScoutingFromCursor(Cursor cursor) throws OurAllianceException, SQLException;
	public abstract B createDataSouce();

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
	public A getScouting() {
		return scouting;
	}
	public void setScouting(A scouting) {
		this.scouting = scouting;
	}
	public B getDataSource() {
		return dataSource;
	}
	public void setDataSource(B dataSource) {
		this.dataSource = dataSource;
	}
	public TeamScoutingWheelDataSource getTeamScoutingWheelData() {
		return teamScoutingWheelData;
	}
	public void setTeamScoutingWheelData(
			TeamScoutingWheelDataSource teamScoutingWheelData) {
		this.teamScoutingWheelData = teamScoutingWheelData;
	}
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		LoaderManager.enableDebugLogging(true);
		teamScoutingWheelData = new TeamScoutingWheelDataSource(this.getActivity());
		setDataSource(createDataSouce());
		prefs = new Prefs(this.getActivity());

//        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
//        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
//        ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
//
//        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
//
//        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
//        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
//        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
//        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
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
		picture = (Button) rootView.findViewById(R.id.picture);
		video = (Button) rootView.findViewById(R.id.video);
		if (this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			if(Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_IMAGE_CAPTURE)) {
				picture.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
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
				});
			} else {
		        picture.setVisibility(View.GONE);
			}
			if(Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_VIDEO_CAPTURE)) {
				video.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
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
				});
			} else {
		        video.setVisibility(View.GONE);
			}
	    } else {
	        picture.setVisibility(View.GONE);
	        video.setVisibility(View.GONE);
	    }
		gallery = (HorizontalListView) rootView.findViewById(R.id.gallery);
//		gallery.setOnScrollListener(new AbsListView.OnScrollListener() {
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//                // Pause fetcher to ensure smoother scrolling when flinging
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    imageFetcher.setPauseWork(true);
//                } else {
//                	imageFetcher.setPauseWork(false);
//                }
//            }
//
//            public void onScroll(AbsListView absListView, int firstVisibleItem,
//                    int visibleItemCount, int totalItemCount) {
//            }
//        });

		addWheel = (Button) rootView.findViewById(R.id.addWheel);
		addWheel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TeamScoutingWheel newWheel = new TeamScoutingWheel();
				newWheel.setSeason(scouting.getSeason());
				newWheel.setTeam(scouting.getTeam());
				createWheel(newWheel);
		    }
		});
		wheels = (LinearLayout) rootView.findViewById(R.id.wheels);
		notes = (TextView) rootView.findViewById(R.id.notes);
		season = (LinearLayout) rootView.findViewById(R.id.season);
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
        if (prefs.getSeason() != 0 && teamId != 0) {
    		this.getLoaderManager().restartLoader(LOADER_TEAMSCOUTING, null, this);
    		this.getLoaderManager().restartLoader(LOADER_WHEELTYPES, null, this);
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
		this.getActivity().setTitle(Integer.toString(scouting.getTeam().getNumber())+": "+scouting.getTeam().getName());
		multimedia = new MultimediaAdapter(this.getActivity(),scouting);
		Log.d(TAG,"thumbs: "+multimedia.getCount());
		gallery.setAdapter(multimedia);
		Log.d(TAG,"imageviews: "+gallery.getChildCount());
		notes.setText(scouting.getNotes());
	}
	
	public void resetMultimediaAdapter() {
		if(null!=multimedia) {
			multimedia.notifyDataSetChanged();
		}
	}
	
	public LinearLayout createWheel(TeamScoutingWheel thisWheel) {
		LinearLayout view = (LinearLayout) this.getActivity().getLayoutInflater().inflate(R.layout.fragment_team_detail_wheel, wheels, false);
		view.setTag(thisWheel);
		//1 is the type field
		AutoCompleteTextView type = (AutoCompleteTextView)view.getChildAt(TeamScoutingWheel.FIELD_TYPE);
		type.setText(thisWheel.getType());
		type.setThreshold(1);
		type.setAdapter(wheelTypesAdapter);
		type.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView textView = (AutoCompleteTextView) v; 
				if(hasFocus) {
					v.setTag(textView.getText().toString());
				} else {
					String oldString = (String) textView.getTag();
					if(null!=oldString && !wheelTypes.contains(oldString)) {
						wheelTypesAdapter.remove(oldString);
					}
					String newString = textView.getText().toString();
					if(null!=newString && !newString.isEmpty()) {
						wheelTypesAdapter.add(newString);
					}
				}
			}
		});
		String num;
		//if the size is currently 0 dont show it for the user's sake
		if(0!=thisWheel.getSize()) {
			//get the number 
			num = Float.toString(thisWheel.getSize());
			TextView size = (TextView)view.getChildAt(TeamScoutingWheel.FIELD_SIZE);
			size.setText(num);
		}
		//if the size is currently 0 dont show it for the user's sake
		if(0!=thisWheel.getCount()) {
			//get the number 
			num = Integer.toString(thisWheel.getCount());
			//6 is the count field
			TextView count = (TextView)view.getChildAt(TeamScoutingWheel.FIELD_COUNT);
			count.setText(num);
		}
		//7 is the delete button, if clicked delete the wheel
		view.getChildAt(TeamScoutingWheel.FIELD_DELETE).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				View view = ((View) v.getParent());
				TeamScoutingWheel wheel = (TeamScoutingWheel) view.getTag();
				//try to delete the wheel
				try {
					teamScoutingWheelData.delete(wheel);
				} catch (Exception e) {
					if(wheel.getId()>0) {
						Toast.makeText(TeamDetailFragment.this.getActivity(), "cannot delete wheel", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				wheels.removeView(view);
			}
		});
		wheels.addView(view);
		return view;
	}
	
	public void updateScouting() {
		for(int i=0;i<wheels.getChildCount(); ++i) {
			LinearLayout theWheelView = (LinearLayout) wheels.getChildAt(i);
			TeamScoutingWheel theWheel = (TeamScoutingWheel) theWheelView.getTag();
			CharSequence type = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_TYPE)).getText();
			theWheel.setType(type);
			CharSequence size = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_SIZE)).getText();
			theWheel.setSize(Utility.getFloatFromText(size));
			CharSequence count = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_COUNT)).getText();
			theWheel.setCount(Utility.getIntFromText(count));
			//see if we should update or insert or just tell the user there isnt enough info
			try {
				try {
					teamScoutingWheelData.update(theWheel);
					System.out.println("Saved "+theWheel);
				} catch (SQLException e) {
					try {
						theWheel = teamScoutingWheelData.insert(theWheel);
						System.out.println("Saved "+theWheel);
					} catch (SQLException e1) {
						String message = "Cannot save because conflicting "+e.getMessage()+" was given.";
						Log.d(TAG,message);
						Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
					}
				}
			} catch (OurAllianceException e) {
				String message = "Cannot save because no "+e.getMessage()+" was given.";
				Log.d(TAG,message);
				Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
			}
			
		}
		scouting.setNotes(notes.getText());
	}
	
	public void commitUpdatedScouting() {
		try {
			getDataSource().update(this.getScouting());
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setWheelTypesFromCursor(Cursor cursor) {
		if(null!=currentWheelTypes) {
			currentWheelTypes.close();
		}
		currentWheelTypes = cursor;
		try {
			wheelTypes = AOurAllianceDataSource.getStringList(currentWheelTypes);
		} catch (OurAllianceException e) {
			wheelTypes = new ArrayList<String>();
		} catch (SQLException e) {
			wheelTypes = new ArrayList<String>();
		}
		wheelTypesAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, wheelTypes);
		this.getLoaderManager().restartLoader(LOADER_TEAMWHEEL, null, this);
//		this.getLoaderManager().destroyLoader(LOADER_WHEELTYPES);
	}
	
	public void setViewFromCursor(Cursor cursor) {
		if(null!=currentView) {
			currentView.close();
		}
		currentView = cursor;
		try {
			this.setScouting(setScoutingFromCursor(currentView));
			setView();
			rootView.setVisibility(View.VISIBLE);
			return;
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createWheelsFromCursor(Cursor cursor) {
		if(wheelCursor!=null) {
			wheelCursor.close();
		}
		wheelCursor = cursor;
		try {
			List<TeamScoutingWheel> teamScoutingWheels = TeamScoutingWheelDataSource.getList(wheelCursor);
			Log.d(TAG, "Count: "+teamScoutingWheels.size());
			for(TeamScoutingWheel each : teamScoutingWheels) {
				createWheel(each);
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case LOADER_WHEELTYPES:
				return teamScoutingWheelData.getAllDistinct(TeamScoutingWheel.TYPE);
			case LOADER_TEAMWHEEL:
				return teamScoutingWheelData.get(prefs.getSeason(), getTeamId());
			case LOADER_TEAMSCOUTING:
				return dataSource.get(prefs.getSeason(), getTeamId());
		}
		return null;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "loader finished");
		switch(loader.getId()) {
			case LOADER_WHEELTYPES:
				setWheelTypesFromCursor(cursor);
				break;
			case LOADER_TEAMSCOUTING:
				setViewFromCursor(cursor);
				break;
			case LOADER_TEAMWHEEL:
				createWheelsFromCursor(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "loader reset");
		switch(loader.getId()) {
			case LOADER_TEAMSCOUTING:
				setViewFromCursor(null);
				break;
			case LOADER_TEAMWHEEL:
				createWheelsFromCursor(null);
				break;
		}
	}
}
