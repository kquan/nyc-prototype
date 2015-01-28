package com.nyc.prototype.models;

import android.content.Context;
import android.text.TextUtils;

import com.nyc.models.BaseGsonModel;
import com.nyc.utils.DeviceUtils;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Kevin on 1/5/2015.
 */
public class DeviceInfo extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = DeviceInfo.class.getSimpleName();

    protected String phone;
    protected String id;

    protected String tz;
    protected String locale;

    public DeviceInfo() {
        // For GSON
    }

    public DeviceInfo(Context context) {
        id = DeviceUtils.getDeviceId(context);
        phone = DeviceUtils.getFirstPhoneNumber(context);
        tz = TimeZone.getDefault().getID();
        locale = Locale.getDefault().getDisplayName();
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getDeviceId() {
        return id;
    }

    public String getTimezone() {
        return tz;
    }

    public String getLocale() {
        return locale;
    }

    @Override public boolean isValid() {
        return !TextUtils.isEmpty(tz) && !TextUtils.isEmpty(locale);
    }
}
