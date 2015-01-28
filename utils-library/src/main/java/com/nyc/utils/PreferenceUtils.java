package com.nyc.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenceUtils {

    @SuppressWarnings("unused")
    private static final String TAG = PreferenceUtils.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getMultiProcessAwarePreferences(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null so could not retrieve preferences");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // The name is the same as PreferencesManager.getDefaultSharedPreferencesName(Context) but that is a private method...
            return context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_MULTI_PROCESS);
        } else {
            // In Gingerbread and below, Context.MODE_MULTI_PROCESS is by default on
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getMultiProcessAwarePreferences(Context context, String name) {
        if (context == null) {
            Log.e(TAG, "Context is null so could not retrieve preferences");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // The name is the same as PreferencesManager.getDefaultSharedPreferencesName(Context) but that is a private method...
            return context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS);
        } else {
            // In Gingerbread and below, Context.MODE_MULTI_PROCESS is by default on
            return context.getSharedPreferences(name, 0);
        }
    }
    
}
