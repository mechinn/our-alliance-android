package com.mechinn.frcscouting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Info extends Activity {
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
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        //initalize variables
    	orientation = 0;
    	numWheels = 0;
    	wheelTypes = 1;
    	deadWheel = false;
    	wheel1Type = 0;
    	wheel1Diameter = 0;
    	wheel2Type = 0;
    	wheel2Diameter = 0;
    	deadWheelType = 0;
    	turret = false;
    	tracking = false;
    	fender = false;
    	key = false;
    	barrier = false;
    	climb = false;
    	notes = "";
        
        team = getIntent().getIntExtra("team", 0);
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
        
        wheelSpinner = (Spinner) findViewById(R.id.wheelSpinner);
        ArrayAdapter<CharSequence> wheels = ArrayAdapter.createFromResource(this, R.array.wheels, android.R.layout.simple_spinner_item);
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
        
        has1WheelTypes = (RadioButton) findViewById(R.id.has1WheelTypes);
        has1WheelTypes.setOnClickListener(wheelTypeListener);
        
        has2WheelTypes = (RadioButton) findViewById(R.id.has2WheelTypes);
        has2WheelTypes.setOnClickListener(wheelTypeListener);
        
        has1WheelTypes.toggle();
        
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

        ArrayAdapter<CharSequence> wheelTypeStrings = ArrayAdapter.createFromResource(this, R.array.wheelTypes, android.R.layout.simple_spinner_item);
        wheelTypeStrings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        wheel1 = (TableRow) findViewById(R.id.wheel1);
        
        
        wheel1TypeSpinner = (Spinner) findViewById(R.id.wheel1Type);
        wheel1TypeSpinner.setAdapter(wheelTypeStrings);
        wheel1TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String wheel1TypeString = (parent.getItemAtPosition(pos).toString());
            	if(wheel1TypeString.equals("Kit")) {
            		wheel1Type = 1;
            	} else if(wheel1TypeString.equals("Traction")) {
            		wheel1Type = 2;
            	} else if(wheel1TypeString.equals("Mechanum")) {
            		wheel1Type = 3;
            	} else if(wheel1TypeString.equals("Omni")) {
            		wheel1Type = 4;
            	} else if(wheel1TypeString.equals("Slick")) {
            		wheel1Type = 5;
            	} else if(wheel1TypeString.equals("Tire")) {
            		wheel1Type = 6;
            	} else if(wheel1TypeString.equals("Track")) {
            		wheel1Type = 7;
            	} else {
            		wheel1Type = 8;
            	}
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        
        wheel1DiameterText = (EditText) findViewById(R.id.wheel1Diameter);
        
        wheel2 = (TableRow) findViewById(R.id.wheel2);
        
        wheel2TypeSpinner = (Spinner) findViewById(R.id.wheel2Type);
        wheel2TypeSpinner.setAdapter(wheelTypeStrings);
        wheel2TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String wheel2TypeString = (parent.getItemAtPosition(pos).toString());
            	if(wheel2TypeString.equals("Kit")) {
            		wheel2Type = 1;
            	} else if(wheel2TypeString.equals("Traction")) {
            		wheel2Type = 2;
            	} else if(wheel2TypeString.equals("Mechanum")) {
            		wheel2Type = 3;
            	} else if(wheel2TypeString.equals("Omni")) {
            		wheel2Type = 4;
            	} else if(wheel2TypeString.equals("Slick")) {
            		wheel2Type = 5;
            	} else if(wheel2TypeString.equals("Tire")) {
            		wheel2Type = 6;
            	} else if(wheel2TypeString.equals("Track")) {
            		wheel2Type = 7;
            	} else {
            		wheel2Type = 8;
            	}
            }
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        });
        
        wheel2DiameterText = (EditText) findViewById(R.id.wheel2Diameter);
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
        
        if(!deadWheel) {
	    	deadWheels.setVisibility(View.GONE);
	    }
        
        turretShooter = (CheckBox) findViewById(R.id.turretShooter);
        turretShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	turret = ((CheckBox) v).isChecked();
            }
        });
        
        autoTracking = (CheckBox) findViewById(R.id.autoTracking);
        autoTracking.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	tracking = ((CheckBox) v).isChecked();
            }
        });
        
        fenderShooter = (CheckBox) findViewById(R.id.fenderShooter);
        fenderShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	fender = ((CheckBox) v).isChecked();
            }
        });
        
        keyShooter = (CheckBox) findViewById(R.id.keyShooter);
        keyShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	key = ((CheckBox) v).isChecked();
            }
        });
        
        crossesBarrier = (CheckBox) findViewById(R.id.crossesBarrier);
        crossesBarrier.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	barrier = ((CheckBox) v).isChecked();
            }
        });
        
        climbBridge = (CheckBox) findViewById(R.id.climbBridge);
        climbBridge.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	climb = ((CheckBox) v).isChecked();
            }
        });
        
        edittext = (EditText) findViewById(R.id.notes);
        
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                Toast.makeText(Info.this, "Save", Toast.LENGTH_SHORT).show();
                Log.d("team", Integer.toString(team));
                Log.d("orientation", Integer.toString(orientation));
                Log.d("numWheels", Integer.toString(numWheels));
                Log.d("wheelTypes", Integer.toString(wheelTypes));
                Log.d("deadWheel", Boolean.toString(deadWheel));
                if(wheelTypes > 0){
	                Log.d("wheel1Type", Integer.toString(wheel1Type));
	                String wheel1DiameterTextCheck = wheel1DiameterText.getText().toString();
	                if(!wheel1DiameterTextCheck.equals("")) {
	                	wheel1Diameter = Integer.parseInt(wheel1DiameterTextCheck);
	                } else {
	                	wheel1Diameter = 0;
	                }
	                Log.d("wheel1Diameter", Integer.toString(wheel1Diameter));
                } else {
                	Log.d("wheel1Type", Integer.toString(0));
                	Log.d("wheel1Diameter", Integer.toString(0));
                }
                if(wheelTypes > 1){
	                Log.d("wheel2Type", Integer.toString(wheel2Type));
	                String wheel2DiameterTextCheck = wheel2DiameterText.getText().toString();
	                if(!wheel2DiameterTextCheck.equals("")) {
	                	wheel2Diameter = Integer.parseInt(wheel2DiameterTextCheck);
	                } else {
	                	wheel2Diameter = 0;
	                }
	                Log.d("wheel2Diameter", Integer.toString(wheel2Diameter));
                } else {
                	Log.d("wheel2Type", Integer.toString(0));
                	Log.d("wheel2Diameter", Integer.toString(0));
                }
                if(deadWheel){
                	Log.d("deadWheelType", Integer.toString(deadWheelType));
                } else {
                	Log.d("deadWheelType", Integer.toString(0));
                }
                Log.d("turret", Boolean.toString(turret));
                Log.d("tracking", Boolean.toString(tracking));
                Log.d("fender", Boolean.toString(fender));
                Log.d("key", Boolean.toString(key));
                Log.d("barrier", Boolean.toString(barrier));
                Log.d("climb", Boolean.toString(climb));
                notes = edittext.getText().toString();
                Log.d("notes", notes);
            }
        });
        
        discard = (Button) findViewById(R.id.discard);
        discard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
