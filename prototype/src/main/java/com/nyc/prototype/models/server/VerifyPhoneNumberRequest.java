package com.nyc.prototype.models.server;

import android.text.TextUtils;

/**
 * Created by Kevin on 1/28/2015.
 */
public class VerifyPhoneNumberRequest extends BaseServerRequest {

    @SuppressWarnings("unused")
    private static final String TAG = VerifyPhoneNumberRequest.class.getSimpleName();

    protected String user;
    protected String phone;
    protected String code;

    public VerifyPhoneNumberRequest() {
        // For GSON
    }

    public String getUserId() {
        return user;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getVerificationCode() {
        return code;
    }

    @Override public boolean isValid() {
        return !TextUtils.isEmpty(user) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code);
    }


}
