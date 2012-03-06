package com.mechinn.android.frcscouting;

import com.mechinn.android.frcscouting.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        
        teamInfoDb = new TeamInfoDb(this);
        table = (TableLayout) this.findViewById(R.id.table);
	}
	
	public void onResume() {
		table.removeAllViews();
		fillData();
		super.onResume();
	}
	
	public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor teamInfo = teamInfoDb.fetchAllTeams();
        startManagingCursor(teamInfo);
        
        String[] colNames = new String[] { "Team", "Orientation", "Number of Wheels", "Wheel Type 1", "Wheel Diameter 1", "Wheel Type 2", "Wheel Diameter 2", "Dead Wheel Type", "Turret Shooter", "Auto Tracking", "Key Shooter", "Crosses Barrier", "Climb Bridge", "Autonomous" };
        
        String[] from = new String[] { TeamInfoDb.KEY_TEAM, TeamInfoDb.KEY_ORIENTATION, TeamInfoDb.KEY_NUMWHEELS, TeamInfoDb.KEY_WHEEL1TYPE, 
        		TeamInfoDb.KEY_WHEEL1DIAMETER, TeamInfoDb.KEY_WHEEL2TYPE, TeamInfoDb.KEY_WHEEL2DIAMETER, TeamInfoDb.KEY_DEADWHEELTYPE, 
        		TeamInfoDb.KEY_TURRET, TeamInfoDb.KEY_TRACKING, TeamInfoDb.KEY_FENDER, TeamInfoDb.KEY_KEY, TeamInfoDb.KEY_BARRIER, TeamInfoDb.KEY_CLIMB, TeamInfoDb.KEY_AUTONOMOUS };
        
        TableRow headerRow = new TableRow(this);
        for(int i=0;i<colNames.length;++i) {
        	TextView text = setupText();
        	String colName = colNames[i];
        	text.setTextSize(26);
        	text.setText(colName);
        	text.setPadding(10, 10, 10, 10);
        	headerRow.addView(text);
        }
        table.addView(headerRow);
        
        teamInfo.moveToFirst();
        for(int i=0;i<teamInfo.getCount();++i) {
        	TableRow tr = setupRow();
        	for(int j=0;j<from.length;++j) {
        		TextView text = setupText();
            	String colName = from[j];
            	int col = teamInfo.getColumnIndex(colName);
            	Log.d("table fill", Integer.toString(col));
            	Log.d("table fill", Integer.toString(teamInfo.getInt(col)));
            	if(colName.equals(TeamInfoDb.KEY_TEAM)) {
            		final int team = teamInfo.getInt(col);
            		text.setText(Integer.toString(team));
            		tr.setOnClickListener(new OnClickListener() {
            			public void onClick(View v) {
	            			String actionName = "com.mechinn.android.frcscouting.OpenTeamInfo";
	        				Intent intent = new Intent(actionName);
							intent.putExtra("team", team);
    			    		startActivity(intent);
						}
            		});
            	} else if(colName.equals(TeamInfoDb.KEY_ORIENTATION)){
            		switch(teamInfo.getInt(col)) {
	        			case 1: text.setText("Long");break;
	        			case 2: text.setText("Wide");break;
	        			case 3: text.setText("Square");break;
	        			default: text.setText("Other");
	        		}
            	} else if (colName.equals(TeamInfoDb.KEY_NUMWHEELS) || colName.equals(TeamInfoDb.KEY_WHEEL1DIAMETER) || 
            			colName.equals(TeamInfoDb.KEY_WHEEL2DIAMETER) || colName.equals(TeamInfoDb.KEY_WHEELTYPES)) {
            		text.setText(Integer.toString(teamInfo.getInt(col)));
            	} else if (colName.equals(TeamInfoDb.KEY_DEADWHEEL) || colName.equals(TeamInfoDb.KEY_TURRET) || 
            			colName.equals(TeamInfoDb.KEY_TRACKING) || colName.equals(TeamInfoDb.KEY_FENDER) || 
            			colName.equals(TeamInfoDb.KEY_KEY) || colName.equals(TeamInfoDb.KEY_BARRIER) || 
            			colName.equals(TeamInfoDb.KEY_CLIMB)) {
            		switch(teamInfo.getInt(col)) {
            			case 0:text.setText("no");break;
            			default:text.setText("yes");
            		}
            	} else if (colName.equals(TeamInfoDb.KEY_WHEEL1TYPE) || colName.equals(TeamInfoDb.KEY_WHEEL2TYPE)) {
            		switch(teamInfo.getInt(col)) {
            			case 1: text.setText("Kit");break;
            			case 2: text.setText("Traction");break;
            			case 3: text.setText("Mechanum");break;
            			case 4: text.setText("Omni");break;
            			case 5: text.setText("Slick");break;
            			case 6: text.setText("Tire");break;
            			case 7: text.setText("Track");break;
            			default: text.setText("Other");
            		}
            	} else if (colName.equals(TeamInfoDb.KEY_DEADWHEELTYPE)) {
            		switch(teamInfo.getInt(col)) {
	        			case 1: text.setText("Kit");break;
	        			case 2: text.setText("Traction");break;
	        			case 3: text.setText("Mechanum");break;
	        			case 4: text.setText("Omni");break;
	        			case 5: text.setText("Slick");break;
	        			case 6: text.setText("Tire");break;
	        			case 7: text.setText("Caster");break;
	        			default: text.setText("Other");
	        		}
            	} else if (colName.equals(TeamInfoDb.KEY_NOTES)) {
            		text.setText(teamInfo.getString(col));
            	}
            	tr.addView(text);
            }
        	teamInfo.moveToNext();
        	table.addView(tr);
        }
        Log.d("table rows",Integer.toString(table.getChildCount()));
        if(table.getChildCount()<2) {
        	TableRow tr = setupRow();
        	TextView tv = setupText();
        	tv.setText("No Teams");
        	tr.addView(tv);
        	table.addView(tr);
        }
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
}
