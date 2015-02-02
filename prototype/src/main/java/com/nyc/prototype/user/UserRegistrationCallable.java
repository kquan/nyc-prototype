package com.nyc.prototype.user;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.CallableWithContext;
import com.nyc.accounts.SamsungAccountHelper;
import com.nyc.prototype.PrototypeApplication;
import com.nyc.prototype.api.BasicServiceCallable;
import com.nyc.prototype.api.CallResult;
import com.nyc.prototype.api.PrototypeService;
import com.nyc.prototype.api.RetrofitHelper;
import com.nyc.prototype.gcm.GCM;
import com.nyc.prototype.models.AccountInfo;
import com.nyc.prototype.models.DeviceInfo;
import com.nyc.prototype.models.UserInfo;
import com.nyc.prototype.models.server.UserRegistrationRequest;
import com.nyc.prototype.models.server.UserRegistrationResponse;
import com.nyc.utils.NetworkUtils;
import com.nyc.utils.PreferenceUtils;

import java.util.Collection;

/**
 * Created by Kevin on 1/30/2015.
 *
 * This class is intended to perform all the actions required to register a user.
 *
 * It is a runnable so that it can be called within an AsyncTask or Service depending on the UX requirements
 */
public class UserRegistrationCallable extends CallableWithContext<UserRegistrationResult> {

    @SuppressWarnings("unused")
    private static final String TAG = UserRegistrationCallable.class.getSimpleName();

    public UserRegistrationCallable(Context context) {
        super(context);
    }

    @Override
    public UserRegistrationResult call() throws Exception {
        Context context = mContextReference.get();
        if (!checkEnvironment(context)) {
            return UserRegistrationResult.ERROR_VALIDATION;
        }

        String gcmId = GCM.ensureGcmIdExists(context);
        if (TextUtils.isEmpty(gcmId)) {
            Log.w(TAG, "Could not register for GCM ID");
            // This is not an error, we will update the server the next time once we
            // register the GCM ID.
        }

        String samsungAccountEmail = SamsungAccountHelper.getFirstSamsungAccountEmail(context);
        String displayName = UserRegistrationHelper.getDisplayName(context);
        // This is local to the device
        Uri profilePhotoUri = UserRegistrationHelper.getProfileUri(context);
        Collection<AccountInfo> accountInfo = UserRegistrationHelper.getAllAccountInfo(context);
        UserInfo userInfo = new UserInfo()
                                .addDeviceInfo(new DeviceInfo(context))
                                .addAccountEmails(accountInfo)
                                .setDisplayName(displayName)
                                // TODO: How to add profile photo?
                                ;
        final UserRegistrationRequest request = new UserRegistrationRequest(context, samsungAccountEmail, userInfo);

        PrototypeService service = RetrofitHelper.createPrototypeService();
        if (!NetworkUtils.hasNetworkConnection(context)) {
            Log.w(TAG, "No network connection to register user");
            return UserRegistrationResult.ERROR_NO_NETWORK;
        }

        CallResult<UserRegistrationResponse> response = new BasicServiceCallable<UserRegistrationResponse>(context) {
            @Override protected UserRegistrationResponse doServiceCall() {
                return RetrofitHelper.createPrototypeService().registerUser(request);
            }

            @Override protected String getCallDescription() {
                return "register user";
            }
        }.invoke();

        if (!response.isOk()) {
            return UserRegistrationResult.ERROR_SERVER;
        }
        /*
        TODO: This needs to change as we need to verify phone number before we have the user ID
        String userId = response.getServerResponse().getUserId();
        if (TextUtils.isEmpty(userId)) {
            Log.e(TAG, "Server response did not contain user ID");
            return UserRegistrationResult.ERROR_SERVER;
        }

        CurrentUserHelper.saveCurrentUserId(context, userId);
        Log.d(TAG, "Saved new user ID: "+userId);
        */
        PreferenceUtils.getMultiProcessAwarePreferences(context).edit().putBoolean(PrototypeApplication.Preferences.STATE_WAITING_FOR_VERIFICATION, true).apply();
        return UserRegistrationResult.OK;
    }

    protected boolean checkEnvironment(Context context) {
        if (context == null) {
            Log.w(TAG, "Could not register user as no context exists.");
            return false;
        }
        String currentUser = CurrentUserHelper.getCurrentUserId(context);
        if (!TextUtils.isEmpty(currentUser)) {
            Log.e(TAG, "Not registering user as a user ID already exists: "+currentUser);
            return false;
        }
        return true;
    }
}
