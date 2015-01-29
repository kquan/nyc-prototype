package com.nyc.prototype.user;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.models.BaseServerResponse;
import com.nyc.prototype.api.PrototypeService;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.models.server.StartPhoneNumberVerificationRequest;
import com.nyc.utils.DeviceUtils;

import retrofit.RetrofitError;

/**
 * Created by Kevin on 1/29/2015.
 * Requests the server to send a verification code to the provided number to verify the phone number.
 *
 */
public class RequestPhoneNumberVerificationService extends IntentService {

    @SuppressWarnings("unused")
    private static final String TAG = RequestPhoneNumberVerificationService.class.getSimpleName();

    public RequestPhoneNumberVerificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String userId = CurrentUserHelper.getCurrentUserId(this);
        if (TextUtils.isEmpty(userId)) {
            Log.w(TAG, "No current user ID");
            return;
        }
        String phoneNumber = DeviceUtils.getFirstPhoneNumber(this);
        if (TextUtils.isEmpty(phoneNumber)) {
            Log.w(TAG, "No phone number to verify");
        }

        StartPhoneNumberVerificationRequest request = new StartPhoneNumberVerificationRequest(this, userId, phoneNumber);
        PrototypeService service = RetrofitHelper.createPrototypeService();

        long start = System.currentTimeMillis();
        try {
            BaseServerResponse response = service.requestPhoneNumberVerification(request);
        } catch (RetrofitError error) {
            Log.w(TAG, "Could not request phone number verification.");
            RetrofitHelper.handleRetrofitError(error);
        }
        Log.v(TAG, "Phone number verification request completed.  Took: "+(System.currentTimeMillis()- start)+"ms.");
    }
}
