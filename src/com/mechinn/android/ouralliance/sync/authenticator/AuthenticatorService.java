package com.mechinn.android.ouralliance.sync.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
	private static final String TAG = "AuthenticationService";
	private static Authenticator sAccountAuthenticator = null;

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
			ret = getAuthenticator().getIBinder();
		}
		return ret;
	}
	
	private Authenticator getAuthenticator() {
		if (sAccountAuthenticator == null) {
			sAccountAuthenticator = new Authenticator(this);
		}
		return sAccountAuthenticator;
	}

}
