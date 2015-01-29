package com.nyc.prototype;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyc.accounts.SamsungAccountHelper;
import com.nyc.prototype.models.AccountInfo;
import com.nyc.prototype.user.UserRegistrationHelper;
import com.nyc.utils.AccountUtils;

import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kevin on 1/26/2015.
 */
public class PrototypeActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = PrototypeActivity.class.getSimpleName();

    @InjectView(R.id.name) protected TextView mDisplayName;
    @InjectView(R.id.profile_photo) protected ImageView mProfilePhoto;
    @InjectView(R.id.email) protected TextView mEmail;
    @InjectView(R.id.accounts) protected ListView mAccountList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype);
        ButterKnife.inject(this);

        String samsungAccount = SamsungAccountHelper.getFirstSamsungAccountEmail(this);
        Toast.makeText(this, TextUtils.isEmpty(samsungAccount) ? "No Samsung account" : "Samsung account: "+samsungAccount, Toast.LENGTH_LONG).show();

        AccountUtils.debug_dumpProfile(this);
        mDisplayName.setText(UserRegistrationHelper.getDisplayName(this));
        Collection<String> emails = UserRegistrationHelper.getProfileEmails(this);
        if (emails.isEmpty()) {
            mEmail.setText("No email found");
        } else {
            mEmail.setText("Primary email: "+emails.iterator().next()+" (found "+emails.size()+" emails.)");
        }
        mProfilePhoto.setImageURI(UserRegistrationHelper.getProfileUri(this));

        Collection<AccountInfo> accountInfos = UserRegistrationHelper.getAllAccountInfo(this);
        mAccountList.setAdapter(new ArrayAdapter<AccountInfo>(this, android.R.layout.simple_list_item_1, android.R.id.text1, accountInfos.toArray(new AccountInfo[0])));
    }
}
