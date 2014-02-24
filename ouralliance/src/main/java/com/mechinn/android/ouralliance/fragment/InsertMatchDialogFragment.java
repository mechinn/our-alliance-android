package com.mechinn.android.ouralliance.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MatchTeamSelectAdapter;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;

import se.emilsjolander.sprinkles.ModelList;

public class InsertMatchDialogFragment extends DialogFragment {
	public static final String TAG = InsertMatchDialogFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
	public static final String TEAMS_ARG = "teams";

    private Prefs prefs;
    private View dialog;
    private TextView practice;
    private Spinner type;
    private Spinner set;
    private LinearLayout numberContainer;
    private EditText number;
    private ArrayList<Spinner> spinners;
    private Spinner red1;
    private Spinner red2;
    private Spinner red3;
    private Spinner blue1;
    private Spinner blue2;
    private Spinner blue3;
    private Match match;
    private ModelList<CompetitionTeam> teams;
    private ArrayList<MatchTeamSelectAdapter> adapters;
    private MatchTeamSelectAdapter red1Adapter;
    private MatchTeamSelectAdapter red2Adapter;
    private MatchTeamSelectAdapter red3Adapter;
    private MatchTeamSelectAdapter blue1Adapter;
    private MatchTeamSelectAdapter blue2Adapter;
    private MatchTeamSelectAdapter blue3Adapter;
    private ArrayAdapter<Integer> quarterfinals;
    private ArrayAdapter<Integer> semifinals;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        teams = (ModelList<CompetitionTeam>) this.getArguments().getSerializable(TEAMS_ARG);
        adapters = new ArrayList<MatchTeamSelectAdapter>(6);
        red1Adapter = new MatchTeamSelectAdapter(this.getActivity(),teams, MatchTeamSelectAdapter.RED1);
        red2Adapter = new MatchTeamSelectAdapter(this.getActivity(),teams, MatchTeamSelectAdapter.RED2);
        red3Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),teams, MatchTeamSelectAdapter.RED3);
        blue1Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),teams, MatchTeamSelectAdapter.BLUE1);
        blue2Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),teams, MatchTeamSelectAdapter.BLUE2);
        blue3Adapter = new MatchTeamSelectAdapter(InsertMatchDialogFragment.this.getActivity(),teams, MatchTeamSelectAdapter.BLUE3);
        adapters.add(red1Adapter);
        adapters.add(red2Adapter);
        adapters.add(red3Adapter);
        adapters.add(blue1Adapter);
        adapters.add(blue2Adapter);
        adapters.add(blue3Adapter);
        prefs = new Prefs(activity);
        quarterfinals = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, new Integer[]{1,2,3,4});
    	semifinals = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, new Integer[]{1,2});
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
		set = (Spinner) dialog.findViewById(R.id.set);
		numberContainer = (LinearLayout) dialog.findViewById(R.id.numberContainer);
		number = (EditText) dialog.findViewById(R.id.number);
		red1 = (Spinner) dialog.findViewById(R.id.red1);
		red2 = (Spinner) dialog.findViewById(R.id.red2);
		red3 = (Spinner) dialog.findViewById(R.id.red3);
		blue1 = (Spinner) dialog.findViewById(R.id.blue1);
		blue2 = (Spinner) dialog.findViewById(R.id.blue2);
		blue3 = (Spinner) dialog.findViewById(R.id.blue3);
        spinners = new ArrayList<Spinner>(6);
        spinners.add(red1);
        spinners.add(red2);
        spinners.add(red3);
        spinners.add(blue1);
        spinners.add(blue2);
        spinners.add(blue3);
		if(prefs.getPractice()) {
			practice.setVisibility(View.VISIBLE);
			type.setVisibility(View.GONE);
			set.setVisibility(View.GONE);
		} else {
			practice.setVisibility(View.GONE);
			type.setVisibility(View.VISIBLE);
			type.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(Match.QUARTERFINAL==position) {
						set.setVisibility(View.VISIBLE);
						set.setAdapter(quarterfinals);
					} else if(Match.SEMIFINAL==position) {
						set.setVisibility(View.VISIBLE);
						set.setAdapter(semifinals);
					} else {
						set.setVisibility(View.INVISIBLE);
						set.setSelection(0);
					}
					if(Match.QUALIFIER==position) {
						numberContainer.setVisibility(View.VISIBLE);
					} else {
						numberContainer.setVisibility(View.GONE);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					//should never happen
				}
			});
		}
		red1.setAdapter(red1Adapter);
		red2.setAdapter(red2Adapter);
		red3.setAdapter(red3Adapter);
		blue1.setAdapter(blue1Adapter);
		blue2.setAdapter(blue2Adapter);
		blue3.setAdapter(blue3Adapter);
        for(int spinner=0;spinner<spinners.size();++spinner) {
            spinners.get(spinner).setSelection(spinner);
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
			number.setText(Integer.toString(match.getNumber()));
    		yes = R.string.update;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			match = new Match();
			yes = R.string.create;
    		Log.d(TAG, "insert");
		}
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (prefs.getPractice()) {
                        match.setType(Match.PRACTICE);
                    } else {
                        match.setType(type.getSelectedItemPosition());
                        if (View.VISIBLE == set.getVisibility()) {
                            match.setSet(set.getSelectedItemPosition() + 1);
                        }
                    }
                    match.setRed1(((MatchTeamSelectAdapter) red1.getAdapter()).getItem(red1.getSelectedItemPosition()));
                    match.setRed2(((MatchTeamSelectAdapter) red2.getAdapter()).getItem(red2.getSelectedItemPosition()));
                    match.setRed3(((MatchTeamSelectAdapter) red3.getAdapter()).getItem(red3.getSelectedItemPosition()));
                    match.setBlue1(((MatchTeamSelectAdapter) blue1.getAdapter()).getItem(blue1.getSelectedItemPosition()));
                    match.setBlue2(((MatchTeamSelectAdapter) blue2.getAdapter()).getItem(blue2.getSelectedItemPosition()));
                    match.setBlue3(((MatchTeamSelectAdapter) blue3.getAdapter()).getItem(blue3.getSelectedItemPosition()));
                    match.setNumber(Utility.getIntFromText(number.getText()));
                    match.setRedScore(-1);
                    match.setBlueScore(-1);
                    match.setCompetition(match.getRed1().getCompetition());
                    match.save();
                    switch (prefs.getYear()) {
                        case 2014:
                            new MatchScouting2014(match, match.getRed1()).save();
                            new MatchScouting2014(match, match.getRed2()).save();
                            new MatchScouting2014(match, match.getRed3()).save();
                            new MatchScouting2014(match, match.getBlue1()).save();
                            new MatchScouting2014(match, match.getBlue2()).save();
                            new MatchScouting2014(match, match.getBlue3()).save();
                            break;
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
