package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.source.AOurAllianceDataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class MatchDetailFragment<A extends Match, B extends AOurAllianceDataSource<A>> extends Fragment implements LoaderCallbacks<Cursor> {
	public static final String TAG = MatchDetailFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
    public static final String TEAM_ARG = "team";
	
	public static final int LOADER_MATCH = 0;
    
    private long matchId;

	private View rootView;
//	private Button red1;
//	private Button red2;
//	private Button red3;
//	private Button blue1;
//	private Button blue2;
//	private Button blue3;
//	private TextView redScore;
//	private TextView blueScore;
	private LinearLayout season;
	private Cursor currentView;

	private A match;
	private B dataSource;
	
	public abstract A setMatchFromCursor(Cursor cursor) throws OurAllianceException, SQLException;
	public abstract B createDataSouce();

	public long getMatchId() {
		return matchId;
	}
	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}
//	public TextView getRedScore() {
//		return redScore;
//	}
//	public void setRedScore(TextView redScore) {
//		this.redScore = redScore;
//	}
//	public TextView getBlueScore() {
//		return blueScore;
//	}
//	public void setBlueScore(TextView blueScore) {
//		this.blueScore = blueScore;
//	}
public LinearLayout getSeason() {
    return season;
}
    public void setSeason(LinearLayout season) {
        this.season = season;
    }
	public A getMatch() {
		return match;
	}
	public void setMatch(A match) {
		this.match = match;
	}
	public B getDataSource() {
		return dataSource;
	}
	public void setDataSource(B dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		LoaderManager.enableDebugLogging(true);
		setDataSource(createDataSouce());

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
            matchId = savedInstanceState.getLong(Match.TAG, 0);
    		Log.d(TAG, "match: "+matchId);
        }
    	
    	OnClickListener teamButton = new OnClickListener() {
			public void onClick(View v) {
				Team team = (Team) v.getTag();
	        	Intent intent = new Intent(MatchDetailFragment.this.getActivity(), TeamScoutingActivity.class);
	        	Bundle args = new Bundle();
	        	args.putString(TeamScoutingActivity.MATCHNAME_ARG, match.toString());
	        	args.putLong(TeamScoutingActivity.MATCH_ARG,match.getId());
                args.putLong(TeamScoutingActivity.TEAM_ARG,team.getId());
	        	intent.putExtras(args);
	            startActivity(intent);

		    }
		};
        
        rootView = inflater.inflate(R.layout.fragment_match_detail, container, false);
		season = (LinearLayout) rootView.findViewById(R.id.season);
		return rootView;
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
    		matchId = getArguments().getLong(MATCH_ARG, 0);
    		Log.d(TAG, "match: "+matchId);
        }
        if (matchId != 0) {
    		this.getLoaderManager().restartLoader(LOADER_MATCH, null, this);
        }
    }
	
	@Override
	public void onPause() {
		super.onPause();
		updateScouting();
		commitUpdatedScouting();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(MATCH_ARG, matchId);
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
		this.getActivity().setTitle(match.toString());
//		red1.setText(Integer.toString(match.getRed1().getNumber()));
//		red2.setText(Integer.toString(match.getRed2().getNumber()));
//		red3.setText(Integer.toString(match.getRed3().getNumber()));
//		blue1.setText(Integer.toString(match.getBlue1().getNumber()));
//		blue2.setText(Integer.toString(match.getBlue2().getNumber()));
//		blue3.setText(Integer.toString(match.getBlue3().getNumber()));
//		red1.setTag(match.getRed1());
//		red2.setTag(match.getRed2());
//		red3.setTag(match.getRed3());
//		blue1.setTag(match.getBlue1());
//		blue2.setTag(match.getBlue2());
//		blue3.setTag(match.getBlue3());
//		if(-1!=match.getRedScore()) {
//			redScore.setText(Integer.toString(match.getRedScore()));
//		}
//		if(-1!=match.getBlueScore()) {
//			blueScore.setText(Integer.toString(match.getBlueScore()));
//		}
	}
	
	public void updateScouting() {
		
	}
	
	public void commitUpdatedScouting() {
		try {
			getDataSource().update(this.getMatch());
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setViewFromCursor(Cursor cursor) {
		if(null!=currentView) {
			currentView.close();
		}
		currentView = cursor;
		try {
			this.setMatch(setMatchFromCursor(currentView));
			setView();
			rootView.setVisibility(View.VISIBLE);
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "loader finished");
		switch(loader.getId()) {
			case LOADER_MATCH:
				setViewFromCursor(cursor);
				break;
		}
	}
	
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "loader reset");
		switch(loader.getId()) {
		case LOADER_MATCH:
			setViewFromCursor(null);
			break;
		}
	}
}
