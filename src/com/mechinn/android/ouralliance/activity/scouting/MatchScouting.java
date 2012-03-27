package com.mechinn.android.ouralliance.activity.scouting;

import com.mechinn.android.ouralliance.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MatchScouting extends FragmentActivity {
	private int matchNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchNum = getIntent().getExtras().getInt("match",0);
        setContentView(R.layout.fullscouting);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.scoutingmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
		    case R.id.previousMatch:
				intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
		    	intent.putExtra("match", matchNum-1);
				startActivity(intent);
				this.finish();
	            return true;
	        case R.id.nextMatch:
	        	intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
		    	intent.putExtra("match", matchNum+1);
				startActivity(intent);
				this.finish();
	            return true;
	        case R.id.settings:
		    	intent = new Intent("com.mechinn.android.ouralliance.OpenSettings");
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }	
	}
}
