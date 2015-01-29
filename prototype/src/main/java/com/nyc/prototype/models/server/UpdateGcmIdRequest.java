package com.nyc.prototype.models.server;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Kevin on 1/29/2015.
 */
public class UpdateGcmIdRequest extends BaseServerRequest {

    @SuppressWarnings("unused")
    private static final String TAG = UpdateGcmIdRequest.class.getSimpleName();

    protected String user;
    protected String gcmId;

    public UpdateGcmIdRequest() {
        // For GSON
    }

    public UpdateGcmIdRequest(Context context, String user, String gcmId) {
        super(context);
        this.user = user;
        this.gcmId = gcmId;
    }

    @Override public boolean isValid() {
        return !TextUtils.isEmpty(user) && !TextUtils.isEmpty(gcmId);
    }

}
