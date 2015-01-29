package com.nyc.prototype.user;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.nyc.prototype.R;

/**
 * Created by Kevin on 1/29/2015.
 * Reads received SMS to see if the message is to verify the user's phone number
 */
public class VerifyPhoneNumberSMSReceiver extends WakefulBroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = VerifyPhoneNumberSMSReceiver.class.getSimpleName();

    protected static final String EXTRA_PDUS = "pdus";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null || context == null || !Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return;
        }

        Object[] pdus = (Object[]) intent.getExtras().get(EXTRA_PDUS);
        if (pdus == null || pdus.length <= 0) {
            return;
        }
        String messagePrefix = context.getString(R.string.account_sms_verification_prefix);
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String msgBody = message.getMessageBody();
            if (TextUtils.isEmpty(msgBody)) {
                continue;
            }
            if (msgBody.startsWith(messagePrefix)) {
                Log.d(TAG, "Received phone number verification message: ["+msgBody+"]");
                String code = messagePrefix.replace(messagePrefix, "").trim();
                Log.d(TAG, "Extracted verification code: "+code);
                startWakefulService(context, new Intent(context, VerifyPhoneNumberService.class).putExtra(VerifyPhoneNumberService.EXTRA_VERIFICATION_CODE, code));
            }
        }
    }
}
