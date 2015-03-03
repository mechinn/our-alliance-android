package com.mechinn.android.ouralliance.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MatchTeamSelectAdapter;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class InsertMatchDialogFragment extends DialogFragment {
    public static final String TAG = "InsertMatchDialogFrag";
	public static final String MATCH_ARG = "match";

    private Prefs prefs;
    private View dialog;
    private TextView practice;
    private Spinner type;
    private Spinner set;
    private Spinner matchSpin;
    private LinearLayout numberContainer;
    private EditText number;
    private Spinner[] spinners;
    private Spinner red1;
    private Spinner red2;
    private Spinner red3;
    private Spinner blue1;
    private Spinner blue2;
    private Spinner blue3;
    private Match match;
    private List<EventTeam> teams;
    private ArrayList<MatchTeamSelectAdapter> adapters;
    private MatchTeamSelectAdapter red1Adapter;
    private MatchTeamSelectAdapter red2Adapter;
    private MatchTeamSelectAdapter red3Adapter;
    private MatchTeamSelectAdapter blue1Adapter;
    private MatchTeamSelectAdapter blue2Adapter;
    private MatchTeamSelectAdapter blue3Adapter;
    private ArrayAdapter<Integer> quarterfinals;
    private ArrayAdapter<Integer> semifinals;
    private ArrayAdapter<Integer> matchAdapter;
    private int teamCount;
    private long eventId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
        adapters = new ArrayList<MatchTeamSelectAdapter>(6);
        teamCount = 0;
        red1Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        red2Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        red3Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        blue1Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        blue2Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        blue3Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),null, teamCount++);
        adapters.add(red1Adapter);
        adapters.add(red2Adapter);
        adapters.add(red3Adapter);
        adapters.add(blue1Adapter);
        adapters.add(blue2Adapter);
        adapters.add(blue3Adapter);
        prefs = new Prefs(activity);
        quarterfinals = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, new Integer[]{1,2,3,4});
    	semifinals = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, new Integer[]{1,2});
        matchAdapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.dialog_match_insert, null);
        practice = (TextView) dialog.findViewById(R.id.practice);
        type = (Spinner) dialog.findViewById(R.id.type);
        type.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 2:
                        set.setVisibility(View.VISIBLE);
                        set.setAdapter(quarterfinals);
                        break;
                    case 3:
                        set.setVisibility(View.VISIBLE);
                        set.setAdapter(semifinals);
                        break;
                    default:
                        set.setVisibility(View.INVISIBLE);
                        set.setSelection(0);
                        break;
                }
                switch(position) {
                    case 0:
                        numberContainer.setVisibility(View.VISIBLE);
                        matchSpin.setVisibility(View.GONE);
                        break;
                    default:
                        numberContainer.setVisibility(View.GONE);
                        matchSpin.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        set = (Spinner) dialog.findViewById(R.id.set);
        matchSpin = (Spinner) dialog.findViewById(R.id.match);
        numberContainer = (LinearLayout) dialog.findViewById(R.id.numberContainer);
        number = (EditText) dialog.findViewById(R.id.number);
        red1 = (Spinner) dialog.findViewById(R.id.red1);
        red2 = (Spinner) dialog.findViewById(R.id.red2);
        red3 = (Spinner) dialog.findViewById(R.id.red3);
        blue1 = (Spinner) dialog.findViewById(R.id.blue1);
        blue2 = (Spinner) dialog.findViewById(R.id.blue2);
        blue3 = (Spinner) dialog.findViewById(R.id.blue3);
        matchSpin.setAdapter(matchAdapter);
        spinners = new Spinner[] {red1, red2, red3, blue1, blue2, blue3};
		if(prefs.isPractice()) {
			practice.setVisibility(View.VISIBLE);
			type.setVisibility(View.GONE);
			set.setVisibility(View.GONE);
		} else {
			practice.setVisibility(View.GONE);
			type.setVisibility(View.VISIBLE);
		}
		red1.setAdapter(red1Adapter);
		red2.setAdapter(red2Adapter);
		red3.setAdapter(red3Adapter);
		blue1.setAdapter(blue1Adapter);
		blue2.setAdapter(blue2Adapter);
		blue3.setAdapter(blue3Adapter);
        for(int spinner=0;spinner<teamCount;++spinner) {
            spinners[spinner].setSelection(spinner);
        }
		OnItemSelectedListener selectTeamListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int map = ((MatchTeamSelectAdapter)parent.getAdapter()).getTeam();
                for(MatchTeamSelectAdapter adapter : adapters) {
                    Log.d(TAG, "disable "+position+" on "+adapter.getTeam());
                    adapter.disablePosition(map, position);
                }
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

            }
		};
		red1.setOnItemSelectedListener(selectTeamListener);
		red2.setOnItemSelectedListener(selectTeamListener);
		red3.setOnItemSelectedListener(selectTeamListener);
		blue1.setOnItemSelectedListener(selectTeamListener);
		blue2.setOnItemSelectedListener(selectTeamListener);
		blue3.setOnItemSelectedListener(selectTeamListener);
		int yes;
		try {
			match = (Match) this.getArguments().getSerializable(MATCH_ARG);
			number.setText(Integer.toString(match.getMatchNumber()));
    		yes = R.string.update;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			match = new Match();
			yes = R.string.create;
    		Log.d(TAG, "insert");
		}
        eventId = prefs.getComp();
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (prefs.isPractice()) {
                        match.setCompLevel(Match.PRACTICE);
                    } else {
                        String level = Match.QUALIFIER;
                        switch (type.getSelectedItemPosition()) {
                            case 1:
                                level = Match.EIGHTH_FINALS;
                                break;
                            case 2:
                                level = Match.QUARTER_FINALS;
                                break;
                            case 3:
                                level = Match.SEMI_FINALS;
                                break;
                            case 4:
                                level = Match.FINALS;
                                break;
                        }
                        match.setCompLevel(level);
                        if (View.VISIBLE == set.getVisibility()) {
                            match.setSetNumber(set.getSelectedItemPosition() + 1);
                        }
                    }
                    if (View.VISIBLE == matchSpin.getVisibility()) {
                        match.setMatchNumber(matchSpin.getSelectedItemPosition() + 1);
                    } else {
                        match.setMatchNumber(Utility.getIntFromText(number.getText()));
                    }
                    match.setRedScore(-1);
                    match.setBlueScore(-1);
                    AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
                        @Override
                        public void run() throws Exception {
                            match.setEvent(Model.load(Event.class, eventId));
                            ActiveAndroid.beginTransaction();
                            try {
                                match.saveMod();
                                switch (prefs.getYear()) {
                                    case 2014:
                                        List<MatchScouting2014> teams = new ArrayList<MatchScouting2014>(6);
                                        for (int team = 0; team < teamCount; team++) {
                                            MatchScouting2014 scouting = new MatchScouting2014();
                                            scouting.setMatch(match);
                                            EventTeam eventTeam = (EventTeam) ((MatchTeamSelectAdapter) spinners[team].getAdapter()).getItem(spinners[team].getSelectedItemPosition());
                                            TeamScouting2014 scouting2014 = new Select().from(TeamScouting2014.class).where(TeamScouting2014.TEAM + "=?", eventTeam.getTeam().getId()).executeSingle();
                                            scouting.setTeamScouting(scouting2014);
                                            if (team < teamCount / 2) {
                                                scouting.setPosition(team);
                                                scouting.setAlliance(false);
                                            } else {
                                                scouting.setPosition((team - teamCount) / 2);
                                                scouting.setAlliance(true);
                                            }
                                            scouting.saveMod();
                                        }
                                        break;
                                }
                                ActiveAndroid.setTransactionSuccessful();
                                EventBus.getDefault().post(match);
                                switch (prefs.getYear()) {
                                    case 2014:
                                        EventBus.getDefault().post(new MatchScouting2014());
                                        break;
                                }

                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        }
                    });
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick (DialogInterface dialog,int id){
                    dialog.cancel();
                }
            });
        loadEventTeams();
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    public void loadEventTeams() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<EventTeam> teams = new Select().from(EventTeam.class).where(EventTeam.EVENT+"=?",prefs.getComp()).execute();
                EventBus.getDefault().post(new LoadEventTeams(teams));
            }
        });
    }
    public void onEventMainThread(EventTeam eventTeamsChanged) {
        loadEventTeams();
    }
    public void onEventMainThread(LoadEventTeams teams) {
        for(MatchTeamSelectAdapter adapter : adapters) {
            adapter.setTeams(teams.getTeams());
            adapter.notifyDataSetChanged();
        }
    }
    private class LoadEventTeams {
        List<EventTeam> teams;
        public LoadEventTeams(List<EventTeam> teams) {
            this.teams = teams;
            Collections.sort(this.teams);
        }
        public List<EventTeam> getTeams() {
            return teams;
        }
    }
}
