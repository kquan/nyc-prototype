package com.nyc;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * Created by Kevin on 2/2/2015.
 *
 * Helper pattern to store and retrieve context for callable
 */
public abstract class CallableWithContext<V> implements Callable<V> {

    @SuppressWarnings("unused")
    private static final String TAG = CallableWithContext.class.getSimpleName();

    protected WeakReference<Context> mContextReference;

    public CallableWithContext(Context context) {
        mContextReference = new WeakReference<Context>(context);
    }

    public V call() throws Exception {
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(getClass().getSimpleName(), "Context is not available.  Terminating.");
            return null;
        }
        return call(context);
    }

    protected V call(Context context) throws Exception {
        return null;
    }

}
