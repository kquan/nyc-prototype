package com.nyc.prototype.models.server;

import com.nyc.models.BaseServerResponse;

/**
 * Created by Kevin on 2/2/2015.
 */
public class VerifyPhoneNumberResponse extends BaseServerResponse {

    @SuppressWarnings("unused")
    private static final String TAG = VerifyPhoneNumberResponse.class.getSimpleName();

    protected String user;

    public VerifyPhoneNumberResponse() {
        // For GSON
    }

    public String getUserId() {
        return user;
    }

}
