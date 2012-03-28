package com.mechinn.android.ouralliance.activity.scouting;

import java.util.HashMap;
import java.util.Map;

import com.bugsense.trace.BugSenseHandler;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScoutingInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchTeamInfo extends Fragment {
	private Activity activity;
	private Bundle args;
	private View view;
	private int matchNum;
	private int team;
	private TextView teamNumber;
	private TextView matchSlot;
	private CheckBox broke;
	private CheckBox autonomousWorked;
	private CheckBox balanced;
	private RadioGroup shootingType;
	private RadioButton noShooting;
	private RadioButton dunker;
	private RadioButton shooter;
	private TextView topScoreTotal;
	private TextView midScoreTotal;
	private TextView botScoreTotal;
	private TextView notes;
	private MatchScoutingInterface matchScouting;
	private TeamScoutingInterface teamScouting;
	private Prefs prefs;
	private String comp;
	private static final String logTag = "MatchTeamInfo";
	
    public static MatchTeamInfo newInstance(int matchNum, int thisTeam) {
    	MatchTeamInfo f = new MatchTeamInfo();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("match", matchNum);
        args.putInt("team", thisTeam);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
    	return getArguments().getInt("team", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        view = inflater.inflate(R.layout.scouting, container, false);
        activity = this.getActivity();
        args = this.getArguments();
		teamScouting = new TeamScoutingInterface(activity);
		matchScouting = new MatchScoutingInterface(activity);
		prefs = new Prefs(activity);
		comp = prefs.getCompetition();
        
        //if we dont have arguments we must have extras
        if(args == null) {
        	args = activity.getIntent().getExtras();
        }

    	matchNum = args.getInt("match",0);
    	team = args.getInt("team",0);

    	teamNumber = (TextView)view.findViewById(R.id.teamNumber);
    	matchSlot = (TextView)view.findViewById(R.id.matchSlot);
    	broke = (CheckBox)view.findViewById(R.id.broke);
    	autonomousWorked = (CheckBox)view.findViewById(R.id.autonomousWorked);
    	balanced = (CheckBox)view.findViewById(R.id.balanced);
    	shootingType = (RadioGroup)view.findViewById(R.id.MatchScoutingShootingType);
    	noShooting = (RadioButton)view.findViewById(R.id.MatchScoutingNoShooting);
    	dunker = (RadioButton)view.findViewById(R.id.MatchScoutingDunker);
    	shooter = (RadioButton)view.findViewById(R.id.MatchScoutingShooter);
    	topScoreTotal = (TextView)view.findViewById(R.id.topScoreTotal);
		midScoreTotal = (TextView)view.findViewById(R.id.midScoreTotal);
		botScoreTotal = (TextView)view.findViewById(R.id.botScoreTotal);
		notes = (TextView)view.findViewById(R.id.notes);
    	
    	teamNumber.setText(Integer.toString(team));
    	
    	getInfo();
    	
    	Button top = (Button)view.findViewById(R.id.addTopScore);
    	top.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int score = Integer.parseInt(topScoreTotal.getText().toString())+1;
				topScoreTotal.setText(Integer.toString(score));
			}
		});
    	
    	Button mid = (Button)view.findViewById(R.id.addMidScore);
    	mid.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int score = Integer.parseInt(midScoreTotal.getText().toString())+1;
				midScoreTotal.setText(Integer.toString(score));
			}
		});
    	
    	Button bot = (Button)view.findViewById(R.id.addBotScore);
    	bot.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int score = Integer.parseInt(botScoreTotal.getText().toString())+1;
				botScoreTotal.setText(Integer.toString(score));
			}
		});
    	
		Button save = (Button)view.findViewById(R.id.matchScoutingSave);
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int type = shootingType.indexOfChild(view.findViewById(shootingType.getCheckedRadioButtonId()));
				matchScouting.updateMatch(comp, matchNum, team, matchSlot.getText().toString(),
						broke.isChecked(), autonomousWorked.isChecked(), balanced.isChecked(), 
						type, Integer.parseInt(topScoreTotal.getText().toString()), 
						Integer.parseInt(midScoreTotal.getText().toString()), 
						Integer.parseInt(botScoreTotal.getText().toString()), notes.getText().toString());
				Cursor thisTeam = matchScouting.fetchTeam(team);
				if(thisTeam!=null && thisTeam.getCount()>0){
					double avgHoops = 0;
					double avgBalance = 0;
					double avgBroke = 0;
					do {
						int top = thisTeam.getColumnIndex(MatchScoutingProvider.keyTop);
						int mid = thisTeam.getColumnIndex(MatchScoutingProvider.keyMid);
						int bot = thisTeam.getColumnIndex(MatchScoutingProvider.keyBot);
						top = thisTeam.getInt(top);
						mid = thisTeam.getInt(mid);
						bot = thisTeam.getInt(bot);
						avgHoops+=(top*3+mid*2+bot);
						avgBalance+=thisTeam.getInt(thisTeam.getColumnIndex(MatchScoutingProvider.keyBalance));
						avgBroke+=thisTeam.getInt(thisTeam.getColumnIndex(MatchScoutingProvider.keyBroke));
					} while(thisTeam.moveToNext());
					avgHoops/=thisTeam.getCount();
					avgBalance/=thisTeam.getCount();
					avgBroke/=thisTeam.getCount();
			        boolean fender;
			        boolean key;
			        switch(type) {
		    			default:
		    				fender = false;
		    				key = false;
		    				break;
		    			case 1:
		    				fender = true;
		    				key = false;
		    				break;
		    			case 2:
		    				fender = false;
		    				key = true;
		    				break;
		    		}
					int updatedRows = teamScouting.updateTeam(team, fender, key, autonomousWorked.isChecked(), avgHoops, avgBalance, avgBroke);
					Log.d("saveMatchTeamInfo", "Updated "+updatedRows+" rows.");
					Toast.makeText(activity, "Finished Saving", Toast.LENGTH_SHORT).show();
				} else {
				    BugSenseHandler.log(logTag, new Exception("no team"));
					Toast.makeText(activity, "Something went wrong loading data, we notified the developer", Toast.LENGTH_SHORT).show();
					activity.finish();
				}
			}
		});
		
		Button discard = (Button)view.findViewById(R.id.matchScoutingDiscard);
		discard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getInfo();
			}
		});
    	
        return view;
    }
    
    public void getInfo() {
		Cursor match = matchScouting.fetchMatch(comp, matchNum, team);
		if(match!=null && match.getCount()>0) {
			int col = match.getColumnIndex(MatchScoutingProvider.keySlot);
			Log.d("getinfo","col index = "+col);
			matchSlot.setText(match.getString(col));
			broke.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyBroke))==0?false:true);
			autonomousWorked.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyAuto))==0?false:true);
			balanced.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyBalance))==0?false:true);
			switch(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyShooter))) {
				default:
					noShooting.toggle();
					break;
				case 1:
					dunker.toggle();
					break;
				case 2:
					shooter.toggle();
					break;
			}
			topScoreTotal.setText(Integer.toString(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyTop))));
			midScoreTotal.setText(Integer.toString(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyMid))));
			botScoreTotal.setText(Integer.toString(match.getInt(match.getColumnIndex(MatchScoutingProvider.keyBot))));
			notes.setText(match.getString(match.getColumnIndex(MatchScoutingProvider.keyNotes)));
		} else {
			BugSenseHandler.log(logTag, new Exception("no match"));
			Toast.makeText(activity, "Something went wrong loading data, we notified the developer", Toast.LENGTH_SHORT).show();
			activity.finish();
		}
    }
}
