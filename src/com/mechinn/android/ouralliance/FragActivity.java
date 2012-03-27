package com.mechinn.android.ouralliance;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;

/**
 * Fancy fragment reflector so we do not need to create a new activity for every fragment we use
 * @author mechinn
 */
public class FragActivity extends FragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
			String fragString = (String)getIntent().getExtras().get("fragment");
			try {
				if(null==fragString){
					Log.e("Fragment Activity", "Must give 'fragment' string");
				} else {
					Class<?> fragClass = Class.forName(fragString);
					String typeString = fragClass.getSuperclass().getName();
					if(typeString.equals("Fragment")){
						Fragment frag = (Fragment) fragClass.newInstance();
						frag.setArguments(getIntent().getExtras());
			            getSupportFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
					} else if(typeString.equals("ListFragment")) {
						ListFragment frag = (ListFragment) fragClass.newInstance();
						frag.setArguments(getIntent().getExtras());
			            getSupportFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
					} else {
						Log.e("Fragment Activity", fragString+" is an unknown fragment type: "+typeString);
					}
				}
			} catch (IllegalAccessException e) {
				Log.e("Fragment Activity", "Illegal Access Exception while reflecting fragment class "+fragString,e);
			} catch (InstantiationException e) {
				Log.e("Fragment Activity", "Instantiation Exception while reflecting fragment class "+fragString,e);
			} catch (ClassNotFoundException e) {
				Log.e("Fragment Activity", "Class Not Found Exception while reflecting fragment class "+fragString,e);
			}
        }
    }
	
}
