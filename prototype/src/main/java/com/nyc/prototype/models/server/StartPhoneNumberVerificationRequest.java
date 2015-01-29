package com.nyc.prototype.models.server;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Kevin on 1/28/2015.
 */
public class StartPhoneNumberVerificationRequest extends BaseServerRequest {

    @SuppressWarnings("unused")
    private static final String TAG = StartPhoneNumberVerificationRequest.class.getSimpleName();

    protected String user;
    protected String phone;

    public StartPhoneNumberVerificationRequest() {
        // For GSON
    }

    public StartPhoneNumberVerificationRequest(Context context, String userId, String phoneNumber) {
        super(context);
        user = userId;
        phone = phoneNumber;
    }

    public String getUserId() {
        return user;
    }

    public String getPhoneNumber() {
        return phone;
    }

    @Override public boolean isValid() {
        return !TextUtils.isEmpty(user) && !TextUtils.isEmpty(phone);
    }


}
