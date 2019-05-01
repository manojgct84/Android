package sms.app.infogen.cs.com.realmsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dell on 3/3/2018.
 */

public class IncomingSMS extends BroadcastReceiver {
    private static final String TAG = "IncomingSMS";
    private static SMSListener smsListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved : " + intent.getAction());
        Bundle data = intent.getExtras();

        Object pdus[] = (Object[]) data.get("puds");
        for (int i = 0 ;i < pdus.length ; i ++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            Toast.makeText(context, "SMS Received", Toast.LENGTH_SHORT).show();
            smsListener.messageReceived(smsMessage.getDisplayMessageBody());

        }
    }

    public static void bindListener(SMSListener mListener){
        smsListener = mListener;
    }
}
