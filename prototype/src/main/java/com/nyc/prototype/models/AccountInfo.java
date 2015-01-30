package com.nyc.prototype.models;

import android.accounts.Account;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.nyc.models.BaseGsonModel;
import com.nyc.utils.AccountUtils;

/**
 * Created by Kevin on 1/28/2015.
 * This stores information from accounts that are retrieved from the device, such as
 * Google accounts, Dropbox accounts, etc
 */
public class AccountInfo extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = AccountInfo.class.getSimpleName();

    protected static final String ACCOUNT_TYPE_PROFILE = "android.profile";

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

    // Prevent instantiation - use static method
    protected AccountInfo(String email, String type) {
        this.email = email;
        this.type = type;
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

    public static AccountInfo createAccountInfo(Account account) {
        if (account == null) {
            return null;
        }
        if (!AccountUtils.doesAccountHaveEmail(account)) {
            Log.w(TAG, "Account does not have an email: "+account.name);
            return null;
        }
        return new AccountInfo(account);
    }

    public static AccountInfo createAndroidProfileAccountInfo(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return null;
        }
        return new AccountInfo(email, ACCOUNT_TYPE_PROFILE);
    }

    @Override public int hashCode() {
        return email.hashCode();
    }

    @Override public boolean equals(Object o) {
        // Note this only takes into account email, and not the type.
        // The reason is we use this method to remove duplicates when sending
        // multiple emails to the server
        if (!(o instanceof AccountInfo)) {
            return false;
        }
        return TextUtils.equals(email, ((AccountInfo)o).email);
    }

    @Override public String toString() {
        return email+" ("+type+")";
    }

}
