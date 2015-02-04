package com.nyc.prototype.api;

import com.nyc.models.BaseServerResponse;
import com.nyc.prototype.BuildConfig;
import com.nyc.prototype.models.server.BaseServerRequest;
import com.nyc.prototype.models.server.StartPhoneNumberVerificationRequest;
import com.nyc.prototype.models.server.UpdateGcmIdRequest;
import com.nyc.prototype.models.server.UserRegistrationRequest;
import com.nyc.prototype.models.server.UserRegistrationResponse;
import com.nyc.prototype.models.server.VerifyPhoneNumberRequest;
import com.nyc.prototype.models.server.VerifyPhoneNumberResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Kevin on 1/29/2015.
 */
public interface PrototypeService {

    // TBA: Update url
    public static final String ENDPOINT_URL = BuildConfig.PROTOTYPE_SERVICE_ENDPOINT;

    @GET("/account/verifyPhone")
    BaseServerResponse requestPhoneNumberVerification(@Body StartPhoneNumberVerificationRequest request);

    @POST("/account/verifyPhone")
    VerifyPhoneNumberResponse verifyPhoneNumber(@Body VerifyPhoneNumberRequest request);

    @POST("/account/updateGcm")
    BaseServerResponse updateGcmId(@Body UpdateGcmIdRequest request);

    @POST("/account/register")
    UserRegistrationResponse registerUser(@Body UserRegistrationRequest request);

    @POST("/account/logout")
    BaseServerResponse logout(@Body BaseServerRequest request);
}
