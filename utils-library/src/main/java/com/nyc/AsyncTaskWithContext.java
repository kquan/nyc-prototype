package com.nyc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Kevin on 2/2/2015.
 */
public abstract class AsyncTaskWithContext<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @SuppressWarnings("unused")
    private static final String TAG = AsyncTaskWithContext.class.getSimpleName();

    protected WeakReference<Context> mContextReference;

    public AsyncTaskWithContext(Context context) {
        mContextReference = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(getClass().getSimpleName(), "Context is not available on pre execute.  Terminating.");
            return;
        }
        onPreExecute(context);
    }

    /* You may want to handle result even if there is no context
    @Override
    protected void onPostExecute(final Result result) {
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(getClass().getSimpleName(), "Context is not available on post execute.  Terminating.");
            return;
        }
        onPostExecute(context, result);
    }
    */

    @Override
    protected Result doInBackground(final Params... params) {
        Context context = mContextReference.get();
        if (context == null) {
            Log.w(getClass().getSimpleName(), "Context is not available while doing in background.  Terminating.");
            return null;
        }
        return doInBackground(context, params);
    }

    protected void onPreExecute(Context context) {}

    //protected void onPostExecute(final Context context, final Result result) {}

    protected abstract Result doInBackground(final Context context, final Params... params);

    protected String getString(int stringId) {
        Context context = mContextReference.get();
        if (context != null) {
            context.getString(stringId);
        }
        return null;
    }
}
