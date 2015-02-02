package com.nyc.prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nyc.prototype.user.CurrentUserHelper;

/**
 * Created by Kevin on 2/2/2015.
 *
 * Simple activity to determine whether to send user to onboarding flow or normal flow
 */
public class LoginDetectionActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = LoginDetectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CurrentUserHelper.hasLoggedIn(this)) {
            // TODO: Send to onboarding flow
        }
        // TODO: Send to normal flow
        startActivity(new Intent(this, PrototypeActivity.class));
        finish();
    }
}
