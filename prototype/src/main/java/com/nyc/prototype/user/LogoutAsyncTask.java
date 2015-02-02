package com.nyc.prototype.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.AsyncTaskWithContext;
import com.nyc.models.BaseServerResponse;
import com.nyc.prototype.R;
import com.nyc.prototype.api.BasicServiceCallable;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.models.server.BaseServerRequest;

/**
 * Created by Kevin on 2/2/2015.
 */
public class LogoutAsyncTask extends AsyncTaskWithContext<Void, Void, Void> {

    @SuppressWarnings("unused")
    private static final String TAG = LogoutAsyncTask.class.getSimpleName();

    protected ProgressDialog mProgressDialog;

    public LogoutAsyncTask(Context context) {
        super(context);
    }

    @Override protected void onPreExecute(final Context context) {
        mProgressDialog = ProgressDialog.show(context, null, getString(R.string.account_logoff_status));
    }

    @Override protected Void doInBackground(final Context context, final Void... params) {
        String currentId = CurrentUserHelper.getCurrentUserId(context);
        if (TextUtils.isEmpty(currentId)) {
            Log.i(TAG, "No logged in user to logout.");
            // Clear local state just in case.
            new ClearUserStateRunnable(context).run();
            return null;
        }

        // Logout from server
        final BaseServerRequest request = new BaseServerRequest(context);
        new BasicServiceCallable<BaseServerResponse>(context) {
            @Override protected BaseServerResponse doServiceCall() {
                return RetrofitHelper.createPrototypeService().logout(request);
            }

            @Override protected String getCallDescription() {
                return "logout";
            }
        }.invoke();

        Log.d(TAG, "Clearing local logged in state");
        new ClearUserStateRunnable(context).run();
        return null;
    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (IllegalArgumentException iae) {
                Log.w(TAG, "Could not close dialog", iae);
            }
        }
    }
}
