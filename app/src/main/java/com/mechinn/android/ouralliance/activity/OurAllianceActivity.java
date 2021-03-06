package com.mechinn.android.ouralliance.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.event.ActivityResult;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by mechinn on 4/3/14.
 */
public class OurAllianceActivity extends ActionBarActivity {
    public static final String TAG = "OurAllianceActivity";
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    protected AdView adView;
    private AdRequest adRequest;
    private AdListener adListener;
    private Prefs prefs;
    public Prefs getPrefs() {
        return prefs;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("request code: " + requestCode+" | result code: "+resultCode+" | data: "+data);
        if(REQUEST_CODE_RECOVER_PLAY_SERVICES == requestCode) {
            if (RESULT_OK == resultCode) {
                adView.loadAd(adRequest);
            } else {
                adView.setVisibility(View.GONE);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this);
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adListener = new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        };
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adView = (AdView) this.findViewById(R.id.adView);
        adView.setAdListener(adListener);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAd();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public boolean showAd() {
        if(getPrefs().isAdsDisabled()) {
            adView.setVisibility(View.GONE);
            return false;
        }
        // Look up the AdView as a resource and load a request.
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (!prefs.isGooglePlayServicesPrompted() && GoogleApiAvailability.getInstance().isUserResolvableError(status)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, status, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
                prefs.setGooglePlayServicesPrompted(true);
            } else {
                adView.setVisibility(View.GONE);
            }
            return false;
        } else {
            adView.loadAd(adRequest);
        }
        return true;
    }

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
            case android.R.id.home:
                if(this.getSupportFragmentManager().getBackStackEntryCount()>0) {
                    this.getSupportFragmentManager().popBackStack();
                } else {
                    this.finish();
                }
                return true;
            case R.id.analysis:
                openActivity(AnalysisActivity.class);
                return true;
            case R.id.settings:
                openActivity(SettingsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void openActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
