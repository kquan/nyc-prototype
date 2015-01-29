package com.nyc.prototype.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Kevin on 1/29/2015.
 */
public class HandleGcmReceivedReceiver extends WakefulBroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = HandleGcmReceivedReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        startWakefulService(context, new Intent(intent).setComponent(new ComponentName(context, HandleGcmService.class)));
    }
}
