package com.mechinn.android.myalliance.activity;

import com.mechinn.android.myalliance.R;
import com.mechinn.android.myalliance.ResetDB;
import com.mechinn.android.myalliance.R.id;
import com.mechinn.android.myalliance.R.layout;
import com.mechinn.android.myalliance.data.Prefs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Settings extends Activity {
    private static final int RESETMSG = 8694;
    private static final int RESETPROG = 8695;
    
    private Prefs prefs;
	private EditText host;
	private EditText port;
	private EditText user;
	private EditText pass;
	private Button save;
	private Button discard;
	private Spinner competitions;
	private Button pullFRC;
	private Button reset;

	private AlertDialog resetDialog;
	
	private void getVals() {
		host.setText(prefs.getHost());
		port.setText(Integer.toString(prefs.getPort()));
		user.setText(prefs.getUser());
		pass.setText(prefs.getPass());
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        prefs = new Prefs(this);
        
        host = (EditText) findViewById(R.id.hostText);
        port = (EditText) findViewById(R.id.portText);
        user = (EditText) findViewById(R.id.usernameText);
        pass = (EditText) findViewById(R.id.passText);
        
        getVals();
        
        save = (Button) findViewById(R.id.saveDbInfo);
        save.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		prefs.setHost(host.getText().toString());
        		prefs.setPort(Integer.parseInt(port.getText().toString()));
        		prefs.setUser(user.getText().toString());
        		prefs.setPass(pass.getText().toString());
        		Toast.makeText(Settings.this, "Saved", Toast.LENGTH_SHORT).show();
			}
        });
        
        discard = (Button) findViewById(R.id.discardDbInfo);
        discard.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
				getVals();
			}
        });
        
        competitions = (Spinner) findViewById(R.id.competitions);
        competitions.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	prefs.setCompetition(parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView parent) {
            	// Do nothing.
            }
        });
        
        pullFRC = (Button) findViewById(R.id.pullfrc);
        pullFRC.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });
        
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	resetDialog = new AlertDialog.Builder(Settings.this)
                .setTitle("Really reset DB?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	new ResetDB(Settings.this).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	resetDialog.dismiss();
                    }
                }).show();
            }
        });
    }
}
