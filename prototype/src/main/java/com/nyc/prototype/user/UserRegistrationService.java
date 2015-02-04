package com.nyc.prototype.user;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nyc.prototype.Prototype;

/**
 * Created by Kevin on 1/30/2015.
 */
public class UserRegistrationService extends IntentService {

    @SuppressWarnings("unused")
    private static final String TAG = UserRegistrationService.class.getSimpleName();

    public UserRegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "Running user registration service");
        UserRegistrationResult result = null;
        try {
            result = new UserRegistrationCallable(this).call();
        } catch (Exception e) {
            Log.e(TAG, "Could not register user", e);
        }

        if (result == null) {
            sendBroadcast(new Intent(Prototype.Broadcasts.USER_REGISTRATION_FAILED));
        } else {
            Log.d(TAG, "User registration completed, sending broadcast.");
            sendBroadcast(new Intent(Prototype.Broadcasts.USER_REGISTRATION_COMPLETED));
        }
    }
}
