package com.nyc.prototype.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Kevin on 1/29/2015.
 */
public class HandleGcmService extends IntentService {

    @SuppressWarnings("unused")
    private static final String TAG = HandleGcmService.class.getSimpleName();

    public HandleGcmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        try {
            onHandlePush(intent);
        } finally {
            HandleGcmReceivedReceiver.completeWakefulIntent(intent);
        }
    }

    protected void onHandlePush(final Intent intent) {

    }
}
