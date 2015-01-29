package com.nyc.prototype.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nyc.prototype.PrototypeApplication;
import com.nyc.utils.DateUtils;
import com.nyc.utils.DeviceUtils;

import java.io.IOException;
import java.util.Date;


/**
 * Constants related to Google Cloud Messaging
 * @author Kevin (kevin.quan@samsung.com)
 *
 */
public class GCM {
    
    private static final String TAG = GCM.class.getSimpleName();

    protected static final String PREFS_GCM_ID = "GcmRegistrationId";
    protected static final String PREFS_GCM_APP_VERSION = "GcmIdAppVersion";
    protected static final String PREFS_GCM_SERVER_EXPIRATION_TIME = "GcmServerExpirationTime"; // In millis
    
    /**
     * Default lifespan (1 day) of a registration until it is considered expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME = DateUtils.ONE_DAY;

    /**
     * The different push types from the server
     * @author Kevin (kevin.quan@samsung.com)
     *
     */
    public interface PushTypes {

        // Miscellaneous
        public static final String PROMOTIONAL_MESSAGE = "PROMO_MESSAGE";
        public static final String UPGRADE_NOTIFICATION = "UPGRADE_AVAILABLE";
    }
    
    /**
     * Other extra fields in the push
     * @author Kevin (kevin.quan@samsung.com)
     *
     */
    public interface Extras {

    }
    
    public static SharedPreferences getGcmPreferences(Context context) {
        if (context == null) {
            Log.w(TAG, "No context provided to retrieve GCM preferences file");
            return null;
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static String getRegistrationId(Context context) {
        if (context == null) return null;
        SharedPreferences prefs = GCM.getGcmPreferences(context);
        String registrationId = prefs.getString(PREFS_GCM_ID, null);
        if (TextUtils.isEmpty(registrationId)) {
            Log.v(TAG, "Registration not found.");
            return null;
        }
        // check if app was updated; if so, it must clear registration id to
        // avoid a race condition if GCM sends a message
        int registeredVersion = prefs.getInt(PREFS_GCM_APP_VERSION, 0);
        int currentVersion = DeviceUtils.getVersionCode(context);
        if (registeredVersion != currentVersion || isRegistrationExpired(context)) {
            Log.v(TAG, "App version changed or registration expired.");
            return null;
        }
        return registrationId;
    }
    
    /**
     * Checks if the registration has expired.
     *
     * <p>To avoid the scenario where the device sends the registration to the
     * server but the server loses it, the app developer may choose to re-register
     * after REGISTRATION_EXPIRY_TIME_MS.
     *
     * @return true if the registration has expired.
     */
    private static boolean isRegistrationExpired(Context context) {
        SharedPreferences prefs = GCM.getGcmPreferences(context);
        long expirationTime = prefs.getLong(PREFS_GCM_SERVER_EXPIRATION_TIME, 0);
        return System.currentTimeMillis() > expirationTime;
    }
    
    public static boolean clearRegistrationId(Context context) {
        if (context == null) return false;
        SharedPreferences.Editor editor = GCM.getGcmPreferences(context).edit();
        editor.remove(PREFS_GCM_ID);
        editor.remove(PREFS_GCM_APP_VERSION);
        editor.remove(PREFS_GCM_SERVER_EXPIRATION_TIME);
        editor.apply();
        Log.v(TAG, "Cleared GCM Registration IDs");
        return true;
    }
    
    public static boolean saveRegistrationId(Context context, String registrationId) {
        if (context == null || TextUtils.isEmpty(registrationId)) {
            Log.w(TAG, "Could not store registration ID: "+registrationId);
            return false;
        }
        SharedPreferences.Editor editor = GCM.getGcmPreferences(context).edit();
        editor.putString(PREFS_GCM_ID, registrationId);
        int appVersion = DeviceUtils.getVersionCode(context);
        editor.putInt(PREFS_GCM_APP_VERSION, appVersion);
        long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME;
        editor.putLong(PREFS_GCM_SERVER_EXPIRATION_TIME, expirationTime);
        editor.apply();
        Log.v(TAG, "Saving GCM ID for app version "+appVersion+" with expiration "+new Date(expirationTime));
        return true;
    }

    /*
     * This method will check:
     * 1) Whether there is a GCM ID for this device
     * 2) Whether that GCM ID is expired
     *
     * If one of the above is true, it will register a new GCM ID and start a service
     * to send the new GCM ID to the server.
     *
     * Sending to the server will only succeed if there is a user ID.
     *
     * @return The GCM ID, whether it's an existing or new one
     */
    public static String ensureGcmIdExists(Context context) {
        if (context == null) {
            Log.w(TAG, "Could not check GCM ID as context does not exist");
            return null;
        }
        String currentId = getRegistrationId(context);
        if (!TextUtils.isEmpty(currentId)) {
            return currentId;
        }

        Log.d(TAG, "No GCM ID exists, registering a new one");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String newRegistrationId = null;
        try {
            newRegistrationId = gcm.register(PrototypeApplication.GCM_SENDER_ID);
        } catch (IOException ioe) {
            Log.e(TAG, "Could not call GCM to register.");
            return null;
        }
        if (TextUtils.isEmpty(newRegistrationId)) {
            Log.e(TAG, "GCM returned an empty id");
            return null;
        }
    }
}
