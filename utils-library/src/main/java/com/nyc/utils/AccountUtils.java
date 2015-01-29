package com.nyc.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kevin on 1/26/2015.
 *
 * Note: You will need this permission: android.permission.GET_ACCOUNTS
 */
public class AccountUtils {

    @SuppressWarnings("unused")
    private static final String TAG = AccountUtils.class.getSimpleName();

    public static final String ACCOUNT_TYPE_GOOGLE = "com.google";

    public static boolean hasAccount(Context context, Account currentAccount, String accountType) {
        if (context == null || TextUtils.isEmpty(accountType)) {
            return false;
        }
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(accountType);
        if (accounts == null) {
            return false;
        }
        for (Account account : accounts) {
            if (currentAccount.equals(account)) {
                return true;
            }
        }
        return false;
    }

    public static List<Account> getAllAccountsOf(Context context, String accountType) {
        List<Account> accounts = new ArrayList<Account>();
        if (context == null || TextUtils.isEmpty(accountType)) {
            return accounts;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return accounts;
        }
        for (Account account : allAccounts) {
            if (accountType.equals(account.type)) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    public static Account getFirstAccountsOf(Context context, String accountType) {
        if (context == null || TextUtils.isEmpty(accountType)) {
            return null;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return null;
        }
        for (Account account : allAccounts) {
            if (accountType.equals(account.type)) {
                return account;
            }
        }
        return null;
    }

    public static List<Account> getAllAccounts(Context context) {
        List<Account> accounts = new ArrayList<Account>();
        if (context == null) {
            return accounts;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return accounts;
        }
        return Arrays.asList(allAccounts);
    }

    public static boolean doesAccountHaveEmail(Account account) {
        if (account == null || TextUtils.isEmpty(account.name)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(account.name).matches();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Cursor getProfile(Context context) {
        // From http://stackoverflow.com/a/2175688/1339200
        if (context == null) {
            return null;
        }
        Uri uri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
        return context.getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.Data.IS_PRIMARY+" DESC");
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void debug_dumpProfile(Context context) {
        Cursor profile = null;
        try {
            profile = getProfile(context);
            if (CursorUtils.hasResults(profile)) {
                Log.d(TAG, "There are "+profile.getCount()+" entries.");
                do {
                    for (int i = 0; i < profile.getColumnCount(); i++) {
                        String value = "not_a_string";
                        try {
                            value = profile.getString(i);
                        } catch (Exception e) {
                            // Squelch
                        }
                        Log.d(TAG, "Column " + i + ": " + profile.getColumnName(i) + " has value " + value);
                    }
                } while (profile.moveToNext());
            }
        } finally {
            CursorUtils.safeClose(profile);
        }
    }
}
