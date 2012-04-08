package com.mechinn.android.ouralliance.activity.scouting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import com.mechinn.android.ouralliance.FragActivity;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.providers.MatchListProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchTeams extends ListFragment {
	private final String TAG = "MatchTeams";
	private FragmentActivity activity;
	private boolean mDualPane;
	private int matchNum;
	private Prefs prefs;
	private MatchListInterface matchList;
	private ListView list;
	private long time;
	private int red1Val;
	private int red2Val;
	private int red3Val;
	private int blue1Val;
	private int blue2Val;
	private int blue3Val;
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	private int curIndex = 0;
	private boolean breakNicely;
	private SimpleDateFormat timeFormatter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        timeFormatter = new SimpleDateFormat("hh:mma");
        timeFormatter.setTimeZone(TimeZone.getDefault());
        breakNicely = false;
        activity = this.getActivity();
        prefs = new Prefs(activity);
        matchNum = activity.getIntent().getExtras().getInt("match",0);
        list = this.getListView();
        matchList = new MatchListInterface(activity);
        
        new getMatches().execute();

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.matchTeamInfo);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            curIndex = savedInstanceState.getInt("index", 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", curIndex);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	showTeam(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showTeam(int index) {
    	curIndex = index;
    	RowData row = (RowData)list.getItemAtPosition(index);
    	int team = Integer.parseInt(row.string);

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            list.setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            MatchTeamInfo details = (MatchTeamInfo) getFragmentManager().findFragmentById(R.id.matchTeamInfo);
            if (details == null || details.getShownIndex() != team) {
                // Make new fragment to show this selection.
                details = MatchTeamInfo.newInstance(matchNum, team);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.matchTeamInfo, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), FragActivity.class);
            intent.putExtra("fragment", "com.mechinn.android.ouralliance.activity.scouting.MatchTeamInfo");
            intent.putExtra("match", matchNum);
            intent.putExtra("team", team);
            
            startActivity(intent);
        }
    }
    
    private class getMatches extends AsyncTask<Void,Void,Void> {    	
    	protected void onPostExecute(Void no) {
    		if(breakNicely){
				Toast.makeText(activity, "Something went wrong loading data", Toast.LENGTH_SHORT).show();
				activity.finish();
    			return;
    		}
	        activity.setTitle("Match: "+Integer.toString(matchNum)+" | Time: "+timeFormatter.format(new Date(time)));
	        
	        mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        data = new Vector<RowData>();
	        RowData rd = new RowData(Integer.toString(red1Val), activity.getResources().getColor(R.color.red));
	        data.add(rd);
	        rd = new RowData(Integer.toString(red2Val), activity.getResources().getColor(R.color.red));
	        data.add(rd);
	        rd = new RowData(Integer.toString(red3Val), activity.getResources().getColor(R.color.red));
	        data.add(rd);
	        rd = new RowData(Integer.toString(blue1Val), activity.getResources().getColor(R.color.blue));
	        data.add(rd);
	        rd = new RowData(Integer.toString(blue2Val), activity.getResources().getColor(R.color.blue));
	        data.add(rd);
	        rd = new RowData(Integer.toString(blue3Val), activity.getResources().getColor(R.color.blue));
	        data.add(rd);

	        CustomAdapter adapter = new CustomAdapter(activity.getApplicationContext(), R.layout.matchteamrow, data);
	        setListAdapter(adapter);        
	        list.setTextFilterEnabled(true);
	        
	        if (mDualPane) {
	            // In dual-pane mode, the list view highlights the selected item.
	            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	            // Make sure our UI is in the correct state.
	            showTeam(curIndex);
	        }
    	}
    	
		protected Void doInBackground(Void... no) {
			String[] from = new String[] {MatchListProvider.KEY_TIME,
					MatchListProvider.KEY_RED1, MatchListProvider.KEY_RED2, MatchListProvider.KEY_RED3,
					MatchListProvider.KEY_BLUE1, MatchListProvider.KEY_BLUE2, MatchListProvider.KEY_BLUE3};
			
			Cursor match = matchList.fetchMatch(prefs.getCompetition(), matchNum);
			if(match!=null && !match.isClosed() && match.getCount()>0){
				do {
		        	for(int j=0;j<from.length;++j) {
		            	String rowName = from[j];
		            	int col = match.getColumnIndex(rowName);
		            	Log.d("matchTeams",rowName);
		            	Log.d("matchTeams",Integer.toString(col));
		            	if(rowName.equals(MatchListProvider.KEY_TIME)) {
		            		time = match.getLong(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_RED1)) {
		            		red1Val = match.getInt(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_RED2)) {
		            		red2Val = match.getInt(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_RED3)) {
		            		red3Val = match.getInt(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_BLUE1)) {
		            		blue1Val = match.getInt(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_BLUE2)) {
		            		blue2Val = match.getInt(col);
		            	} else if(rowName.equals(MatchListProvider.KEY_BLUE3)) {
		            		blue3Val = match.getInt(col);
		            	}
		            }
		        } while(match.moveToNext());
			} else {
				breakNicely = true;
			}
	        return null;
		}
    }
    
    private class RowData {
        protected String string;
        protected int color;
        public RowData(String item, int col){
        	string = item; 
        	color = col;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    private class CustomAdapter extends ArrayAdapter<RowData> {        
        public CustomAdapter(Context context, int layoutResourceId, List<RowData> objects) {
            super(context, layoutResourceId, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	ViewHolder holder = null;

            //widgets displayed by each item in your list
            TextView item = null;

            //data from your adapter
            RowData rowData= getItem(position);


            //we want to reuse already constructed row views...
            if(null == convertView){
                    convertView = mInflater.inflate(R.layout.matchteamrow, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
            }
            // 
            holder = (ViewHolder) convertView.getTag();
            item = holder.getItem();
            item.setText(rowData.string);
            item.setTextColor(rowData.color);
            item.setCompoundDrawables(null, null, null, null);

            return convertView;
        }
        
        private class ViewHolder {      
            private View mRow;
            private TextView text = null;
            
            public ViewHolder(View row) {
                mRow = row;
            }
            
            public TextView getItem() {
	            if(null == text){
	            	text = (TextView) mRow.findViewById(R.id.matchTeamNum);
	            }
	            return text;
	        } 
        }
    }
}
