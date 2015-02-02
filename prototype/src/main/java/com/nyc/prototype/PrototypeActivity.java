package com.nyc.prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nyc.prototype.debug.DebugActivity;

import butterknife.ButterKnife;

/**
 * Created by Kevin on 1/26/2015.
 */
public class PrototypeActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = PrototypeActivity.class.getSimpleName();



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype);
        ButterKnife.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug_debug_menu:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (BuildConfig.DEBUG) {
            menu.add(0, R.id.debug_debug_menu, 0, R.string.debug_menu_name);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
