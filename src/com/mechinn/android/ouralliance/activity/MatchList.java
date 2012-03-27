package com.mechinn.android.ouralliance.activity;

import java.util.ArrayList;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.providers.MatchListProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MatchList extends Activity {
	private static final int ADDMATCH = 0;
	private final String[] white = new String[] {
		"Match #", "Time",	
	};
	private final String[] red = new String[] {
		"Red 1", "Red 2", "Red 3"
	};
	private final String[] blue = new String[] {
		"Blue 1", "Blue 2", "Blue 3"
	};
	private MatchListInterface matchListData;
	private TableLayout matchTable;
	private TableRow header;
	private TableRow tableRow;
	private TextView headerText;
	private TextView textView;
	private ArrayList<Intent> intents;
	private Prefs prefs;
	private int matchCount;
	private int lastMatchTime;
	private boolean loading;
	
	private Dialog addMatchDialog;
	private EditText addMatchNum;
	private EditText addMatchTime;
	private EditText addMatchRed1;
	private EditText addMatchRed2;
	private EditText addMatchRed3;
	private EditText addMatchBlue1;
	private EditText addMatchBlue2;
	private EditText addMatchBlue3;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.matchlist);
        loading = true;
        prefs = new Prefs(this);
        matchTable = (TableLayout) this.findViewById(R.id.matchList);
        header = (TableRow) this.findViewById(R.id.header);
        matchListData = new MatchListInterface(this);
        for(String col : white) {
			headerText = textSetup();
			headerText.setText(col);
			header.addView(headerText);
		}
        for(String col : red) {
			headerText = textSetup();
			headerText.setText(col);
			headerText.setTextColor(getResources().getColor(R.color.red));
			header.addView(headerText);
		}
		for(String col : blue) {
			headerText = textSetup();
			headerText.setText(col);
			headerText.setTextColor(getResources().getColor(R.color.blue));
			header.addView(headerText);
		}
        header.setBackgroundColor(getResources().getColor(R.color.black));
        this.findViewById(R.id.matchListHeader).bringToFront();
        setup();
	}
	
	private void setup() {
		matchTable.removeAllViews();
		tableRow = new TableRow(MatchList.this);
		for(String col : white) {
			addText(col);
		}
		for(String col : red) {
			addRed(col);
		}
		for(String col : blue) {
			addBlue(col);
		}
		matchTable.addView(tableRow);
		intents = new ArrayList<Intent>();
        new getMatches().execute();
	}
	
	private void addText(String s) {
		textView = textSetup();
		textView.setText(s);
		tableRow.addView(textView);
	}
	
	private void addRed(String s) {
		textView = textSetup();
		textView.setText(s);
		textView.setTextColor(getResources().getColor(R.color.red));
		tableRow.addView(textView);
	}
	
	private void addBlue(String s) {
		textView = textSetup();
		textView.setText(s);
		textView.setTextColor(getResources().getColor(R.color.blue));
		tableRow.addView(textView);
	}
	
	private TextView textSetup() {
		TextView tv = new TextView(MatchList.this);
		tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, 10, 0, 10);
		tv.setTextAppearance(MatchList.this, android.R.style.TextAppearance_Large);
		return tv;
	}
	
	private class getMatches extends AsyncTask<Void,Object,Void> {
		private final static int NEWROW = 1;
		private final static int ADDTEXT = 2;
		private final static int SETMATCH = 3;
		private final static int ADDROW = 4;
		private final static int ADDRED = 5;
		private final static int ADDBLUE = 6;
		
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
    				Intent intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
    				intents.add(intent);
    				break;
    			case ADDTEXT:
    				addText((String)data[1]);
    				break;
    			case SETMATCH:
					intents.get(intents.size()-1).putExtra("match", (Integer)data[1]);
    				break;
    			case ADDROW:
    				tableRow.setOnClickListener(new OnClickListener() {
    					private int index = intents.size()-1;
						public void onClick(View v) {
							startActivity(intents.get(index));
						}
    				});
    				matchTable.addView(tableRow);
    				break;
    			case ADDRED:
    				addRed((String)data[1]);
    				break;
    			case ADDBLUE:
    				addBlue((String)data[1]);
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
	        loading = false;
    	}
    	
		protected Void doInBackground(Void... no) {
			String[] from = new String[] {MatchListProvider.keyMatchNum, MatchListProvider.keyTime,
					MatchListProvider.keyRed1, MatchListProvider.keyRed2, MatchListProvider.keyRed3,
					MatchListProvider.keyBlue1, MatchListProvider.keyBlue2, MatchListProvider.keyBlue3};
			
			Cursor thisMatch = matchListData.fetchMatches(prefs.getCompetition());
			matchCount = 0;
			do {
	        	publishProgress(NEWROW);
	        	for(int j=0;j<from.length;++j) {
	            	String colName = from[j];
	            	int col = thisMatch.getColumnIndex(colName);
	            	if(colName.equals(MatchListProvider.keyMatchNum)) {
	            		publishProgress(SETMATCH,thisMatch.getInt(col));
	            		publishProgress(ADDTEXT,Integer.toString(thisMatch.getInt(col)));
	            	} else if(colName.equals(MatchListProvider.keyTime)) {
	            		lastMatchTime = thisMatch.getInt(col);
	            		publishProgress(ADDTEXT,Integer.toString(lastMatchTime));
	            	} else if(colName.equals(MatchListProvider.keyRed1) || colName.equals(MatchListProvider.keyRed2) || 
	            			colName.equals(MatchListProvider.keyRed3)){
	            		publishProgress(ADDRED,Integer.toString(thisMatch.getInt(col)));
	            	} else if (colName.equals(MatchListProvider.keyBlue1) || colName.equals(MatchListProvider.keyBlue2) || 
	            			colName.equals(MatchListProvider.keyBlue3)) {
	            		publishProgress(ADDBLUE,Integer.toString(thisMatch.getInt(col)));
	            	}
	            }
	        	++matchCount;
	        	publishProgress(ADDROW);
	        } while(thisMatch.moveToNext());
			
	        return null;
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.matchlistmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
		    case R.id.addMatch:
		    	if(!loading) {
		    		showDialog(ADDMATCH);
		    	} else {
		    		Toast.makeText(MatchList.this, "Please wait till table finished loading and try again.", Toast.LENGTH_SHORT).show();
		    	}
	            return true;
	        case R.id.settings:
		    	intent = new Intent("com.mechinn.android.ouralliance.OpenSettings");
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }	
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch(id) {
		    case ADDMATCH:
		    	addMatchDialog = new Dialog(MatchList.this);
		        addMatchDialog.setContentView(R.layout.addmatch);
		        addMatchDialog.setTitle("Add a Match");
		        addMatchNum = (EditText) addMatchDialog.findViewById(R.id.addMatchNum);
		        addMatchNum.setText(Integer.toString(matchCount+1));
		        addMatchTime = (EditText) addMatchDialog.findViewById(R.id.addMatchTime);
		        addMatchTime.setText(Integer.toString(lastMatchTime+(8*60)));
		        addMatchRed1 = (EditText) addMatchDialog.findViewById(R.id.addMatchRed1);
		        addMatchRed2 = (EditText) addMatchDialog.findViewById(R.id.addMatchRed2);
		        addMatchRed3 = (EditText) addMatchDialog.findViewById(R.id.addMatchRed3);
		        addMatchBlue1 = (EditText) addMatchDialog.findViewById(R.id.addMatchBlue1);
		        addMatchBlue2 = (EditText) addMatchDialog.findViewById(R.id.addMatchBlue2);
		        addMatchBlue3 = (EditText) addMatchDialog.findViewById(R.id.addMatchBlue3);
		    	Button add = (Button) addMatchDialog.findViewById(R.id.add);
		    	add.setOnClickListener(new OnClickListener() {
		    		public void onClick(View arg0) {
		    			String matchNum = addMatchNum.getText().toString();
		    			String matchTime = addMatchTime.getText().toString();
		    			String matchRed1 = addMatchRed1.getText().toString();
		    			String matchRed2 = addMatchRed2.getText().toString();
		    			String matchRed3 = addMatchRed3.getText().toString();
		    			String matchBlue1 = addMatchBlue1.getText().toString();
		    			String matchBlue2 = addMatchBlue2.getText().toString();
		    			String matchBlue3 = addMatchBlue3.getText().toString();
		    			if(matchNum.equals("") || matchTime.equals("") ||
		    					matchRed1.equals("") || matchRed2.equals("") || matchRed3.equals("") ||
		    					matchBlue1.equals("") || matchBlue2.equals("") || matchBlue3.equals("")) {
		    				Toast.makeText(MatchList.this, "Must have a value in each field", Toast.LENGTH_SHORT).show();
		    			} else {
		    				String comp = prefs.getCompetition();
		    				int matchVal = Integer.parseInt(matchNum);
		    				Cursor match = matchListData.fetchMatch(comp, matchVal);
		    				if(matchVal<=0) {
		    					Toast.makeText(MatchList.this, "Must have a match number >= 1", Toast.LENGTH_SHORT).show();
		    				} else if(match.getCount()==0){
		    					matchListData.createMatch(comp, matchVal, Integer.parseInt(matchTime),
			    						 Integer.parseInt(matchRed1), Integer.parseInt(matchRed2), Integer.parseInt(matchRed3),
										 Integer.parseInt(matchBlue1), Integer.parseInt(matchBlue2), Integer.parseInt(matchBlue3));
		    					setup();
		    					addMatchDialog.dismiss();
		    				} else {
		    					Toast.makeText(MatchList.this, "Match number already exists", Toast.LENGTH_SHORT).show();
		    				}
		    			}
					}
		    	});
		    	Button cancel = (Button) addMatchDialog.findViewById(R.id.cancel);
		    	cancel.setOnClickListener(new OnClickListener() {
		    		public void onClick(View arg0) {
		    			addMatchDialog.cancel();
					}
		    	});
		        break;
		    default:
		        return super.onCreateDialog(id);
	    }
	    return addMatchDialog;
	}
}