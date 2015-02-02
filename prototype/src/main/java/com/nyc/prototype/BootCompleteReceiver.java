package com.nyc.prototype;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.nyc.prototype.user.CurrentUserHelper;
import com.nyc.prototype.user.RequestPhoneNumberVerificationService;

/**
 * Created by Kevin on 2/2/2015.
 */
public class BootCompleteReceiver extends WakefulBroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context == null) {
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
