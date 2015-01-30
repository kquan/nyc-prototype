package com.nyc.prototype.api;

import android.text.TextUtils;

import com.nyc.models.BaseServerResponse;

/**
 * Created by Kevin on 1/30/2015.
 */
public class ServiceCallStatus extends BaseServerResponse {

    @SuppressWarnings("unused")
    private static final String TAG = ServiceCallStatus.class.getSimpleName();

    public ServiceCallStatus() {

    }

    public static class ServiceCallOk extends ServiceCallStatus {}

    public static class ServiceErrorStatus extends ServiceCallStatus {

        protected String mMessage;
        protected Throwable mThrowable;

        public ServiceErrorStatus() {
            super();
        }

        public ServiceErrorStatus(String message) {
            mMessage = message;
        }

        public ServiceErrorStatus(Throwable t) {
            mThrowable = t;
            mMessage = t.getMessage();
        }

        public ServiceErrorStatus(String message, Throwable t) {
            mThrowable = t;
            mMessage = !TextUtils.isEmpty(message) ? message : t.getMessage();
        }

        public String getMessage() {
            return mMessage;
        }

        public Throwable getThrowable() {
            return mThrowable;
        }
    }

    public static class NoNetworkErrorResponse extends ServiceErrorStatus {
        public NoNetworkErrorResponse() {
            super("No network connection available");
        }

        public NoNetworkErrorResponse(String message) {
            super(message);
        }
    }

    public static class EmptyServerResponse extends ServiceErrorStatus {
        public EmptyServerResponse() {
            super("Response from server was empty");
        }

        public EmptyServerResponse(String message) {
            super(message);
        }
    }

    public static class InvalidServerResponse extends ServiceErrorStatus {

        public InvalidServerResponse() {
            super("Server response does not pass validation");
        }

        public InvalidServerResponse(String message) {
            super(message);
        }
    }
}
