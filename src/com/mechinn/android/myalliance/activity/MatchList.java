package com.mechinn.android.myalliance.activity;

import java.util.ArrayList;

import com.mechinn.android.myalliance.R;
import com.mechinn.android.myalliance.data.MatchListInterface;
import com.mechinn.android.myalliance.providers.MatchListProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MatchList extends Activity {
	private MatchListInterface matchListData;
	private TableLayout matchTable;
	private TableRow tableRow;
	private TextView textView;
	private ArrayList<Intent> intents;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.matchlist);
        matchTable = (TableLayout) this.findViewById(R.id.matchList);
        matchListData = new MatchListInterface(this);
        intents = new ArrayList<Intent>();
        new getMatches().execute();
	}
	
	private class getMatches extends AsyncTask<Void,Object,Void> {
		private final static int NEWROW = 1;
		private final static int ADDTEXT = 2;
		private final static int SETMATCH = 3;
		private final static int ADDROW = 4;
		private final static int ADDRED = 5;
		private final static int ADDBLUE = 6;
		
		private TextView textSetup() {
			TextView tv = new TextView(MatchList.this);
			tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(0, 10, 0, 10);
			tv.setTextAppearance(MatchList.this, android.R.style.TextAppearance_Large);
			return tv;
		}
		
    	protected void onProgressUpdate(Object... data) {
    		switch((Integer)data[0]) {
    			case NEWROW:
    				tableRow = new TableRow(MatchList.this);
    				tableRow.setFocusable(true);
    				tableRow.setFocusableInTouchMode(true);
    				tableRow.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							v.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.TRANSPARENT);
    						}
    					}
    		    	});
    				Intent intent = new Intent("com.mechinn.android.myalliance.OpenScouting");
    				intent.putExtra("competition", "CT");
    				intents.add(intent);
    				break;
    			case ADDTEXT:
    				textView =textSetup();
    				textView.setText((String)data[1]);
    				tableRow.addView(textView);
    				break;
    			case SETMATCH:
					intents.get(intents.size()-1).putExtra("match", (Integer)data[1]);
    				break;
    			case ADDROW:
    				tableRow.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							startActivity(intents.get(intents.size()-1));
						}
    				});
    				matchTable.addView(tableRow);
    				break;
    			case ADDRED:
    				textView = textSetup();
    				textView.setTextColor(getResources().getColor(R.color.red));
    				textView.setText((String)data[1]);
    				tableRow.addView(textView);
    				break;
    			case ADDBLUE:
    				textView = textSetup();
    				textView.setTextColor(getResources().getColor(R.color.blue));
    				textView.setText((String)data[1]);
    				tableRow.addView(textView);
    				break;
    		}
    	}
    	
    	protected void onPostExecute(Void no) {
    		Log.d("table rows",Integer.toString(matchTable.getChildCount()-1));
	        if(matchTable.getChildCount()<2) {
	        	TableRow tr = new TableRow(MatchList.this);
	        	TextView tv = new TextView(MatchList.this);
	        	tv.setText("No Teams");
	        	tr.addView(tv);
	        	matchTable.removeAllViews();
	        	matchTable.addView(tr);
	        }
    	}
    	
		protected Void doInBackground(Void... no) {
			String[] from = new String[] {MatchListProvider.keyMatchNum, MatchListProvider.keyTime,
					MatchListProvider.keyRed1, MatchListProvider.keyRed2, MatchListProvider.keyRed3,
					MatchListProvider.keyBlue1, MatchListProvider.keyBlue2, MatchListProvider.keyBlue3};
			
			Cursor thisMatch = matchListData.fetchAllMatches();
	        startManagingCursor(thisMatch);
			
			while(thisMatch.moveToNext()) {
	        	publishProgress(NEWROW);
	        	for(int j=0;j<from.length;++j) {
	            	String colName = from[j];
	            	int col = thisMatch.getColumnIndex(colName);
	            	if(colName.equals(MatchListProvider.keyMatchNum)) {
	            		publishProgress(SETMATCH,thisMatch.getInt(col));
	            		publishProgress(ADDTEXT,Integer.toString(thisMatch.getInt(col)));
	            	} else if(colName.equals(MatchListProvider.keyTime)) {
	            		publishProgress(ADDTEXT,Integer.toString(thisMatch.getInt(col)));
	            	} else if(colName.equals(MatchListProvider.keyRed1) || colName.equals(MatchListProvider.keyRed2) || 
	            			colName.equals(MatchListProvider.keyRed3)){
	            		publishProgress(ADDRED,Integer.toString(thisMatch.getInt(col)));
	            	} else if (colName.equals(MatchListProvider.keyBlue1) || colName.equals(MatchListProvider.keyBlue2) || 
	            			colName.equals(MatchListProvider.keyBlue3)) {
	            		publishProgress(ADDBLUE,Integer.toString(thisMatch.getInt(col)));
	            	}
	            }
	        	publishProgress(ADDROW);
	        }
			
	        return null;
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	String actionName = "com.mechinn.android.myalliance.OpenSettings";
		    	Intent intent = new Intent(actionName);
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
			
	}
}
