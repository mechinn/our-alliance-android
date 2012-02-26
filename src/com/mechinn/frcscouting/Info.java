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
        }
    };
    
    private void wheel1DiameterSet(EditText wheel1DiameterText) {
    	String text = wheel1DiameterText.getText().toString();
    	if(text.equals("")) {
    		return;
    	}
    	wheel1Diameter = Integer.parseInt(text);
    }
    
    private void wheel2DiameterSet(EditText wheel2DiameterText) {
    	String text = wheel2DiameterText.getText().toString();
    	if(text.equals("")) {
    		return;
    	}
    	wheel2Diameter = Integer.parseInt(text);
    }
    
    private void notesSet(EditText edittext) {
    	Log.d("edittext",edittext.getText().toString());
    	notes = edittext.getText().toString();
    }
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        //initalize variables
    	orientation = 0;
    	numWheels = 0;
    	wheelTypes = 0;
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
        
        final RadioButton orientationLong = (RadioButton) findViewById(R.id.orientationLong);
        orientationLong.setOnClickListener(orientationListener);
        
        final RadioButton orientationWide = (RadioButton) findViewById(R.id.orientationWide);
        orientationWide.setOnClickListener(orientationListener);
        
        final RadioButton orientationSquare = (RadioButton) findViewById(R.id.orientationSquare);
        orientationSquare.setOnClickListener(orientationListener);
        
        final RadioButton orientationOther = (RadioButton) findViewById(R.id.orientationOther);
        orientationOther.setOnClickListener(orientationListener);
        
        Spinner wheelSpinner = (Spinner) findViewById(R.id.wheelSpinner);
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
        
        final RadioButton has1WheelTypes = (RadioButton) findViewById(R.id.has1WheelTypes);
        has1WheelTypes.setOnClickListener(wheelTypeListener);
        
        final RadioButton has2WheelTypes = (RadioButton) findViewById(R.id.has2WheelTypes);
        has2WheelTypes.setOnClickListener(wheelTypeListener);
        
        final CheckBox hasDeadWheel = (CheckBox) findViewById(R.id.hasDeadWheel);
        hasDeadWheel.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	deadWheel = ((CheckBox) v).isChecked();
            }
        });

        ArrayAdapter<CharSequence> wheelTypeStrings = ArrayAdapter.createFromResource(this, R.array.wheelTypes, android.R.layout.simple_spinner_item);
        wheelTypeStrings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Spinner wheel1TypeSpinner = (Spinner) findViewById(R.id.wheel1Type);
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
        
        final EditText wheel1DiameterText = (EditText) findViewById(R.id.wheel1Diameter);
        wheel1DiameterText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
			public void onFocusChange(View arg0, boolean arg1) {
				wheel1DiameterSet(wheel1DiameterText);
			}
        });
        wheel1DiameterText.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	wheel1DiameterSet(wheel1DiameterText);
            		return true;
                }
                return false;
            }
        });
        
        Spinner wheel2TypeSpinner = (Spinner) findViewById(R.id.wheel2Type);
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
        
        final EditText wheel2DiameterText = (EditText) findViewById(R.id.wheel2Diameter);
        wheel2DiameterText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
			public void onFocusChange(View arg0, boolean arg1) {
				wheel2DiameterSet(wheel2DiameterText);
			}
        });
        wheel2DiameterText.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	wheel2DiameterSet(wheel2DiameterText);
            		return true;
                }
                return false;
            }
        });
        
        Spinner deadWheelTypeSpinner = (Spinner) findViewById(R.id.deadWheelType);
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
        
        final CheckBox turretShooter = (CheckBox) findViewById(R.id.turretShooter);
        turretShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	turret = ((CheckBox) v).isChecked();
            }
        });
        
        final CheckBox autoTracking = (CheckBox) findViewById(R.id.autoTracking);
        autoTracking.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	tracking = ((CheckBox) v).isChecked();
            }
        });
        
        final CheckBox fenderShooter = (CheckBox) findViewById(R.id.fenderShooter);
        fenderShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	fender = ((CheckBox) v).isChecked();
            }
        });
        
        final CheckBox keyShooter = (CheckBox) findViewById(R.id.keyShooter);
        keyShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	key = ((CheckBox) v).isChecked();
            }
        });
        
        final CheckBox crossesBarrier = (CheckBox) findViewById(R.id.crossesBarrier);
        crossesBarrier.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	barrier = ((CheckBox) v).isChecked();
            }
        });
        
        final CheckBox climbBridge = (CheckBox) findViewById(R.id.climbBridge);
        climbBridge.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	climb = ((CheckBox) v).isChecked();
            }
        });
        
        final EditText edittext = (EditText) findViewById(R.id.notes);
        edittext.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
			public void onFocusChange(View arg0, boolean arg1) {
				notesSet(edittext);
			}
        });
        edittext.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	notesSet(edittext);
            		return true;
                }
                return false;
            }
        });
        
        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                Toast.makeText(Info.this, "Save", Toast.LENGTH_SHORT).show();
                Log.d("team", Integer.toString(team));
                Log.d("orientation", Integer.toString(orientation));
                Log.d("numWheels", Integer.toString(numWheels));
                Log.d("wheelTypes", Integer.toString(wheelTypes));
                Log.d("deadWheel", Boolean.toString(deadWheel));
                Log.d("wheel1Type", Integer.toString(wheel1Type));
                Log.d("wheel1Diameter", Integer.toString(wheel1Diameter));
                Log.d("wheel2Type", Integer.toString(wheel2Type));
                Log.d("wheel2Diameter", Integer.toString(wheel2Diameter));
                Log.d("deadWheelType", Integer.toString(deadWheelType));
                Log.d("turret", Boolean.toString(turret));
                Log.d("tracking", Boolean.toString(tracking));
                Log.d("fender", Boolean.toString(fender));
                Log.d("key", Boolean.toString(key));
                Log.d("barrier", Boolean.toString(barrier));
                Log.d("climb", Boolean.toString(climb));
                Log.d("notes", notes.toString());
            }
        });
        
        final Button discard = (Button) findViewById(R.id.discard);
        discard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                Toast.makeText(Info.this, "Discard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
