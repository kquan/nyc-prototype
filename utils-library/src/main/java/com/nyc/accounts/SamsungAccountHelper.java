package com.nyc.accounts;

import android.accounts.Account;
import android.content.Context;

import com.nyc.utils.AccountUtils;

import java.util.List;

/**
 * Created by Kevin on 1/26/2015.
 */
public class SamsungAccountHelper {

    @SuppressWarnings("unused")
    private static final String TAG = SamsungAccountHelper.class.getSimpleName();

    public static final String ACCOUNT_TYPE_SAMSUNG = "com.osp.app.signin";

    public static boolean hasSamsungAccount(Context context) {
        return getFirstSamsungAccount(context) != null;
    }

    public static List<Account> getSamsungAccounts(Context context) {
        return AccountUtils.getAllAccountsOf(context, ACCOUNT_TYPE_SAMSUNG);
    }

    public static String getFirstSamsungAccountEmail(Context context) {
        Account firstAccount = getFirstSamsungAccount(context);
        return firstAccount != null ? firstAccount.name : new String();
    }

    public static Account getFirstSamsungAccount(Context context) {
        return AccountUtils.getFirstAccountsOf(context, ACCOUNT_TYPE_SAMSUNG);
    }

}
