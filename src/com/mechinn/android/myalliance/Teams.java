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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Teams extends Activity {
	private TeamInfoDB teamInfoDb;
	private TableLayout table;
	private TextView text;
	private TableRow row;
	private Prefs prefs;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);

        //delete before putting myalliance.java back as main
        BugSenseHandler.setup(this, "a92dda4a");
        prefs = new Prefs(this);
        
        //first run setup the DB
        Log.d("first run",Boolean.toString(prefs.getFirstRun()));
        if(prefs.getFirstRun()) {
        	new ResetDB(Teams.this,true).show();
        	prefs.setRunned();
        }
        
        teamInfoDb = new TeamInfoDB(this);
        table = (TableLayout) this.findViewById(R.id.table);
	}
	
	public void onStart() {
		table.removeAllViews();
		new getTeams().execute(this);
		super.onStart();
	}
	
	public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
    }
    
    private TableRow setupRow() {
    	TableRow tr = new TableRow(this);
    	tr.setFocusable(true);
    	tr.setFocusableInTouchMode(true);
    	tr.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					v.setBackgroundColor(Color.GRAY);
				} else {
					v.setBackgroundColor(Color.TRANSPARENT);
				}
			}
    	});
    	return tr;
    }
    
    private TextView setupText() {
		TextView text = new TextView(this);
		text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
		text.setGravity(Gravity.CENTER);
		text.setTextSize(20);
    	text.setPadding(0, 10, 0, 10);
		return text;
    }
    
    private class getTeams extends AsyncTask<Context,Object,Void> {
    	
    	protected void onProgressUpdate(Object... data) {
    		switch((Integer)data[0]) {
    			case 0:
    				row = new TableRow((Context)data[1]);
    				break;
    			case 1:
    				text = setupText();
    	        	text.setTextSize(26);
    	        	text.setText((String)data[1]);
    	        	text.setPadding(10, 10, 10, 10);
    			case 7:
    	        	row.addView(text);
    	        	break;
    			case 2:
    				table.addView(row);
    				break;
    			case 3:
    				row = setupRow();
    				break;
    			case 4:
    				text = setupText();
    				break;
    			case 5:
    				final int team = (Integer) data[1];
        			text.setText(Integer.toString(team));
            		row.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
                			String actionName = "com.mechinn.android.myalliance.OpenTeamInfo";
            				Intent intent = new Intent(actionName);
        					intent.putExtra("team", team);
        		    		startActivity(intent);
        				}
            		});
            		break;
    			case 6:
    				text.setText((String)data[1]);
    				break;
    		}
    	}
    	
    	protected void onPostExecute(Void no) {
    		Log.d("table rows",Integer.toString(table.getChildCount()));
	        if(table.getChildCount()<2) {
	        	TableRow tr = setupRow();
	        	TextView tv = setupText();
	        	tv.setText("No Teams");
	        	tr.addView(tv);
	        	table.addView(tr);
	        }
    	}
    	
		protected Void doInBackground(Context... con) {
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
	        
	        publishProgress(0,con[0]);
	        for(int i=0;i<colNames.length;++i) {
	        	publishProgress(1,colNames[i]);
	        }
	        publishProgress(2);
	        
	        while(teamInfo.moveToNext()) {
	        	publishProgress(3);
	        	for(int j=0;j<from.length;++j) {
	        		publishProgress(4);
	            	String colName = from[j];
	            	int col = teamInfo.getColumnIndex(colName);
//	            	Log.d("table fill", Integer.toString(col));
//	            	Log.d("table fill", Integer.toString(teamInfo.getInt(col)));
	            	if(colName.equals(TeamInfoDB.KEY_TEAM)) {
	            		publishProgress(5,teamInfo.getInt(col));
	            	} else if(colName.equals(TeamInfoDB.KEY_ORIENTATION) || colName.equals(TeamInfoDB.KEY_WHEEL1TYPE) || 
	            			colName.equals(TeamInfoDB.KEY_WHEEL2TYPE) || colName.equals(TeamInfoDB.KEY_DEADWHEELTYPE) || 
	            			colName.equals(TeamInfoDB.KEY_NOTES)){
	            		publishProgress(6,teamInfo.getString(col));
	            	} else if (colName.equals(TeamInfoDB.KEY_NUMWHEELS) || colName.equals(TeamInfoDB.KEY_WHEEL1DIAMETER) || 
	            			colName.equals(TeamInfoDB.KEY_WHEEL2DIAMETER) || colName.equals(TeamInfoDB.KEY_WHEELTYPES)) {
	            		publishProgress(6,Integer.toString(teamInfo.getInt(col)));
	            	} else if (colName.equals(TeamInfoDB.KEY_DEADWHEEL) || colName.equals(TeamInfoDB.KEY_TURRET) || 
	            			colName.equals(TeamInfoDB.KEY_TRACKING) || colName.equals(TeamInfoDB.KEY_FENDER) || 
	            			colName.equals(TeamInfoDB.KEY_KEY) || colName.equals(TeamInfoDB.KEY_BARRIER) || 
	            			colName.equals(TeamInfoDB.KEY_CLIMB) || colName.equals(TeamInfoDB.KEY_AUTONOMOUS)) {
	            		switch(teamInfo.getInt(col)) {
	            			case 0:publishProgress(6,"no");break;
	            			default:publishProgress(6,"yes");
	            		}
	            	}
	            	publishProgress(7);
	            }
	        	publishProgress(2);
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
