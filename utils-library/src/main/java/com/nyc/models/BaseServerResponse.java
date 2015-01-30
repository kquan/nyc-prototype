package com.nyc.models;

/**
 * Created by Kevin on 12/23/2014.
 */
public class BaseServerResponse extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = BaseServerResponse.class.getSimpleName();

    protected long time;

    public BaseServerResponse() {
        // For GSON
    }

    public long getServerTime() {
        return time;
    }

    @Override public boolean isValid() {
        return time > 0;
    }

}
