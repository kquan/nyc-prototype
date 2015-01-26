package com.nyc.prototype;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.nyc.accounts.SamsungAccountHelper;

import butterknife.InjectView;

/**
 * Created by Kevin on 1/26/2015.
 */
public class PrototypeActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = PrototypeActivity.class.getSimpleName();

    @InjectView(R.id.butterknife_test) protected TextView mFontTest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype);

        String samsungAccount = SamsungAccountHelper.getFirstSamsungAccountEmail(this);
        Toast.makeText(this, TextUtils.isEmpty(samsungAccount) ? "No Samsung account" : "Samsung account: "+samsungAccount, Toast.LENGTH_LONG).show();
    }
}
