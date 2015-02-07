package com.mechinn.android.ouralliance.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.adapter.TeamScoutingWheelAdapter;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.TeamScoutingWheel;

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

public abstract class TeamDetailFragment<A extends TeamScouting> extends Fragment {
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
    private ModelList<TeamScoutingWheel> wheelCursor;
	private MultimediaAdapter multimedia;

	private TeamScoutingWheelAdapter wheelTypesAdapter;

	private LinearLayout season;
	private long teamId;
	private A scouting;
    private Competition comp;

    private OneQuery.ResultHandler<Competition> onCompetitionLoaded =
            new OneQuery.ResultHandler<Competition>() {
                @Override
                public boolean handleResult(Competition result) {
                    if(null!=result) {
                        Log.d(TAG, "result: " + result);
                        setComp(result);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private OneQuery.ResultHandler<A> onScoutingLoaded =
            new OneQuery.ResultHandler<A>() {
                @Override
                public boolean handleResult(A result) {
                    if(null!=result) {
                        Log.d(TAG, "result: " + result);
                        setScouting(result);
                        setView();
                        rootView.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        rootView.setVisibility(View.GONE);
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<TeamScoutingWheel> onWheelTypesLoaded =
            new ManyQuery.ResultHandler<TeamScoutingWheel>() {

                @Override
                public boolean handleResult(CursorList<TeamScoutingWheel> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<TeamScoutingWheel> wheelModel = ModelList.from(result);
                        result.close();
                        wheelTypesAdapter.swapList(wheelModel);
                        Log.d(TAG, "Count: " + result.size());
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<TeamScoutingWheel> onTeamWheelsLoaded =
            new ManyQuery.ResultHandler<TeamScoutingWheel>() {

                @Override
                public boolean handleResult(CursorList<TeamScoutingWheel> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        Log.d(TAG, "Count: " + result.size());
                        wheelCursor = ModelList.from(result);
                        result.close();
                        for(TeamScoutingWheel each : wheelCursor) {
                            createWheel(each);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };

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
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}
    public OneQuery.ResultHandler<A> getOnScoutingLoaded() {
        return onScoutingLoaded;
    }

    public void setOnScoutingLoaded(OneQuery.ResultHandler<A> onScoutingLoaded) {
        this.onScoutingLoaded = onScoutingLoaded;
    }

    public ManyQuery.ResultHandler<TeamScoutingWheel> getOnWheelTypesLoaded() {
        return onWheelTypesLoaded;
    }

    public void setOnWheelTypesLoaded(ManyQuery.ResultHandler<TeamScoutingWheel> onWheelTypesLoaded) {
        this.onWheelTypesLoaded = onWheelTypesLoaded;
    }

    public ManyQuery.ResultHandler<TeamScoutingWheel> getOnTeamWheelsLoaded() {
        return onTeamWheelsLoaded;
    }

    public void setOnTeamWheelsLoaded(ManyQuery.ResultHandler<TeamScoutingWheel> onTeamWheelsLoaded) {
        this.onTeamWheelsLoaded = onTeamWheelsLoaded;
    }
    public Competition getComp() {
        return comp;
    }

    public void setComp(Competition comp) {
        this.comp = comp;
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
		gallery = (TwoWayView) rootView.findViewById(R.id.gallery);
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
				newWheel.setYear(getComp().getYear());
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wheelTypesAdapter = new TeamScoutingWheelAdapter(getActivity(), null, TeamScoutingWheelAdapter.TYPE);
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
            Query.one(Competition.class, "select * from "+Competition.TAG+" where "+Competition._ID+"=?",prefs.getComp()).getAsync(this.getLoaderManager(),onCompetitionLoaded);
            Query.many(TeamScoutingWheel.class, "select * from " + TeamScoutingWheel.TAG + " group by " + TeamScoutingWheel.TYPE).getAsync(this.getLoaderManager(), onWheelTypesLoaded);
            Query.many(TeamScoutingWheel.class, "select * from "+TeamScoutingWheel.TAG+" where "+TeamScoutingWheel.YEAR+"=? and "+TeamScoutingWheel.TEAM+"=?", prefs.getYear(), getTeamId()).getAsync(this.getLoaderManager(), onTeamWheelsLoaded);
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
		this.getActivity().setTitle(Integer.toString(scouting.getTeam().getTeamNumber())+": "+scouting.getTeam().getNickName());
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
	
	public LinearLayout createWheel(TeamScoutingWheel thisWheel) {
		LinearLayout view = (LinearLayout) this.getActivity().getLayoutInflater().inflate(R.layout.fragment_team_detail_wheel, wheels, false);
		view.setTag(thisWheel);
		//1 is the type field
		AutoCompleteTextView type = (AutoCompleteTextView)view.getChildAt(TeamScoutingWheel.FIELD_TYPE);
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
			TextView size = (TextView)view.getChildAt(TeamScoutingWheel.FIELD_SIZE);
			size.setText(num);
		}
		//if the size is currently 0 dont show it for the user's sake
		if(0!=thisWheel.getWheelCount()) {
			//get the number 
			num = Integer.toString(thisWheel.getWheelCount());
			//6 is the count field
			TextView count = (TextView)view.getChildAt(TeamScoutingWheel.FIELD_COUNT);
			count.setText(num);
		}
		//7 is the delete button, if clicked delete the wheel
		view.getChildAt(TeamScoutingWheel.FIELD_DELETE).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				View view = ((View) v.getParent());
				TeamScoutingWheel wheel = (TeamScoutingWheel) view.getTag();
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
			TeamScoutingWheel theWheel = (TeamScoutingWheel) theWheelView.getTag();
			CharSequence type = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_TYPE)).getText();
			theWheel.setWheelType(type);
			CharSequence size = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_SIZE)).getText();
			theWheel.setWheelSize(Utility.getFloatFromText(size));
			CharSequence count = ((TextView) theWheelView.getChildAt(TeamScoutingWheel.FIELD_COUNT)).getText();
			theWheel.setWheelCount(Utility.getIntFromText(count));
			//see if we should update or insert or just tell the user there isnt enough info
            theWheel.asyncSave();
		}
		scouting.setNotes(notes.getText());
	}
	
	public void commitUpdatedScouting() {
        this.getScouting().asyncSave();
	}
}
