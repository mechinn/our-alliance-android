package com.mechinn.android.ouralliance.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.MatchListProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MatchList extends Activity {
	private final String TAG = "MatchList";
	private static final int ADDMATCH = 0;
	private final String[] WHITE = new String[] {
		"Match #", "Time",	
	};
	private final String[] RED = new String[] {
		"Red 1", "Red 2", "Red 3"
	};
	private final String[] BLUE = new String[] {
		"Blue 1", "Blue 2", "Blue 3"
	};
	private MatchListInterface matchListData;
	private TeamScoutingInterface teamScoutingData;
	private TableLayout matchTable;
	private TableRow header;
	private TableRow tableRow;
	private TextView headerText;
	private TextView textView;
	private Prefs prefs;
	private int matchCount;
	private long lastMatchTime;
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
	private SimpleDateFormat timeFormatter;
	
	private int match;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.matchlist);
        timeFormatter = new SimpleDateFormat("hh:mma");
        timeFormatter.setTimeZone(TimeZone.getDefault());
        loading = true;
        prefs = new Prefs(this);
        matchTable = (TableLayout) this.findViewById(R.id.matchList);
        header = (TableRow) this.findViewById(R.id.header);
        matchListData = new MatchListInterface(this);
        teamScoutingData = new TeamScoutingInterface(this);
        for(String col : WHITE) {
			headerText = textSetup();
			headerText.setText(col);
			header.addView(headerText);
		}
        for(String col : RED) {
			headerText = textSetup();
			headerText.setText(col);
			headerText.setTextColor(getResources().getColor(R.color.red));
			header.addView(headerText);
		}
		for(String col : BLUE) {
			headerText = textSetup();
			headerText.setText(col);
			headerText.setTextColor(getResources().getColor(R.color.blue));
			header.addView(headerText);
		}
        header.setBackgroundColor(getResources().getColor(R.color.black));
        setup();
	}
	
	private void setup() {
		matchTable.removeAllViews();
		tableRow = new TableRow(MatchList.this);
		for(String col : WHITE) {
			textView = textSetup();
			textView.setText(col);
			textView.setHeight(0);
			tableRow.addView(textView);
		}
		for(String col : RED) {
			textView = textSetup();
			textView.setText(col);
			textView.setTextColor(getResources().getColor(R.color.red));
			textView.setHeight(0);
			tableRow.addView(textView);
		}
		for(String col : BLUE) {
			textView = textSetup();
			textView.setText(col);
			textView.setTextColor(getResources().getColor(R.color.blue));
			textView.setHeight(0);
			tableRow.addView(textView);
		}
		matchTable.addView(tableRow);
        new getMatches().execute();
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
    							match = (Integer) v.getTag();
    						} else {
    							v.setBackgroundColor(Color.TRANSPARENT);
    						}
    					}
    		    	});
    				break;
    			case ADDTEXT:
    				textView = textSetup();
    				textView.setText((String)data[1]);
    				tableRow.addView(textView);
    				break;
    			case SETMATCH:
    				tableRow.setTag((Integer)data[1]);
    				break;
    			case ADDROW:
    				tableRow.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
							intent.putExtra("match", (Integer)v.getTag());
							startActivity(intent);
						}
    				});
    				matchTable.addView(tableRow);
    				break;
    			case ADDRED:
    				textView = textSetup();
    				textView.setText((String)data[1]);
    				textView.setTextColor(getResources().getColor(R.color.red));
    				tableRow.addView(textView);
    				break;
    			case ADDBLUE:
    				textView = textSetup();
    				textView.setText((String)data[1]);
    				textView.setTextColor(getResources().getColor(R.color.blue));
    				tableRow.addView(textView);
    				break;
    		}
    	}
    	
    	protected void onPostExecute(Void no) {
    		Log.d("table rows",Integer.toString(matchTable.getChildCount()-1));
	        if(matchTable.getChildCount()<2) {
	        	TableRow tr = new TableRow(MatchList.this);
	        	TextView tv = textSetup();
	        	tv.setText("No Matches");
	        	tr.addView(tv);
	        	matchTable.removeAllViews();
	        	matchTable.addView(tr);
	        }
	        loading = false;
    	}
    	
		protected Void doInBackground(Void... no) {
			String[] from = new String[] {MatchListProvider.KEY_MATCH_NUM, MatchListProvider.KEY_TIME,
					MatchListProvider.KEY_RED1, MatchListProvider.KEY_RED2, MatchListProvider.KEY_RED3,
					MatchListProvider.KEY_BLUE1, MatchListProvider.KEY_BLUE2, MatchListProvider.KEY_BLUE3};
			
			Cursor thisMatch = matchListData.fetchMatches(prefs.getCompetition());
			if(thisMatch!=null && !thisMatch.isClosed() && thisMatch.getCount()>0){
				matchCount = 0;
				do {
		        	publishProgress(NEWROW);
		        	for(int j=0;j<from.length;++j) {
		            	String colName = from[j];
		            	int col = thisMatch.getColumnIndex(colName);
		            	if(colName.equals(MatchListProvider.KEY_MATCH_NUM)) {
		            		publishProgress(SETMATCH,thisMatch.getInt(col));
		            		publishProgress(ADDTEXT,Integer.toString(thisMatch.getInt(col)));
		            	} else if(colName.equals(MatchListProvider.KEY_TIME)) {
							lastMatchTime = thisMatch.getLong(col);
		            		publishProgress(ADDTEXT,timeFormatter.format(new Date(lastMatchTime)));
		            	} else if(colName.equals(MatchListProvider.KEY_RED1) || colName.equals(MatchListProvider.KEY_RED2) || 
		            			colName.equals(MatchListProvider.KEY_RED3)){
		            		publishProgress(ADDRED,Integer.toString(thisMatch.getInt(col)));
		            	} else if (colName.equals(MatchListProvider.KEY_BLUE1) || colName.equals(MatchListProvider.KEY_BLUE2) || 
		            			colName.equals(MatchListProvider.KEY_BLUE3)) {
		            		publishProgress(ADDBLUE,Integer.toString(thisMatch.getInt(col)));
		            	}
		            }
		        	++matchCount;
		        	publishProgress(ADDROW);
		        } while(thisMatch.moveToNext());
			}
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
		    case R.id.deleteMatch:
		    	if(!loading) {
		    		if(matchListData.deleteMatch(prefs.getCompetition(), match)==7) {
		        		Toast.makeText(MatchList.this, "Match "+match+" deleted successfully.", Toast.LENGTH_SHORT).show();
		        	} else {

		        		Toast.makeText(MatchList.this, "Match "+match+" deleteion failed.", Toast.LENGTH_SHORT).show();
		        	}
		    		setup();
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
		        addMatchTime.setText(timeFormatter.format(new Date(lastMatchTime+(8*60000))));
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
		    				int red1 = Integer.parseInt(matchRed1);
		    				int red2 = Integer.parseInt(matchRed2);
		    				int red3 = Integer.parseInt(matchRed3);
		    				int blue1 = Integer.parseInt(matchBlue1);
		    				int blue2 = Integer.parseInt(matchBlue2);
		    				int blue3 = Integer.parseInt(matchBlue3);
		    				String comp = prefs.getCompetition();
		    				int matchVal = Integer.parseInt(matchNum);
		    				Cursor match = matchListData.fetchMatch(comp, matchVal);
		    				if(matchVal<=0) {
		    					Toast.makeText(MatchList.this, "Must have a match number >= 1", Toast.LENGTH_SHORT).show();
		    				} else if(match !=null && !match.isClosed() && match.getCount()==0){
		    					if(teamScoutingData.fetchTeam(red1).getCount()==1) {
		    						if(teamScoutingData.fetchTeam(red2).getCount()==1) {
		    							if(teamScoutingData.fetchTeam(red3).getCount()==1){
		    								if(teamScoutingData.fetchTeam(blue1).getCount()==1) {
		    									if(teamScoutingData.fetchTeam(blue2).getCount()==1) {
		    										if(teamScoutingData.fetchTeam(blue3).getCount()==1) {
		    											try {
		    												long newTime = timeFormatter.parse(matchTime).getTime();
															matchListData.addMatch(comp, matchVal, newTime,
																	 red1, red2, red3, blue1, blue2, blue3);
															lastMatchTime = newTime; //save the new time
															++matchCount;
															setup();
			    					    					addMatchDialog.dismiss();
			    					    			        addMatchNum.setText(Integer.toString(matchCount+1));
			    					    			        addMatchTime.setText(timeFormatter.format(new Date(lastMatchTime+(8*60000))));
			    					    			        addMatchRed1.setText("");
			    					    			        addMatchRed2.setText("");
			    					    			        addMatchRed3.setText("");
			    					    			        addMatchBlue1.setText("");
			    					    			        addMatchBlue2.setText("");
			    					    			        addMatchBlue3.setText("");
														} catch (ParseException e) {
															Toast.makeText(MatchList.this, "Illegal time given. (ex: 12:59pm)", Toast.LENGTH_SHORT).show();
														}
		    										} else {
		    											Toast.makeText(MatchList.this, "Blue 3 is not a valid team.", Toast.LENGTH_SHORT).show();
		    										}
		    									} else {
	    											Toast.makeText(MatchList.this, "Blue 2 is not a valid team.", Toast.LENGTH_SHORT).show();
		    									}
		    								} else {
    											Toast.makeText(MatchList.this, "Blue 1 is not a valid team.", Toast.LENGTH_SHORT).show();
		    								}
		    							} else {
											Toast.makeText(MatchList.this, "Red 3 is not a valid team.", Toast.LENGTH_SHORT).show();
		    							}
		    						} else {
										Toast.makeText(MatchList.this, "Red 2 is not a valid team.", Toast.LENGTH_SHORT).show();
		    						}
		    					} else {
									Toast.makeText(MatchList.this, "Red 1 is not a valid team.", Toast.LENGTH_SHORT).show();
		    					}
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
