package com.nyc.prototype;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nyc.prototype.debug.DebugActivity;
import com.nyc.prototype.gcm.GCM;
import com.nyc.prototype.user.CurrentUserHelper;
import com.nyc.prototype.user.LogoutAsyncTask;
import com.nyc.prototype.user.RequestPhoneNumberVerificationService;
import com.nyc.prototype.user.UserRegistrationService;
import com.nyc.utils.DeviceUtils;
import com.nyc.utils.PreferenceUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kevin on 1/26/2015.
 */
public class PrototypeActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = PrototypeActivity.class.getSimpleName();

    @InjectView(R.id.current_phone) protected TextView mCurrentPhone;
    @InjectView(R.id.current_device_id) protected TextView mCurrentDevice;

    @InjectView(R.id.signup_holder) protected View mSignupHolder;
    @InjectView(R.id.start_signup) protected View mStartSignupButton;

    @InjectView(R.id.verification_holder) protected View mVerificationHolder;
    @InjectView(R.id.waiting_for_verification) protected TextView mSignupState;
    @InjectView(R.id.request_verification) protected View mRequestVerificationButton;

    @InjectView(R.id.logged_in_holder) protected View mLoggedInHolder;
    @InjectView(R.id.current_user_id) protected TextView mCurrentId;
    @InjectView(R.id.logoff) protected View mLogoffButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GCM.ensureGcmIdExists(this);
        setContentView(R.layout.activity_prototype);
        ButterKnife.inject(this);

        if (CurrentUserHelper.hasLoggedIn(this)) {
            mSignupHolder.setVisibility(View.GONE);
            mVerificationHolder.setVisibility(View.GONE);
            mCurrentId.setText("User ID: "+CurrentUserHelper.getCurrentUserId(this));
        } else {
            mLoggedInHolder.setVisibility(View.GONE);
            if (PreferenceUtils.getMultiProcessAwarePreferences(this).getBoolean(PrototypeApplication.Preferences.STATE_WAITING_FOR_VERIFICATION, false)) {
                // Waiting for verification
                mSignupHolder.setVisibility(View.GONE);
            } else {
                // Need to signup
                mVerificationHolder.setVisibility(View.GONE);
            }
        }

        mCurrentPhone.setText("Phone #: "+DeviceUtils.getFirstPhoneNumber(this));
        mCurrentDevice.setText("Device ID: "+DeviceUtils.getDeviceId(this));

        mStartSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                IntentFilter filter = new IntentFilter(PrototypeApplication.Broadcasts.USER_REGISTRATION_COMPLETED);
                filter.addAction(PrototypeApplication.Broadcasts.USER_REGISTRATION_FAILED);
                registerReceiver(new BroadcastReceiver() {
                    @Override public void onReceive(final Context context, final Intent intent) {
                        Log.d(TAG, "Registration completed: " + intent.getAction());
                        Toast.makeText(context, "Registration completed: " + intent.getAction(), Toast.LENGTH_LONG).show();
                        recreate();
                    }
                }, filter);
                startService(new Intent(PrototypeActivity.this, UserRegistrationService.class));
                Toast.makeText(PrototypeActivity.this, "Initiating signup", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(PrototypeActivity.this, "Initiating verification request", Toast.LENGTH_SHORT).show();
                startService(new Intent(PrototypeActivity.this, RequestPhoneNumberVerificationService.class));
            }
        });
        mLogoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(PrototypeActivity.this, "Initiating logout", Toast.LENGTH_SHORT).show();
                new LogoutAsyncTask(PrototypeActivity.this) {
                    @Override
                    protected void onPostExecute(final Void aVoid) {
                        super.onPostExecute(aVoid);
                        recreate();
                    }
                }.execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug_debug_menu:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (BuildConfig.DEBUG) {
            menu.add(0, R.id.debug_debug_menu, 0, R.string.debug_menu_name);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
