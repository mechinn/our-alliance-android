package com.mechinn.android.ouralliance.activity;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Teams extends Activity {
	private final String logTag = "Teams";
	private final int ADDTEAM = 0;
	private TeamScoutingInterface teamInfo;
	private TableLayout staticTable;
	private TableLayout staticTeams;
	private TableLayout staticCols;
	private TableLayout teamsTable;
	private TextView staticTableText;
	private TextView staticTeamText;
	private TextView staticColText;
	private TextView text;
	private TableRow staticTableRow;
	private TableRow staticTeamRow;
	private TableRow staticColRow;
	private TableRow row;

    private float curX, curY;
    private float mx, my;
    private boolean startTrack;

    private ScrollView vScroll;
    private HorizontalScrollView hScrollHeader;
    private HorizontalScrollView hScroll;
    
    private Dialog addTeamDialog;
    private EditText addTeamNum;
    private Prefs prefs;
    
    private String orderBy;
    private boolean desc;
    private int selectedTeam;
    
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                Log.d("down", Float.toString(mx)+", "+Float.toString(my));
        }
    	return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x, y;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            	//annoying workaround for action up not working cause of focus?
            	if(startTrack) {
            		mx = event.getX();
                    my = event.getY();
                    startTrack = false;
            	} else {
            		curX = event.getX();
            		curY = event.getY();
            		x = (int) (mx - curX);
                    y = (int) (my - curY);
                    Log.d("scroll", Integer.toString(x)+", "+Integer.toString(y));
                    vScroll.scrollBy(x,y);
                    hScrollHeader.scrollBy(x,y);
                    hScroll.scrollBy(x,y);
                    mx = curX;
                    my = curY;
            	}
                break;
            case MotionEvent.ACTION_UP:
                startTrack = true;
                break;
        }

        return true;
    }
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        selectedTeam = 0;
        startTrack = true;
        prefs = new Prefs(this);
        orderBy = TeamScoutingProvider.keyTeam;
        desc = false;
        vScroll = (ScrollView) findViewById(R.id.vScroll);
        hScrollHeader = (HorizontalScrollView) findViewById(R.id.hScrollHeader);
        hScroll = (HorizontalScrollView) findViewById(R.id.hScroll);
        
        teamInfo = new TeamScoutingInterface(this);
        staticTable = (TableLayout) this.findViewById(R.id.staticTable);
        staticTeams = (TableLayout) this.findViewById(R.id.staticTeams);
        staticCols = (TableLayout) this.findViewById(R.id.staticCols);
        teamsTable = (TableLayout) this.findViewById(R.id.table);
        teamsTable.setBackgroundColor(Color.BLACK);
        
//        hScroll.bringToFront();
//        staticTeams.bringToFront();
//        findViewById(R.id.header).bringToFront();
//        staticTable.bringToFront();
	}
	
	public void onStart() {
		staticTable.removeAllViews();
		staticTeams.removeAllViews();
		staticCols.removeAllViews();
		teamsTable.removeAllViews();
		new getTeams().execute();
		super.onStart();
	}
    
    private TextView setupText() {
		TextView text = new TextView(this);
		text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
		text.setGravity(Gravity.CENTER);
		text.setTextSize(20);
    	text.setPadding(0, 10, 0, 10);
		return text;
    }
    
    private class getTeams extends AsyncTask<Void,Object,Void> {
    	private final int NEWSIMPLEROW = 0;
    	private final int STATICCOL = 1;
    	private final int ADDROW = 2;
    	private final int NEWROW = 3;
    	private final int NEWTEXT = 4;
    	private final int ROWONCLICK = 5;
    	private final int SETTEXT = 6;
    	private final int ADDTEXT = 7;
    	private final int ADDSTATICCOL = 8;
    	private final int ADDTEAM = 9;
    	private final int ADDSTATICTEAM = 10;
    	private final int ADDSTATICTABLE = 11;
    	
    	private final String[] colNames = new String[] { "Team", "Orientation", "Number of Wheels", "Wheel Type 1", "Wheel Diameter 1", "Wheel Type 2", "Wheel Diameter 2", "Dead Wheel Type", "Turret Shooter", "Auto Tracking", "Fender Shooter", "Key Shooter", "Crosses Barrier", "Climb Bridge", "Autonomous", "Average Hoop Points", "Average Matches Balanced", "Average Matches Broke" };
        
    	private final String[] from = new String[] {TeamScoutingProvider.keyTeam, TeamScoutingProvider.keyOrientation, TeamScoutingProvider.keyNumWheels, 
        		TeamScoutingProvider.keyWheel1Type, TeamScoutingProvider.keyWheel1Diameter, 
    			TeamScoutingProvider.keyWheel2Type, TeamScoutingProvider.keyWheel2Diameter, TeamScoutingProvider.keyDeadWheelType, 
    			TeamScoutingProvider.keyTurret, TeamScoutingProvider.keyTracking, TeamScoutingProvider.keyFenderShooter, TeamScoutingProvider.keyKeyShooter, 
    			TeamScoutingProvider.keyBarrier, TeamScoutingProvider.keyClimb, TeamScoutingProvider.keyAutonomous, TeamScoutingProvider.keyAvgHoops, TeamScoutingProvider.keyAvgBalance, TeamScoutingProvider.keyAvgBroke};
    	
    	private TextView addText(String s) {
    		TextView v = setupText();
        	v.setTextSize(26);
        	v.setText(s);
        	v.setPadding(10, 10, 10, 10);
        	return v;
    	}
    	
    	protected void onProgressUpdate(Object... data) {
    		switch((Integer)data[0]) {
    			case ADDTEAM:
    				staticTeamText = addText((String)data[1]);
    				staticTeamText.setHeight(0);
    	        	staticTeamRow.addView(staticTeamText);
    				staticTableText = addText((String)data[1]);
    				staticTableText.setFocusable(true);
    				staticTableText.setFocusableInTouchMode(true);
    				staticTableText.setBackgroundColor(Color.BLACK);
    				staticTableText.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
            				for(int i = 0; i< colNames.length;++i) {
            					if(((TextView)v).getText().toString().equals(colNames[i])) {
            						if(orderBy.equals(from[i])) {
            							desc = !desc;
            						} else {
            							orderBy = from[i];
            							desc = false;
            						}
            						break;
            					}
            				}
                        	onStart();
        				}
            		});
    				staticTableText.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							v.setBackgroundColor(Color.GRAY);
    							staticTeamRow.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.BLACK);
    							staticTeamRow.setBackgroundColor(Color.BLACK);
    						}
    					}
    		    	});
    				if(orderBy.equals((String)data[1])) {
    					staticTableRow.requestFocus();
    				}
    				staticTableRow.addView(staticTableText);
    	        	break;
    			case STATICCOL:
    				staticColText = addText((String)data[1]);
    				staticColText.setFocusable(true);
    				staticColText.setFocusableInTouchMode(true);
    				staticColText.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
            				for(int i = 0; i< colNames.length;++i) {
            					if(((TextView)v).getText().toString().equals(colNames[i])) {
            						if(orderBy.equals(from[i])) {
            							desc = !desc;
            						} else {
            							orderBy = from[i];
            							desc = false;
            						}
            						break;
            					}
            				}
                        	onStart();
        				}
            		});
    				staticColText.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							v.setBackgroundColor(Color.GRAY);
    							staticTeamRow.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.BLACK);
    							staticTeamRow.setBackgroundColor(Color.BLACK);
    						}
    					}
    		    	});
    	        	staticColRow.addView(staticColText);
    				text = addText((String)data[1]);
    				text.setHeight(0);
    	        	row.addView(text);
    	        	break;
    			case ADDTEXT:
    	        	row.addView(text);
    	        	break;
    			case ADDSTATICTABLE:
    				staticTable.addView(staticTableRow);
    				staticTeams.addView(staticTeamRow);
    				staticColRow.setBackgroundColor(Color.BLACK);
    				staticCols.addView(staticColRow);
    				teamsTable.addView(row);
    				break;
    			case ADDSTATICCOL:
    				staticCols.addView(staticColRow);
    				teamsTable.addView(row);
    				break;
    			case ADDSTATICTEAM:
    				staticTeams.addView(staticTeamRow);
    				teamsTable.addView(row);
    				break;
    			case ADDROW:
    				teamsTable.addView(row);
    				break;
    			case NEWSIMPLEROW:
    				staticTableRow = new TableRow(Teams.this);
    				staticColRow = new TableRow(Teams.this);
    			case NEWROW:
    				row = new TableRow(Teams.this);
    				row.setFocusable(true);
    				row.setFocusableInTouchMode(true);
    				row.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							selectedTeam = (Integer) v.getTag();
    							v.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.BLACK);
    						}
    					}
    		    	});
    				staticTeamRow = new TableRow(Teams.this);
    				staticTeamRow.setFocusable(true);
    				staticTeamRow.setFocusableInTouchMode(true);
    				staticTeamRow.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							selectedTeam = (Integer) v.getTag();
    							v.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.BLACK);
    						}
    					}
    		    	});
    				break;
    			case NEWTEXT:
    				text = setupText();
    				break;
    			case ROWONCLICK:
    				Log.d(logTag, "Adding team "+(Integer)data[1]);
    				staticTeamRow.setTag((Integer)data[1]);
    				row.setTag((Integer)data[1]);
    				final String teamString = Integer.toString((Integer)data[1]);
        			OnClickListener clickRow = new OnClickListener() {
            			public void onClick(View v) {
                			String actionName = "com.mechinn.android.ouralliance.OpenTeamInfo";
            				Intent intent = new Intent(actionName);
        					intent.putExtra("team", (Integer) v.getTag());
        		    		startActivity(intent);
        				}
            		};
        			staticTeamRow.setOnClickListener(clickRow);
            		row.setOnClickListener(clickRow);
            		staticTeamText = setupText();
            		staticTeamText.setText(teamString);
            		staticTeamText.setBackgroundColor(Color.BLACK);
    	        	staticTeamRow.addView(staticTeamText);
            		break;
    			case SETTEXT:
    				text.setText((String)data[1]);
    				break;
    		}
    	}
    	
    	protected void onPostExecute(Void no) {
    		Log.d("table rows",Integer.toString(teamsTable.getChildCount()-1));
	        if(teamsTable.getChildCount()<2) {
	        	TableRow tr = new TableRow(Teams.this);
	        	TextView tv = setupText();
	        	tv.setText("No Teams");
	        	tr.addView(tv);
	        	teamsTable.addView(tr);
	        }
    	}
    	
		protected Void doInBackground(Void... no) {
			Cursor thisTeam = null;
			if(prefs.getAllTeams()) {
				thisTeam = teamInfo.fetchAllTeams(from,orderBy, desc);
			} else {
				thisTeam = teamInfo.fetchCompetitionTeams(prefs.getCompetition(), from, orderBy, desc);
			}
			if(thisTeam!=null && thisTeam.getCount()>0) {
				publishProgress(NEWSIMPLEROW);
		        for(int i=0;i<colNames.length;++i) {
		        	if(i==0) {
			        	publishProgress(ADDTEAM,colNames[i]);
		        	} else {
			        	publishProgress(STATICCOL,colNames[i]);
		        	}
		        }
		        publishProgress(ADDSTATICTABLE);
		        
		        do {
		        	publishProgress(NEWROW);
		        	for(int j=0;j<from.length;++j) {
		        		
		            	String colName = from[j];
		            	int col = thisTeam.getColumnIndex(colName);
//		            	Log.d("table fill", Integer.toString(col));
//		            	Log.d("table colname", colName);
		            	if(colName.equals(TeamScoutingProvider.keyTeam)) {
		            		publishProgress(ROWONCLICK,thisTeam.getInt(col));
		            	} else {
		            		publishProgress(NEWTEXT);
		            		if(colName.equals(TeamScoutingProvider.keyOrientation) || colName.equals(TeamScoutingProvider.keyWheel1Type) || 
		            				colName.equals(TeamScoutingProvider.keyWheel2Type) || colName.equals(TeamScoutingProvider.keyDeadWheelType) || 
			            			colName.equals(TeamScoutingProvider.keyNotes)){
			            		publishProgress(SETTEXT,thisTeam.getString(col));
			            	} else if (colName.equals(TeamScoutingProvider.keyNumWheels) || colName.equals(TeamScoutingProvider.keyWheel1Diameter) || 
			            			colName.equals(TeamScoutingProvider.keyWheel2Diameter)) {
			            		publishProgress(SETTEXT,Integer.toString(thisTeam.getInt(col)));
			            	} else if (colName.equals(TeamScoutingProvider.keyTurret) || 
			            			colName.equals(TeamScoutingProvider.keyTracking) || colName.equals(TeamScoutingProvider.keyFenderShooter) || 
			            			colName.equals(TeamScoutingProvider.keyKeyShooter) || colName.equals(TeamScoutingProvider.keyBarrier) || 
			            			colName.equals(TeamScoutingProvider.keyClimb) || colName.equals(TeamScoutingProvider.keyAutonomous)) {
			            		switch(thisTeam.getInt(col)) {
			            			case 0:publishProgress(SETTEXT,"no");break;
			            			default:publishProgress(SETTEXT,"yes");
			            		}
			            	} else if (colName.equals(TeamScoutingProvider.keyAvgHoops) || colName.equals(TeamScoutingProvider.keyAvgBalance) || 
			            			colName.equals(TeamScoutingProvider.keyAvgBroke)) {
			            		publishProgress(SETTEXT,Double.toString(thisTeam.getDouble(col)));
			            	} 
			            	publishProgress(ADDTEXT);
		            	}
		            			
		            }
		        	publishProgress(ADDSTATICTEAM);
		        } while(thisTeam.moveToNext());
			}
	        
	        return null;
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.teamsmenu, menu);
	    menu.findItem(R.id.allTeams).setChecked(prefs.getAllTeams());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	String actionName = "com.mechinn.android.ouralliance.OpenSettings";
		    	Intent intent = new Intent(actionName);
		    	startActivity(intent);
	            return true;
	        case R.id.refresh:
	        	onStart();
	            return true;
	        case R.id.addTeam:
	        	showDialog(ADDTEAM);
	            return true;
	        case R.id.deleteTeam:
	        	if(teamInfo.deleteTeam(selectedTeam)==1) {
	        		Toast.makeText(Teams.this, "Team "+selectedTeam+" deleted successfully.", Toast.LENGTH_SHORT).show();
	        	} else {
	        		Toast.makeText(Teams.this, "Team "+selectedTeam+" deletion failed.", Toast.LENGTH_SHORT).show();
	        	}
	    		onStart();
	            return true;
            case R.id.allTeams:
            	prefs.setAllTeams();
            	item.setChecked(prefs.getAllTeams());
            	onStart();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch(id) {
		    case ADDTEAM:
		    	addTeamDialog = new Dialog(Teams.this);
		        addTeamDialog.setContentView(R.layout.addteam);
		        addTeamDialog.setTitle("Add a Team Number");
		        addTeamNum = (EditText) addTeamDialog.findViewById(R.id.addTeamNum);
		    	Button add = (Button) addTeamDialog.findViewById(R.id.add);
		    	add.setOnClickListener(new OnClickListener() {
		    		public void onClick(View arg0) {
		    			int teamNum = Integer.parseInt(addTeamNum.getText().toString());
	    				String comp = prefs.getCompetition();
	    				Cursor match = teamInfo.fetchTeam(comp, teamNum);
	    				if(teamNum<=0) {
	    					Toast.makeText(Teams.this, "Must have a team number >= 1", Toast.LENGTH_SHORT).show();
	    				} else if(match.getCount()==0){
	    					teamInfo.createTeam(comp, teamNum);
	    					onStart();
	    					addTeamDialog.dismiss();
	    				} else {
	    					Toast.makeText(Teams.this, "Match number already exists", Toast.LENGTH_SHORT).show();
	    				}
					}
		    	});
		    	Button cancel = (Button) addTeamDialog.findViewById(R.id.cancel);
		    	cancel.setOnClickListener(new OnClickListener() {
		    		public void onClick(View arg0) {
		    			addTeamDialog.cancel();
					}
		    	});
		        break;
		    default:
		        return super.onCreateDialog(id);
	    }
	    return addTeamDialog;
	}
}
