package com.nyc.prototype.user;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.models.BaseServerResponse;
import com.nyc.prototype.api.PrototypeService;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.models.server.VerifyPhoneNumberRequest;
import com.nyc.utils.DeviceUtils;

import retrofit.RetrofitError;

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

        VerifyPhoneNumberRequest request = new VerifyPhoneNumberRequest(this, userId, phoneNumber, verificationCode);
        PrototypeService service = RetrofitHelper.createPrototypeService();

        long start = System.currentTimeMillis();
        try {
            BaseServerResponse response = service.verifyPhoneNumber(request);
        } catch (RetrofitError error) {
            Log.w(TAG, "Could not send phone number verification.");
            RetrofitHelper.handleRetrofitError(error);
        }
        Log.v(TAG, "Phone number verification code submission completed.  Took: "+(System.currentTimeMillis()- start)+"ms.");
    }

}
