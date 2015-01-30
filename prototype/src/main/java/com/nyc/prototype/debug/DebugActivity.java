package com.nyc.prototype.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.nyc.prototype.R;

import butterknife.ButterKnife;

/**
 * Created by Kevin on 1/30/2015.
 */
public class DebugActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = DebugActivity.class.getSimpleName();

    protected ListView mDebugListView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.inject(this);
    }
}
