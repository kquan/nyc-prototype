package com.nyc.prototype.debug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nyc.prototype.R;
import com.nyc.prototype.gcm.GCM;
import com.nyc.prototype.gcm.UpdateGcmIdOnServerService;

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        items.add(new DebugItem(this, "GCM Actions...") {
            @Override protected void run(final Context context) {
                CharSequence[] options = new CharSequence[4];
                options[0] = "Display GCM ID";
                options[1] = "Send GCM ID up to server";
                options[2] = "Re-register GCM ID";
                options[3] = "Delete GCM ID";
                final String gcmId = GCM.getRegistrationId(context);
                new AlertDialog.Builder(context).setTitle("GCM Actions...").setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        switch (which) {
                            case 0:
                                Log.d(TAG, "Current GCM ID: "+gcmId);
                                Toast.makeText(context, gcmId, Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                if (TextUtils.isEmpty(gcmId)) {
                                    Toast.makeText(context, "Cannot send GCM ID to server as non exists.", Toast.LENGTH_LONG).show();
                                } else {
                                    startService(new Intent(context, UpdateGcmIdOnServerService.class).putExtra(UpdateGcmIdOnServerService.EXTRA_GCM_ID, gcmId));
                                }
                                break;
                            case 2:
                                Log.d(TAG, "Clearing GCM ID");
                                GCM.clearRegistrationId(context);
                                String newGcmId = GCM.ensureGcmIdExists(context);
                                Log.d(TAG, "New GCM ID: "+newGcmId);
                                Toast.makeText(context, "New GCM ID registered: "+newGcmId, Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                GCM.clearRegistrationId(context);
                                Toast.makeText(context, "GCM ID cleared.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }).show();
            }
        });
        mAdapter = new ArrayAdapter<DebugItem>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items.toArray(new DebugItem[0]));
        mDebugListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
