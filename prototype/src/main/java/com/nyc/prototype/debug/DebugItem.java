package com.nyc.prototype.debug;

import android.content.Context;

import com.nyc.RunnableWithContext;

/**
 * Created by Kevin on 2/2/2015.
 */
public abstract class DebugItem extends RunnableWithContext{

    @SuppressWarnings("unused")
    private static final String TAG = DebugItem.class.getSimpleName();

    protected String mTitle;

    public DebugItem(Context context, String title) {
        super(context);
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override public String toString() {
        return getTitle();
    }

}
