package com.nyc.prototype.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.utils.PreferenceUtils;

public class CurrentUserHelper {

    private static final String TAG = CurrentUserHelper.class.getSimpleName();
    
    // This is the file name that stores the logged in ID
    protected static final String USER_ID_PREFERENCE_FILE = "CurrentUser";
    protected static final String USER_ID_PREFERENCE_KEY = "StateCurrentId";
    
    public static String getCurrentUserId(Context context) {
        if (context == null) {
            Log.w(TAG, "Not retrieving current user ID as context is null");
            // Don't crash caller
            return new String();
            
        }
        SharedPreferences prefs = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_ID_PREFERENCE_FILE);
        return prefs.getString(CurrentUserHelper.USER_ID_PREFERENCE_KEY, null);
    }
    
    public static boolean saveCurrentUserId(Context context, String userId) {
        if (context == null) {
            Log.w(TAG, "No context available to set current user id.");
            return false;
        }
        if (TextUtils.isEmpty(userId)) {
            Log.w(TAG, "Not setting current user Id as value is null");
            return false;
        }
        SharedPreferences.Editor editor = PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_ID_PREFERENCE_FILE).edit();
        editor.putString(CurrentUserHelper.USER_ID_PREFERENCE_KEY, userId);
        editor.apply();
        return true;
    }
    
    public static boolean clearCurrentUserState(Context context) {
        if (context == null) {
            Log.w(TAG, "No context available to clear current user state.");
            return false;
        }
        PreferenceUtils.getMultiProcessAwarePreferences(context, CurrentUserHelper.USER_ID_PREFERENCE_FILE).edit().clear().apply();
        return true;
    }
    
    /**
     * Quick check to see whether we have logged in or not, by checking whether the user state has been stored in the preference.
     * @param context
     * @return
     */
    public static boolean hasLoggedIn(Context context) {
        return !TextUtils.isEmpty(getCurrentUserId(context));
    }

}
