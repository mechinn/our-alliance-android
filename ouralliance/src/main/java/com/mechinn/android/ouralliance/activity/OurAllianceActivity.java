package com.mechinn.android.ouralliance.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.R;

/**
 * Created by mechinn on 4/3/14.
 */
public class OurAllianceActivity extends Activity implements BackgroundProgress.Listener {
    public static final String TAG = "OurAllianceActivity";
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    private AdView adView;
    private AdRequest adRequest;

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
    public void onResume() {
        super.onResume();
        showAd();
    }

    public void cancelled(int flag) {
        switch(flag) {

        }
    }

    public void complete(int flag) {
        switch(flag) {

        }
    }

    public boolean showAd() {
        // Look up the AdView as a resource and load a request.
        adView = (AdView) this.findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
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
        });
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("014691060B018004")
                .build();
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
