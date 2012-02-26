package com.mechinn.frcscouting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Info extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        int team = getIntent().getIntExtra("team", 0);
        TextView teamNumber = (TextView) findViewById(R.id.teamNumber);
        teamNumber.setText(Integer.toString(team));
        
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
