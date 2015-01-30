package com.nyc.prototype.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.models.BaseServerResponse;
import com.nyc.prototype.api.BasicServiceCallable;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.models.server.UpdateGcmIdRequest;
import com.nyc.prototype.user.CurrentUserHelper;

/**
 * Created by Kevin on 1/29/2015.
 */
public class UpdateGcmIdOnServerService extends IntentService {

    @SuppressWarnings("unused")
    private static final String TAG = UpdateGcmIdOnServerService.class.getSimpleName();

    public static final String EXTRA_GCM_ID = "ExtraGcmId";

    public UpdateGcmIdOnServerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String gcmId = intent != null ? intent.getStringExtra(EXTRA_GCM_ID) : null;
        if (TextUtils.isEmpty(gcmId)) {
            Log.w(TAG, "No GCM ID to update");
            return;
        }
        String user = CurrentUserHelper.getCurrentUserId(this);
        if (TextUtils.isEmpty(user)) {
            Log.w(TAG, "No user to update");
            return;
        }

        final UpdateGcmIdRequest request = new UpdateGcmIdRequest(this, user, gcmId);
        new BasicServiceCallable<BaseServerResponse>(this) {
            @Override protected BaseServerResponse doServiceCall() {
                return RetrofitHelper.createPrototypeService().updateGcmId(request);
            }

            @Override protected String getCallDescription() {
                return "update GCM ID";
            }
        }.invoke();
    }
}
