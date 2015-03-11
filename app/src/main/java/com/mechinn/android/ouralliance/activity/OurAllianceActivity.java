package com.mechinn.android.ouralliance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;

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
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    adView.loadAd(adRequest);
                } else {
                    adView.setVisibility(View.GONE);
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        adView = (AdView) this.findViewById(R.id.adView);
        adView.setAdListener(adListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        showAd();
    }

    public boolean showAd() {
        if(getPrefs().isAdsDisabled()) {
            adView.setVisibility(View.GONE);
            return false;
        }
        // Look up the AdView as a resource and load a request.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            } else {
                adView.setVisibility(View.GONE);
            }
            return false;
        } else {
            adView.loadAd(adRequest);
        }
        return true;
    }
}
