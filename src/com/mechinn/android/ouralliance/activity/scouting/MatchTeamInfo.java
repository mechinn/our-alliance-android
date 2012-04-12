package com.mechinn.android.ouralliance.activity.scouting;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScoutingInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MatchTeamInfo extends Fragment {
	private static final String TAG = "MatchTeamInfo";
	private Activity activity;
	private Bundle args;
	private View view;
	private int matchNum;
	private int team;
	private TextView teamNumber;
	private TextView matchSlot;
	private CheckBox broke;
	private CheckBox autoBridge;
	private CheckBox autoShooter;
	private CheckBox balanced;
	private RadioGroup shootingType;
	private RadioButton noShooting;
	private RadioButton dunker;
	private RadioButton shooter;
	private RadioGroup mode;
	private RadioButton autonomous;
	private RadioButton teleoperated;
	private TextView topScoreTotal;
	private TextView midScoreTotal;
	private TextView botScoreTotal;
	private TextView missTotal;
	private TextView notes;
	private MatchScoutingInterface matchScouting;
	private TeamScoutingInterface teamScouting;
	private Prefs prefs;
	private String comp;
	private int topAuto;
	private int midAuto;
	private int botAuto;
	private int missAuto;
	private int top;
	private int mid;
	private int bot;
	private int miss;
	
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
    	autoBridge = (CheckBox)view.findViewById(R.id.autoBridge);
    	autoShooter = (CheckBox)view.findViewById(R.id.autoShooter);
    	balanced = (CheckBox)view.findViewById(R.id.balanced);
    	shootingType = (RadioGroup)view.findViewById(R.id.MatchScoutingShootingType);
    	noShooting = (RadioButton)view.findViewById(R.id.MatchScoutingNoShooting);
    	dunker = (RadioButton)view.findViewById(R.id.MatchScoutingDunker);
    	shooter = (RadioButton)view.findViewById(R.id.MatchScoutingShooter);
    	topScoreTotal = (TextView)view.findViewById(R.id.topScoreTotal);
		midScoreTotal = (TextView)view.findViewById(R.id.midScoreTotal);
		botScoreTotal = (TextView)view.findViewById(R.id.botScoreTotal);
		missTotal = (TextView)view.findViewById(R.id.missTotal);
		
		autonomous = (RadioButton)view.findViewById(R.id.autonomous);
    	autonomous.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					top = Integer.parseInt(topScoreTotal.getText().toString());
					mid = Integer.parseInt(midScoreTotal.getText().toString());
					bot = Integer.parseInt(botScoreTotal.getText().toString());
					miss = Integer.parseInt(missTotal.getText().toString());
					topScoreTotal.setText(Integer.toString(topAuto));
					midScoreTotal.setText(Integer.toString(midAuto));
					botScoreTotal.setText(Integer.toString(botAuto));
					missTotal.setText(Integer.toString(missAuto));
				}
			}
    	});
    	teleoperated = (RadioButton)view.findViewById(R.id.teleoperated);
    	teleoperated.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					topAuto = Integer.parseInt(topScoreTotal.getText().toString());
					midAuto = Integer.parseInt(midScoreTotal.getText().toString());
					botAuto = Integer.parseInt(botScoreTotal.getText().toString());
					missAuto = Integer.parseInt(missTotal.getText().toString());
					topScoreTotal.setText(Integer.toString(top));
					midScoreTotal.setText(Integer.toString(mid));
					botScoreTotal.setText(Integer.toString(bot));
					missTotal.setText(Integer.toString(miss));
				}
			}
    	});
		
		notes = (TextView)view.findViewById(R.id.notes);
    	
    	teamNumber.setText(Integer.toString(team));
    	
    	getInfo();
    	Button button;
    	button = (Button)view.findViewById(R.id.subTopScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						topAuto = Integer.parseInt(topScoreTotal.getText().toString())-1;
						if(topAuto<0) {
							topAuto = 0;
						}
					} catch(NumberFormatException e) {
						topAuto = 0;
					}
					topScoreTotal.setText(Integer.toString(topAuto));
				} else {
					try{
						top = Integer.parseInt(topScoreTotal.getText().toString())-1;
						if(top<0) {
							top = 0;
						}
					} catch(NumberFormatException e) {
						top = 0;
					}
					topScoreTotal.setText(Integer.toString(top));
				}
			}
		});
    	button = (Button)view.findViewById(R.id.addTopScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						topAuto = Integer.parseInt(topScoreTotal.getText().toString())+1;
						if(topAuto<0) {
							topAuto = 0;
						}
					} catch(NumberFormatException e) {
						topAuto = 0;
					}
					topScoreTotal.setText(Integer.toString(topAuto));
				} else {
					try{
						top = Integer.parseInt(topScoreTotal.getText().toString())+1;
						if(top<0) {
							top = 0;
						}
					} catch(NumberFormatException e) {
						top = 0;
					}
					topScoreTotal.setText(Integer.toString(top));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.subMidScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						midAuto = Integer.parseInt(midScoreTotal.getText().toString())-1;
						if(midAuto<0) {
							midAuto = 0;
						}
					} catch(NumberFormatException e) {
						midAuto = 0;
					}
					midScoreTotal.setText(Integer.toString(midAuto));
				} else {
					try{
						mid = Integer.parseInt(midScoreTotal.getText().toString())-1;
						if(mid<0) {
							mid = 0;
						}
					} catch(NumberFormatException e) {
						mid = 0;
					}
					midScoreTotal.setText(Integer.toString(mid));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.addMidScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						midAuto = Integer.parseInt(midScoreTotal.getText().toString())+1;
						if(midAuto<0) {
							midAuto = 0;
						}
					} catch(NumberFormatException e) {
						midAuto = 0;
					}
					midScoreTotal.setText(Integer.toString(midAuto));
				} else {
					try{
						mid = Integer.parseInt(midScoreTotal.getText().toString())+1;
						if(mid<0) {
							mid = 0;
						}
					} catch(NumberFormatException e) {
						mid = 0;
					}
					midScoreTotal.setText(Integer.toString(mid));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.subBotScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						botAuto = Integer.parseInt(botScoreTotal.getText().toString())-1;
						if(botAuto<0) {
							botAuto = 0;
						}
					} catch(NumberFormatException e) {
						botAuto = 0;
					}
					botScoreTotal.setText(Integer.toString(botAuto));
				} else {
					try{
						bot = Integer.parseInt(botScoreTotal.getText().toString())-1;
						if(bot<0) {
							bot = 0;
						}
					} catch(NumberFormatException e) {
						bot = 0;
					}
					botScoreTotal.setText(Integer.toString(bot));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.addBotScore);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						botAuto = Integer.parseInt(botScoreTotal.getText().toString())+1;
						if(botAuto<0) {
							botAuto = 0;
						}
					} catch(NumberFormatException e) {
						botAuto = 0;
					}
					botScoreTotal.setText(Integer.toString(botAuto));
				} else {
					try{
						bot = Integer.parseInt(botScoreTotal.getText().toString())+1;
						if(bot<0) {
							bot = 0;
						}
					} catch(NumberFormatException e) {
						bot = 0;
					}
					botScoreTotal.setText(Integer.toString(bot));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.subMiss);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						missAuto = Integer.parseInt(missTotal.getText().toString())-1;
						if(missAuto<0) {
							missAuto = 0;
						}
					} catch(NumberFormatException e) {
						missAuto = 0;
					}
					missTotal.setText(Integer.toString(missAuto));
				} else {
					try{
						miss = Integer.parseInt(missTotal.getText().toString())-1;
						if(miss<0) {
							miss = 0;
						}
					} catch(NumberFormatException e) {
						miss = 0;
					}
					missTotal.setText(Integer.toString(miss));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.addMiss);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(autonomous.isChecked()){
					try{
						missAuto = Integer.parseInt(missTotal.getText().toString())+1;
						if(missAuto<0) {
							missAuto = 0;
						}
					} catch(NumberFormatException e) {
						missAuto = 0;
					}
					missTotal.setText(Integer.toString(missAuto));
				} else {
					try{
						miss = Integer.parseInt(missTotal.getText().toString())+1;
						if(miss<0) {
							miss = 0;
						}
					} catch(NumberFormatException e) {
						miss = 0;
					}
					missTotal.setText(Integer.toString(miss));
				}
			}
		});
    	
    	button = (Button)view.findViewById(R.id.teamInfo);
    	button.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
				Intent intent = new Intent("com.mechinn.android.ouralliance.OpenTeamInfo");
				intent.putExtra("team", team);
	    		startActivity(intent);
			}
    	});
    	
    	button = (Button)view.findViewById(R.id.matchScoutingSave);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int type = shootingType.indexOfChild(view.findViewById(shootingType.getCheckedRadioButtonId()));
				if(autonomous.isChecked()) {
					topAuto = Integer.parseInt(topScoreTotal.getText().toString());
					midAuto = Integer.parseInt(midScoreTotal.getText().toString());
					botAuto = Integer.parseInt(botScoreTotal.getText().toString());
					missAuto = Integer.parseInt(missTotal.getText().toString());
				} else {
					top = Integer.parseInt(topScoreTotal.getText().toString());
					mid = Integer.parseInt(midScoreTotal.getText().toString());
					bot = Integer.parseInt(botScoreTotal.getText().toString());
					miss = Integer.parseInt(missTotal.getText().toString());
				}
				matchScouting.updateMatch(comp, matchNum, team, matchSlot.getText().toString(),
						broke.isChecked(), autoBridge.isChecked(), autoShooter.isChecked(), balanced.isChecked(), 
						type, top, mid, bot, miss, topAuto, midAuto, botAuto, missAuto, notes.getText().toString());
				Cursor thisTeam = matchScouting.fetchTeam(team);
				if(thisTeam!=null && !thisTeam.isClosed() && thisTeam.getCount()>0){
					double avgAuto = 0;
					double avgHoops = 0;
					double avgBalance = 0;
					double avgBroke = 0;
					do {
						avgAuto+=(topAuto*6+midAuto*4+botAuto*2);
						avgHoops+=(top*3+mid*2+bot);
						avgBalance+=thisTeam.getInt(thisTeam.getColumnIndex(MatchScoutingProvider.KEY_BALANCE));
						avgBroke+=thisTeam.getInt(thisTeam.getColumnIndex(MatchScoutingProvider.KEY_BROKE));
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
					int updatedRows = teamScouting.updateTeam(team, fender, key, autoBridge.isChecked(), autoShooter.isChecked(), avgAuto, avgHoops, avgBalance, avgBroke);
					Log.d("saveMatchTeamInfo", "Updated "+updatedRows+" rows.");
					Toast.makeText(activity, "Finished Saving", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "Something went wrong loading data", Toast.LENGTH_SHORT).show();
					activity.finish();
				}
			}
		});
		
    	button = (Button)view.findViewById(R.id.matchScoutingDiscard);
    	button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getInfo();
			}
		});
    	
        return view;
    }
    
    public void getInfo() {
		Cursor match = matchScouting.fetchMatch(comp, matchNum, team);
		if(match!=null && !match.isClosed() && match.getCount()>0) {
			int col = match.getColumnIndex(MatchScoutingProvider.KEY_SLOT);
			Log.d("getinfo","col index = "+col);
			matchSlot.setText(match.getString(col));
			broke.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_BROKE))==0?false:true);
			autoBridge.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_AUTO_BRIDGE))==0?false:true);
			autoShooter.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_AUTO_SHOOTER))==0?false:true);
			balanced.setChecked(match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_BALANCE))==0?false:true);
			switch(match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_SHOOTER))) {
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
			top = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_TOP));
			mid = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_MID));
			bot = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_BOT));
			miss = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_MISS));
			topAuto = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_TOP_AUTO));
			midAuto = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_MID_AUTO));
			botAuto = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_BOT_AUTO));
			missAuto = match.getInt(match.getColumnIndex(MatchScoutingProvider.KEY_MISS_AUTO));
			topScoreTotal.setText(Integer.toString(topAuto));
			midScoreTotal.setText(Integer.toString(midAuto));
			botScoreTotal.setText(Integer.toString(botAuto));
			missTotal.setText(Integer.toString(missAuto));
			notes.setText(match.getString(match.getColumnIndex(MatchScoutingProvider.KEY_NOTES)));
		} else {
			Toast.makeText(activity, "Something went wrong loading data", Toast.LENGTH_SHORT).show();
			activity.finish();
		}
    }
}
