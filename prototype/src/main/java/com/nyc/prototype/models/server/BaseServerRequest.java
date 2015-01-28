package com.nyc.prototype.models.server;

import android.content.Context;

import com.nyc.models.BaseGsonModel;
import com.nyc.prototype.models.DeviceInfo;

/**
 * Created by Kevin on 12/23/2014.
 */
public class BaseServerRequest extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = BaseServerRequest.class.getSimpleName();

    protected DeviceInfo device;
    protected String user;

    public BaseServerRequest() {
        // For JSON
    }

    public BaseServerRequest(Context context) {
        device = new DeviceInfo(context);
    }

    public DeviceInfo getDeviceInfo() {
        return device;
    }

    public String getUserId() {
        return user;
    }
}
