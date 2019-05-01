package sms.app.infogen.cs.com.realmsms;

/**
 * Created by Dell on 3/3/2018.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class SMSActivity extends AppCompatActivity {

    BroadcastReceiver smsReceiver;
    private IntentFilter myFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

    public static final String OTP_REGEX = "[0-9]{1,6}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
//        registerReceiver(smsReceiver, myFilter);
        Realm.init(this);
        /*IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        this.registerReceiver(incomingSMS, filter);*/

        //SMS Received

        IncomingSMS.bindListener(new SMSListener() {
            @Override
            public void messageReceived(String textMsg) {
                Log.e("Message", textMsg);
                Toast.makeText(SMSActivity.this, "Message: " + textMsg, Toast.LENGTH_LONG).show();
                Pattern pattern = Pattern.compile(OTP_REGEX);
                Matcher matcher = pattern.matcher(textMsg);
                String otp = "";
                while (matcher.find()) {
                    otp = matcher.group();
                }

                Toast.makeText(SMSActivity.this, "OTP: " + otp, Toast.LENGTH_LONG).show();

            }
        });
        Realm realm = Realm.getDefaultInstance();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {

            }
        });
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(smsReceiver); // UnRegister BroadCastReceiver as you no longer have your activity at Foreground -- Saving CPU & Battery Drainage
        super.onPause();
    }

    @Override
    protected void onResume() {
//        registerReceiver(smsReceiver, myFilter); // Register BroadCastReceiver Once again as your activity comes from pause to forground state again.
        super.onResume();

//        smsReceiver = new BroadcastReceiver() //Implementation of your BroadCastReceiver
//        {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Do whatever you like as sms is received and caught by these BroadCastReceiver
//                Toast.makeText(context, "SMS Received", Toast.LENGTH_LONG).show();
//
//            }
//        };
    }
}