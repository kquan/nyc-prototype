package com.nyc.prototype.debug;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nyc.prototype.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kevin on 1/30/2015.
 */
public class DebugActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = DebugActivity.class.getSimpleName();

    protected static final DebugItem EMPTY_ROW = new DebugItem(null, "") { @Override protected void run(final Context context) {} };

    @InjectView(R.id.debug_items) protected ListView mDebugListView;
    protected ArrayAdapter<DebugItem> mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.inject(this);

        mDebugListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                mAdapter.getItem(position).run();
            }
        });
        List<DebugItem> items = new ArrayList<DebugItem>();

        items.add(new DebugItem(this, "See Owner Profile") {
            @Override protected void run(final Context context) {
                DebugDeviceProfileDisplayFragment.newInstance().show(getSupportFragmentManager(), "OWNER_PROFILE");
            }
        });
        items.add(EMPTY_ROW);
        items.add(new DebugItem(this, "Display GCM Info") {
            @Override protected void run(final Context context) {

            }
        });
        mAdapter = new ArrayAdapter<DebugItem>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items.toArray(new DebugItem[0]));
        mDebugListView.setAdapter(mAdapter);
    }
}
