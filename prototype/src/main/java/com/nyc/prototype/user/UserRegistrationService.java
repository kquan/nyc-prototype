package com.nyc.prototype.user;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
        new UserRegistrationCallable(this).run();
    }
}
