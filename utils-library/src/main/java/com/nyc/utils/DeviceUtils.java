package com.nyc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

public class DeviceUtils {
    
    private static final String TAG = DeviceUtils.class.getSimpleName();
    
    public static final float BATTERY_LEVEL_UNKNOWN = -1;

    public static float getCurrentBatteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            return level / (float)scale;
        }
        return BATTERY_LEVEL_UNKNOWN;
    }
    
    public static int convertDpToPixels(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 0.5 is added to allow proper rounding when truncating
        return (int)((dp * displayMetrics.density) + 0.5);
    }
    
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            Log.w("Could not get package name: ", e);
        } catch (RuntimeException re) {
            Log.w(TAG, "Could not get app version", re);
        }
        return -1;
    }
    
    public static String getDeviceId(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e(TAG, "Could not get device id",e);
            return null;
        }
    }
    
    public static int getStatusBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        Rect windowDisplayFrame = new Rect();
        Window window = activity.getWindow();
        if (window == null) {
            return 0;
        }
        window.getDecorView().getWindowVisibleDisplayFrame(windowDisplayFrame);
        return windowDisplayFrame.top;
    }
}
