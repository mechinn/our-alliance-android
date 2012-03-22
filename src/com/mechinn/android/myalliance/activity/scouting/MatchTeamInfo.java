package com.mechinn.android.myalliance.activity.scouting;

import com.mechinn.android.myalliance.R;
import com.mechinn.android.myalliance.data.MatchScoutingInterface;
import com.mechinn.android.myalliance.data.Prefs;
import com.mechinn.android.myalliance.providers.MatchScoutingProvider;

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
				matchScouting.updateMatch(comp, matchNum, team, matchSlot.getText().toString(),
						broke.isChecked(), autonomousWorked.isChecked(), balanced.isChecked(), 
						shootingType.indexOfChild(view.findViewById(shootingType.getCheckedRadioButtonId())),
						Integer.parseInt(topScoreTotal.getText().toString()), Integer.parseInt(midScoreTotal.getText().toString()), 
						Integer.parseInt(botScoreTotal.getText().toString()), notes.getText().toString());
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
    	String[] from = new String[] {MatchScoutingProvider.keySlot, MatchScoutingProvider.keyBroke, 
    			MatchScoutingProvider.keyAuto, MatchScoutingProvider.keyBalance, MatchScoutingProvider.keyShooter, 
    			MatchScoutingProvider.keyTop, MatchScoutingProvider.keyMid, MatchScoutingProvider.keyBot, MatchScoutingProvider.keyNotes};
		Cursor match = matchScouting.fetchMatch(comp, matchNum, team);
		match.moveToFirst();
		
    	for(int j=0;j<from.length;++j) {
        	String rowName = from[j];
        	int col = match.getColumnIndex(rowName);
        	Log.d("matchTeams",rowName);
        	Log.d("matchTeams",Integer.toString(col));
        	if(rowName.equals(MatchScoutingProvider.keySlot)) {
        		matchSlot.setText(match.getString(col));
        	} else if(rowName.equals(MatchScoutingProvider.keyBroke)) {
        		broke.setChecked(match.getInt(col)==0?false:true);
        	} else if(rowName.equals(MatchScoutingProvider.keyAuto)) {
        		autonomousWorked.setChecked(match.getInt(col)==0?false:true);
        	} else if(rowName.equals(MatchScoutingProvider.keyBalance)) {
        		balanced.setChecked(match.getInt(col)==0?false:true);
        	} else if(rowName.equals(MatchScoutingProvider.keyShooter)) {
        		Log.d("match team info",Integer.toString(match.getInt(col)));
        		switch(match.getInt(col)) {
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
        	} else if(rowName.equals(MatchScoutingProvider.keyTop)) {
        		topScoreTotal.setText(Integer.toString(match.getInt(col)));
        	} else if(rowName.equals(MatchScoutingProvider.keyMid)) {
        		midScoreTotal.setText(Integer.toString(match.getInt(col)));
        	} else if(rowName.equals(MatchScoutingProvider.keyBot)) {
        		botScoreTotal.setText(Integer.toString(match.getInt(col)));
        	} else if(rowName.equals(MatchScoutingProvider.keyNotes)) {
        		notes.setText(match.getString(col));
        	}
        }
    }
}
