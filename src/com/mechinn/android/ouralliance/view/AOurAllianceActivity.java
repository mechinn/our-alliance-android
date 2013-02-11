package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public abstract class AOurAllianceActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.ouralliance, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
//	            EditText editText = (EditText) findViewById(R.id.edit_message);
//	            CharSequence message = editText.getText();
//	            intent.putExtra(EXTRA_MESSAGE, message);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
