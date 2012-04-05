package com.mechinn.android.ouralliance.activity.scouting;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.Prefs;
import com.mechinn.android.ouralliance.providers.MatchListProvider;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MatchScouting extends FragmentActivity {
	private int matchNum;
	private MatchListInterface matchList;
	private Prefs prefs;
	private String comp;
	private int lastMatchNum;
	private int firstMatchNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchNum = getIntent().getExtras().getInt("match",0);
        prefs = new Prefs(this);
        comp = prefs.getCompetition();
        matchList = new MatchListInterface(this);
    	Cursor firstNlast = matchList.fetchMatches(comp);
    	firstNlast.moveToFirst();
    	firstMatchNum = firstNlast.getInt(firstNlast.getColumnIndex(MatchListProvider.keyMatchNum));
    	firstNlast.moveToLast();
    	lastMatchNum = firstNlast.getInt(firstNlast.getColumnIndex(MatchListProvider.keyMatchNum));
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
		    	int prevMatch = matchNum;
		    	while(firstMatchNum<=(--prevMatch)){
		    		if(matchList.fetchMatch(comp, prevMatch).getCount()>0) {
		    			intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
				    	intent.putExtra("match", prevMatch);
						startActivity(intent);
						this.finish();
			            return true;
		    		}
		    	}
		    	Toast.makeText(MatchScouting.this, "No matches before.", Toast.LENGTH_SHORT).show();
				return false;
	        case R.id.nextMatch:
	        	int nextMatch = matchNum;
		    	while(lastMatchNum>=(++nextMatch)){
		    		if(matchList.fetchMatch(comp, nextMatch).getCount()>0) {
		    			intent = new Intent("com.mechinn.android.ouralliance.OpenScouting");
				    	intent.putExtra("match", nextMatch);
						startActivity(intent);
						this.finish();
			            return true;
		    		}
		    	}
		    	Toast.makeText(MatchScouting.this, "No matches next.", Toast.LENGTH_SHORT).show();
				return false;
	        case R.id.settings:
		    	intent = new Intent("com.mechinn.android.ouralliance.OpenSettings");
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }	
	}
}
