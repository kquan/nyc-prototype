package com.nyc.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;

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

}
