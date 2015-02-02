package com.nyc.prototype.user;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.prototype.api.BasicServiceCallable;
import com.nyc.prototype.api.CallResult;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.models.server.VerifyPhoneNumberRequest;
import com.nyc.prototype.models.server.VerifyPhoneNumberResponse;
import com.nyc.utils.DeviceUtils;

/**
 * Created by Kevin on 1/29/2015.
 *
 * Sends a verification code to the server for the phone number on the device, to verify the user owns this phone number
 */
public class VerifyPhoneNumberService extends IntentService {

    @SuppressWarnings("unused")
    private static final String TAG = VerifyPhoneNumberService.class.getSimpleName();

    public static final String EXTRA_VERIFICATION_CODE = "ExtraVerificationCode";

    public VerifyPhoneNumberService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        try {
            doVerification(intent);
        } finally {
            if (VerifyPhoneNumberSMSReceiver.completeWakefulIntent(intent)) {
                Log.d(TAG, "SMS receiver wakelock completed.");
            }
        }
    }

    protected void doVerification(final Intent intent) {
        String verificationCode = intent != null ? intent.getStringExtra(EXTRA_VERIFICATION_CODE) : null;
        if (TextUtils.isEmpty(verificationCode)) {
            Log.w(TAG, "No verification code provided.");
            return;
        }
        String userId = CurrentUserHelper.getCurrentUserId(this);
        if (TextUtils.isEmpty(userId)) {
            Log.w(TAG, "No current user ID");
            return;
        }
        String phoneNumber = DeviceUtils.getFirstPhoneNumber(this);
        if (TextUtils.isEmpty(phoneNumber)) {
            Log.w(TAG, "No phone number to verify");
            return;
        }
        Log.d(TAG, "Verifying "+phoneNumber+" for "+userId+" with "+verificationCode);

        final VerifyPhoneNumberRequest request = new VerifyPhoneNumberRequest(this, userId, phoneNumber, verificationCode);
        CallResult<VerifyPhoneNumberResponse> result = new BasicServiceCallable<VerifyPhoneNumberResponse>(this) {
            @Override protected VerifyPhoneNumberResponse doServiceCall() {
                return RetrofitHelper.createPrototypeService().verifyPhoneNumber(request);
            }

            @Override protected String getCallDescription() {
                return "send phone number verification";
            }
        }.invoke();

        if (result.isOk()) {
            VerifyPhoneNumberResponse response = result.getServerResponse();
            if (!TextUtils.isEmpty(response.getUserId())) {
                Log.d(TAG, "Saving current user ID: "+response.getUserId());
                CurrentUserHelper.saveCurrentUserId(this, response.getUserId(), phoneNumber, DeviceUtils.getDeviceId(this));
            } else {
                Log.d(TAG, "Verified new channel for: "+phoneNumber);
                CurrentUserHelper.updateChannel(this, phoneNumber, DeviceUtils.getDeviceId(this));
            }
        }
    }

}
