package com.nyc.prototype.models.server;

import android.content.Context;

import com.nyc.prototype.models.UserInfo;

/**
 * Created by Kevin on 1/28/2015.
 */
public class UserRegistrationRequest extends BaseServerRequest {

    @SuppressWarnings("unused")
    private static final String TAG = UserRegistrationRequest.class.getSimpleName();

    protected String samsungAccount;
    protected UserInfo user;

    public UserRegistrationRequest() {
        // For GSON
    }

    public UserRegistrationRequest(Context context, String samsungAccountEmail, UserInfo userInfo) {
        super(context);
        samsungAccount = samsungAccountEmail;
        user = userInfo;
    }

    public UserInfo getUserInfo() {
        return user;
    }

    public String getSamsungAccountEmail() {
        return samsungAccount;
    }

    @Override public boolean isValid() {
        // When registering, there MUST be device information for the user
        return user != null && user.getFirstDevice() != null;
    }
}
