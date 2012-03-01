package com.mechinn.android.frcscouting;

import com.mechinn.android.frcscouting.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

public class Info extends Activity {
	private TeamInfoDb teamInfoDb;
	
	int team;
	int orientation;
	int numWheels;
	int wheelTypes;
	boolean deadWheel;
	int wheel1Type;
	int wheel1Diameter;
	int wheel2Type;
	int wheel2Diameter;
	int deadWheelType;
	boolean turret;
	boolean tracking;
	boolean fender;
	boolean key;
	boolean barrier;
	boolean climb;
	String notes;
	
	private RadioButton orientationLong;
	private RadioButton orientationWide;
	private RadioButton orientationSquare;
	private RadioButton orientationOther;
	private Spinner wheelSpinner;
	private RadioButton has1WheelTypes;
	private RadioButton has2WheelTypes;
	private CheckBox hasDeadWheel;
	private TableRow wheel1;
	private Spinner wheel1TypeSpinner;
	private EditText wheel1DiameterText;
	private TableRow wheel2;
	private Spinner wheel2TypeSpinner;
	private EditText wheel2DiameterText;
	private TableRow deadWheels;
	private Spinner deadWheelTypeSpinner;
	private CheckBox turretShooter;
	private CheckBox autoTracking;
	private CheckBox fenderShooter;
	private CheckBox keyShooter;
	private CheckBox crossesBarrier;
	private CheckBox climbBridge;
	private EditText edittext;
	private Button save;
	private Button discard;
	
	private ArrayAdapter<CharSequence> wheels;
	private ArrayAdapter<CharSequence> wheelTypeStrings;
    
    private OnClickListener orientationListener = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            if(rb.getText().equals("Long")) {
            	orientation = 1;
            } else if(rb.getText().equals("Wide")) {
            	orientation = 2;
            } else if(rb.getText().equals("Square")) {
            	orientation = 3;
            } else {
            	orientation = 4;
            }
        }
    };
    
    private OnClickListener wheelTypeListener = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            wheelTypes = Integer.parseInt(rb.getText().toString());
            if(wheelTypes > 1) {
            	wheel2.setVisibility(View.VISIBLE);
            } else {
            	wheel2.setVisibility(View.GONE);
            }
        }
    };
    
    private void wheelTypeToInt(String s, Integer wheelType) {
    	if(s.equals("Kit")) {
    		wheelType = 1;
    	} else if(s.equals("Traction")) {
    		wheelType = 2;
    	} else if(s.equals("Mechanum")) {
    		wheelType = 3;
    	} else if(s.equals("Omni")) {
    		wheelType = 4;
    	} else if(s.equals("Slick")) {
    		wheelType = 5;
    	} else if(s.equals("Tire")) {
    		wheelType = 6;
    	} else if(s.equals("Track")) {
    		wheelType = 7;
    	} else {
    		wheelType = 8;
    	}
    }
    
    private void intToWheelType(int wheelType, Spinner spinner) {
    	if(wheelType==1) {
    		spinner.setSelection(wheelTypeStrings.getPosition("Kit"));
        } else if(wheelType==2) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Traction"));
        } else if(wheelType==3) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Mechanum"));
        } else if(wheelType==4) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Omni"));
        } else if(wheelType==5) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Slick"));
        } else if(wheelType==6) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Tire"));
        } else if(wheelType==7) {
        	spinner.setSelection(wheelTypeStrings.getPosition("Track"));
        } else {
        	spinner.setSelection(wheelTypeStrings.getPosition("Other"));
    	}
    }
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        teamInfoDb = new TeamInfoDb(this, true);
        team = getIntent().getIntExtra("team", 0);
        Cursor teamInfo = teamInfoDb.fetchTeam(team);
        
      //get variables from DB
        for(int i=0;i<teamInfo.getColumnCount();++i) {
        	String colName = teamInfo.getColumnName(i);
        	Log.d("column",colName);
        	if(colName.equals(TeamInfoDb.KEY_ORIENTATION)){
        		orientation = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_NUMWHEELS)) {
        		numWheels = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_WHEELTYPES)) {
        		wheelTypes = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_DEADWHEEL)) {
        		if(teamInfo.getInt(i)==0) {
            		deadWheel = false;
            	} else {
            		deadWheel = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_WHEEL1TYPE)) {
        		wheel1Type = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_WHEEL1DIAMETER)) {
        		wheel1Diameter = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_WHEEL2TYPE)) {
        		wheel2Type = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_WHEEL2DIAMETER)) {
        		wheel2Diameter = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_DEADWHEELTYPE)) {
        		deadWheelType = teamInfo.getInt(i);
        	} else if (colName.equals(TeamInfoDb.KEY_TURRET)) {
        		if(teamInfo.getInt(i)==0) {
            		turret = false;
            	} else {
            		turret = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_TRACKING)) {
        		if(teamInfo.getInt(i)==0) {
            		tracking = false;
            	} else {
            		tracking = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_FENDER)) {
        		if(teamInfo.getInt(i)==0) {
            		fender = false;
            	} else {
            		fender = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_KEY)) {
        		if(teamInfo.getInt(i)==0) {
            		key = false;
            	} else {
            		key = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_BARRIER)) {
        		if(teamInfo.getInt(i)==0) {
            		barrier = false;
            	} else {
            		barrier = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_CLIMB)) {
        		if(teamInfo.getInt(i)==0) {
            		climb = false;
            	} else {
            		climb = true;
            	}
        	} else if (colName.equals(TeamInfoDb.KEY_NOTES)) {
        		notes = teamInfo.getString(i);
        	}
        }
        
        TextView teamNumber = (TextView) findViewById(R.id.teamNumber);
        teamNumber.setText(Integer.toString(team));
        
        orientationLong = (RadioButton) findViewById(R.id.orientationLong);
        orientationLong.setOnClickListener(orientationListener);
        
        orientationWide = (RadioButton) findViewById(R.id.orientationWide);
        orientationWide.setOnClickListener(orientationListener);
        
        orientationSquare = (RadioButton) findViewById(R.id.orientationSquare);
        orientationSquare.setOnClickListener(orientationListener);
        
        orientationOther = (RadioButton) findViewById(R.id.orientationOther);
        orientationOther.setOnClickListener(orientationListener);
        
        if(orientation == 1){
        	orientationLong.toggle();
        } else if(orientation == 2){
        	orientationWide.toggle();
        } else if(orientation == 3){
        	orientationSquare.toggle();
        } else if(orientation == 4){
        	orientationOther.toggle();
        }
        
        wheelSpinner = (Spinner) findViewById(R.id.wheelSpinner);
        wheels = ArrayAdapter.createFromResource(this, R.array.wheels, android.R.layout.simple_spinner_item);
        wheels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wheelSpinner.setAdapter(wheels);
        wheelSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String numWheelsString = (parent.getItemAtPosition(pos).toString());
            	if(numWheelsString.equals("Treads")) {
            		numWheels = -1;
            	} else {
            		numWheels = Integer.parseInt(numWheelsString);
            	}
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        
        if(numWheels==-1) {
        	wheelSpinner.setSelection(wheels.getPosition("Treads"));
        } else {
        	wheelSpinner.setSelection(wheels.getPosition(Integer.toString(numWheels)));
        }
        
        has1WheelTypes = (RadioButton) findViewById(R.id.has1WheelTypes);
        has1WheelTypes.setOnClickListener(wheelTypeListener);
        
        has2WheelTypes = (RadioButton) findViewById(R.id.has2WheelTypes);
        has2WheelTypes.setOnClickListener(wheelTypeListener);
        
        if(wheelTypes==2) {
        	has2WheelTypes.toggle();
        } else {
        	has1WheelTypes.toggle();
        }
        
        hasDeadWheel = (CheckBox) findViewById(R.id.hasDeadWheel);
        hasDeadWheel.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	deadWheel = ((CheckBox) v).isChecked();
            	if(deadWheel) {
            		deadWheels.setVisibility(View.VISIBLE);
                } else {
                	deadWheels.setVisibility(View.GONE);
                }
            }
        });
        
        hasDeadWheel.setChecked(deadWheel);

        wheelTypeStrings = ArrayAdapter.createFromResource(this, R.array.wheelTypes, android.R.layout.simple_spinner_item);
        wheelTypeStrings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        wheel1 = (TableRow) findViewById(R.id.wheel1);
        
        wheel1TypeSpinner = (Spinner) findViewById(R.id.wheel1Type);
        wheel1TypeSpinner.setAdapter(wheelTypeStrings);
        wheel1TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String wheel1TypeString = (parent.getItemAtPosition(pos).toString());
            	wheelTypeToInt(wheel1TypeString,wheel1Type);
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        intToWheelType(wheel1Type, wheel1TypeSpinner);
        
        wheel1DiameterText = (EditText) findViewById(R.id.wheel1Diameter);
        wheel1DiameterText.setText(Integer.toString(wheel1Diameter));
        
        wheel2 = (TableRow) findViewById(R.id.wheel2);
        
        wheel2TypeSpinner = (Spinner) findViewById(R.id.wheel2Type);
        wheel2TypeSpinner.setAdapter(wheelTypeStrings);
        wheel2TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String wheel2TypeString = (parent.getItemAtPosition(pos).toString());
            	wheelTypeToInt(wheel2TypeString,wheel1Type);
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        
        intToWheelType(wheel2Type, wheel2TypeSpinner);
        
        wheel2DiameterText = (EditText) findViewById(R.id.wheel2Diameter);
        wheel2DiameterText.setText(Integer.toString(wheel2Diameter));
        
        if(wheelTypes < 2) {
	    	wheel2.setVisibility(View.GONE);
	    }
        
        deadWheels = (TableRow) findViewById(R.id.deadWheels);
        
        deadWheelTypeSpinner = (Spinner) findViewById(R.id.deadWheelType);
        ArrayAdapter<CharSequence> deadWheelTypes = ArrayAdapter.createFromResource(this, R.array.deadWheelTypes, android.R.layout.simple_spinner_item);
        deadWheelTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadWheelTypeSpinner.setAdapter(deadWheelTypes);
        deadWheelTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String deadWheelTypeString = (parent.getItemAtPosition(pos).toString());
            	if(deadWheelTypeString.equals("Kit")) {
            		deadWheelType = 1;
            	} else if(deadWheelTypeString.equals("Traction")) {
            		deadWheelType = 2;
            	} else if(deadWheelTypeString.equals("Mechanum")) {
            		deadWheelType = 3;
            	} else if(deadWheelTypeString.equals("Omni")) {
            		deadWheelType = 4;
            	} else if(deadWheelTypeString.equals("Slick")) {
            		deadWheelType = 5;
            	} else if(deadWheelTypeString.equals("Tire")) {
            		deadWheelType = 6;
            	} else if(deadWheelTypeString.equals("Caster")) {
            		deadWheelType = 7;
            	} else {
            		deadWheelType = 8;
            	}
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        
        if(deadWheelType==1) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Kit"));
        } else if(deadWheelType==2) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Traction"));
        } else if(deadWheelType==3) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Mechanum"));
        } else if(deadWheelType==4) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Omni"));
        } else if(deadWheelType==5) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Slick"));
        } else if(deadWheelType==6) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Tire"));
        } else if(deadWheelType==7) {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Caster"));
        } else {
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition("Other"));
    	}
        
        if(!deadWheel) {
	    	deadWheels.setVisibility(View.GONE);
	    }
        
        turretShooter = (CheckBox) findViewById(R.id.turretShooter);
        turretShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	turret = ((CheckBox) v).isChecked();
            }
        });
        turretShooter.setChecked(turret);
        
        autoTracking = (CheckBox) findViewById(R.id.autoTracking);
        autoTracking.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	tracking = ((CheckBox) v).isChecked();
            }
        });
        autoTracking.setChecked(tracking);
        
        fenderShooter = (CheckBox) findViewById(R.id.fenderShooter);
        fenderShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	fender = ((CheckBox) v).isChecked();
            }
        });
        fenderShooter.setChecked(fender);
        
        keyShooter = (CheckBox) findViewById(R.id.keyShooter);
        keyShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	key = ((CheckBox) v).isChecked();
            }
        });
        keyShooter.setChecked(key);
        
        crossesBarrier = (CheckBox) findViewById(R.id.crossesBarrier);
        crossesBarrier.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	barrier = ((CheckBox) v).isChecked();
            }
        });
        crossesBarrier.setChecked(barrier);
        
        climbBridge = (CheckBox) findViewById(R.id.climbBridge);
        climbBridge.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	climb = ((CheckBox) v).isChecked();
            }
        });
        climbBridge.setChecked(climb);
        
        edittext = (EditText) findViewById(R.id.notes);
        edittext.setText(notes);
        
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.d("team", Integer.toString(team));
                Log.d("orientation", Integer.toString(orientation));
                Log.d("numWheels", Integer.toString(numWheels));
                Log.d("wheelTypes", Integer.toString(wheelTypes));
                Log.d("deadWheel", Boolean.toString(deadWheel));
                if(wheelTypes > 0){
	                String wheel1DiameterTextCheck = wheel1DiameterText.getText().toString();
	                if(!wheel1DiameterTextCheck.equals("")) {
	                	wheel1Diameter = Integer.parseInt(wheel1DiameterTextCheck);
	                } else {
	                	wheel1Diameter = 0;
	                }
                } else {
                	wheel1Type = 0;
                	wheel1Diameter = 0;
                }
                Log.d("wheel1Type", Integer.toString(wheel1Type));
            	Log.d("wheel1Diameter", Integer.toString(wheel1Diameter));
                if(wheelTypes > 1){
	                String wheel2DiameterTextCheck = wheel2DiameterText.getText().toString();
	                if(!wheel2DiameterTextCheck.equals("")) {
	                	wheel2Diameter = Integer.parseInt(wheel2DiameterTextCheck);
	                } else {
	                	wheel2Diameter = 0;
	                }
                } else {
                	wheel2Type = 0;
                	wheel2Diameter = 0;
                }
                Log.d("wheel2Type", Integer.toString(wheel1Type));
            	Log.d("wheel2Diameter", Integer.toString(wheel2Diameter));
                if(!deadWheel){
                	deadWheelType = 0;
                }
                Log.d("deadWheelType", Integer.toString(deadWheelType));
                Log.d("turret", Boolean.toString(turret));
                Log.d("tracking", Boolean.toString(tracking));
                Log.d("fender", Boolean.toString(fender));
                Log.d("key", Boolean.toString(key));
                Log.d("barrier", Boolean.toString(barrier));
                Log.d("climb", Boolean.toString(climb));
                notes = edittext.getText().toString();
                Log.d("notes", notes);
                
                teamInfoDb.updateTeam(team, orientation, numWheels, wheelTypes, 
            			deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter, 
            			deadWheelType, turret, tracking, fender, key, barrier, climb, notes);
                finish();
            }
        });
        
        discard = (Button) findViewById(R.id.discard);
        discard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	//just exit without saving
                finish();
            }
        });
    }
    
    public void onDestroy() {
    	teamInfoDb.close();
    	super.onDestroy();
    }
}
