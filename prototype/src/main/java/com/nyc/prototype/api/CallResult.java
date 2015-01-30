package com.nyc.prototype.api;

import com.nyc.models.BaseServerResponse;

/**
 * Created by Kevin on 1/30/2015.
 */
public class CallResult<R extends BaseServerResponse> {

    @SuppressWarnings("unused")
    private static final String TAG = CallResult.class.getSimpleName();


    protected R mServerResponse;
    protected ServiceCallStatus mStatus;

    public CallResult(R response) {
        mServerResponse = response;
        mStatus = new ServiceCallStatus.ServiceCallOk();
    }

    public CallResult(R response, ServiceCallStatus status) {
        mServerResponse = response;
        mStatus = status;
    }

    public CallResult(ServiceCallStatus status) {
        mStatus = status;
    }

    public R getServerResponse() {
        return mServerResponse;
    }

    public ServiceCallStatus getStatus() {
        return mStatus;
    }

    public boolean isOk() {
        return mServerResponse != null && mStatus instanceof ServiceCallStatus.ServiceCallOk;
    }
}
