package com.nyc.prototype.models.server;

import com.nyc.models.BaseServerResponse;

/**
 * Created by Kevin on 1/30/2015.
 */
public class UserRegistrationResponse extends BaseServerResponse {

    @SuppressWarnings("unused")
    private static final String TAG = UserRegistrationResponse.class.getSimpleName();

    protected String user;

    public UserRegistrationResponse() {
        // For GSON
    }

    public String getUserId() {
        return user;
    }

}
