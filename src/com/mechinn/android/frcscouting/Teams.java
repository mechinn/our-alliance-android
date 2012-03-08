package com.mechinn.android.frcscouting;

import com.mechinn.android.frcscouting.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Teams extends Activity {
	private TeamInfoDb teamInfoDb;
	private TableLayout table;
	private TextView text;
	private TableRow row;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        
        teamInfoDb = new TeamInfoDb(this);
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
                			String actionName = "com.mechinn.android.frcscouting.OpenTeamInfo";
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
	        // Get all of the notes from the database and create the item list
	        Cursor teamInfo = teamInfoDb.fetchAllTeams();
	        startManagingCursor(teamInfo);
	        
	        String[] colNames = new String[] { "Team", "Orientation", "Number of Wheels", "Wheel Type 1", "Wheel Diameter 1", "Wheel Type 2", "Wheel Diameter 2", "Dead Wheel Type", "Turret Shooter", "Auto Tracking", "Fender Shooter", "Key Shooter", "Crosses Barrier", "Climb Bridge", "Autonomous" };
	        
	        String[] from = new String[] { TeamInfoDb.KEY_TEAM, TeamInfoDb.KEY_ORIENTATION, TeamInfoDb.KEY_NUMWHEELS, TeamInfoDb.KEY_WHEEL1TYPE, 
	        		TeamInfoDb.KEY_WHEEL1DIAMETER, TeamInfoDb.KEY_WHEEL2TYPE, TeamInfoDb.KEY_WHEEL2DIAMETER, TeamInfoDb.KEY_DEADWHEELTYPE, 
	        		TeamInfoDb.KEY_TURRET, TeamInfoDb.KEY_TRACKING, TeamInfoDb.KEY_FENDER, TeamInfoDb.KEY_KEY, TeamInfoDb.KEY_BARRIER, TeamInfoDb.KEY_CLIMB, TeamInfoDb.KEY_AUTONOMOUS };
	        
	        publishProgress(0,con[0]);
	        for(int i=0;i<colNames.length;++i) {
	        	publishProgress(1,colNames[i]);
	        }
	        publishProgress(2);
	        
	        teamInfo.moveToFirst();
	        for(int i=0;i<teamInfo.getCount();++i) {
	        	publishProgress(3);
	        	for(int j=0;j<from.length;++j) {
	        		publishProgress(4);
	            	String colName = from[j];
	            	int col = teamInfo.getColumnIndex(colName);
//	            	Log.d("table fill", Integer.toString(col));
//	            	Log.d("table fill", Integer.toString(teamInfo.getInt(col)));
	            	if(colName.equals(TeamInfoDb.KEY_TEAM)) {
	            		publishProgress(5,teamInfo.getInt(col));
	            	} else if(colName.equals(TeamInfoDb.KEY_ORIENTATION) || colName.equals(TeamInfoDb.KEY_WHEEL1TYPE) || 
	            			colName.equals(TeamInfoDb.KEY_WHEEL2TYPE) || colName.equals(TeamInfoDb.KEY_DEADWHEELTYPE) || 
	            			colName.equals(TeamInfoDb.KEY_NOTES)){
	            		publishProgress(6,teamInfo.getString(col));
	            	} else if (colName.equals(TeamInfoDb.KEY_NUMWHEELS) || colName.equals(TeamInfoDb.KEY_WHEEL1DIAMETER) || 
	            			colName.equals(TeamInfoDb.KEY_WHEEL2DIAMETER) || colName.equals(TeamInfoDb.KEY_WHEELTYPES)) {
	            		publishProgress(6,Integer.toString(teamInfo.getInt(col)));
	            	} else if (colName.equals(TeamInfoDb.KEY_DEADWHEEL) || colName.equals(TeamInfoDb.KEY_TURRET) || 
	            			colName.equals(TeamInfoDb.KEY_TRACKING) || colName.equals(TeamInfoDb.KEY_FENDER) || 
	            			colName.equals(TeamInfoDb.KEY_KEY) || colName.equals(TeamInfoDb.KEY_BARRIER) || 
	            			colName.equals(TeamInfoDb.KEY_CLIMB) || colName.equals(TeamInfoDb.KEY_AUTONOMOUS)) {
	            		switch(teamInfo.getInt(col)) {
	            			case 0:publishProgress(6,"no");break;
	            			default:publishProgress(6,"yes");
	            		}
	            	}
	            	publishProgress(7);
	            }
	        	teamInfo.moveToNext();
	        	publishProgress(2);
	        }
	        return null;
		}
    }
}
