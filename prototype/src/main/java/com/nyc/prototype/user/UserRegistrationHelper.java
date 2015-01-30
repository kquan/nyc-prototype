package com.nyc.prototype.user;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;

import com.nyc.accounts.SamsungAccountHelper;
import com.nyc.prototype.models.AccountInfo;
import com.nyc.utils.AccountUtils;
import com.nyc.utils.CursorUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin on 1/29/2015.
 */
public class UserRegistrationHelper {

    @SuppressWarnings("unused")
    private static final String TAG = UserRegistrationHelper.class.getSimpleName();

    /*
     * Merges account emails with profile emails, and removes any Samsung account emails.
     */
    public static Collection<AccountInfo> getAllAccountInfo(Context context) {
        // This leverages the fact that AccountInfo only compares emails when comparing for equality
        // If this is not true in the future, we need to convert this to a Hashtable with key based
        // on the email address.
        HashSet<AccountInfo> allInfo = new HashSet<AccountInfo>();
        Collection<Account> accountEmails = getNonSamsungEmailAccounts(context);
        for (Iterator<Account> i = accountEmails.iterator(); i.hasNext();) {
            Account account = i.next();
            AccountInfo info = AccountInfo.createAccountInfo(account);
            if (info != null
                    // Same email might be used for multiple accounts
                    && !allInfo.contains(info)) {
                allInfo.add(info);
            }
        }
        Collection<String> profileEmails = getProfileEmails(context);
        for (Iterator<String> i = profileEmails.iterator(); i.hasNext();) {
            String email = i.next();
            AccountInfo info = AccountInfo.createAndroidProfileAccountInfo(email);
            if (info != null && !allInfo.contains(info)) {
                allInfo.add(info);
            }
        }
        String samsungEmail = SamsungAccountHelper.getFirstSamsungAccountEmail(context);
        if (!TextUtils.isEmpty(samsungEmail)) {
            AccountInfo samsungAccountInfo = AccountInfo.createAndroidProfileAccountInfo(samsungEmail);
            if (samsungAccountInfo != null) {
                allInfo.remove(samsungAccountInfo);
            }
        }
        return allInfo;
    }

    public static Collection<Account> getNonSamsungEmailAccounts(Context context) {
        List<Account> allAccounts = AccountUtils.getAllAccounts(context);
        if (allAccounts == null || allAccounts.isEmpty()) {
            return allAccounts;
        }
        List<Account> resultAccounts = new ArrayList<Account>(allAccounts.size());
        for (Account account : allAccounts) {
            if (!AccountUtils.doesAccountHaveEmail(account)
                    || SamsungAccountHelper.isAccountASamsungAccount(account)) {
                continue;
            }
            resultAccounts.add(account);
        }
        return resultAccounts;
    }

    /*
     * Note: this is unreliable as given name is mapped to data2 column and some tuples
     * may store non-name related data in that column
     */
    @Deprecated
    public static String getFirstName(Context context) {
        Cursor profile = null;
        try {
            profile = AccountUtils.getProfile(context);
            if (!CursorUtils.hasResults(profile)) {
                return null;
            }
            String givenName = null;
            String displayName = null;
            do {
                givenName = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                if (TextUtils.isEmpty(givenName) && TextUtils.isEmpty(displayName)) {
                    displayName = CursorUtils.safeGetString(profile, "display_name");
                } else {

                    return givenName;
                }
            } while (profile.moveToNext());
            // We couldn't find a given name, so return the primary display name
            return displayName;
        } finally {
            CursorUtils.safeClose(profile);
        }
    }

    /*
     * Note: this is unreliable as given name is mapped to data3 column and some tuples
     * may store non-name related data in that column
     */
    @Deprecated
    public static String getLastName(Context context) {
        Cursor profile = null;
        try {
            profile = AccountUtils.getProfile(context);
            if (!CursorUtils.hasResults(profile)) {
                return null;
            }
            String lastName = null;
            do {
                lastName = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
                if (TextUtils.isEmpty(lastName)) {
                    // We previously returned the display name, so don't return anything for last name.
                    if (!TextUtils.isEmpty(CursorUtils.safeGetString(profile, "display_name"))) {
                        return null;
                    }
                } else {
                    return lastName;
                }
            } while (profile.moveToNext());
            return lastName;
        } finally {
            CursorUtils.safeClose(profile);
        }
    }

    public static String getDisplayName(Context context) {
        Cursor profile = null;
        try {
            profile = AccountUtils.getProfile(context);
            if (!CursorUtils.hasResults(profile)) {
                return null;
            }
            String displayName = null;
            do {
                displayName = CursorUtils.safeGetString(profile, "display_name");
                if (!TextUtils.isEmpty(displayName)) {
                    return displayName;
                }
            } while (profile.moveToNext());
        } finally {
            CursorUtils.safeClose(profile);
        }
        return null;
    }

    public static Collection<String> getProfileEmails(Context context) {
        HashSet<String> emails = new HashSet<String>();
        Cursor profile = null;
        try {
            profile = AccountUtils.getProfile(context);
            if (!CursorUtils.hasResults(profile)) {
                return emails;
            }
            do {
                String email = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.Email.ADDRESS);
                // User could have entered garbage
                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emails.add(email.trim());
                }
            } while (profile.moveToNext());
        } finally {
            CursorUtils.safeClose(profile);
        }
        return emails;
    }

    public static Uri getProfileUri(Context context) {
        Cursor profile = null;
        try {
            profile = AccountUtils.getProfile(context);
            if (!CursorUtils.hasResults(profile)) {
                return null;
            }
            String uri = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.Photo.PHOTO_URI);
            if (!TextUtils.isEmpty(uri)) {
                return Uri.parse(uri);
            }
        } finally {
            CursorUtils.safeClose(profile);
        }
        return null;
    }

}
