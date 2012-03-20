package com.mechinn.android.myalliance.activity.scouting;

import com.mechinn.android.myalliance.R;
import com.mechinn.android.myalliance.R.layout;
import com.mechinn.android.myalliance.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Scouting extends FragmentActivity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscouting);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String actionName = "com.mechinn.android.myalliance.OpenSettings";
    	Intent intent = new Intent(actionName);
    	startActivity(intent);
		return true;
	}
}
