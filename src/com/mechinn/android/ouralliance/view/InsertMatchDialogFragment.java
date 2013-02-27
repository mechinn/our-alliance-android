package com.mechinn.android.ouralliance.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;

public class InsertMatchDialogFragment extends DialogFragment {
	public static final String TAG = InsertMatchDialogFragment.class.getSimpleName();
	public static final String MATCH_ARG = "match";
	public static final String TEAMS_ARG = "teams";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onInsertMatchDialogPositiveClick(boolean update, Match match);
    }
    
    private static final int RED1 = 0;
    private static final int RED2 = 1;
    private static final int RED3 = 2;
    private static final int BLUE1 = 3;
    private static final int BLUE2 = 4;
    private static final int BLUE3 = 5;
    
    private Listener listener;
    private Prefs prefs;
    private View dialog;
    private TextView practice;
    private Spinner type;
    private Spinner set;
    private EditText number;
    private Spinner red1;
    private Spinner red2;
    private Spinner red3;
    private Spinner blue1;
    private Spinner blue2;
    private Spinner blue3;
    private Match match;
    private boolean update;
    private Comparator<Integer> intCompare;
    private ArrayList<Integer> teams;
    private ArrayList<Integer> red1Teams;
    private ArrayList<Integer> red2Teams;
    private ArrayList<Integer> red3Teams;
    private ArrayList<Integer> blue1Teams;
    private ArrayList<Integer> blue2Teams;
    private ArrayList<Integer> blue3Teams;
    private ArrayAdapter<Integer> red1Adapter;
    private ArrayAdapter<Integer> red2Adapter;
    private ArrayAdapter<Integer> red3Adapter;
    private ArrayAdapter<Integer> blue1Adapter;
    private ArrayAdapter<Integer> blue2Adapter;
    private ArrayAdapter<Integer> blue3Adapter;
    private SparseIntArray selected;
    private SparseArray<ArrayList<Integer>> arrayLists;
    private SparseArray<ArrayAdapter<Integer>> arrayAdapters;
    private SparseArray<Spinner> teamSpinners;
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
        intCompare = new Comparator<Integer>() {
			public int compare(Integer object1, Integer object2) {
				return object1.compareTo(object2);
			};
		};
        teams = new ArrayList<Integer>();
        teams.add(RED1);
        teams.add(RED2);
        teams.add(RED3);
        teams.add(BLUE1);
        teams.add(BLUE2);
        teams.add(BLUE3);
        red1Teams = this.getArguments().getIntegerArrayList(TEAMS_ARG);
        red2Teams = new ArrayList<Integer>(red1Teams);
        red3Teams = new ArrayList<Integer>(red1Teams);
        blue1Teams = new ArrayList<Integer>(red1Teams);
        blue2Teams = new ArrayList<Integer>(red1Teams);
        blue3Teams = new ArrayList<Integer>(red1Teams);
        arrayLists = new SparseArray<ArrayList<Integer>>();
        arrayLists.put(RED1, red1Teams);
        arrayLists.put(RED2, red2Teams);
        arrayLists.put(RED3, red3Teams);
        arrayLists.put(BLUE1, blue1Teams);
        arrayLists.put(BLUE2, blue2Teams);
        arrayLists.put(BLUE3, blue3Teams);
		red1Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, red1Teams);
		red2Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, red2Teams);
		red3Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, red3Teams);
		blue1Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, blue1Teams);
		blue2Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, blue2Teams);
		blue3Adapter = new ArrayAdapter<Integer>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, blue3Teams);
		arrayAdapters = new SparseArray<ArrayAdapter<Integer>>();
		arrayAdapters.put(RED1, red1Adapter);
		arrayAdapters.put(RED2, red2Adapter);
		arrayAdapters.put(RED3, red3Adapter);
		arrayAdapters.put(BLUE1, blue1Adapter);
		arrayAdapters.put(BLUE2, blue2Adapter);
		arrayAdapters.put(BLUE3, blue3Adapter);
        prefs = new Prefs(activity);
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
		number = (EditText) dialog.findViewById(R.id.number);
		red1 = (Spinner) dialog.findViewById(R.id.red1);
		red2 = (Spinner) dialog.findViewById(R.id.red2);
		red3 = (Spinner) dialog.findViewById(R.id.red3);
		blue1 = (Spinner) dialog.findViewById(R.id.blue1);
		blue2 = (Spinner) dialog.findViewById(R.id.blue2);
		blue3 = (Spinner) dialog.findViewById(R.id.blue3);
		teamSpinners = new SparseArray<Spinner>();
		teamSpinners.put(RED1, red1);
		teamSpinners.put(RED2, red2);
		teamSpinners.put(RED3, red3);
		teamSpinners.put(BLUE1, blue1);
        teamSpinners.put(BLUE2, blue2);
        teamSpinners.put(BLUE3, blue3);
		if(prefs.getPractice()) {
			practice.setVisibility(View.VISIBLE);
			type.setVisibility(View.GONE);
			set.setVisibility(View.GONE);
		} else {
			type.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(Match.QUARTERFINAL==position) {
						set.setVisibility(View.VISIBLE);
						ArrayAdapter<String> quarterfinals = new ArrayAdapter<String>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, R.array.quarterfinals);
						set.setAdapter(quarterfinals);
					} else if(Match.SEMIFINAL==position) {
						set.setVisibility(View.VISIBLE);
						ArrayAdapter<String> semifinals = new ArrayAdapter<String>(InsertMatchDialogFragment.this.getActivity(),android.R.layout.simple_list_item_1, R.array.semifinals);
						set.setAdapter(semifinals);
					} else {
						set.setVisibility(View.INVISIBLE);
						set.setSelection(0);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					//should never happen
				}
			});
		}
		red1.setTag(RED1);
		red2.setTag(RED2);
		red3.setTag(RED3);
		blue1.setTag(BLUE1);
		blue2.setTag(BLUE2);
		blue3.setTag(BLUE3);
		red1.setAdapter(red1Adapter);
		red2.setAdapter(red2Adapter);
		red3.setAdapter(red3Adapter);
		blue1.setAdapter(blue1Adapter);
		blue2.setAdapter(blue2Adapter);
		blue3.setAdapter(blue3Adapter);
		selected = new SparseIntArray();
		OnItemSelectedListener selectTeamListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Integer selectedTeam = (Integer) parent.getAdapter().getItem(position);
				Integer map = (Integer) parent.getTag();
				if(null!=selectedTeam && null!=map) {
					int old = selected.get(map);
					selected.put(map, selectedTeam);
					for(int team : teams) {
						if(map!=team) {
							if(0!=old) {
								arrayLists.get(team).add(old);
							}
							int teamsOld = selected.get(team);
							arrayLists.get(team).remove(selectedTeam);
//							Collections.sort(arrayLists.get(team));
							arrayAdapters.get(team).sort(intCompare);
							arrayAdapters.get(team).notifyDataSetChanged();
							teamSpinners.get(team).setSelection(arrayAdapters.get(team).getPosition(teamsOld));
						}
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//should never happen
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
    		update = true;
    		Log.d(TAG, "update");
		} catch(NullPointerException e) {
			match = new Match();
			yes = R.string.create;
			update = false;
    		Log.d(TAG, "insert");
		}
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
//					team.setNumber(Utility.getIntFromText(teamNumber.getText()));
//					listener.onInsertTeamDialogPositiveClick(update, team);
					dialog.cancel();
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
