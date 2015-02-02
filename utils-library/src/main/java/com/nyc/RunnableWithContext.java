package com.nyc;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Kevin on 2/2/2015.
 *
 * Helper pattern to store and retrieve context for runnable
 */
public abstract class RunnableWithContext implements Runnable {

    @SuppressWarnings("unused")
    private static final String TAG = RunnableWithContext.class.getSimpleName();

    protected WeakReference<Context> mContextReference;

    public RunnableWithContext(Context context) {
        mContextReference = new WeakReference<Context>(context);
    }

    public void run() {
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(getClass().getSimpleName(), "Context is not available.  Terminating.");
            return;
        }
        run(context);
    }

    protected abstract void run(Context context);
}
