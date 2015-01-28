package com.nyc.prototype.models;

import android.accounts.Account;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.nyc.models.BaseGsonModel;

/**
 * Created by Kevin on 1/28/2015.
 * This stores information from accounts that are retrieved from the device, such as
 * Google accounts, Dropbox accounts, etc
 */
public class AccountInfo extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = AccountInfo.class.getSimpleName();

    protected String email;
    protected String type;

    public AccountInfo() {
        // For GSON
    }

    // Prevent instantiation - use static method
    protected AccountInfo(Account account) {
        email = account.name;
        type = account.type;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountType() {
        return type;
    }

    @Override public boolean isValid() {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(type);
    }

    public static boolean doesAccountHaveEmail(Account account) {
        if (account == null || TextUtils.isEmpty(account.name)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(account.name).matches();
    }

    public static AccountInfo createAccountInfo(Account account) {
        if (account == null) {
            return null;
        }
        if (!doesAccountHaveEmail(account)) {
            Log.w(TAG, "Account does not have an email: "+account.name);
            return null;
        }
        return new AccountInfo(account);
    }

}
