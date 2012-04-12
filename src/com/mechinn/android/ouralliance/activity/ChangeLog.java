package com.mechinn.android.ouralliance.activity;

import com.mechinn.android.ouralliance.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChangeLog extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);
        TextView changelog = (TextView) findViewById(R.id.changelog);
        changelog.setMovementMethod(LinkMovementMethod.getInstance());
        changelog.setText(Html.fromHtml(
				"<h1>ChangeLog:</h1>" +
	    		"<p>" +
	    			"0.5.2 - added turret checkbox back to team scouting" +
	    			"0.5.1 - major bug fixed in adding team to team scouting list" +
	    			"0.5 - added multiple pictures and team info button to match scouting" +
	    			"0.4.1 - major bug fixed in match scouting scoring.<br />" +
	        		"0.4 - Added scouting ranking, frame dimensions, drive train types, autonomous types, shooting rating, and balancing rating to team scouting. Added autonomous scoring, and shooting misses to match scouting. Also hopefully fixed a few bugs here and there.<br />"+
					"0.3.1 - Fixed minor bugs in team info UI and export CSV.<br />"+
					"0.3 - Fixed major bug in match scouting and added exporting CSVs to an FTP server<br />"+
					"0.2 - Added csv exporting and minor changes/fixes<br />"+
					"0.1 - Initial Release, very basic functionality right now to scout teams and matches at the Northeast Utilities FIRST Connecticut Regional.<br />" +
	    		"</p>"
    		));
        
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		ChangeLog.this.finish();
			}
        });
    }
}
