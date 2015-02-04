package com.nyc.prototype;

import android.app.Application;
import android.os.StrictMode;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Kevin on 1/29/2015.
 */
public class Prototype extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = Prototype.class.getSimpleName();

    public static final String GCM_SENDER_ID = "add_this_in_later";

    public interface Preferences {
        public static final String STATE_WAITING_FOR_VERIFICATION = "StateWaitingForVerification";
    }

    public interface Broadcasts {
        public static final String USER_REGISTRATION_COMPLETED = "com.nyc.prototype.user.REGISTRATION_COMPLETE";
        public static final String USER_REGISTRATION_FAILED = "com.nyc.prototype.user.REGISTRATION_FAILED";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        }
    }
}
