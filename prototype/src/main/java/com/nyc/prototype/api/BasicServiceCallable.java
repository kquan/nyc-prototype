package com.nyc.prototype.api;

import android.content.Context;
import android.util.Log;

import com.nyc.api.HttpResponseCodes;
import com.nyc.models.BaseServerResponse;
import com.nyc.utils.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import retrofit.RetrofitError;

/**
 * Created by Kevin on 1/30/2015.
 *
 * This is a basic callable to make a server call.  It handles checks like
 * making sure there is a network connection, handling status codes etc.
 */
public abstract class BasicServiceCallable<R extends BaseServerResponse> implements Callable<CallResult<R>> {

    @SuppressWarnings("unused")
    private static final String TAG = BasicServiceCallable.class.getSimpleName();

    protected WeakReference<Context> mContextReference;

    public BasicServiceCallable(Context context) {
        mContextReference = new WeakReference<Context>(context);
    }

    /*
     * Implement this with the actual service call
     */
    protected abstract R doServiceCall();

    public CallResult<R> invoke() {
        try {
            return call();
        } catch (Exception e) {
            String message = "Could not perform call";
            Log.w(TAG, message, e);
            return new CallResult(new ServiceCallStatus.ServiceErrorStatus(message, e));
        }
    }

    @Override
    public CallResult<R> call() throws Exception {
        Context context = mContextReference.get();
        if (context == null) {
            String message = "Could not make server call as context no longer exists.";
            Log.d(TAG, message);
            return new CallResult<R>(new ServiceCallStatus.ServiceErrorStatus(message));
        }
        if (!NetworkUtils.hasNetworkConnection(context)) {
            String message = "No network connection available.";
            Log.d(TAG, message);
            return new CallResult<R>(new ServiceCallStatus.NoNetworkErrorResponse(message));
        }

        long now = System.currentTimeMillis();
        try {
            R response = doServiceCall();
            if (response == null) {
                Log.w(TAG, "Server response was empty");
                return new CallResult<R>(new ServiceCallStatus.EmptyServerResponse());
            } else if (!response.isValid()) {
                Log.w(TAG, "Server response does not pass validation");
                return new CallResult<R>(response, new ServiceCallStatus.InvalidServerResponse());
            }
            return new CallResult<R>(response);

        } catch (RetrofitError re) {
            Log.w(TAG, "Could not"+getCallDescription());
            String message = null;
            if (re.getResponse() != null) {

                switch (re.getResponse().getStatus()) {
                    case HttpResponseCodes.OK:
                        // This should never happen if we are in "error"
                        break;
                    case HttpResponseCodes.CREATED: // 201
                        // TODO: Check that this actually occurs, I suspect this is not a RetrofitError
                        message = "Status code 201 (created) returned for "+re.getResponse().getUrl();
                        onCreated(re);
                        break;
                    case HttpResponseCodes.NO_CONTENT: // 204
                        // TODO: Check that this actually occurs, I suspect this is not a RetrofitError
                        message = "Status code 204 (no content) returned for "+re.getResponse().getUrl();
                        onNoContent(re);
                        break;
                    case HttpResponseCodes.BAD_REQUEST: // 400
                        message = "Status code 400 (bad request) returned for "+re.getResponse().getUrl();
                        onBadRequest(re);
                        break;
                    case HttpResponseCodes.UNAUTHORIZED: // 401
                        message = "Status code 401 (unauthorized) returned for "+re.getResponse().getUrl();
                        onUnauthorizedRequest(re);
                        break;
                    case HttpResponseCodes.FORBIDDEN: // 403
                        message = "Status code 203 (forbidden) returned for "+re.getResponse().getUrl();
                        onForbiddenRequest(re);
                        break;
                    case HttpResponseCodes.NOT_FOUND: // 404
                        message = "Status code 404 (not found) returned for "+re.getResponse().getUrl();
                        onResourceNotFound(re);
                        break;
                    case HttpResponseCodes.ERROR: // 500
                        message = "Status code 500 (internal server error) returned for "+re.getResponse().getUrl();
                        onInternalServerError(re);
                        break;
                    default:
                        message = "RETROFIT ERROR for "+re.getResponse().getUrl()+" Status code was "+re.getResponse().getStatus()+" due to "+re.getResponse().getReason();
                        break;

                }

            } else {
                message = "Unexpected RETROFIT error: " + re.toString();
            }
            Log.w(TAG, message);
            return new CallResult<R>(new ServiceCallStatus.ServiceErrorStatus(message));

        } finally {
            Log.v(TAG, getCallDescription() + " completed.  Took: " + (System.currentTimeMillis() - now) + "ms.");
        }
    }

    protected String getCallDescription() {
        return "call to server";
    }

    protected void onCreated(RetrofitError error) {}

    protected void onNoContent(RetrofitError error) {}

    protected void onBadRequest(RetrofitError error) {}

    protected void onUnauthorizedRequest(RetrofitError error) {}

    protected void onForbiddenRequest(RetrofitError error) {}

    protected void onResourceNotFound(RetrofitError error) {}

    protected void onInternalServerError(RetrofitError error) {}
}
