package com.nyc.prototype.models;

import com.nyc.models.BaseGsonModel;

import java.util.Collection;

/**
 * Created by Kevin on 1/28/2015.
 */
public class UserInfo extends BaseGsonModel {

    @SuppressWarnings("unused")
    private static final String TAG = UserInfo.class.getSimpleName();

    protected String firstName;
    protected String lastName;
    protected String displayName;
    // For future use
    //protected String email;
    protected String profilePhoto;

    protected AccountInfo[] accounts;
    protected DeviceInfo[] devices;


    public UserInfo() {
        // For GSON
    }

    public UserInfo setDisplayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePhotoUri() {
        return profilePhoto;
    }

    public UserInfo addAccountEmail(AccountInfo info) {
        if (info == null) {
            return this;
        }
        if (accounts == null) {
            accounts = new AccountInfo[1];
        } else {
            AccountInfo[] newAccounts = new AccountInfo[accounts.length+1];
            System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
            accounts = newAccounts;
        }
        accounts[accounts.length-1] = info;
        return this;
    }

    public UserInfo addAccountEmails(Collection<AccountInfo> info) {
        if (info == null || info.isEmpty()) {
            return this;
        }
        int oldSize = 0;
        if (accounts == null) {
            accounts = new AccountInfo[info.size()];
        } else {
            AccountInfo[] newAccounts = new AccountInfo[accounts.length+info.size()];
            System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
            oldSize = accounts.length;
            accounts = newAccounts;
        }
        AccountInfo[] newInfos = info.toArray(new AccountInfo[0]);
        System.arraycopy(newInfos, 0, accounts, oldSize, newInfos.length);
        return this;
    }

    public UserInfo addDeviceInfo(DeviceInfo info) {
        if (info == null) {
            return this;
        }
        if (devices == null) {
            devices = new DeviceInfo[1];
        } else {
            DeviceInfo[] newDevices = new DeviceInfo[devices.length+1];
            System.arraycopy(devices, 0, newDevices, 0, devices.length);
            devices = newDevices;
        }
        devices[devices.length-1] = info;
        return this;
    }

    public AccountInfo[] getAccountEmails() {
        return accounts;
    }

    public boolean hasEmail(String email) {
        if (accounts == null) {
            return false;
        }
        for (AccountInfo info : accounts) {
            if (info.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public DeviceInfo[] getDevices() {
        return devices;
    }

    public DeviceInfo getFirstDevice() {
        return devices == null || devices[0] == null ? null : devices[0];
    }

    @Override public boolean isValid() {
        // All fields are optional
        return true;
    }
}
