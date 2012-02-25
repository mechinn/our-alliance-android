package com.mechinn.frcscouting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Teams extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        
        Spinner teamSpinner = (Spinner) findViewById(R.id.teamSpinner);
        ArrayAdapter<CharSequence> teams = ArrayAdapter.createFromResource(this, R.array.teams, android.R.layout.simple_spinner_item);
        teams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teams);
        
        teamSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        Spinner wheelSpinner = (Spinner) findViewById(R.id.wheelSpinner);
        ArrayAdapter<CharSequence> wheels = ArrayAdapter.createFromResource(this, R.array.wheels, android.R.layout.simple_spinner_item);
        wheels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wheelSpinner.setAdapter(wheels);
        
        wheelSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
    
    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	Toast.makeText(parent.getContext(), "The planet is " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
    }
}
