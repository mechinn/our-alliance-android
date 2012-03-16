package com.mechinn.android.myalliance;

import java.sql.SQLException;

import com.bugsense.trace.BugSenseHandler;
import com.mechinn.android.myalliance.R;

import android.app.Activity;
import android.content.Context;
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
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Teams extends Activity {
	private TeamInfoDB teamInfoDb;
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
	private Prefs prefs;

    private float curX, curY;
    private float mx, my;
    private boolean startTrack;

    private ScrollView vScrollTeams;
    private ScrollView vScrollTable;
    private HorizontalScrollView hScroll;
    
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
                    vScrollTeams.scrollBy(x,y);
                    vScrollTable.scrollBy(x,y);
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
        startTrack = true;

        vScrollTeams = (ScrollView) findViewById(R.id.vScrollTeams);
        vScrollTable = (ScrollView) findViewById(R.id.vScrollTable);
        hScroll = (HorizontalScrollView) findViewById(R.id.hScroll);
        
        hScroll.bringToFront();
        vScrollTeams.bringToFront();

        //delete before putting myalliance.java back as main
//        BugSenseHandler.setup(this, "a92dda4a");
        prefs = new Prefs(this);
        
        //first run setup the DB
        Log.d("first run",Boolean.toString(prefs.getFirstRun()));
        if(prefs.getFirstRun()) {
        	new ResetDB(Teams.this,true).show();
        	prefs.setRunned();
        }
        
        teamInfoDb = new TeamInfoDB(this);
        staticTable = (TableLayout) this.findViewById(R.id.staticTable);
        staticTeams = (TableLayout) this.findViewById(R.id.staticTeams);
        staticCols = (TableLayout) this.findViewById(R.id.staticCols);
        teamsTable = (TableLayout) this.findViewById(R.id.teamsTable);
        teamsTable.setBackgroundColor(Color.BLACK);
        
        staticCols.bringToFront();
    	staticTable.bringToFront();
	}
	
	public void onStart() {
		staticTable.removeAllViews();
		staticTeams.removeAllViews();
		staticCols.removeAllViews();
		teamsTable.removeAllViews();
		new getTeams().execute();
		super.onStart();
	}
	
	public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
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
    	
    	protected void onProgressUpdate(Object... data) {
    		switch((Integer)data[0]) {
    			case NEWSIMPLEROW:
    				staticTableRow = new TableRow(Teams.this);
    				staticColRow = new TableRow(Teams.this);
    				break;
    			case ADDTEAM:
    				text = setupText();
    	        	text.setTextSize(26);
    	        	text.setText((String)data[1]);
    	        	text.setPadding(10, 10, 10, 10);
    	        	row.addView(text);
    				staticColText = setupText();
    				staticColText.setTextSize(26);
    				staticColText.setText((String)data[1]);
    				staticColText.setPadding(10, 10, 10, 10);
    	        	staticColRow.addView(staticColText);
    				staticTeamText = setupText();
    				staticTeamText.setTextSize(26);
    				staticTeamText.setText((String)data[1]);
    				staticTeamText.setPadding(10, 10, 10, 10);
    	        	staticTeamRow.addView(staticTeamText);
    				staticTableText = setupText();
    				staticTableText.setTextSize(26);
    				staticTableText.setText((String)data[1]);
    				staticTableText.setPadding(10, 10, 10, 10);
    				staticTableText.setBackgroundColor(Color.BLACK);
    				staticTableRow.addView(staticTableText);
    	        	break;
    			case STATICCOL:
    				staticColText = setupText();
    				staticColText.setTextSize(26);
    				staticColText.setText((String)data[1]);
    				staticColText.setPadding(10, 10, 10, 10);
    	        	staticColRow.addView(staticColText);
    				text = setupText();
    	        	text.setTextSize(26);
    	        	text.setText((String)data[1]);
    	        	text.setPadding(10, 10, 10, 10);
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
    			case NEWROW:
    				row = new TableRow(Teams.this);
    				row.setFocusable(true);
    				row.setFocusableInTouchMode(true);
    				staticTeamRow = new TableRow(Teams.this);
    				staticTeamRow.setFocusable(true);
    				staticTeamRow.setFocusableInTouchMode(true);
    				row.setOnFocusChangeListener(new OnFocusChangeListener() {
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
    				staticTeamRow.setOnFocusChangeListener(new OnFocusChangeListener() {
    					public void onFocusChange(View v, boolean hasFocus) {
    						if(hasFocus) {
    							v.setBackgroundColor(Color.GRAY);
    							row.setBackgroundColor(Color.GRAY);
    						} else {
    							v.setBackgroundColor(Color.BLACK);
    							row.setBackgroundColor(Color.BLACK);
    						}
    					}
    		    	});
    				break;
    			case NEWTEXT:
    				text = setupText();
    				break;
    			case ROWONCLICK:
    				final int team = (Integer)data[1];
    				final String teamString = Integer.toString(team);
        			text.setText(teamString);
        			staticTeamRow.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
                			String actionName = "com.mechinn.android.myalliance.OpenTeamInfo";
            				Intent intent = new Intent(actionName);
        					intent.putExtra("team", team);
        		    		startActivity(intent);
        				}
            		});
            		row.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
                			String actionName = "com.mechinn.android.myalliance.OpenTeamInfo";
            				Intent intent = new Intent(actionName);
        					intent.putExtra("team", team);
        		    		startActivity(intent);
        				}
            		});
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
	        	staticCols.removeAllViews();
	        	staticCols.addView(tr);
	        }
    	}
    	
		protected Void doInBackground(Void... no) {
			SyncDB sdb = null;
			try {
				sdb = new SyncDB(Teams.this);
				Cursor teamNums = teamInfoDb.fetchTeamNums();
				while(teamNums.moveToNext()) {
					sdb.getTeam(teamNums.getInt(teamNums.getColumnIndex(TeamInfoDB.KEY_TEAM)),teamInfoDb);
				}
				sdb.close();
			} catch (SQLException e1) {
				Log.i("teams list", "mysql error",e1);
			} catch (ClassNotFoundException e1) {
				Log.e("teams list","no mysql driver",e1);
			}
	        // Get all of the notes from the database and create the item list
	        Cursor teamInfo = teamInfoDb.fetchAllTeams();
	        startManagingCursor(teamInfo);
	        
	        String[] colNames = new String[] { "Team", "Orientation", "Number of Wheels", "Wheel Type 1", "Wheel Diameter 1", "Wheel Type 2", "Wheel Diameter 2", "Dead Wheel Type", "Turret Shooter", "Auto Tracking", "Fender Shooter", "Key Shooter", "Crosses Barrier", "Climb Bridge", "Autonomous" };
	        
	        String[] from = new String[] { TeamInfoDB.KEY_TEAM, TeamInfoDB.KEY_ORIENTATION, TeamInfoDB.KEY_NUMWHEELS, TeamInfoDB.KEY_WHEEL1TYPE, 
	        		TeamInfoDB.KEY_WHEEL1DIAMETER, TeamInfoDB.KEY_WHEEL2TYPE, TeamInfoDB.KEY_WHEEL2DIAMETER, TeamInfoDB.KEY_DEADWHEELTYPE, 
	        		TeamInfoDB.KEY_TURRET, TeamInfoDB.KEY_TRACKING, TeamInfoDB.KEY_FENDER, TeamInfoDB.KEY_KEY, TeamInfoDB.KEY_BARRIER, TeamInfoDB.KEY_CLIMB, TeamInfoDB.KEY_AUTONOMOUS };

        	publishProgress(NEWROW);
	        publishProgress(NEWSIMPLEROW);
	        for(int i=0;i<colNames.length;++i) {
	        	if(i==0) {
		        	publishProgress(ADDTEAM,colNames[i]);
	        	} else {
		        	publishProgress(STATICCOL,colNames[i]);
	        	}
	        }
	        publishProgress(ADDSTATICTABLE);
	        
	        while(teamInfo.moveToNext()) {
	        	publishProgress(NEWROW);
	        	for(int j=0;j<from.length;++j) {
	        		publishProgress(NEWTEXT);
	            	String colName = from[j];
	            	int col = teamInfo.getColumnIndex(colName);
//	            	Log.d("table fill", Integer.toString(col));
//	            	Log.d("table fill", Integer.toString(teamInfo.getInt(col)));
	            	if(colName.equals(TeamInfoDB.KEY_TEAM)) {
	            		publishProgress(ROWONCLICK,teamInfo.getInt(col));
	            	} else if(colName.equals(TeamInfoDB.KEY_ORIENTATION) || colName.equals(TeamInfoDB.KEY_WHEEL1TYPE) || 
	            			colName.equals(TeamInfoDB.KEY_WHEEL2TYPE) || colName.equals(TeamInfoDB.KEY_DEADWHEELTYPE) || 
	            			colName.equals(TeamInfoDB.KEY_NOTES)){
	            		publishProgress(SETTEXT,teamInfo.getString(col));
	            	} else if (colName.equals(TeamInfoDB.KEY_NUMWHEELS) || colName.equals(TeamInfoDB.KEY_WHEEL1DIAMETER) || 
	            			colName.equals(TeamInfoDB.KEY_WHEEL2DIAMETER) || colName.equals(TeamInfoDB.KEY_WHEELTYPES)) {
	            		publishProgress(SETTEXT,Integer.toString(teamInfo.getInt(col)));
	            	} else if (colName.equals(TeamInfoDB.KEY_DEADWHEEL) || colName.equals(TeamInfoDB.KEY_TURRET) || 
	            			colName.equals(TeamInfoDB.KEY_TRACKING) || colName.equals(TeamInfoDB.KEY_FENDER) || 
	            			colName.equals(TeamInfoDB.KEY_KEY) || colName.equals(TeamInfoDB.KEY_BARRIER) || 
	            			colName.equals(TeamInfoDB.KEY_CLIMB) || colName.equals(TeamInfoDB.KEY_AUTONOMOUS)) {
	            		switch(teamInfo.getInt(col)) {
	            			case 0:publishProgress(SETTEXT,"no");break;
	            			default:publishProgress(SETTEXT,"yes");
	            		}
	            	}
	            	publishProgress(ADDTEXT);
	            }
	        	publishProgress(ADDSTATICTEAM);
	        }
	        return null;
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.teamsmenu, menu);
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
	        case R.id.refresh:
	        	onStart();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
