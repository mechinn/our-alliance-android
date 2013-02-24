package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class StartupActivity extends Activity {
	public static final String TAG = StartupActivity.class.getName();
	/**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.startup);
        
        final StartupActivity sPlashScreen = this;   
        
        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(5000);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, OurAllianceActivity.class);
                startActivity(intent);
            }
        };
        
        mSplashThread.start();        
    }
        
    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}
