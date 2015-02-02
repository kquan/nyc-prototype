package com.nyc.prototype.user;

import android.content.Context;

import com.nyc.RunnableWithContext;

/**
 * Created by Kevin on 2/2/2015.
 *
 * This clears the local user state when a user logs out
 */
public class ClearUserStateRunnable extends RunnableWithContext {

    @SuppressWarnings("unused")
    private static final String TAG = ClearUserStateRunnable.class.getSimpleName();

    public ClearUserStateRunnable(Context context) {
       super(context);
    }

    public void run(Context context) {
        CurrentUserHelper.clearCurrentUserState(context);
    }

}
