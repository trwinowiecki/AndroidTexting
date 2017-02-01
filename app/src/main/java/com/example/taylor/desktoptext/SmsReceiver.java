package com.example.taylor.desktoptext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            MainActivity inst = MainActivity.instance();

            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                int protocol = smsMessage.getProtocolIdentifier();

                if (protocol != 0) {
                    smsMessageStr += "Sent: " + "\n";
                    smsMessageStr += smsBody + "\n";
                } else {
                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                }


            }

            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            inst.updateList(smsMessageStr);
        }
    }
}
