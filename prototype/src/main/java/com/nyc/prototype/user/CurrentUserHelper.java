package com.nyc.prototype.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.prototype.PrototypeApplication;
import com.nyc.utils.DeviceUtils;
import com.nyc.utils.PreferenceUtils;

public class CurrentUserHelper {

    private static final String TAG = CurrentUserHelper.class.getSimpleName();
    
    // This is the file name that stores the logged in ID
    protected static final String USER_STATE_PREFERENCE_FILE = "CurrentUser";
    protected static final String USER_ID_PREFERENCE_KEY = "StateCurrentId";
    protected static final String LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY = "StateLastVerifiedPhone";
    protected static final String LAST_VERIFIED_DEVICE_PREFERENCE_KEY = "StateLastVerifiedDevice";
    
    public static String getCurrentUserId(Context context) {
        if (context == null) {
            Log.w(TAG, "Not retrieving current user ID as context is null");
            // Don't crash caller
            return new String();
            
        }
        SharedPreferences prefs = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE);
        return prefs.getString(CurrentUserHelper.USER_ID_PREFERENCE_KEY, null);
    }
    
    public static boolean saveCurrentUserId(Context context, String userId, String phoneNumber, String deviceId) {
        if (context == null) {
            Log.w(TAG, "No context available to set current user id.");
            return false;
        }
        if (TextUtils.isEmpty(userId)) {
            Log.w(TAG, "Not setting current user Id as value is null");
            return false;
        }
        Log.d(TAG, "Saved current user ID and channel");
        SharedPreferences.Editor editor = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE).edit();
        editor.putString(CurrentUserHelper.USER_ID_PREFERENCE_KEY, userId);
        editor.putString(CurrentUserHelper.LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY, phoneNumber);
        editor.putString(CurrentUserHelper.LAST_VERIFIED_DEVICE_PREFERENCE_KEY, deviceId);
        editor.apply();
        return true;
    }

    public static boolean updateChannel(Context context, String phoneNumber, String deviceId) {
        if (context == null) {
            Log.w(TAG, "No context available to set channel");
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Log.w(TAG, "No phone number provided");
            return false;
        }
        Log.d(TAG, "Saved new channel");
        SharedPreferences.Editor editor = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE).edit();
        editor.putString(CurrentUserHelper.LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY, phoneNumber);
        editor.putString(CurrentUserHelper.LAST_VERIFIED_DEVICE_PREFERENCE_KEY, deviceId);
        editor.apply();
        PreferenceUtils.getMultiProcessAwarePreferences(context).edit().remove(PrototypeApplication.Preferences.STATE_WAITING_FOR_VERIFICATION).apply();
        return true;
    }

    public static void removeActiveChannel(Context context) {
        if (context == null) {
            Log.w(TAG, "No context available to remove channel");
            return;
        }
        Log.d(TAG, "Removing active channel");
        PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE)
                .edit()
                .remove(LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY)
                .remove(LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY)
                .apply();
    }

    public static boolean hasActiveChannel(Context context) {
        SharedPreferences prefs = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE);
        return !TextUtils.isEmpty(prefs.getString(CurrentUserHelper.LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY, null)) && !TextUtils.isEmpty(prefs.getString(CurrentUserHelper.LAST_VERIFIED_DEVICE_PREFERENCE_KEY, null));
    }

    public static boolean isActiveChannelCorrect(Context context) {
        SharedPreferences prefs = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE);
        String currentPhoneNumber = DeviceUtils.getFirstPhoneNumber(context);
        String currentDeviceId = DeviceUtils.getDeviceId(context);
        String channelPhoneNumber = prefs.getString(CurrentUserHelper.LAST_VERIFIED_PHONE_NUMBER_PREFERENCE_KEY, null);
        String channelDeviceId = prefs.getString(CurrentUserHelper.LAST_VERIFIED_DEVICE_PREFERENCE_KEY, null);
        return TextUtils.equals(currentPhoneNumber, channelPhoneNumber) && TextUtils.equals(currentDeviceId, channelDeviceId);
    }
    
    public static boolean clearCurrentUserState(Context context) {
        if (context == null) {
            Log.w(TAG, "No context available to clear current user state.");
            return false;
        }
        PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_STATE_PREFERENCE_FILE).edit().clear().apply();
        return true;
    }

    public static boolean hasUserId(Context context) {
        return !TextUtils.isEmpty(getCurrentUserId(context));
    }
    
    /**
     * Quick check to see whether we have logged in or not, by checking whether the user state has been stored in the preference.
     * @param context
     * @return
     */
    public static boolean hasLoggedIn(Context context) {
        return hasUserId(context) && hasActiveChannel(context);
    }

}
