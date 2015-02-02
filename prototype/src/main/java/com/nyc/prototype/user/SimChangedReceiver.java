package com.nyc.prototype.user;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Kevin on 2/2/2015.
 */
public class SimChangedReceiver extends WakefulBroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = SimChangedReceiver.class.getSimpleName();

    // Not in Android SDK - some manufacturers may not support this
    protected static final String ACTION_SIM_CHANGED = "android.intent.action.SIM_STATE_CHANGED";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context == null || intent == null || !intent.getAction().equals(ACTION_SIM_CHANGED)) {
            return;
        }
        if (CurrentUserHelper.hasUserId(context)) {
            if (CurrentUserHelper.isActiveChannelCorrect(context)) {
                Log.d(TAG, "Device and phone number are unchanged");
            } else {
                startWakefulService(context, new Intent(context, RequestPhoneNumberVerificationService.class));
            }
        }
    }
}
