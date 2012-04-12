package com.mechinn.android.ouralliance.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.FTPConnection;
import com.mechinn.android.ouralliance.Filename;
import com.mechinn.android.ouralliance.OurAllianceCSVWriter;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.ResetDB;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	private final String TAG = "settings";
    private Prefs prefs;
	private EditText scouter;
	private EditText host;
	private EditText port;
	private EditText user;
	private EditText pass;
	private Button save;
	private Button discard;
	private Spinner competitions;
	private Button export;
	private Button exportSend;
	private Button reset;

	private OurAllianceCSVWriter csvTool;
	private AlertDialog resetDialog;
	private FTPConnection ftp;
	
	public void loadInfo() {
		scouter.setText(prefs.getScouter());
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
        
        TextView credits = (TextView) findViewById(R.id.credits);
        credits.setMovementMethod(LinkMovementMethod.getInstance());
        credits.setText(Html.fromHtml(
        		"<h1>Credits</h1>" +
        		"<b>Copyright 2012 <a href='http://mechinn.com/android'>Michael Chinn</a>.</b>" +
        		"<p>" +
	        		"You choose to run this software with the full knowledge that it is in a pre-release form and any damage or loss of data while using this software I cannot be held responsible for.<br />" +
	        		"<br />" +
	        		"This app uses software that is under the The Apache Software Foundation's <a href='http://www.apache.org/licenses/LICENSE-2.0.txt'>Apache License 2.0</a>." +
        		"</p>"
    		));
        
        Button changelog = (Button) findViewById(R.id.changelog);
        changelog.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		String actionName = "com.mechinn.android.ouralliance.OpenChangeLog";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
			}
        });

        scouter = (EditText) findViewById(R.id.scouterText);
        host = (EditText) findViewById(R.id.hostText);
        port = (EditText) findViewById(R.id.portNumber);
        user = (EditText) findViewById(R.id.userText);
        pass = (EditText) findViewById(R.id.passText);
        
        loadInfo();
        
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		prefs.setScouter(scouter.getText().toString());
        		prefs.setHost(host.getText().toString());
        		prefs.setPort(Integer.parseInt(port.getText().toString()));
        		prefs.setUser(user.getText().toString());
        		prefs.setPass(pass.getText().toString());
        		Toast.makeText(Settings.this, "Saved", Toast.LENGTH_SHORT).show();
			}
        });
        
        discard = (Button) findViewById(R.id.discard);
        discard.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		loadInfo();
			}
        });
        
        competitions = (Spinner) findViewById(R.id.competitions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.matchteamrow);
//        for(String comp : TeamScoutingProvider.COMPETITIONS) {
//        	adapter.add(comp);
//        }
        adapter.add("This");
        competitions.setAdapter(adapter);
        competitions.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	prefs.setCompetition(parent.getItemAtPosition(pos).toString());
            }
			public void onNothingSelected(AdapterView<?> parent) {
				//none
			}
        });
        
        export = (Button) findViewById(R.id.exportCSV);
        export.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			csvTool = new OurAllianceCSVWriter(Settings.this);
    			Log.i(TAG,"Finished building database to CSVs");
    			try {
					csvTool.writeMatchList();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			try {
					csvTool.writeMatchScouting();
				} catch (IOException e) {
					e.printStackTrace();
				}
//    			csvTool.writeTeamRankings();
    			try {
					csvTool.writeTeamScouting();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			Log.i(TAG,"Finished writing database to CSVs");
			}
        });
        
        exportSend = (Button) findViewById(R.id.exportCSVSend);
        exportSend.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			csvTool = new OurAllianceCSVWriter(Settings.this);
    			Log.i(TAG,"Finished building database to CSVs");
    			Toast.makeText(Settings.this, "Working...", Toast.LENGTH_LONG).show();
    			new SendCSVFTP().execute();
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
    
    private class SendCSVFTP extends AsyncTask<Void,Void,List<String>> {
	
		protected void onPostExecute(List<String> filenames) {
			for(String file : filenames) {
				if(file.equals("")) {
					Toast.makeText(Settings.this, "Unable to write table to CSV.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Settings.this, "Wrote match list to "+file, Toast.LENGTH_SHORT).show();
				}
			}
		}
    	
		protected List<String> doInBackground(Void... no) {
			
			String dir = "/"+prefs.getScouter();
			ftp = new FTPConnection(Settings.this);
	        ftp.connect();
	        if(!ftp.changeDirectory(dir)) {
	        	ftp.makeDirectory(dir);
	        }
	        List<String> filenames = new ArrayList<String>();
	        try {
				filenames.add(csvTool.writeMatchList());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				filenames.add(csvTool.writeMatchScouting());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				filenames.add(csvTool.writeTeamScouting());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        //csvTool.writeTeamRankings()
			for(String file : filenames) {
				Filename filename = new Filename(file,"/",".csv");
    			ftp.upload(file, filename.filename()+".csv", dir);
			}
			Log.i(TAG,"Finished writing database to CSVs");
	        return filenames;
		}
    }
}
