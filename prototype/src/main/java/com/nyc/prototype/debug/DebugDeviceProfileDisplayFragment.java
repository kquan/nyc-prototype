package com.nyc.prototype.debug;

import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nyc.accounts.SamsungAccountHelper;
import com.nyc.prototype.R;
import com.nyc.prototype.models.AccountInfo;
import com.nyc.prototype.user.UserRegistrationHelper;
import com.nyc.utils.AccountUtils;

import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kevin on 2/2/2015.
 */
public class DebugDeviceProfileDisplayFragment extends DialogFragment {

    public static DebugDeviceProfileDisplayFragment newInstance() {
        return new DebugDeviceProfileDisplayFragment();
    }

    @SuppressWarnings("unused")
    private static final String TAG = DebugDeviceProfileDisplayFragment.class.getSimpleName();

    @InjectView(R.id.debug_name) protected TextView mDisplayName;
    @InjectView(R.id.debug_profile_photo) protected ImageView mProfilePhoto;
    @InjectView(R.id.debug_email) protected TextView mEmail;
    @InjectView(R.id.samsung_id) protected TextView mSamsungId;
    @InjectView(R.id.debug_accounts) protected ListView mAccountList;

    public DebugDeviceProfileDisplayFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_profile_display, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(final Void... params) {
                final String displayName = UserRegistrationHelper.getDisplayName(getActivity());
                final Collection<String> emails = UserRegistrationHelper.getProfileEmails(getActivity());
                final String samsungId = SamsungAccountHelper.getFirstSamsungAccountEmail(getActivity());
                final Uri profilePhoto = UserRegistrationHelper.getProfileUri(getActivity());

                final Collection<AccountInfo> accountInfos = UserRegistrationHelper.getAllAccountInfo(getActivity());
                getView().post(new Runnable() {
                    public void run() {
                        mDisplayName.setText(displayName);
                        if (emails.isEmpty()) {
                            mEmail.setText("No email found");
                        } else {
                            mEmail.setText(emails.iterator().next()+(emails.size() > 1 ? " ("+emails.size()+" profile emails.)" : ""));
                        }
                        if (!TextUtils.isEmpty(samsungId)) {
                            mSamsungId.setText(samsungId);
                        } else {
                            mSamsungId.setText("None");
                        }
                        mProfilePhoto.setImageURI(profilePhoto);
                        mAccountList.setAdapter(new ArrayAdapter<AccountInfo>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, accountInfos.toArray(new AccountInfo[0])));
                    }
                });
                AccountUtils.debug_dumpProfile(getActivity());
                return null;
            }
        }.execute();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
    }
}
